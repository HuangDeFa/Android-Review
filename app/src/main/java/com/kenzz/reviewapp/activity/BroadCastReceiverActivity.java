package com.kenzz.reviewapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kenzz.reviewapp.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class BroadCastReceiverActivity extends BaseActivity {

    private BroadcastReceiver mReceiver;
    @BindView(R.id.broadCastReceiver_btn)
    Button mButton;
    @BindView(R.id.broadCastReceiver_text)
    TextView mTextView;
    private String note="BroadCastReceiver广播接收者,可以拆分为发送广播和接收广播，一般的套路为：\n 1.自定义广播接受者继承BroadcastReceiver" +
            "重写onReceive方法 \n 2.注册广播接受者：静态注册即系在mainfest文件注册，动态注册registerReceiver(一般在Activity中)。本质上都是通过PMS" +
            "进行注册 \n 3.发送广播，通过context的sendBroadcast方法进行发送。这里可以分为普通广播和有序广播，其中有序广播根据receiver的优先级进行分发" +
            "高优先级优化接收并可以对此进行拦截进行停止广播或者修改广播内容 \n 4.限制广播范围和限制接收范围，通过IntentFilter设置receiver可以监听的" +
            "Action，设置优先级，在注册时添加权限校验(据此匹配具有对应权限应用发出的广播);发送广播通过Intent设置发送的action，设置receiver所在的包setPackage" +
            "添加权限校验 \n 5.设置发送的flag,默认不分发给停止状态的app \n 6.推荐使用LocalBroadCastReceiver仅仅作用在当前App内部，安全和效率都较高";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_receiver);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mReceiver = new MyReceiver("Receiver1");
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.kenzz.reviewapp");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.setPriority(100);
        //增加权限校验
        registerReceiver(mReceiver,filter,"com.kenzz.reviewapp.permission.broadcastreceiver",null);

        filter.setPriority(200);
        registerReceiver(new MyReceiver("Receiver2"),filter,"com.kenzz.reviewapp.permission.broadcastreceiver",null);

        //LocalBroadCastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    String str = bundle.getString("K1");
                    Toast.makeText(context,"localBroadCastReceiver",Toast.LENGTH_SHORT).show();
                }
            }
        },new IntentFilter("com.kenzz.reviewapp"));
     mTextView.setText(note);
    }

    @OnClick({R.id.broadCastReceiver_btn,R.id.broadCastReceiver_btn2})
    public void onClick(View view){
        int resId=view.getId();
        if(resId==R.id.broadCastReceiver_btn) {
            Intent intent = new Intent();
            intent.setAction("com.kenzz.reviewapp");

            //intent指定广播发送给某个APP,增加安全性。不指定则其他APP也可以接受该广播
            intent.setPackage("com.kenzz.reviewapp");

            Bundle bundle = new Bundle();
            bundle.putString("K1", "Hello world!");
            intent.putExtras(bundle);
            //增加权限校验
            sendBroadcast(intent,"com.kenzz.reviewapp.permission.broadcastreceiver");
            //LocalBroadCastReceiver
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else if(resId==R.id.broadCastReceiver_btn2){
            Intent intent = new Intent();
            intent.setAction("com.kenzz.reviewapp");
            Bundle bundle = new Bundle();
            bundle.putString("K1", "Order BroadCastReceiver");
            intent.putExtras(bundle);
            sendOrderedBroadcast(intent,null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class MyReceiver extends BroadcastReceiver{
        private String mName;
        public MyReceiver(String name) {
            this.mName=name;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
               String str = bundle.getString("K1");
                Snackbar snackbar = Snackbar.make(mButton,mName+":" + str,200);
                snackbar.show();
                //OrderBroadCastReceiver 可以拦截/终止
                this.abortBroadcast();
            }else{
                Snackbar snackbar = Snackbar.make(mButton,mName+": NetWork!",200);
                snackbar.show();
            }
        }
    }
}
