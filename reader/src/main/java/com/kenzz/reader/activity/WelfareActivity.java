package com.kenzz.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.kenzz.reader.R;
import com.kenzz.reader.utils.ImageLoader;
import com.kenzz.reader.widget.SuperViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WelfareActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_welfare_activity)
    SuperViewPager mViewPager;
    @BindView(R.id.tv_welfare_activity)
    TextView mTextView;

    final static String IMAGE_URL = "image_url";
    final static String START_INDEX="start_index";
    private List<String> imageUrls = new ArrayList<>();
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        imageUrls.clear();
        imageUrls.addAll(getIntent().getStringArrayListExtra(IMAGE_URL));
        currentIndex=getIntent().getIntExtra(START_INDEX,0);
    }

    private void initView() {
      mViewPager.setOffscreenPageLimit(2);
      mViewPager.setAdapter(new WelfarePageAdapter());
      mViewPager.setCurrentItem(currentIndex,true);
      mViewPager.addOnPageChangeListener(this);
      mTextView.setText(String.format("%s/%s",currentIndex+1,imageUrls.size()));
    }

    public static void startActivity(Activity activity, ArrayList<String> imageUrls, int start) {
        Intent intent = new Intent(activity, WelfareActivity.class);
        intent.putStringArrayListExtra(IMAGE_URL, imageUrls);
        intent.putExtra(START_INDEX,start);
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
            result.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    if(mTextView.getAlpha()==0.f){
                       Animation animation = AnimationUtils.
                               loadAnimation(WelfareActivity.this,R.anim.pop_in_bottom);
                        mTextView.startAnimation(animation);
                    }else {
                        Animation animation = AnimationUtils.
                                loadAnimation(WelfareActivity.this,R.anim.pop_out_bottom);
                        mTextView.startAnimation(animation);
                    }
                }
            });
           // imageViewCache.add(result);
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
        mTextView.setText(String.format("%s/%s",currentIndex+1,imageUrls.size()));
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
            ImageLoader.LoadImage(imageView, imageUrls.get(position), R.mipmap.img_four_bi_three);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
