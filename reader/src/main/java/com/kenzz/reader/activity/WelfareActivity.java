package com.kenzz.reader.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.kenzz.reader.R;
import com.kenzz.reader.http.DownloadListener;
import com.kenzz.reader.http.DownloadManager;
import com.kenzz.reader.http.DownloadService;
import com.kenzz.reader.utils.ImageLoader;
import com.kenzz.reader.utils.ToastUtil;
import com.kenzz.reader.widget.SuperViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class WelfareActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_welfare_activity)
    SuperViewPager mViewPager;
    @BindView(R.id.tv_welfare_activity)
    TextView mTextView;
    @BindView(R.id.tb_head)
    Toolbar mToolbar;
    @BindView(R.id.status_view)
    View mView;
    @BindView(R.id.ll_bar)
    LinearLayout headLayout;
    @BindView(R.id.tv_toolbar_title)
    TextView titleText;

    final static String IMAGE_URL = "image_url";
    final static String START_INDEX = "start_index";
    private List<Map.Entry<String,String>> imageUrls = new ArrayList<>();
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        initData();
        initView();
    }

    private void initData() {
        imageUrls.clear();
        imageUrls.addAll(getIntent().getParcelableArrayListExtra(IMAGE_URL));
        currentIndex = getIntent().getIntExtra(START_INDEX, 0);
    }

    private void initView() {
        int statusBarHeight = getStatusBarHeight(this);
        mView.getLayoutParams().height=statusBarHeight;
        mView.setVisibility(View.VISIBLE);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new WelfarePageAdapter());
        mViewPager.setCurrentItem(currentIndex, true);
        mViewPager.addOnPageChangeListener(this);
        mTextView.setText(String.format("%s/%s", currentIndex + 1, imageUrls.size()));
        titleText.setText(imageUrls.get(currentIndex).getValue());
    }

    public static void startActivity(Activity activity, ArrayList<Map.Entry<String,String>> imageUrls, int start) {
        Intent intent = new Intent(activity, WelfareActivity.class);
        intent.putExtra(IMAGE_URL,imageUrls);
        intent.putExtra(START_INDEX, start);
        activity.startActivity(intent);
    }

    private List<ImageView> imageViewCache = new ArrayList<>(10);

    public ImageView obtainImageView() {
        PhotoView result = null;
        for (View imageView : imageViewCache) {
            if (imageView.getParent() == null) {
                result = (PhotoView) imageView;
                break;
            }
        }
        if (result == null) {
            result = new PhotoView(this);
            result.setBackgroundColor(Color.BLACK);
            result.setOnPhotoTapListener((view, x, y) -> {
                if (mTextView.getAlpha() == 0.f) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
                    final int originalHeight = (int) mTextView.getTag();
                    ViewGroup.LayoutParams lp2 = headLayout.getLayoutParams();
                    final int originalHeight2= (int) headLayout.getTag();
                    ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
                            .setDuration(350);
                    animator.addUpdateListener(animation -> {
                        float value = (float) animation.getAnimatedValue();
                        mTextView.setAlpha(value);
                        lp.height = (int) (originalHeight * value);
                        mTextView.setLayoutParams(lp);
                        headLayout.setAlpha(value);
                        lp2.height= (int) (originalHeight2*value);
                    });
                    animator.start();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
                    final int originalHeight = lp.height;
                    mTextView.setTag(originalHeight);
                    ViewGroup.LayoutParams lp2 = headLayout.getLayoutParams();
                    final int originalHeight2= headLayout.getMeasuredHeight();
                    headLayout.setTag(originalHeight2);

                    ValueAnimator animator = ValueAnimator.ofFloat(1f, 0)
                            .setDuration(350);
                    animator.addUpdateListener(animation -> {
                        float value = (float) animation.getAnimatedValue();
                        mTextView.setAlpha(value);
                        lp.height = (int) (originalHeight * value);
                        mTextView.setLayoutParams(lp);
                        headLayout.setAlpha(value);
                        lp2.height= (int) (originalHeight2*value);
                    });
                    animator.start();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
             imageViewCache.add(result);
        }
        return result;
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_welfare;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        mTextView.setText(String.format("%s/%s", currentIndex + 1, imageUrls.size()));
        titleText.setText(imageUrls.get(currentIndex).getValue());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class WelfarePageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = obtainImageView();
            ImageLoader.LoadImage(imageView, imageUrls.get(position).getKey(), R.mipmap.img_four_bi_three);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welfare_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // ToastUtil.showShortToast(this, "you click menu-" + item.getTitle());
        switch (item.getItemId()){
            case R.id.copy_link:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Url", imageUrls.get(currentIndex).getKey());
                clipboardManager.setPrimaryClip(clipData);
                ToastUtil.showShortToast(this,"复制成功");
                break;
            case R.id.share:
                ToastUtil.showShortToast(this, "you click menu-" + item.getTitle());
                break;
            case R.id.open_browser:
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(imageUrls.get(currentIndex).getKey()));
                startActivity(intent);
                break;
            case R.id.download_file:
                String path =DownloadManager.getDefaultDirPath("meizi", Calendar.getInstance().getTimeInMillis()+".jpg");
                DownloadManager.downloadFile(imageUrls.get(currentIndex).getKey(),
                        path, new DownloadListener() {
                            @Override
                            public void onError(Throwable error) {
                                ToastUtil.showShortToast(WelfareActivity.this,error.getMessage());
                            }

                            @Override
                            public void onSuccess() {
                                ToastUtil.showShortToast(WelfareActivity.this,"妹子存盘成功！！");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_back})
    public void onBack(View view){
        onBackPressed();
    }

    @VisibleForTesting
    public void testDownload(){
        OkHttpClient client = new  OkHttpClient.Builder()
                .build();
        Retrofit.Builder builder=new Retrofit.Builder();
        DownloadService service =
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl("http://aa.example.com/api/")
                        .client(client)
                        .build()
                        .create(DownloadService.class);
        service.downloadFile("http://ww2.sinaimg.cn/large/7a8aed7bjw1ewym3nctp0j20i60qon23.jpg")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        System.out.println(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete!!");
                    }
                });
    }
}
