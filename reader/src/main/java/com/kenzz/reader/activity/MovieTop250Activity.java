package com.kenzz.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.adapter.BaseRecyclerViewAdapter;
import com.kenzz.reader.adapter.GankDailyAdapter;
import com.kenzz.reader.bean.MovieEntity;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.DouService;
import com.kenzz.reader.utils.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieTop250Activity extends BaseActivity implements OnLoadmoreListener {

    @BindView(R.id.tb_head)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView titleView;

    @BindView(R.id.ll_fragment_content_error)
    LinearLayout errorLayout;
    @BindView(R.id.ll_fragment_content_loading)
    LinearLayout loadingLayout;
    private ImageView loadingImageVew;
    @BindView(R.id.fragment_content)
    FrameLayout viewContainer;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<MovieEntity.Subject> movieList=new ArrayList<>();
    private int offset;
    private MovieAdapter mMovieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
           getSupportActionBar().setDisplayShowTitleEnabled(false);
           getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        initView();
    }

    private void initView() {
        viewContainer.removeAllViews();
        mRefreshLayout=new SmartRefreshLayout(this);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableAutoLoadmore(true);
        mRefreshLayout.setOnLoadmoreListener(this);
        mRefreshLayout.setPrimaryColorsId(R.color.colorPrimary);
        viewContainer.addView(mRefreshLayout,new FrameLayout.LayoutParams(-1,-1));
        loadingImageVew = (ImageView) loadingLayout.getChildAt(0);

        mRecyclerView=new RecyclerView(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        mMovieAdapter=new MovieAdapter(movieList);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRefreshLayout.addView(mRecyclerView,new SmartRefreshLayout.LayoutParams(-1,-1));
        titleView.setText("电影TOP 250");
        showLoadingPage();
        loadData();
    }

    public static void startActivity(Activity activity){
        activity.startActivity(new Intent(activity,MovieTop250Activity.class));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_movie_top250;
    }

    public void showErrorPage(){
        errorLayout.setVisibility(View.VISIBLE);
        viewContainer.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    public void hideErrorPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    public void showLoadingPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) loadingImageVew.getDrawable();
        drawable.start();
    }



    public void hideLoadingPage(){
        errorLayout.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        AnimationDrawable drawable = (AnimationDrawable) loadingImageVew.getDrawable();
        drawable.stop();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
       offset=offset+movieList.size();
       loadData();
    }
    private Disposable mDisposable;
    private void loadData(){
        ApiManager.getInstance().getService(DouService.class)
                .getTop250Movie(offset,20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                      mDisposable=d;
                      if(movieList.size()==0){
                          showLoadingPage();
                      }
                    }

                    @Override
                    public void onNext(MovieEntity entity) {
                       if(entity.subjects!=null && entity.subjects.size()>0){
                           movieList.addAll(entity.subjects);
                       }
                       hideLoadingPage();
                    }

                    @Override
                    public void onError(Throwable e) {
                      showErrorPage();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable!=null && !mDisposable.isDisposed())
        mDisposable.dispose();
    }

    @OnClick({R.id.iv_back})
    public void onBack(){
        onBackPressed();
    }
    @OnClick({R.id.iv_fragment_content_error})
    public void onErrorRefresh(){
        loadData();
    }

    private class MovieAdapter extends BaseRecyclerViewAdapter<MovieEntity.Subject,GankDailyAdapter.GankDailyVH>{

        public MovieAdapter(List<MovieEntity.Subject> dataList) {
         this.dataList=dataList;
        }

        @Override
        public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item_one_book,parent,false);
            return new GankDailyAdapter.GankDailyVH(view);
        }

        @Override
        public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
            MovieEntity.Subject data=this.dataList.get(position);
            ImageView imageView=holder.getView(R.id.iv_item_one_book);
            TextView title=holder.getView(R.id.tv_item_one_title);
            TextView rate=holder.getView(R.id.tv_item_one_rate);
            ImageLoader.LoadImage(imageView,data.images.medium,R.mipmap.img_default_movie);
            title.setText(data.title);
            rate.setText(String.valueOf(data.rating.average));
        }
    }
}
