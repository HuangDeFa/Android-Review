package com.kenzz.reviewapp.skinDemo.attr;

import android.view.View;

import java.util.List;

/**
 * Created by huangdefa on 17/11/2017.
 * Version 1.0
 */

public class SkinItem {
    View mview;
    List<SkinAttr> mSkinAttrs;

    public SkinItem(View mview, List<SkinAttr> skinAttrs) {
        this.mview = mview;
        mSkinAttrs = skinAttrs;
    }
    public void skin(){
        if(mview!=null && mSkinAttrs!=null && mSkinAttrs.size()>0){
            for (SkinAttr attr:
                 mSkinAttrs) {
                attr.skin(mview);
            }
        }
    }
}
