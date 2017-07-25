package com.ellactron.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.ellactron.configuration.AppConfiguration;
import com.ellactron.http.volley.RestRequestFactory;
import com.ellactron.http.volley.VolleyRequestQueue;

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
            mQueue = VolleyRequestQueue.getInstance(context).getRequestQueue();

        requestFactory = new RestRequestFactory(context);
        appConfiguration = AppConfiguration.CreateConfiguration(context);
    }
}
