package com.kenzz.reviewapp.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.adapter.ComBaseAdapter;
import com.kenzz.reviewapp.adapter.ComBaseVH;
import com.kenzz.reviewapp.fragment.Design_MainPageFragment;
import com.kenzz.reviewapp.fragment.Design_SecondPageFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DesignActivity extends BaseActivity {

   // @InjectView(R.id.design_toolbar)
   // Toolbar mToolbar;
    @InjectView(R.id.design_bottom_tab)
    TabLayout mTabLayout;

  //  @InjectView(R.id.design_appbar)AppBarLayout mBarLayout;
  //  @InjectView(R.id.design_toolbar_title)TextView mView;
    @InjectView(R.id.design_recyclerView)
    RecyclerView mRecyclerView;

    @IntDef({APPBARCLOSE,APPBARCOLLAPSING,APPBAREXPAND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppBarStatus{}

    final static int APPBAREXPAND=0;
    final static int APPBARCLOSE=1;
    final static int APPBARCOLLAPSING=2;

    private int toolBarColor;

    final static String TAG=DesignActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT) {
            getWindow().getAttributes().flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_design);
        initFragment();
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {

        //setSupportActionBar(mToolbar);
        toolBarColor = getResources().getColor(R.color.colorPrimary);
        mTabLayout.addTab(mTabLayout.newTab().setText("暴漫"));
        mTabLayout.addTab(mTabLayout.newTab().setText("肯打鸡"));
        mTabLayout.addTab(mTabLayout.newTab().setText("小黑屋"));
        mTabLayout.addTab(mTabLayout.newTab().setText("王尼玛"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       /* mBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG,"verticalOffset: "+verticalOffset);
                float scale = Math.abs(verticalOffset)/ (float)(appBarLayout.getHeight() -mToolbar.getHeight());
                Log.d(TAG,"Scale: "+scale);
                float alpha=Color.alpha(toolBarColor)*scale;
                int argb = Color.argb((int) alpha, Color.red(toolBarColor), Color.green(toolBarColor), Color.blue(toolBarColor));
                mToolbar.setBackgroundColor(argb);
                mView.setAlpha(scale);
            }
        });
        */

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private Paint mPaint;
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int left=parent.getPaddingLeft();
                int right=parent.getWidth();
                int childCount=parent.getChildCount();
                View child;
                for(int i=0;i<childCount;i++){
                    child = parent.getChildAt(i);
                    if(child.getVisibility()==View.GONE){
                        continue;
                    }
                 c.drawRect(left,child.getBottom()-20,right,child.getBottom(),mPaint);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,20);
                mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(Color.GRAY);
            }
        });

        final List<String> dataList=new ArrayList<String>(){{
            add("Item1");
            add("Item2");
            add("Item3");
            add("Item4");
            add("Item5");
            add("Item6");
            add("Item7");
            add("Item7");
            add("Item7");
            add("Item7");
            add("Item7");
            add("Item7");
            add("Item7");
        }};

        mRecyclerView.setAdapter(new ComBaseAdapter(dataList) {
            @Override
            public View createItemView(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).
                        inflate(android.R.layout.simple_list_item_1,parent,false);
                return view;
            }

            @Override
            public void onBindView(ComBaseVH holder, int position) {
                ((TextView)holder.itemView).setText(dataList.get(position));
            }
        });
    }

    private int currentIndex;
    private Design_MainPageFragment mainPageFragment;
    private Design_SecondPageFragment secondFragment;
    private Design_SecondPageFragment thirdFragment;
    private Design_SecondPageFragment forthFragment;

    private void initFragment(){
        mainPageFragment = Design_MainPageFragment.newInstance("", "");
        secondFragment = Design_SecondPageFragment.newInstance("second","");
        thirdFragment = Design_SecondPageFragment.newInstance("third","");
        forthFragment = Design_SecondPageFragment.newInstance("forth","");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.design_page_content,mainPageFragment,"main_page")
                .commit();
        currentIndex = 0;
    }

    private void switchFragment(int index){
     if(currentIndex==index) return;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        switch (index){
            case 0:
                transaction.show(mainPageFragment);
                break;
            case 1:
                if(secondFragment.isAdded()){
                    transaction.show(secondFragment);
                }else {
                    transaction.add(R.id.design_page_content, secondFragment, "second");
                }
                break;
            case 2:
                if(thirdFragment.isAdded()){
                    transaction.show(thirdFragment);
                }else {
                    transaction.add(R.id.design_page_content, thirdFragment, "third");
                }
                break;
            case 3:
                if(forthFragment.isAdded()){
                    transaction.show(forthFragment);
                }else {
                    transaction.add(R.id.design_page_content, forthFragment, "forth");
                }
                break;
        }

        switch (currentIndex){
            case 0:
                transaction.hide(mainPageFragment);
                break;
            case 1:
                transaction.hide(secondFragment);
                break;
            case 2:
                transaction.hide(thirdFragment);
                break;
            case 3:
                transaction.hide(forthFragment);
                break;
        }

        transaction.commit();
        currentIndex = index;
    }
}
