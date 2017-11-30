package com.kenzz.reader.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kenzz.reader.R;
import com.kenzz.reader.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    private static final String URL_KEY ="url_key";
    private static final String TITLE_KEY ="title_key";

    @BindView(R.id.wb_web_activity)
    WebView mWebView;
    @BindView(R.id.tbl_head)
    ViewGroup headLayout;
    @BindView(R.id.tv_toolbar_title)
    TextView mTextView;
    @BindView(R.id.tb_head)
    Toolbar mToolbar;

    private String url;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        initData();
        initView();
    }

    private void initView() {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>=90){
                    mTextView.setText(title);
                }else {
                    if(mTextView!=null)
                    mTextView.setText("加载......");
                }
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setDisplayZoomControls(false);
        mTextView.setText("加载......");
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        url = getIntent().getStringExtra(URL_KEY);
        title =getIntent().getStringExtra(TITLE_KEY);
    }

    public static void startActivity(Activity context,String url,String title){
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra(URL_KEY,url);
        intent.putExtra(TITLE_KEY,title);
        context.startActivity(intent);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_web;
    }
    @OnClick({R.id.iv_back})
    public void onBack(){
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reload:
                mWebView.reload();
                break;
            case R.id.copy_link:
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("webUrl",url));
                ToastUtil.showShortToast(this,"复制成功");
                break;
            case R.id.open_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
