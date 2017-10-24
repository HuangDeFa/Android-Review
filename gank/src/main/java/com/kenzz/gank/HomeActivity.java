package com.kenzz.gank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kenzz.gank.activity.BaseActivity;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
