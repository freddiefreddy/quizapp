package com.todocode.quizv3.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.todocode.quizv3.R;

public class BrowserActivity extends AppCompatActivity {


    private String url;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downR;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        url = getIntent().getStringExtra("url");

        if(TextUtils.isEmpty(url)){
            finish();
        }

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        initializeWebView();
        webView.loadUrl(url);
    }

    private void initializeWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener(){

            public boolean onTouch(View v, MotionEvent event) {
                if(event.getPointerCount()>1){
                    return true;
                }
                switch (event.getAction()){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add items in action bar if present
        getMenuInflater().inflate(R.menu.browser, menu);

        if (Utilities.isBookmarked(this, webView.getUrl())) {
            // change icon color
            Utilities.tintMenuIcon(getApplicationContext(), menu.getItem(0), R.color.colorAccent);
        } else {
            Utilities.tintMenuIcon(getApplicationContext(), menu.getItem(0), android.R.color.white);
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!webView.canGoBack()){
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        }
        else{
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        if(!webView.canGoForward()){
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        }
        else{
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_bookmark) {
            // bookmark / unbookmark the url
            Utilities.bookmarkUrl(this, webView.getUrl());

            String msg = Utilities.isBookmarked(this, webView.getUrl()) ?
                    webView.getTitle() + "is Bookmarked!" :
                    webView.getTitle() + " removed!";
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            snackbar.show();

            // refresh the toolbar icons, so that bookmark icon color changes
            // depending on bookmark status
            invalidateOptionsMenu();
        }

        if(item.getItemId() == R.id.action_back){
            back();
        }

        if(item.getItemId() == R.id.action_forward){
            forward();
        }

        return super.onOptionsItemSelected(item);
    }

    private void forward() {
        if(webView.canGoForward()){
            webView.goForward();
        }
    }

    private void back() {
        if(webView.canGoBack()){
            webView.goBack();
        }
    }

    private class MyWebChromeClient extends WebChromeClient{
        Context context;

        public  MyWebChromeClient(Context context){
            super();
            this.context = context;
        }

    }
}