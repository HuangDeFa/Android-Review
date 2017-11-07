package com.kenzz.gank.ndk;

/**
 * Created by ken.huang on 11/6/2017.
 *
 */

public class HelloSample {
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
}
