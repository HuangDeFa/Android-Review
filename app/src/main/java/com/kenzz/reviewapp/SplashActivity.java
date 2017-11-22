package com.kenzz.reviewapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.textView2)
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

       /* mImageView.animate()
                .alpha(0)
                .scaleX(2)
                .scaleY(2)
                .setDuration(2000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        toMainActivity();
                    }
                })
                .start();*/
        ScaleAnimation animation = new ScaleAnimation(1.0f,1.5f,1.0f,1.5f,
                ScaleAnimation.RELATIVE_TO_SELF,ScaleAnimation.RELATIVE_TO_SELF);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.f,.5f);
        AnimationSet animationSet =new AnimationSet(true);
        animationSet.setDuration(2200);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(animation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
             toMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(animationSet);
    }

    private void toMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(R.anim.activity_zoom_in,R.anim.activity_zoom_out);
        finish();
    }

    @OnClick({R.id.textView2})
    public void onClick(){

    }
}
