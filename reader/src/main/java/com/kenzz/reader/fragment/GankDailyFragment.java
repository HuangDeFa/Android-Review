package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzz.reader.Constant;
import com.kenzz.reader.MyApplication;
import com.kenzz.reader.R;
import com.kenzz.reader.adapter.GankDailyAdapter;
import com.kenzz.reader.bean.GankDailyEntity;
import com.kenzz.reader.bean.GankDailyModel;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.GankService;
import com.kenzz.reader.utils.SPUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankDailyFragment extends BaseFragment {

    private static final String TAG = GankDailyFragment.class.getSimpleName();
    @BindView(R.id.rv_gank_daily)
    RecyclerView mRecyclerView;

    private List<GankDailyModel> modelList = new ArrayList<>();
    private GankDailyAdapter mAdapter;
    private String[] date = new String[3];
    private String[] daily_date = new String[3];
    private List<Object> bannerDatas = new ArrayList<>();

    public GankDailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate--");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView--");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        date[0] = String.valueOf(calendar.get(Calendar.YEAR));
        date[1] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        date[2] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String daily_update = SPUtil.getString(MyApplication.getInstance(), Constant.DAILY_UPDATE);
        String last_daily_date = SPUtil.getString(MyApplication.getInstance(), Constant.LAST_DAILY_DATE);
        daily_date = last_daily_date.split("_");
        String result = String.format("%s_%s_%s", date[0], date[1], date[2]);
        List<String> bannerUrls = SPUtil.getStrings(MyApplication.getInstance(), Constant.BANNERURL);
        if (bannerUrls != null) {
            bannerDatas.addAll(bannerUrls);
        }
        if (!TextUtils.isEmpty(daily_update)) {
            if (!daily_update.equals(result)) {
                SPUtil.putString(MyApplication.getInstance(), result, Constant.DAILY_UPDATE);
                initBannerDatas();
            }
        } else {
            SPUtil.putString(MyApplication.getInstance(), result, Constant.DAILY_UPDATE);
            initBannerDatas();
        }

    }

    private void initBannerDatas() {
        ApiManager.getInstance()
                .getService(GankService.class)
                .getRandomGank("福利", 5)
                .flatMap(x -> {
                    if (!x.error) {
                        return Observable.fromIterable(x.results);
                    }
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    bannerDatas.clear();
                    addDisable(d);
                })
                .doOnComplete(() -> {
                    if (mAdapter != null && bannerDatas.size()>0) {
                        mAdapter.updateBannerData(bannerDatas);
                        List<String> list = Arrays.asList(bannerDatas.toArray(new String[bannerDatas.size()]));
                        SPUtil.putStrings(MyApplication.getInstance(),Constant.BANNERURL,list);
                    }
                })
                .subscribe(x -> bannerDatas.add(x.url));
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new GankDailyAdapter(modelList);
        mRecyclerView.setAdapter(mAdapter);
        Log.d(TAG, "onInitView--");
        if (modelList.size() == 0) {
            showLoadingPage();
        }
    }

    @Override
    protected void onLazyLoad() {
        loadData(date);
    }

    @Override
    protected void onVisible() {
        initDate();
    }

    private void loadData(String[] date) {
        ApiManager.getInstance().getService(GankService.class).
                getDailyGank(date[0], date[1], date[2])
                .subscribeOn(Schedulers.io())
                .map(x -> {
                    List<GankDailyModel> modelList = new ArrayList<>();
                    if (!x.error) {
                        GankDailyEntity.ResultsBean results = x.results;
                        if (results != null) {
                            if (results.Android != null) {
                                modelList.add(new GankDailyModel("Android", null));
                                modelList.add(new GankDailyModel(null, results.Android));
                            }
                            if (results.iOS != null) {
                                modelList.add(new GankDailyModel("IOS", null));
                                modelList.add(new GankDailyModel(null, results.iOS));
                            }
                            if (results.Welfare != null) {
                                modelList.add(new GankDailyModel("福利", null));
                                modelList.add(new GankDailyModel(null, results.Welfare));
                            }
                            if (results.Recommend != null) {
                                modelList.add(new GankDailyModel("瞎推荐", null));
                                modelList.add(new GankDailyModel(null, results.Recommend));
                            }
                            if (results.App != null) {
                                modelList.add(new GankDailyModel("应用", null));
                                modelList.add(new GankDailyModel(null, results.App));
                            }
                            if (results.FrontEnd != null) {
                                modelList.add(new GankDailyModel("前端", null));
                                modelList.add(new GankDailyModel(null, results.FrontEnd));
                            }
                            if (results.RestVideo != null) {
                                modelList.add(new GankDailyModel("小视频", null));
                                modelList.add(new GankDailyModel(null, results.RestVideo));
                            }
                            if (results.Resource != null) {
                                modelList.add(new GankDailyModel("拓展资源", null));
                                modelList.add(new GankDailyModel(null, results.Resource));
                            }
                        }
                    }
                    return modelList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankDailyModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisable(d);
                    }

                    @Override
                    public void onNext(List<GankDailyModel> gankDailyEntity) {
                        Log.d(TAG, "onInitRecyclerView--");
                        if (gankDailyEntity.size() > 0) {
                            gankDailyEntity.add(0, new GankDailyModel("head", null));
                            modelList = gankDailyEntity;
                            if (mRecyclerView != null) {
                                mAdapter.updateDataList(gankDailyEntity, bannerDatas);
                                hideLoadingPage();
                            }
                            String updateDaily = String.format("%s_%s_%s", date[0], date[1], date[2]);
                            SPUtil.putString(MyApplication.getInstance(), updateDaily, Constant.LAST_DAILY_DATE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mRecyclerView != null) {
                            showErrorPage();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (modelList.size() == 0) {
                            loadData(daily_date);
                        }
                    }
                });
    }

    @Override
    protected void onErrorRefresh() {
        super.onErrorRefresh();
        loadData(date);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank_daily;
    }

}
