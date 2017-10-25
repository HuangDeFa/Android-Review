package com.kenzz.gank.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ken.huang on 10/25/2017.
 * 添加加载更多功能
 */

public class SuperSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mTarget;

    public SuperSwipeRefreshLayout(Context context) {
        this(context,null);
    }

    public SuperSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
