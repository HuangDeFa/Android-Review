package com.kenzz.reviewapp.util;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;

/**
 * Created by ken.huang on 2/24/2018.
 * description:当使用startActivityForResult时经常需要在Activity的
 * onActivityResult的回调中进行判断和结果的处理，这里提供一个简单的工具类
 * 避免在Activity中处理回调
 */

public class ActivityResultUtil {
    private static final String TAG="ActivityResultFragment";
    private ActivityResultFragment mResultFragment;

    public ActivityResultUtil(FragmentActivity activity){
        mResultFragment=getActivityResultFragment(activity);
    }

    private ActivityResultFragment getActivityResultFragment(FragmentActivity activity) {
        mResultFragment=findFragment(activity);
        if(mResultFragment==null){
            mResultFragment=new ActivityResultFragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(mResultFragment,TAG)
                    .commitAllowingStateLoss();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
        return mResultFragment;
    }

    public ActivityResultUtil(Fragment fragment){
        this(fragment.getActivity());
    }

    public void startActivityForResult(Intent intent, int requestCode, ActivityResultCallBack callBack){
       mResultFragment.startActivityForResult(intent,requestCode,callBack);
    }

    public void startActivityForResult(Class<?> clazz,int requestCode, ActivityResultCallBack callBack){
        Intent intent=new Intent(mResultFragment.getContext(),clazz);
        startActivityForResult(intent,requestCode,callBack);
    }

    private ActivityResultFragment findFragment(FragmentActivity activity){
        return (ActivityResultFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }


    static interface ActivityResultCallBack{
        void onResult(int requestCode, int resultCode,Intent data);
    }

    public static class ActivityResultFragment extends Fragment{
        HashMap<Integer,ActivityResultCallBack> mResultCallBacks=new HashMap<>();


        public void startActivityForResult(Intent intent, int requestCode, ActivityResultCallBack callBack){
              mResultCallBacks.put(requestCode,callBack);
              startActivityForResult(intent,requestCode);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ActivityResultCallBack callBack = mResultCallBacks.remove(requestCode);
            if(callBack!=null){
                callBack.onResult(requestCode,resultCode,data);
            }
        }
    }
}
