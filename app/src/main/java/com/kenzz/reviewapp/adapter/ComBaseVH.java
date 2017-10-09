package com.kenzz.reviewapp.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by ken.huang on 10/9/2017.
 * ViewHolder的基类
 */

public class ComBaseVH extends RecyclerView.ViewHolder {
    private SparseArray<View> mSparseArray;

    public ComBaseVH(View itemView) {
        super(itemView);
        mSparseArray = new SparseArray<>();
    }

    public <V extends View> V getView(@IdRes int resId){
        View result=null;
        result = mSparseArray.get(resId);
        if(result==null){
            result = itemView.findViewById(resId);
            mSparseArray.put(resId,result);
        }
        return (V) result;
    }
}
