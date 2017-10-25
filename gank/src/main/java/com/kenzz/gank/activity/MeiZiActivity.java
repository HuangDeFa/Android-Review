package com.kenzz.gank.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kenzz.gank.R;
import com.kenzz.gank.net.ApiManager;
import com.kenzz.gank.net.IApiCallBack;
import com.kenzz.gank.util.ImageLoader;
import com.kenzz.gank.util.PermissionUtil;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
    private String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_zi);
        setFullScreen();
        ButterKnife.inject(this);
        initViw();
    }

    private void initViw() {
        mUrl = getIntent().getStringExtra("URL");
        ImageLoader.getInstance().loadImage(this,mImageView,mUrl);
        mFrameLayout.setBackgroundColor(Color.BLACK);
        mImageView.setOnTouchListener(mTouchListener);
        int titleBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mTitleBarLayout.getLayoutParams();
        layoutParams.topMargin=titleBarHeight;
        mTitleBarLayout.setLayoutParams(layoutParams);
    }

    @OnClick({R.id.meizi_page_delete,R.id.meizi_page_save,R.id.meizi_page_share})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.meizi_page_delete:
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.meizi_page_save:
                doSaveImage();
                break;
            case R.id.meizi_page_share:
                shareImage();
                break;
        }
    }

    private void shareImage(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"分享一张好看的妹子图");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(resolveInfos!=null && resolveInfos.size()>0){
            startActivity(intent);
        }
    }

    private void doSaveImage(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            List<String> strings = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(strings.size()>0){
                ActivityCompat.requestPermissions(this,strings.toArray(new String[strings.size()]),100);
            }
           else  {
                downloadImage();
            }
        }else {
            downloadImage();
        }
    }

    private Handler mHandler = new Handler();

    private void downloadImage(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Meizi";
            File dirFile=new File(dir);
            if(!dirFile.exists())dirFile.mkdir();
            String path = dir + File.separator + mUrl.substring(mUrl.lastIndexOf('/')+1);
            File file = new File(path);
            if (file.exists()) file.delete();
            ApiManager.getInstance().downloadFile(mUrl, new File(path), new IApiCallBack<String>() {
                @Override
                public void onError(Throwable throwable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MeiZiActivity.this,"download error!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                }

                @Override
                public void onSuccess(String data) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MeiZiActivity.this,"妹子图已经帮你保存好嘞！！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            downloadImage();
        }else {
            Snackbar.make(mFrameLayout,"You denied the permission to save image !",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        private int statY;
        private FrameLayout.LayoutParams mLayoutParams;
        private boolean isAnimation=false;
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
                    if(mTitleBarLayout.getAlpha()==1 && Math.abs(dy)>0&&!isAnimation){
                        mTitleBarLayout.animate()
                                .alpha(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        isAnimation = false;
                                    }
                                })
                                .setDuration(200).start();
                        isAnimation = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(mLayoutParams!=null) {
                        mLayoutParams.topMargin = 0;
                        mLayoutParams.bottomMargin = 0;
                        v.setLayoutParams(mLayoutParams);
                    }
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
