package com.kenzz.reviewapp.widget;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
 * Created by ken.huang on 10/10/2017.
 */

public class AnimationView extends View {

    private Paint mPaint;
    private float progress;
    private RectF mRectF;
    private int mRadius;
    private ObjectAnimator mObjectAnimator;

    public float getProgress() {
        return progress;
    }


    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public AnimationView(Context context) {
        this(context, null);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setTextSize(35);
        mRectF = new RectF();
        mRadius = dp2px(40);
    }

    public void startAnimator(){
        Keyframe keyframe=Keyframe.ofFloat(0,0);
        Keyframe keyframe1=Keyframe.ofFloat(0.5f,1f);
        Keyframe keyframe2=Keyframe.ofFloat(1f,0.8f);
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofKeyframe("progress", keyframe, keyframe1, keyframe2);
        mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this,valuesHolder);
        mObjectAnimator.setDuration(1500);
        //mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
       // mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(getWidth()/2-mRadius,getHeight()/2-mRadius,getWidth()/2+mRadius,getHeight()/2+mRadius);
        mPaint.setStrokeWidth(10);
        canvas.drawArc(mRectF,120,300*progress,false,mPaint);
        float length = mPaint.measureText(progress*100 + "%");
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseY = mRectF.centerY()+(fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom;
        mPaint.setStrokeWidth(3);
        canvas.drawText(progress*100+"%",getWidth()/2-length/2,baseY,mPaint);
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getContext().getResources().getDisplayMetrics());
    }
}
