package com.kenzz.reader.fragment;


import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kenzz.reader.Constant;
import com.kenzz.reader.MyApplication;
import com.kenzz.reader.R;
import com.kenzz.reader.activity.WebActivity;
import com.kenzz.reader.adapter.BaseRecyclerViewAdapter;
import com.kenzz.reader.adapter.GankDailyAdapter;
import com.kenzz.reader.adapter.GankIOAdapter;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.GankService;
import com.kenzz.reader.utils.SPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankIOFragment extends BaseFragment implements OnLoadmoreListener {


    public GankIOFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.smartRL_gank_io)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_gank_io)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_gank_io_type)
    TextView mTextView;
    @BindView(R.id.ll_changeType)
    LinearLayout mChangeTypeLayout;
    PopupWindow mPopupWindow;

    HashMap<String,List<GankEntity.ResultsBean>> dataList=new HashMap<>();
    String currentType;
    GankIOAdapter mAdapter;
    String[] gankIOTypes={"iOS","休息视频","拓展资源","前端","all"};
    HashMap<String,Integer> pageIndexMap=new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void initView() {
     mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
     mRecyclerView.setItemAnimator(new DefaultItemAnimator());
     List<GankEntity.ResultsBean> list = dataList.get(currentType);
     if(list==null){
         if(checkNetConnected())
         showLoadingPage();
         else showErrorPage();
     }
     mAdapter = new GankIOAdapter(list);
     mAdapter.setListener((view, position) -> {
         GankEntity.ResultsBean data = dataList.get(currentType).get(position);
         WebActivity.startActivity(getActivity(),data.url,data.desc);
     });
     mRecyclerView.setAdapter(mAdapter);
     mRefreshLayout.setOnLoadmoreListener(this);
     mTextView.setText(currentType);
     mChangeTypeLayout.setOnClickListener(v -> showPopWindow());
    }

    @Override
    protected void onLazyLoad() {
        currentType =SPUtil.getString(MyApplication.getInstance(), Constant.LAST_GANK_IO_TYPE);
        int page = pageIndexMap.get(currentType)==null?1:pageIndexMap.get(currentType);
        loadData(page);
        if(mTextView!=null)
        mTextView.setText(currentType);
    }

    private void loadData(int page){
        if(!checkNetConnected())return;
        ApiManager.getInstance().getService(GankService.class)
                .getGankDayByPage(currentType,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::addDisable)
                .doOnError(x-> {
                            if(mRefreshLayout!=null)
                                showErrorPage();
                        }
                )
                .subscribe(x->{
                    List<GankEntity.ResultsBean> list = dataList.get(currentType);
                    if(!x.error){
                        if(list==null){
                            list = x.results;
                        }else {
                            list.addAll(x.results);
                        }
                        dataList.put(currentType,list);
                    }
                    if(mRefreshLayout!=null) {
                        hideLoadingPage();
                        mRefreshLayout.finishLoadmore();
                        mAdapter.updateDataList(list);
                    }
                });
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_gank_io;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(!checkNetConnected()){
            mRefreshLayout.finishLoadmore();
            return;
        }
        int page = pageIndexMap.get(currentType)==null?1:pageIndexMap.get(currentType);
        loadData(++page);
        pageIndexMap.put(currentType,page);
    }



    RecyclerView gankTypeRV;

    private void showPopWindow(){
        if(mPopupWindow==null){
           mPopupWindow =new PopupWindow(getContext());
           View contentView = getLayoutInflater().inflate(R.layout.popwindow_change_type,mChangeTypeLayout,false);
           gankTypeRV = contentView.findViewById(R.id.rv_gank_io_pop_window);
           gankTypeRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
           gankTypeRV.setItemAnimator(new DefaultItemAnimator());
           gankTypeRV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
           gankTypeRV.setAdapter(gankTypeAdapter);
          // contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST),
           //        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST));
           mPopupWindow.setWidth(contentView.getLayoutParams().width);
           mPopupWindow.setContentView(contentView);
           mPopupWindow.setFocusable(true);
           mPopupWindow.setOutsideTouchable(true);
           //注意：设置动画时如果动画集合中有Alpha动画 最好放到集合最后执行要不然会出现闪烁。
           mPopupWindow.setAnimationStyle(R.style.PopWindowAnimStyle);
        }
        if(mPopupWindow.isShowing())return;
        int[] location=new int[2];
        //获取控件相对于Window的坐标x,y
        mChangeTypeLayout.getLocationInWindow(location);
        //showAtLocation 比showAsDropDown要灵活，第一参数是用来获取Token的并不是意义上的依附或者锚点，
        //Gravity设置相对于Window的原点坐标系 比如这里Right|Top就设置咯右上角为原点
        mPopupWindow.showAtLocation(mChangeTypeLayout, Gravity.RIGHT|Gravity.TOP,dpToPx(18),location[1]+dpToPx(20));
       // mPopupWindow.showAsDropDown(mChangeTypeLayout,-dpToPx(20),0);
    }

    private BaseRecyclerViewAdapter gankTypeAdapter=new BaseRecyclerViewAdapter<String,GankDailyAdapter.GankDailyVH>() {

        @Override
        public int getItemCount() {
            return gankIOTypes.length;
        }

        @Override
        public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView=getLayoutInflater().inflate(R.layout.item_gank_io_change_type,parent,false);
            return new GankDailyAdapter.GankDailyVH(itemView);
        }

        @Override
        public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
             TextView textView =holder.getView(R.id.tv_item_gank_io_change_type);
            ImageView imageView =holder.getView(R.id.iv_item_gank_io_change_type);
            String data=gankIOTypes[position];
            if(currentType.equals(data)){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }
            textView.setText(data);
            holder.itemView.setOnClickListener(v -> {
                currentType = data;
                mTextView.setText(currentType);
                mTextView.setScaleX(0);
                mTextView.animate()
                         .scaleX(1)
                         .rotationY(360)
                         .setDuration(500)
                         .start();

                notifyDataSetChanged();
                mPopupWindow.dismiss();
                triggerTypeChange();
            });
        }
    };

    @UiThread
    private void triggerTypeChange() {
        List<GankEntity.ResultsBean> list = dataList.get(currentType);
        int page = pageIndexMap.get(currentType)==null?1:pageIndexMap.get(currentType);
        if(list==null && checkNetConnected()){
            showLoadingPage();
            loadData(page);
        }else{
           mAdapter.updateDataList(list);
        }
    }

    private int dpToPx(float dp){
        float density = getResources().getDisplayMetrics().density;
        return (int) (density*dp+.5f);
    }

    @Override
    protected void onErrorRefresh() {
        if(!checkNetConnected())return;
        super.onErrorRefresh();
        int page = pageIndexMap.get(currentType)==null?1:pageIndexMap.get(currentType);
        loadData(page);
    }
}
