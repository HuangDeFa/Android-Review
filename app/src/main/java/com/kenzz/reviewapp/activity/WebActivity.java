package com.kenzz.reviewapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.kenzz.reviewapp.R;

public class WebActivity extends BaseActivity {

    private WebView mWebView;
    private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mFrameLayout=findViewById(R.id.web_page_content);
        mWebView=new WebView(this);
        mFrameLayout.addView(mWebView,new FrameLayout.LayoutParams(-1,-1));
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //设置可以无限放大
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        mWebView.addJavascriptInterface(new WebJsBridgeObject(this),"Control");

        mWebView.setWebChromeClient(new WebChromeClient(){

            //加载进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFrameLayout.removeView(mWebView);
        mWebView.destroy();
    }

    @Override
    public void onBackPressed() {
        if(mWebView!=null){
           if(mWebView.canGoBack()){
               mWebView.goBack();
           }else {
               super.onBackPressed();
           }
        }else {
            super.onBackPressed();
        }
    }
}


class WebJsBridgeObject{
    private Context mContext;
    public WebJsBridgeObject(Context context) {
     mContext=context;
    }

    @JavascriptInterface
    public void showMessage(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(mContext);
        builder.setMessage("hello WongNiMa")
                .setCancelable(true)
                .setTitle("Welcome")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
