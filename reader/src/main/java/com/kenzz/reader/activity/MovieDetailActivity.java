package com.kenzz.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.adapter.BaseRecyclerViewAdapter;
import com.kenzz.reader.adapter.GankDailyAdapter;
import com.kenzz.reader.bean.MovieDetailEntity;
import com.kenzz.reader.bean.MovieViewModel;
import com.kenzz.reader.http.ApiManager;
import com.kenzz.reader.http.DouService;
import com.kenzz.reader.utils.ImageLoader;
import com.kenzz.reader.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends BaseActivity {

    @BindView(R.id.ll_title_bar)
    ViewGroup titleBarLayout;
    @BindView(R.id.tb_head)
    Toolbar toolBar;
    @BindView(R.id.status_view)
    View mView;
    @BindView(R.id.ll_movie_activity_content_container)
    LinearLayout contentLayout;
    @BindView(R.id.rl_activity_movie_content)
    RelativeLayout movieHeadLayout;
    @BindView(R.id.iv_activity_movie_background)
    ImageView backgroundImage;
    @BindView(R.id.iv_activity_movie)
    ImageView movieImage;
    @BindView(R.id.tv_toolbar_subtitle)
    TextView subTitle;
    @BindView(R.id.tv_toolbar_title)
    TextView title;
    @BindView(R.id.tv_movie_activity_rate)
    TextView rate;
    @BindView(R.id.tv_movie_activity_director)
    TextView director;
    @BindView(R.id.tv_movie_activity_cast)
    TextView cast;
    @BindView(R.id.tv_movie_activity_type)
    TextView movieType;
    @BindView(R.id.tv_movie_activity_showtime)
    TextView showTime;
    @BindView(R.id.tv_movie_activity_country)
    TextView country;
    @BindView(R.id.tv_movie_activity_summary)
    TextView movieSummary;
    @BindView(R.id.tv_movie_activity_names)
    TextView movieNames;
    @BindView(R.id.sv_activity_movie)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.rv_movie_member)
    RecyclerView mRecyclerView;
    private static final String SUBJECT_KEY="subject_key";
    private MovieViewModel mMovieViewModel;
    private MovieDetailEntity mMovieDetailEntity;
    private boolean hasSetBackgroundImage;
    private MovieMemberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setSupportActionBar(toolBar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        initData();
        initView();
    }

    private void initData() {
      mMovieViewModel = getIntent().getParcelableExtra(SUBJECT_KEY);
      loadData();
    }
    private Disposable mDisposable;
    private void loadData() {
        ApiManager.getInstance().getService(DouService.class)
                .getMovieDetail(mMovieViewModel.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                       mDisposable=d;
                    }

                    @Override
                    public void onNext(MovieDetailEntity movieDetail) {
                       if(movieDetail!=null){
                        mMovieDetailEntity=movieDetail;
                        expandMovieDetail();
                       }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShortToast(MovieDetailActivity.this,e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void expandMovieDetail() {
        StringBuilder sb=new StringBuilder();
        for (String s : mMovieDetailEntity.countries) {
            sb.append(s);
            sb.append("/");
        }
        country.setText(String.format("%s%s","制片国家/地区：",sb.substring(0,sb.length()-1)));
        if(mMovieDetailEntity.aka!=null &&mMovieDetailEntity.aka.size()>0){
            sb=new StringBuilder();
            for (String s : mMovieDetailEntity.aka) {
                sb.append(s);
                sb.append("/");
            }
            movieNames.setText(sb.subSequence(0,sb.length()-1));
        }
        movieSummary.setText(mMovieDetailEntity.summary);
        List<CastModel> dataList=new ArrayList<>();
        if(mMovieDetailEntity.casts!=null && mMovieDetailEntity.casts.size()>0){
            for (MovieDetailEntity.Casts cast : mMovieDetailEntity.casts) {
                dataList.add(new CastModel(cast,1));
            }
        }
        if(mMovieDetailEntity.directors!=null && mMovieDetailEntity.directors.size()>0){
            for (MovieDetailEntity.Casts cast : mMovieDetailEntity.directors) {
                dataList.add(new CastModel(cast,0));
            }
        }
      mAdapter.updatDataList(dataList);
    }

    private void initView() {
        titleBarLayout.measure(0,0);
        int titleBarHeight = titleBarLayout.getMeasuredHeight();
        int statusBarHeight = getStatusBarHeight(this);
        //占据状态栏
        mView.getLayoutParams().height=statusBarHeight;
        mView.setVisibility(View.VISIBLE);
        titleBarLayout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        toolBar.setBackground(new ColorDrawable(Color.TRANSPARENT));

        ((ViewGroup.MarginLayoutParams)movieHeadLayout.getLayoutParams()).topMargin=titleBarHeight+statusBarHeight*2;

        title.setText(mMovieViewModel.title);
        subTitle.setText(mMovieViewModel.casts);
        subTitle.setVisibility(View.VISIBLE);
        ImageLoader.LoadImage(movieImage,mMovieViewModel.imageUrl,R.mipmap.img_default_movie);
        String original=String.format("%s %s","评分:"+mMovieViewModel.rate,mMovieViewModel.rateNum+"人评分");
        SpannableString spannableString=new SpannableString(original);
        ForegroundColorSpan span=new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        spannableString.setSpan(span,0,original.indexOf(mMovieViewModel.rateNum),0);
        rate.setText(spannableString);

        director.setText(String.format("%s%s","导演：",mMovieViewModel.directors));
        cast.setText(String.format("%s%s","主演：",mMovieViewModel.casts));
        movieType.setText(String.format("%s%s","类型：",mMovieViewModel.type));
        showTime.setText(String.format("%s%s","上映时间：",mMovieViewModel.year));
        country.setText("制片国家/地区：");

        ImageLoader.LoadImageAsBackground(backgroundImage,mMovieViewModel.imageUrl,R.drawable.blue_bg);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!hasSetBackgroundImage){
                    final Drawable drawable = backgroundImage.getDrawable();
                    Drawable imageDrawable;
                    if(drawable instanceof BitmapDrawable){
                      imageDrawable = new BitmapDrawable(getResources(),((BitmapDrawable)drawable).getBitmap());
                    }else {
                        Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        drawable.draw(new Canvas(bitmap));
                        imageDrawable = new BitmapDrawable(getResources(),bitmap);
                    }
                    titleBarLayout.setBackground(imageDrawable);
                    hasSetBackgroundImage =true;
                }
                float alpha = scrollY*1.0f/(titleBarHeight+statusBarHeight);
                alpha = alpha*255;
                if(alpha>=255f){
                    titleBarLayout.getBackground().setAlpha(255);
                }else {
                    titleBarLayout.getBackground().setAlpha((int)alpha);
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter=new MovieMemberAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void startActivity(Activity activity, MovieViewModel subject, View shareView){
        Intent intent=new Intent(activity,MovieDetailActivity.class);
        intent.putExtra(SUBJECT_KEY,subject);
        Bundle bundle=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ActivityOptionsCompat option =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shareView, "share_view");
            bundle =option.toBundle();
        }
        activity.startActivity(intent,bundle);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_movie_detail;
    }

    @OnClick({R.id.iv_back})
    public void onBack(){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable!=null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    private class CastModel{
        MovieDetailEntity.Casts cast;
        int type;

        public CastModel(MovieDetailEntity.Casts cast, int type) {
            this.cast = cast;
            this.type = type;
        }
    }
    private class MovieMemberAdapter extends BaseRecyclerViewAdapter<CastModel,GankDailyAdapter.GankDailyVH>{

        public void updatDataList(List<CastModel> dataList)
        {
            this.dataList=dataList;
            notifyDataSetChanged();
        }

        @Override
        public GankDailyAdapter.GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
           View view= getLayoutInflater().inflate(R.layout.item_movie_member,parent,false);
            return new GankDailyAdapter.GankDailyVH(view);
        }

        @Override
        public void onBindViewHolder(GankDailyAdapter.GankDailyVH holder, int position) {
            CastModel data=dataList.get(position);
            ImageView imageView=holder.getView(R.id.iv_item_movie_member);
            TextView type=holder.getView(R.id.tv_item_movie_member_type);
            TextView name=holder.getView(R.id.tv_item_movie_member_name);
            String typeValue=data.type==0?"导演":"演员";
            type.setText(typeValue);
            ImageLoader.LoadImage(imageView,data.cast.avatars.small,R.drawable.blue_bg);
            name.setText(data.cast.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(MovieDetailActivity.this,data.cast.alt,data.cast.name);
                }
            });
        }
    }
}
