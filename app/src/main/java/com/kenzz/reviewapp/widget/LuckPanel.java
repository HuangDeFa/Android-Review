package com.kenzz.reviewapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ken.huang on 2/23/2018.
 * 幸运转盘
 * TODO：中奖的计算 根据一定的规则算出概率，控制偏移量 mStartAngle 即可；
 */

public class LuckPanel extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    SurfaceHolder mSurfaceHolder;
    Thread mThread;
    boolean isRunning=false;
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

    Rect touchRange;
    //转动速度
    int mSpeed=2;

    int mTotalTime=5;
    long mStartTime;

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
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,
                getContext().getResources().getDisplayMetrics()));
        mTextPaint.setStrokeWidth(10);
        mPath=new Path();
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
        if(touchRange==null)
        touchRange=new Rect(mCenter-20,mCenter-20,mCenter+20,mCenter+20);
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
        draw();
      for(;;) {
          long timeSpan = System.currentTimeMillis() - mStartTime;
          if(timeSpan>=mTotalTime*1000){
              isRunning=false;
          }
          else if(timeSpan>=3500){
              mSpeed=3;
          }else if(timeSpan>=2000){
              mSpeed=5;
          }else {
              mSpeed=7;
          }

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
                mStartAngle+=mSpeed;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mCanvas!=null)
              mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    Path mPath;

    private void drawPanel() {
       int sweepAngle=360/pieces.length;
        for (int i=0;i<pieces.length;i++) {
            if(i%2==0){
                mPanelPaint.setColor(Color.RED);
            }else {
                mPanelPaint.setColor(Color.YELLOW);
            }
            mCanvas.drawArc(mRange,mStartAngle+i*sweepAngle,sweepAngle,true,mPanelPaint);
            float len = mTextPaint.measureText(pieces[i]);
            Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
            int baseY=(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom+mRadius/5;
            mCanvas.save();
            mCanvas.rotate(90+mStartAngle+sweepAngle*i+sweepAngle/2,mCenter,mCenter);
            mCanvas.drawText(pieces[i],mCenter-len/2,baseY,mTextPaint);
            mCanvas.restore();
        }

        mPanelPaint.setColor(Color.BLUE);
        //mCanvas.drawLine(mCenter,mCenter,mCenter,mRadius/5,mPanelPaint);
        mPath.moveTo(mCenter-(float)Math.cos(Math.PI/6)*40,mCenter-(float)Math.sin(Math.PI/6)*40);
        mPath.lineTo(mCenter,mCenter-mRadius/4);
        mPath.lineTo(mCenter+(float)Math.cos(Math.PI/6)*40,mCenter-(float)Math.sin(Math.PI/6)*40);
        mCanvas.drawPath(mPath,mPanelPaint);

        mPanelPaint.setColor(Color.RED);
        mCanvas.drawCircle(mCenter,mCenter,40,mPanelPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_UP){
                int x= (int) event.getX();
                int y= (int) event.getY();
                if(touchRange.contains(x,y) &&!isRunning){
                    isRunning=true;
                    mStartTime=System.currentTimeMillis();
                }
        }
        return true;
    }
}
