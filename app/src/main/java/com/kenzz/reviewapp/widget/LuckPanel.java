package com.kenzz.reviewapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ken.huang on 2/23/2018.
 * 幸运转盘
 */

public class LuckPanel extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    SurfaceHolder mSurfaceHolder;
    Thread mThread;
    boolean isRunning=true;
    Canvas mCanvas;

    //圆盘直径，中心
    int mRadius,mCenter;
    //绘制范围
    RectF mRange;

    //圆盘外环
    Paint mPanelPaint;
    //文字
    Paint mTextPaint;

    String[] pieces={"iPhone","MacBookPro","软妹子","山寨机","张公子","王尼玛"};
    //转动的角度
    int  mStartAngle;

    public LuckPanel(Context context) {
        this(context,null);
    }

    public LuckPanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        mPanelPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setColor(Color.RED);

        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int size=Math.min(width,height);
        mCenter=size/2;
        mRadius=size;
        setMeasuredDimension(size,size);
        if(mRange==null)
        mRange=new RectF(getPaddingLeft(),getPaddingLeft(),mRadius-getPaddingLeft(),mRadius-getPaddingLeft());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning=true;
       mThread=new Thread(this);
       mThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
      for(;;) {
          if(isRunning)
          draw();
          else {
              try {
                  Thread.sleep(50);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                drawPanel();
                if(mStartAngle>=360)
                    mStartAngle=0;
                mStartAngle+=2;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mCanvas!=null)
              mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawPanel() {
       int sweepAngle=360/pieces.length;
        for (int i=0;i<pieces.length;i++) {
            mCanvas.drawArc(mRange,mStartAngle+i*sweepAngle,sweepAngle,true,mPanelPaint);
            float len = mTextPaint.measureText(pieces[i]);
            Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
            int baseY=(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom+mRadius/2;
            mCanvas.save();
            mCanvas.rotate(90*i+sweepAngle/2,mCenter,mCenter);
            mCanvas.drawText(pieces[i],mCenter-len/2,baseY,mTextPaint);
            mCanvas.restore();
        }



    }
}
