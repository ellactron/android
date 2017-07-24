package com.ellactron.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class HomeActivity extends WebViewBasedActivity {
    @Override
    protected int getActivityId() {
        return R.layout.activity_home;
    }

    @Override
    void addWebView(WebView mWebView) {
        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.homeBaseLayout);
        baseLayout.addView(mWebView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://"+ getApplicationContext().getString(R.string.hostname)+":8443/rest/v1/user");
    }
}
