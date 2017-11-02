package com.kenzz.gank.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kenzz.gank.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class GankWebActivity extends ToolbarBaseActivity {

   // @BindView(R.id.home_head_bar)
   // Toolbar mToolbar;
    @BindView(R.id.gank_webView)
    WebView mWebView;
   // @BindView(R.id.home_head_title)
   // TextView mTitleText;
   // @BindView(R.id.home_head_subtitle)
   // TextView mSubTitleText;
    @BindView(R.id.gank_webView_progressBar)
    ProgressBar mProgressBar;


    private String mURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_gank_web);
        setFullScreen();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        initView();
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }

    @Override
    protected boolean isNeedButterKnife() {
        return true;
    }

    private void initView() {
       /* int statusBarHeight = getStatusBarHeight();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        layoutParams.topMargin=statusBarHeight;*/
        String content=getIntent().getStringExtra("CONTENT");
        mToolbar.setTitle(content);
        mURL=getIntent().getStringExtra("URL");
        mToolbar.setSubtitle(mURL);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if(newProgress>=100){
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        mWebView.loadUrl(mURL);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_gank_web;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gank_web_menu,menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.web_refresh:
                mWebView.reload();
                break;
            case R.id.web_open_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                startActivity(intent);
                break;
            case R.id.web_copyLink:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Url", mWebView.getUrl());
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(findViewById(android.R.id.content), "Url Copied", Snackbar.LENGTH_SHORT).show();
                break;
        }
       return true;
    }
}
