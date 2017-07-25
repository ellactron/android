package com.ellactron.activities;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ji.wang on 2017-07-19.
 */

public abstract class WebViewBasedActivity extends BaseActivity {
    WebView mWebView = null;
    final String[] localResources = {/*"/static/","/js/", "/local/","/images/"*/};

    AssetManager assetManager;

    abstract void addWebView(WebView mWebView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetManager = getAssets();
        addWebView(createWebView());
    }

    private WebView createWebView() {
        mWebView = new WebView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & getApplicationInfo().FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }

        mWebView.setWebViewClient(new WebViewClient() {
            /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                if(req.getUrl().toString().contains(hostAddress)){
                    req.getRequestHeaders().put("Authorization", "Bearer "+token);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, req);
            }*/

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                if (req.getUrl().toString().contains(getResources().getString(R.string.hostname))) {
                    for(String localResource: localResources){
                        if (req.getUrl().toString().contains(localResource)) {
                            try {
                                return localRequest(req, localResource);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }
                    return interceptRequest(req);
                } else
                    return super.shouldInterceptRequest(view, req);
            }
        });

        return mWebView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse interceptRequest(WebResourceRequest req) {
        try {
            URL url = new URL(req.getUrl().toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod(req.getMethod().toUpperCase());
            conn.setRequestProperty("Authorization", "Bearer " + getSiteToken());

            switch (req.getMethod().toUpperCase()) {
                case "POST":
                case "PUT":
                    conn.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    //wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();
                    break;
                case "GET":
                case "DELETE":
                default:
                    break;
            }

            InputStream in = null;
            int responseCode = conn.getResponseCode();
            if (responseCode != 200)
                in = conn.getErrorStream();
            else {
                in = conn.getInputStream();
            }
            String contentTypeValue = "text/html; charset=UTF-8";//conn.getContentType();
            String encodingValue = conn.getContentEncoding();
            return new WebResourceResponse(contentTypeValue, encodingValue, in);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse localRequest(WebResourceRequest req, String localResource) throws IOException {
        String path = req.getUrl().toString();
        path = path.substring(path.indexOf(localResource)+1);
        InputStream raw = assetManager.open(path);

        return new WebResourceResponse(getMimeType(path), "UTF-8", raw);
    }

    private String getMimeType(String path) {
        String ext = path.substring(path.lastIndexOf('.'));
        switch(ext){
            case ".html":
            case ".htm":
                return "text/html";
            case ".css":
                return "text/css";
            case ".js":
                return "application/javascript";
            case ".png":
                return "image/png";
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".gif":
                return "image/gif";
            case ".xhtml":
                return "application/xhtml+xml";
            case ".xml":
                return "application/xml";
            case ".json":
                return "application/json";
            case ".mpeg":
                return "video/mpeg";
            case ".aac":
                return "audio/aac";
            case ".mid":
            case ".midi":
                return "audio/midi";
            case ".wav":
                return "audio/x-wav";
            default:
                return null;
        }
    }

    private int getResourceId(String resName, Class<?> c) {
        try {
            int id = this.getResources().getIdentifier(resName,
                    "drawable",
                    this.getPackageName());
            if (id == 0 && resName.contains(".")) {
                id = this.getResources().getIdentifier(resName.substring(0,
                        resName.indexOf(".")),
                        "drawable",
                        this.getPackageName());
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
