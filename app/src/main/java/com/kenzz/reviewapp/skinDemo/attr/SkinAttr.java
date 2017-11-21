package com.kenzz.reviewapp.skinDemo.attr;

import android.view.View;

/**
 * Created by huangdefa on 17/11/2017.
 * Version 1.0
 */

public class SkinAttr {
    //属性名称 eg:textColor
    String attrName;
    //属性值 eg:R.color.red=>red
    String attrValue;
    //资源值 eg:@12345678
    String attrResourceValue;

    SkinType skinType;

    public SkinAttr(String attrName, String attrValue, String attrResourceValue, SkinType skinType) {
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.attrResourceValue = attrResourceValue;
        this.skinType = skinType;
    }

    public void skin(View view){
        skinType.skin(view);
    }
}
