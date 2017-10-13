package com.kenzz.reviewapp.widget;

import android.content.ContentUris;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ken.huang on 10/13/2017.
 */

public class SimpleBehavior extends CoordinatorLayout.Behavior<View> {

    public SimpleBehavior() {
    }

    public SimpleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.scrollTo(0, dependency.getScrollY());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return (dependency instanceof NestedScrollView);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        consumed[0] = 0;
        consumed[1] = 0;
        child.scrollTo(0, target.getScrollY());
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {

        return true;
    }

    int getScrollRange(ViewGroup view) {
        int scrollRange = 0;
        if (view.getChildCount() > 0) {
            View child = view.getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (view.getHeight() - view.getPaddingBottom() - view.getPaddingTop()));
        }
        return scrollRange;
    }

}
