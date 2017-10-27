package com.kenzz.reviewapp.activity;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kenzz.reviewapp.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Drawable 相关Demo
 */
public class DrawableActivity extends BaseActivity {

    @BindView(R.id.drawable_clip_iv)
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        ButterKnife.bind(this);
        clipDrawable();
    }

    private void clipDrawable(){
        final ClipDrawable drawable = (ClipDrawable) mImageView.getDrawable();
        drawable.setLevel(6000);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            private int level=6000;
            private boolean reverse;
            @Override
            public void run() {
                if(reverse){
                    level+=200;
                    if(level>=6000){
                        level=6000;
                        reverse=!reverse;
                    }
                }else {
                    level-=200;
                    if(level<0){
                        level=200;
                        reverse=!reverse;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawable.setLevel(level);
                    }
                });
            }
        },2000,500);
    }

    private Runnable clipTask=new Runnable() {
        public Drawable drawable;
        @Override
        public void run() {

        }
    };

    public void requestMoney(View view){
        RunChainRequest();
    }


    /**
     * 责任链模式测试
     */
    private void RunChainRequest(){
       Request request = new Request("Go to WWDC",3000);
       LeaderHandler leader=new LeaderHandler();
       ProjectManager projectManager=new ProjectManager();
        Boos boos=new Boos();

        leader.setNextHandler(projectManager);
        projectManager.setNextHandler(boos);

       for(int i=0;i<5;i++){
           request.requestMoney=(int)(10000 * Math.random());
           leader.handlerRequest(request);
       }
    }

    //请求
    private class Request{
        public String note;
        public int requestMoney;

        public Request(String note, int requestMoney) {
            this.note = note;
            this.requestMoney = requestMoney;
        }
    }

    private abstract class Handler{
        private int maxResponseMoney;

        public void setNextHandler(Handler nextHandler) {
            this.nextHandler = nextHandler;
        }

        public Handler(int maxResponseMoney) {
            this.maxResponseMoney = maxResponseMoney;
        }

        private Handler nextHandler;
        //The default handle process
        public void handlerRequest(Request request){
            if(request.requestMoney<=maxResponseMoney){
                reply(request);
            }else {
                if(null!=nextHandler){
                    nextHandler.handlerRequest(request);
                }
            }
        }

        public abstract void reply(Request request);
    }

    private class LeaderHandler extends Handler {

        public LeaderHandler() {
            super(1000);
        }

        @Override
        public void reply(Request request) {
            Log.d("Approve","Leader Approve: $"+request.requestMoney+" for "+request.note);
        }
    }

    private class ProjectManager extends Handler{

        public ProjectManager() {
            super(5000);
        }

        @Override
        public void reply(Request request) {
            Log.d("Approve","ProjectManager Approve: $"+request.requestMoney+" for "+request.note);
        }
    }

    private class Boos extends Handler{

        public Boos() {
            super(10000);
        }

        @Override
        public void reply(Request request) {
            Log.d("Approve","Boos Approve: $"+request.requestMoney+" for "+request.note);
        }
    }

}
