package com.kenzz.skinlibrary.attr;

import android.view.View;

/**
 * Created by ken.huang on 11/3/2017.
 * version:1.0
 * author:ken
 * description:需要更换的属性
 */

public abstract class SkinAttr {

    private String attrType;

    private String attrValue;

    abstract void applySkin(View view);
    String resourceName;
    abstract void skin(View view);
}
