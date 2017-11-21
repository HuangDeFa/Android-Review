package com.kenzz.reviewapp.skinDemo.attr;

import android.view.View;

/**
 * Created by huangdefa on 17/11/2017.
 * Version 1.0
 */

public enum SkinType {
    BACKGROUND("background"){
        @Override
        void skin(View view) {

        }
    },
    TEXTCOLOR("textColor"){
        @Override
        void skin(View view) {

        }
    },
    SRC("src"){
        @Override
        void skin(View view) {

        }
    };
    String skinType;
    SkinType(String type){
     this.skinType = type;
    }

    abstract void skin(View view);
}
