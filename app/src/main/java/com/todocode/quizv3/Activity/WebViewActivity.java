package com.todocode.quizv3.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.todocode.quizv3.R;




public class WebViewActivity extends AppCompatActivity {

    private String postUrl = "https://www.google.com";
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downR;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.backdrop);

        if(!TextUtils.isEmpty(getIntent().getStringExtra("postUrl"))){
            postUrl = getIntent().getStringExtra("postUrl");
        }

        initWebView();
        initCollapsingToolbar();
        renderPost();
    }




//    private void initWebView() {
////        webView.setWebChromeClient(new MyWebChromeClient(this));
////        webView.setWebViewClient(new WebViewClient(){
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                if (Utilities.isSameDomain(postUrl, url)){
////                    Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
////                    intent.putExtra("postUrl", url);
////                    startActivity(intent);
////                }
////                else{
////                    //launch In-app Browser
////                    openInAppBrowser(url);
////                }
////                return true;
////            }
////
////            @Override
////            public void onPageFinished(WebView view, String url) {
////                super.onPageFinished(view, url);
////                progressBar.setVisibility(View.GONE);
////            }
////        });

        private void initWebView() {
            webView.setWebChromeClient(new MyWebChromeClient(this));
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    /**
                     * Check for the url, if the url is from same domain
                     * open the url in the same activity as new intent
                     * else pass the url to browser activity
                     * */
                    if (Utilities.isSameDomain(postUrl, url)) {
                        Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                        intent.putExtra("postUrl", url);
                        startActivity(intent);
                    } else {
                        // launch in-app browser i.e BrowserActivity
                        openInAppBrowser(url);
                    }

                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        m_downR = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        event.setLocation(m_downR, event.getY());
                    }
                    break;
                }
                return false;
            }

        });
    }

    private void openInAppBrowser(String url) {
        Intent intent = new Intent(WebViewActivity.this, BrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollLevel = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollLevel == -1){
                    scrollLevel = appBarLayout.getTotalScrollRange();

                }
                if(scrollLevel + i == 0){
                    collapsingToolbarLayout.setTitle("Web View");
                    isShow = true;
                }
                else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });


    }

    //Glide.

    private void renderPost() {
        webView.loadUrl(postUrl);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;
        public MyWebChromeClient(Context context){
            super();
            this.context = context;
        }
    }
}

