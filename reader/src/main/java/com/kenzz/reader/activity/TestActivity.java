package com.kenzz.reader.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kenzz.reader.R;
import com.kenzz.reader.widget.Banner;
import com.kenzz.reader.widget.DefaultBannerLoader;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Banner mBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mBanner = findViewById(R.id.testBanner);
        List<Object> bannerDatas=new ArrayList<>();
        bannerDatas.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171109095254_dOw5qh_bluenamchu_9_11_2017_9_52_47_256.jpeg");
        bannerDatas.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
        bannerDatas.add("http://ww2.sinaimg.cn/large/7a8aed7bjw1ewym3nctp0j20i60qon23.jpg");
        bannerDatas.add("http://ww4.sinaimg.cn/large/7a8aed7bgw1eujhfwyj27j20qo0hs0vy.jpg");
        bannerDatas.add("http://ww3.sinaimg.cn/large/7a8aed7bgw1ewy3cst6rzj20lx0v4wj5.jpg");
        mBanner.setDataAndLoader(bannerDatas,new DefaultBannerLoader(this));
    }
}
