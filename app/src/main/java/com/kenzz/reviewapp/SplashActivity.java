package com.kenzz.reviewapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mImageView.animate()
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
                .start();
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
