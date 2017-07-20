package com.ellactron.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.ellactron.http.volley.RestRequestFactory;
import com.ellactron.http.volley.VolleyRequestQueue;

/**
 * Created by ji.wang on 2017-07-10.
 */

public class RestService {
    protected static RequestQueue mQueue;
    protected RestRequestFactory requestFactory;
    protected Context context;

    public RestService(Context context) {
        this.context = context;
        if(null == mQueue)
            mQueue = VolleyRequestQueue.getInstance(context).getRequestQueue();
    }
}
