package com.kenzz.gank.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by ken.huang on 11/2/2017.
 * author:ken
 * version:1.0
 * description:支持空页面和网络失败页面
 */

public class SuperRecyclerView extends RecyclerView {

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(mDataObserver);
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (getAdapter().getItemCount() < 3) {
                showSateView();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (itemCount < 3) {
                showSateView();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (itemCount < 3) {
                showSateView();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (itemCount < 3) {
                showSateView();
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (itemCount < 3) {
                showSateView();
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (itemCount < 3) {
                showSateView();
            }
        }
    };

    /**
     * 设置空页面
     *
     * @param view
     */
    public void setEmptyView(View view) {

    }

    public void setEmptyViewResource(@LayoutRes int layResId) {
        View emptyView = LayoutInflater.from(getContext()).inflate(layResId, this, false);

    }

    public void setNetWorkErrorView(View view) {

    }

    public void setNetWorkErrorView(@LayoutRes int layResId) {
        View netWorkErrorView = LayoutInflater.from(getContext()).inflate(layResId, this, false);
    }

    public void setNetWorkError(boolean netWorkError) {
        isNetWorkError = netWorkError;
    }

    private boolean isNetWorkError = false;

    /**
     * 展示空页面或是网络错误页面
     */
    private void showSateView() {
        //网络错误且当前Adapter没有数据才显示错误页面
        if (isNetWorkError) {
            //显示错误页面

        } else {

        }
    }

}
