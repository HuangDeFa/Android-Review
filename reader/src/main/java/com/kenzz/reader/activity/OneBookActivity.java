package com.kenzz.reader.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.bean.OneBookDetailViewModel;
import com.kenzz.reader.bean.OneBookEntity;
import com.kenzz.reader.utils.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

public class OneBookActivity extends BaseActivity {

    @BindView(R.id.iv_activity_book)
    ImageView mImageView;
    @BindView(R.id.tv_book_activity_author)
    TextView authorText;
    @BindView(R.id.tv_book_activity_rate)
    TextView rateText;
    @BindView(R.id.tv_book_activity_publishAt)
    TextView publishAtText;
    @BindView(R.id.tv_book_activity_publishTime)
    TextView publishTimeText;
    @BindView(R.id.tv_book_activity_summary)
    TextView summaryText;
    @BindView(R.id.tv_book_activity_author_info)
    TextView authorInfoText;
    @BindView(R.id.tv_book_activity_catalog)
    TextView bookCatalogText;
    @BindView(R.id.fl_activity_book_head)
    FrameLayout contentHead;
    @BindView(R.id.ll_title_bar)
    LinearLayout titleBarLayout;
    @BindView(R.id.tv_toolbar_title)
    TextView titleView;
    @BindView(R.id.tv_toolbar_subtitle)
    TextView subTitleView;
    @BindView(R.id.tb_head)
    Toolbar mToolbar;
    @BindView(R.id.status_view)
    View mView;
    @BindView(R.id.sv_activity_book)
    NestedScrollView mNestedScrollView;

    private OneBookDetailViewModel mViewModel;
    private final static String BOOK_KEY = "book_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setFullScreen();
        initView();
    }

    private void initView() {
       mViewModel = getIntent().getParcelableExtra(BOOK_KEY);
        ImageLoader.LoadImage(mImageView,mViewModel.imageUrl,R.mipmap.img_default_book);
        titleView.setText(mViewModel.title);
        subTitleView.setText(String.format("%s%s","作者：",mViewModel.authors==null?"佚名":mViewModel.authors.get(0)));
        subTitleView.setVisibility(View.VISIBLE);

        publishAtText.setText(String.format("%s%s","出版社：", mViewModel.publisher));
        publishTimeText.setText(String.format("%s%s","出版社时间：",mViewModel.publishTime));
        summaryText.setText(mViewModel.summary);
        authorInfoText.setText(mViewModel.authorInfo);
        bookCatalogText.setText(mViewModel.catalog);

        authorText.setText(String.format("%s%s","作者：",mViewModel.authors==null?"佚名":mViewModel.authors.get(0)));
        String s="评分："+mViewModel.averageRate;
        String s1=s+" "+mViewModel.raters +"人评分";
        SpannableString spannableString=new SpannableString(s1);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),0,s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        rateText.setText(spannableString);
        titleBarLayout.measure(0,0);
        int titleBarHeight = titleBarLayout.getMeasuredHeight();
        int statusBarHeight = getStatusBarHeight(this);
        //占据状态栏
        mView.getLayoutParams().height=statusBarHeight;
        mView.setVisibility(View.VISIBLE);
        //设置Toolbar透明
        titleBarLayout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        mToolbar.setBackground(new ColorDrawable(Color.TRANSPARENT));
        //增加内容的padding 延伸至全屏
        contentHead.setPadding(contentHead.getPaddingLeft(),titleBarHeight+statusBarHeight*2,
                contentHead.getPaddingRight(),contentHead.getPaddingBottom());
        //设置ScrollView滚动监听 TODO：计算Toolbar的背景颜色
    }

    public static void startActivity(Activity context, OneBookEntity.Books books, View sharedView) {
        OneBookDetailViewModel model = OneBookDetailViewModel.build(books);
        Intent intent = new Intent(context, OneBookActivity.class);
        intent.putExtra(BOOK_KEY, model);
        Bundle bundle = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (sharedView != null) {
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(context, sharedView, "share_view");
                bundle = options.toBundle();
            }
        }
        context.startActivity(intent, bundle);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_one_book;
    }

    @OnClick({R.id.iv_back})
    public void onBack(){
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.book_more_detail){
            WebActivity.startActivity(this,mViewModel.url,mViewModel.title);
        }
        return super.onOptionsItemSelected(item);
    }
}
