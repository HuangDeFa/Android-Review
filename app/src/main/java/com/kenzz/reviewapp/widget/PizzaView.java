package com.kenzz.reviewapp.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ken.huang on 10/9/2017.
 */

public class PizzaView extends View {

    private Paint mPaint;
    private int backgroundColor;
    private RectF mRectF;
    private int mDrawType;
    public final static int DRAWPIZZ = 1;
    public final static int DRAWBASE = 2;
    public final static int DRAWZHEXIAN = 3;
    public final static int DRAWZHIFANGTU = 4;

    public PizzaView(Context context) {
        this(context, null);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundColor = Color.parseColor("#89bddc");
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
        mDrawType = DRAWPIZZ;
    }

    public void setDrawType(int drawType) {
      mDrawType = drawType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        switch (mDrawType) {
            case DRAWBASE:
                drawBase(canvas);
                break;
            case DRAWPIZZ:
                drawPizza(canvas);
                break;
            case DRAWZHEXIAN:
                drawZhexian(canvas);
                break;
            case DRAWZHIFANGTU:
                drawZhiFangtu(canvas);
                break;
        }
    }

    private void drawZhiFangtu(Canvas canvas) {

    }

    private void drawZhexian(Canvas canvas) {

    }

    private void drawBase(Canvas canvas) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        int left = dp2px(60);
        int top = dp2px(20);
        int length = dp2px(200);
        mRectF.set(left, top, left + length, top + length);
        mPaint.setColor(Color.RED);
        canvas.drawArc(mRectF,0,120,false,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawArc(mRectF,120,120,false,mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(mRectF,240,120,false,mPaint);

        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setTextSize(85);
        float width = mPaint.measureText("7");
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseY = mRectF.centerY()+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom;
        canvas.drawText("7",mRectF.centerX()-width/2,baseY,mPaint);

    }

    private void drawPizza(Canvas canvas) {
        int left = dp2px(60);
        int top = dp2px(20);
        int length = dp2px(200);
        mRectF.set(left, top, left + length, top + length);
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(mRectF, -60, 55, true, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawArc(mRectF, 0, 120, true, mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawArc(mRectF, 125, 20, true, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.save();
        canvas.translate(-5, -5);
        canvas.drawArc(mRectF, 150, 145, true, mPaint);
        canvas.restore();
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getContext().getResources().getDisplayMetrics());
    }
}
