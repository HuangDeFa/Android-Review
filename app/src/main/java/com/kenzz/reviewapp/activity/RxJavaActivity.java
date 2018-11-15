package com.kenzz.reviewapp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.kenzz.reviewapp.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * RXJava 使用案例分析
 */
public class RxJavaActivity extends BaseActivity {

    private static final String TAG=RxJavaActivity.class.getSimpleName();
    private EditText mNameText;
    private EditText mPasswordText;
    private EditText nEmailText;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        long starTime= SystemClock.uptimeMillis();
        Log.d(TAG,"Activity 启动时间: "+starTime);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            //黑科技 6.0以后MessageQueue 添加了IdlHandler接口，该接口在
            //MessageQueue里面的消息被looper取走完了之后进入空闲就会被回调
            //回调返回false表示调用后即移除，返回true在下次进入空闲状态继续被回调
            //显然Activity中的View 是在ViewRootImpl的handler post了一绘制的操作。当该操作完成
            //MainLooper的MessageQueue空闲回调过来即可计算出Activity渲染界面的时间了
            getMainLooper().getQueue().addIdleHandler(() -> {
                long endTime=SystemClock.uptimeMillis();
                Log.d(TAG,"Activity 渲染完成时间："+(endTime-starTime));
                return false;
            });
        }
        mNameText=findViewById(R.id.rxjava_userName_edit_text);
        mPasswordText=findViewById(R.id.rxjava_password_edit_text);
        nEmailText=findViewById(R.id.rxjava_email_edit_text);
        mButton=findViewById(R.id.rxjava_login_button);
        combineData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 轮询访问
     */
    private void pollingMode(){
        //无条件轮询，间隔1s
        Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).subscribe(this::printLog);
    }

    /**
     * 有条件轮询
     */
    private int pollingCount=0;
    private void pollingConditionMode(){
        Observable.just("pollingCondition").repeatWhen(o->{
            return o.flatMap((Object x) ->{
                if(pollingCount>4){
                    return Observable.error(new Throwable("轮询结束"));
                }
                return Observable.just(1).delay(2,TimeUnit.SECONDS);
            });
        }).doOnError(x->pollingCount=0).subscribe(x->{
            pollingCount++;
            printLog(x);
        });
    }


    /**
     * 实现数据三级加载
     */
    private String memoryData;
    private String diskData;

    private void loadData(){
        Observable memoryLoad=Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if(memoryData!=null){
                    e.onNext(memoryData);
                }else {
                    e.onComplete();
                }
            }
        });

        Observable diskLoad=Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
              if(diskData!=null){
                  e.onNext(diskData);
              }else {
                  e.onComplete();
              }
            }
        });

        Observable netLoad=Observable.just("网络加载数据");
        //firstElement()，从串联队列中取出并发送第1个有效事件（Next事件），即依次判断检查memory、disk、network
        Observable.concat(memoryLoad,diskLoad,netLoad) //依次发送每个observable中的数据。
                                                       // 若遇到有效发送的数据（调用了onNext则停止发送）其他的视为无效
                .firstElement()
                .subscribe(x->{
                  printLog(x);
                });
    }

    /**
     * 数据合并,将observable合并起来依次发射数据
     */
    private void mergeData(){
       Observable<String> localData=Observable.just("本地上数据");
       Observable<String> netData=Observable.just("网络数据");
       Observable.merge(localData,netData).subscribe(this::printLog);
    }

    /**
     * 将observable合并起来把数据一次发送出去
     */
    private void zidData(){
        Observable<String> localData=Observable.just("本地上数据");
        Observable<String> netData=Observable.just("网络数据");
        Observable.zip(localData,netData,(x,y)->x+y).subscribe(this::printLog);
    }

    private void combineData(){
        Observable.combineLatest(
        EditTextChangeObservable(nEmailText),
                EditTextChangeObservable(mNameText),
                EditTextChangeObservable(mPasswordText),(email,name,password)->{
                 if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                     return "miss field";
                 }
                 return "ok";
                }).subscribe(result->{
                    if(result.equals("ok")){
                        mButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    }else {
                        mButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    }
                    this.printLog(result);
            });
    }

    /**
     * 防抖，经典场景：短时间内多次点击按钮只响应一次
     */
    private void avoidShake(){

    }

    private void printLog(Object object){
        Log.d(TAG,"print Log : "+object.toString());
    }

    /**
     * 将EditText的内容改变成Observable数据的发送
     * @param editText
     */
    private Observable<String> EditTextChangeObservable(EditText editText){
        PublishSubject subject=PublishSubject.create();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              subject.onNext(s.toString());
            }
        });
        return subject;
    }
}
