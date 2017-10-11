package com.kenzz.reviewapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ken.huang on 10/11/2017.
 * 圆形ImageView
 */

public class CircleImageView extends ImageView {

    private BitmapShader mBitmapShader;
    private Paint mPaint;
    private int mRadius;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        updateImage();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        updateImage();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        updateImage();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        updateImage();
    }

    private void updateImage(){
        mBitmapShader=null;
    }


    private void initBitmapShader() {
        if (mBitmapShader != null) return;
        final Drawable drawable = getDrawable();
        mRadius = Math.min(getWidth(), getHeight())/2;
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (bitmap == null) {
            throw new IllegalArgumentException("Must set a bitmap");
        }
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        float scaleY = getHeight()*1.0f/bitmap.getHeight();
        float scaleX = getWidth()*1.0f/bitmap.getWidth();
        float scale = Math.max(scaleX,scaleY);
        matrix.postScale(scale,scale);
        matrix.postTranslate(mRadius-bitmap.getWidth()*scale/2,mRadius-bitmap.getHeight()*scale/2);
        mBitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(mBitmapShader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        initBitmapShader();
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
    }
}
