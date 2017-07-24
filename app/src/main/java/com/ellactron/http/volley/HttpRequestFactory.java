package com.ellactron.http.volley;

import android.content.Context;

import com.android.volley.Response;

import java.util.Map;

/**
 * Created by ji.wang on 2017-07-19.
 */

public class HttpRequestFactory {
    static Context context;

    public HttpRequestFactory(Context context) {
        setContext(context);
    }

    private synchronized void setContext(Context context) {
        if (null == RestRequestFactory.context)
            RestRequestFactory.context = context;
    }

    public synchronized VolleyStringRequest getHttpRequest(int method,
                                                             String serviceName,
                                                             Map<String, String> headers,
                                                             Map<String, String> data,
                                                             final Response.Listener<String> listener,
                                                             Response.ErrorListener errorListener) {
        return new VolleyStringRequest(
                method,
                serviceName,
                headers,
                data,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                errorListener
        );
    }
}
