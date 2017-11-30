package com.kenzz.reviewapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuggestionSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenzz.reviewapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpannableActivity extends BaseActivity {

    final static String TEST_CONTENT="this is a test content of spannable string which created by wong ni ma";
    final static String TEST_CONTENT_LONG ="《百年孤独》是魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事，" +
            "以及加勒比海沿岸小镇马孔多的百年兴衰，反映了拉丁美洲一个世纪以来风云变幻的历史。作品融入神话传说、民间故事、" +
            "宗教典故等神秘因素，巧妙地糅合了现实与虚幻，展现出一个瑰丽的想象世界，成为20世纪最重要的经典文学巨著之一。" +
            "1982年加西亚•马尔克斯获得诺贝尔文学奖，奠定世界级文学大师的地位，很大程度上乃是凭借《百年孤独》的巨大影响。\"\n";
    @BindView(R.id.design_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_content)
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        for (int i=0;i<19;i++){
            createSpannedString(i);
        }
       // createFireworkSpanForTitle();
        createTypeWriterSpan();
    }

    private void createSpannedString(int index){

        //创建spannableString
        final SpannableString spannableString=new SpannableString(TEST_CONTENT);
        //创建一个Span
         Object Span=createSpan(index);


        //设置Span 到spannableString上
        spannableString.setSpan(Span, 0, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        final TextView textView=new TextView(this);
        textView.setPadding(0,25,0,25);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);
        textView.setLayoutParams(lp);
        if(Span instanceof  MuteColorSpan){
            ObjectAnimator animator=ObjectAnimator.ofObject((MuteColorSpan)Span,"ForegroundColor"
                    , new ArgbEvaluator(),Color.BLACK,Color.RED);
            animator.setDuration(600);
            animator.setRepeatMode(ObjectAnimator.REVERSE);
            animator.setRepeatCount(ObjectAnimator.INFINITE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    textView.setText(spannableString);
                }
            });
            animator.start();

        }else {
            textView.setText(spannableString);
        }
        mLinearLayout.addView(textView);
    }

    private class MuteColorSpan extends ForegroundColorSpan{

        private int mForegroundColor;
        private int mAlpha;
        public MuteColorSpan(int color,int alpha) {
            super(color);
            mForegroundColor=color;
            mAlpha = alpha;
        }
        public MuteColorSpan(Parcel src){
            super(src);
            mForegroundColor=src.readInt();
            mAlpha=src.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mForegroundColor);
            dest.writeInt(mAlpha);
        }

        public int getAlpha() {
            return mAlpha;
        }

        //主要设置颜色的方法
        @Override
        public void updateDrawState(TextPaint ds) {
           ds.setColor(getForegroundColor());
        }

        @Override
        public int getForegroundColor() {
           return Color.argb(mAlpha,Color.red(mForegroundColor),
                    Color.green(mForegroundColor),Color.blue(mForegroundColor));
        }

        public void setForegroundColor(int foregroundColor) {
            mForegroundColor = foregroundColor;
        }

        public void setAlpha(int alpha) {
            mAlpha = alpha;
        }
    }


    private static class FireWorkGroup{
        private float mAlpha;
        private List<MuteColorSpan> mSpans;
        private FireWorkGroup(float alpha){
            mAlpha=alpha;
            mSpans=new ArrayList<>();
        }

        public float getAlpha() {
            return mAlpha;
        }

        public void setAlpha(float alpha) {
            int size = mSpans.size();
            float total = 1.0f * size * alpha;
            for(int index = 0 ; index < size; index++) {
                MuteColorSpan span = mSpans.get(index);
                if(total >= 1.0f) {
                    span.setAlpha(255);
                    total -= 1.0f;
                } else {
                    span.setAlpha((int) (total * 255));
                    total = 0.0f;
                }
            }
        }

        public void addSpan(MuteColorSpan span){
            span.setAlpha((int) (mAlpha*255));
            mSpans.add(span);
        }

        public void init(){
            Collections.shuffle(mSpans);
        }
    }

    private Object createSpan(int index){
        Object object=null;
        switch (index){
            case 0:
                //前景色，即系字体颜色
                object = new ForegroundColorSpan(Color.RED);
                break;
            case 1:
                //加下划线
                final UnderlineSpan span =new UnderlineSpan();
                TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.BLUE);
                paint.setTextSize(15);
                span.updateDrawState(paint);
                object = span;
                break;
            case 2:
                //删除线
                object =new StrikethroughSpan();
                break;
            case 3:
                //设置指定字体大小 绝对值
                object=new AbsoluteSizeSpan(20,true);
                break;
            case 4:
                //设置相对大小
                object = new RelativeSizeSpan(2);
                break;
            case 5:
                //设置样式
                object=new StyleSpan(Typeface.BOLD_ITALIC);
                break;
            case 6:
                //设置URL
                object=new URLSpan("http://www.baidu.com");
                break;
            case 7:
                //下标
                object=new SubscriptSpan();
                break;
            case 8:
                //上标
                object =new SuperscriptSpan();
                break;
            case 9:
                object = new SuggestionSpan(this,new String[]{"wang ni ma","content"},0);
                break;
            case 10:
                //段落开头加红点
                object = new BulletSpan(12,Color.RED);
                break;
            case 11:
                //段落开头应用号
                object = new QuoteSpan(Color.CYAN);
                break;
            case 12:
                //对齐方式
                object=new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
                break;
            case 13:
                //设置字体
                object =new TypefaceSpan("cursive");
                break;
            case 14:
                //横向放大字体
                object=new ScaleXSpan(3.0f);
                break;
            case 15:
                object = new MuteColorSpan(Color.RED,255);
                break;
            case 16:
                object=new FrameSpan();
                break;
            case 17:
                object=new ImageSpan(this,R.mipmap.ic_launcher);
                break;
                default:
                    object=new BackgroundColorSpan(Color.YELLOW);
                    break;
        }

        return object;
    }

     private void createFireworkSpanForTitle(){
         CharSequence title = mToolbar.getTitle();

         final SpannableString spannableString=new SpannableString(title);
         FireWorkGroup fireWorkGroup=new FireWorkGroup(1.0f);
         for(int i=0;i<title.length();i++){
             title.charAt(1);
             MuteColorSpan span=new MuteColorSpan(Color.WHITE,255);
             fireWorkGroup.addSpan(span);
             spannableString.setSpan(span,i,i+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         }
         fireWorkGroup.init();
         ObjectAnimator animator = ObjectAnimator.ofObject(fireWorkGroup, "Alpha", new FloatEvaluator(), 0f, 1.0);
         animator.setDuration(2000);
         animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
             @Override
             public void onAnimationUpdate(ValueAnimator animation) {
                 mToolbar.setTitle(spannableString);
             }
         });
         animator.start();
     }

    /**
     * 添加边框
     */
    private class FrameSpan extends ReplacementSpan{

         private Paint mPaint;
         private int mWidth;
         FrameSpan(){
             mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
             mPaint.setColor(Color.RED);
             mPaint.setStyle(Paint.Style.STROKE);
             mPaint.setStrokeWidth(3);
         }
         @Override
         public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            return mWidth =(int) paint.measureText(text,start,end);
         }

         @Override
         public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            canvas.drawRect(x,top,x+mWidth,bottom,mPaint);
            canvas.drawText(text,start,end,x,y,paint);
         }
     }

    /**
     * 模拟打印机的效果
     */
    private void createTypeWriterSpan(){
        final SpannableString stringBuilder=new SpannableString(TEST_CONTENT_LONG);
        FireWorkGroup group=new FireWorkGroup(1.0f);
        int length=stringBuilder.length();
        for(int i=0;i<length;i++){
            MuteColorSpan span=new MuteColorSpan(Color.LTGRAY,255);
            stringBuilder.setSpan(span,i,i+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            group.addSpan(span);
        }
        final TextView textView=new TextView(this);
        ObjectAnimator animator = ObjectAnimator.ofObject(group,"Alpha",new FloatEvaluator(),0,1.f);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               textView.setText(stringBuilder);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animation.cancel();
            }
        });
        textView.setPadding(0,25,0,25);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);
        textView.setLayoutParams(lp);
        mLinearLayout.addView(textView,0);
        animator.start();
    }
}
