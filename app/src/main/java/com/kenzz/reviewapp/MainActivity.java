package com.kenzz.reviewapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenzz.reviewapp.activity.BaseActivity;
import com.kenzz.reviewapp.activity.BroadCastReceiverActivity;
import com.kenzz.reviewapp.activity.DesignActivity;
import com.kenzz.reviewapp.activity.DrawableActivity;
import com.kenzz.reviewapp.activity.GlideActivity;
import com.kenzz.reviewapp.activity.QQContactListActivity;
import com.kenzz.reviewapp.activity.RemoteViewsActivity;
import com.kenzz.reviewapp.activity.ServiceActivity;
import com.kenzz.reviewapp.activity.SkinActivity;
import com.kenzz.reviewapp.activity.SocketActivity;
import com.kenzz.reviewapp.activity.SpannableActivity;
import com.kenzz.reviewapp.activity.ViewLearningActivity;
import com.kenzz.reviewapp.adapter.ComBaseAdapter;
import com.kenzz.reviewapp.adapter.ComBaseVH;
import com.kenzz.reviewapp.bean.DaggerTestComponent;
import com.kenzz.reviewapp.bean.TestComponent;
import com.kenzz.reviewapp.bean.TestModule;
import com.kenzz.reviewapp.bean.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.BindView;
import dagger.Lazy;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_my_recyclerView)
    RecyclerView mRecyclerView;
    private List<String> mList=new ArrayList<>();

    //For Dagger2 依赖注入
    @Inject
    @Named("wnm")
    User mUser;

    @Inject
    @Named("wnmF")
    User mOtherUser;

  //  @Inject
  //  Lazy<User> mUserLazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initDependencyObject();
    }

    private void initData(){
        mList.add("View的学习和总结");
        mList.add("SupportDesign View的学习和总结");
        mList.add("仿QQ联系人列表");
        mList.add("BroadcastReceiver广播接收者");
        mList.add("Service服务");
        mList.add("RemoteViews");
        mList.add("Drawable Demo");
        mList.add("SocketDemo");
        mList.add("换肤Demo");
        mList.add("Glide ActivityDemo");
        mList.add("SpannableString Demo");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
               outRect.set(20,20,20,20);
            }
        });
        mRecyclerView.setAdapter(new ComBaseAdapter<String>(mList) {
            @Override
            public View createItemView(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return view;
            }

            @Override
            public void onBindView(ComBaseVH holder, final int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setBackgroundColor(Color.GRAY);
                textView.setText(mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (position){
                            case 0:
                                startActivity(new Intent(MainActivity.this, ViewLearningActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this, DesignActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, QQContactListActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this, BroadCastReceiverActivity.class));
                                break;
                            case 4:
                                startActivity(new Intent(MainActivity.this, ServiceActivity.class));
                                break;
                            case 5:
                                startActivity(new Intent(MainActivity.this, RemoteViewsActivity.class));
                                break;
                            case 6:
                                startActivity(new Intent(MainActivity.this, DrawableActivity.class));
                                break;
                            case 7:
                                startActivity(new Intent(MainActivity.this, SocketActivity.class));
                                break;
                            case 8:
                                startActivity(new Intent(MainActivity.this, SkinActivity.class));
                                break;
                            case 9:
                                startActivity(new Intent(MainActivity.this, GlideActivity.class));
                                break;
                            case 10:
                                startActivity(new Intent(MainActivity.this,SpannableActivity.class));
                                break;
                                default:
                                    break;
                        }
                    }
                });
            }
        });
    }

    private void initDependencyObject() {
        TestComponent build = DaggerTestComponent.builder().
                baseComponent(((MyApplication)getApplication()).
                        getBaseComponent())
                .testModule(new TestModule()).build();
        build.inject(this);

        Log.d("Dagger","The person is: "+mUser.getName()+", "+mOtherUser.getName());
    }

    //动态代理模式
    private <T> T getProxy(Class<T> clazz ){
       return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
    }

}
