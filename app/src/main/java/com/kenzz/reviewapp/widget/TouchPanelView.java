package com.kenzz.reviewapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by ken.huang on 2/27/2018.
 * 仿微信小视频拍摄按钮
 */

public class TouchPanelView extends View {

    final static String TAG=TouchPanelView.class.getSimpleName();

    private int mSmallRadius=60/2;
    private int mBigRadius=90/2;

    //大圆和小圆的比例
    private int ratio=3;

    private Paint mPaint;
    //绘制进度的path
    private RectF mRange;

    private boolean isTouch=false;

    private final static int RECORDERTIMEOUT=10*1000;//10s

    private int mStrokeWidth =8;



    public TouchPanelView(Context context) {
        this(context,null);
    }

    public TouchPanelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w_mode=MeasureSpec.getMode(widthMeasureSpec);
        int h_mode=MeasureSpec.getMode(heightMeasureSpec);
        int w=MeasureSpec.getSize(widthMeasureSpec);
        int h=MeasureSpec.getSize(heightMeasureSpec);
        int size=Math.min(w,h);
        size=Math.max(size,applyDpValue(90));
        if(w_mode==MeasureSpec.AT_MOST && h_mode==MeasureSpec.AT_MOST){
            size=applyDpValue(90);
        }
        if(mRange==null){
            mRange=new RectF(mStrokeWidth/2,mStrokeWidth/2,size-mStrokeWidth/2,size-mStrokeWidth/2);
        }
        setMeasuredDimension(size,size);
    }

    private void init() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    private int applyDpValue(float dpValue){
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,
                getContext().getResources().getDisplayMetrics())+.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center=getWidth()/2;
        mPaint.setStyle(Paint.Style.FILL);

        int sR=applyDpValue(isTouch? mBigRadius/ratio:mSmallRadius/ratio);
        int bR=applyDpValue(isTouch?mBigRadius:mSmallRadius);

        mPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(center,center,bR,mPaint);

        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(center,center,sR,mPaint);

        if(isTouch){
            //绘制进度条
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.STROKE);
            long dTime= SystemClock.uptimeMillis()-mStartTime;
            float degree = dTime*1f/RECORDERTIMEOUT *360;
            Log.d(TAG,"degree: "+degree);
            canvas.drawArc(mRange,270,degree,false,mPaint);
        }
    }

    private long mStartTime;

    @SuppressLint("HandlerLeak")
    private  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                case 300:
                    isTouch=true;
                    sendEmptyMessage(500);
                    break;
                case 200:
                    isTouch=false;
                    mHandler.removeMessages(100);
                    mHandler.removeMessages(200);
                    sendEmptyMessage(500);
                    break;
                case 500:
                    invalidate();
                    sendEmptyMessage(500);
                    break;
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartTime=SystemClock.uptimeMillis();
                mHandler.sendEmptyMessageDelayed(100, 200);
                break;
            case MotionEvent.ACTION_MOVE:
                mHandler.sendEmptyMessage(300);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessage(200);
                break;
        }
        return true;
    }
}
