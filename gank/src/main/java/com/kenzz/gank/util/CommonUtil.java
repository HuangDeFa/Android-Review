package com.kenzz.gank.util;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * Created by ken.huang on 10/26/2017.
 *
 */

public class CommonUtil {
    public static void doAnimation(View view){
        view.setAlpha(0);
        view.setScaleX(0.6f);
        ViewCompat.animate(view)
                .alpha(1)
                .scaleX(1.0f)
                .setDuration(800)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }
}
