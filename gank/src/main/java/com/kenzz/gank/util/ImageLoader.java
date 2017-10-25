package com.kenzz.gank.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by ken.huang on 10/25/2017.
 *
 */

public class ImageLoader {
    private ImageLoader(){}
    private static ImageLoader sImageLoader;
    public static ImageLoader getInstance(){
        if(sImageLoader==null){
            synchronized (ImageLoader.class){
                if(sImageLoader==null){
                    sImageLoader = new ImageLoader();
                }
            }
        }
        return sImageLoader;
    }

    public void loadImage(Context context, ImageView imageView,String url){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(imageView);
    }

    public void loadImage(Activity activity, ImageView imageView, String url){
        Glide.with(activity)
                .load(url)
                .into(imageView);
    }

    public void loadImage(Fragment fragment, ImageView imageView, String url){
        Glide.with(fragment)
                .load(url)
                .into(imageView);
    }


}
