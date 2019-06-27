package com.example.mohamed.lostchilds.View.news;

import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;

public class MyBrowser extends WebViewClient {
    private ProgressDialog progressDialog;

    public MyBrowser(ProgressDialog progressDialog) {
        this.progressDialog=progressDialog;
        progressDialog.show();
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        progressDialog.dismiss();
    }
}