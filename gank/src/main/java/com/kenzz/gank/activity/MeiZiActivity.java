package com.kenzz.gank.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kenzz.gank.R;
import com.kenzz.gank.util.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MeiZiActivity extends BaseActivity {

    @InjectView(R.id.meizi_page_image)
    ImageView mImageView;
    @InjectView(R.id.meizi_page_container)
    FrameLayout mFrameLayout;
    @InjectView(R.id.meizi_page_delete)
    ImageView mDeleteIV;
    @InjectView(R.id.meizi_page_save)
    ImageView mSaveIV;
    @InjectView(R.id.meizi_page_share)
    ImageView mShareIV;
    @InjectView(R.id.meizi_page_titleBar)
    RelativeLayout mTitleBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_zi);
        setFullScreen();
        ButterKnife.inject(this);
        initViw();
    }

    private void initViw() {
        String url = getIntent().getStringExtra("URL");
        ImageLoader.getInstance().loadImage(this,mImageView,url);
        mFrameLayout.setBackgroundColor(Color.BLACK);
        mImageView.setOnTouchListener(mTouchListener);
        int titleBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mTitleBarLayout.getLayoutParams();
        layoutParams.topMargin=titleBarHeight;
        mTitleBarLayout.setLayoutParams(layoutParams);
    }

    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        private int statY;
        private FrameLayout.LayoutParams mLayoutParams;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    statY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int dy= (int) (event.getRawY()-statY);
                    mLayoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                    mLayoutParams.topMargin=dy/2;
                    mLayoutParams.bottomMargin=-dy/2;
                    v.setLayoutParams(mLayoutParams);
                    if (Math.abs(dy) > 400) {
                        mFrameLayout.getBackground().setAlpha(128);

                    } else {
                        //Alpha min 128 max 255
                        //255 - 128 = 127
                        double ratioAlpha = (Math.abs(dy) / 400.0) * 127;
                        mFrameLayout.getBackground().setAlpha(255 - (int) ratioAlpha);
                    }
                    if(mTitleBarLayout.getAlpha()==1 && Math.abs(dy)>0){
                        mTitleBarLayout.animate()
                                .alpha(0)
                                .setDuration(200).start();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mLayoutParams.topMargin=0;
                    mLayoutParams.bottomMargin=0;
                    v.setLayoutParams(mLayoutParams);
                    mFrameLayout.getBackground().setAlpha(255);
                    if(mTitleBarLayout.getAlpha()==0){
                        mTitleBarLayout.animate()
                                .alpha(1)
                                .setDuration(200).start();
                    }
                    break;
            }
            return true;
        }
    };
}
