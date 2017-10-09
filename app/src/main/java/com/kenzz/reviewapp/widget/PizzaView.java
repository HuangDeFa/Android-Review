package com.kenzz.reviewapp.widget;

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
    public PizzaView(Context context) {
        this(context,null);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PizzaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundColor = Color.parseColor("#89bddc");
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        int left = dp2px(60);
        int top = dp2px(20);
        int length = dp2px(200);
        mPaint.setColor(Color.RED);
        mRectF.set(left,top,left+length,top+length);
        canvas.drawArc(mRectF,0,120,true,mPaint);
    }

    private int dp2px(float dpValue){
       return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
               dpValue,getContext().getResources().getDisplayMetrics());
    }
}
