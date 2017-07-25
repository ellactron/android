package com.ellactron.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ellactron.configuration.AppConfiguration;
import com.ellactron.services.UIService;

public class HomeActivity extends WebViewBasedActivity {
    UIService uiService;

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

        uiService = new UIService(getApplicationContext());

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(uiService.getMainPage());
    }
}
