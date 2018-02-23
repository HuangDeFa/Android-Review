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
 * Created by huangdefa on 23/02/2018.
 * Version 1.0
 * 幸运转盘
 */

public class LuckPanelView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    SurfaceHolder mHolder;
    Thread mThread;
    Canvas mCanvas;
    int mRadius;
    int mCenter;
    RectF mRange;
    String[] pieces={"iPhone","MackBookPro","MI6","软妹子","涨工资","Holiday"};
    Paint mPanelPaint;
    Paint mTextPaint;

    public LuckPanelView(Context context) {
        this(context, null);
    }

    public LuckPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);

        mPanelPaint=new Paint();
        mPanelPaint.setAntiAlias(true);
        mPanelPaint.setDither(true);
        mPanelPaint.setColor(Color.RED);


        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w=MeasureSpec.getSize(widthMeasureSpec);
        int h=MeasureSpec.getSize(heightMeasureSpec);
        int size=Math.min(w,h);
        mCenter=size/2;
        mRadius=size;
        setMeasuredDimension(size,size);
        mRange=new RectF(getPaddingLeft(),getPaddingLeft(),size-getPaddingLeft(),size-getPaddingLeft());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new Thread(this);
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
        while (true) {
            draw();
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                drawPanel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    private void drawPanel() {

    }
}
