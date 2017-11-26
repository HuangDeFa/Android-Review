package com.kenzz.reader.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kenzz.reader.R;

/**
 * Created by ken.huang on 11/24/2017.
 * DefaultBannerLoader
 */

public class DefaultBannerLoader implements Banner.IBannerDataViewLoader {
    private Context mContext;
    public DefaultBannerLoader(Context context){
        this.mContext = context;
    }

    @Override
    public View loadView(ViewGroup parent, Object bannerData) {
        ImageView imageView=new ImageView(mContext);
        return imageView;
    }

    @Override
    public void dataBind(View view,Object bannerData) {
        Glide.with(mContext)
                .asDrawable()
                .load(bannerData)
                .apply(new RequestOptions().placeholder(R.mipmap.img_two_bi_one).centerCrop())
                .into((ImageView) view);
    }
}
