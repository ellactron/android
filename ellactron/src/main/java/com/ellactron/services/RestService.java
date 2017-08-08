package com.ellactron.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.ellactron.configuration.AppConfiguration;
import com.ellactron.http.volley.RestRequestFactory;
import com.ellactron.http.volley.VolleyRequestQueue;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by ji.wang on 2017-07-10.
 */

public class RestService {
    protected static RequestQueue mQueue;
    protected RestRequestFactory requestFactory;
    protected Context context;
    protected AppConfiguration appConfiguration;

    public RestService(Context context) {
        this.context = context;
        if (null == mQueue)
            mQueue = getRequestQueue();//VolleyRequestQueue.getInstance(context).getRequestQueue();

        requestFactory = new RestRequestFactory(context);
        appConfiguration = AppConfiguration.CreateConfiguration(context);
    }

    public RequestQueue getRequestQueue() {
        RequestQueue mQueue;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            HttpStack stack = new HurlStack(null, HttpsURLConnection.getDefaultSSLSocketFactory());
            mQueue = Volley.newRequestQueue(context, stack);
        } else {
            mQueue = Volley.newRequestQueue(context);
        }

        return mQueue;
    }
}
