package com.kenzz.reader.bean;

/**
 * Created by ken.huang on 11/29/2017.
 * OneBookViewModel
 */

public class OneBookViewModel {

    public String url;
    public String title;
    public float rate;

    public OneBookViewModel(String url, String title, float rate) {
        this.url = url;
        this.title = title;
        this.rate = rate;
    }

    public OneBookViewModel() {
    }
}
