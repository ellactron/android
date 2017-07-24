package com.ellactron.http.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by ji.wang on 2017-07-11.
 */

public class VolleyStringRequest extends StringRequest {
    static Context context;
    Map<String, String> params;
    Map<String, String> headers = new HashMap<String, String>();;

    VolleyStringRequest(int method,
                               String serviceUrl,
                               Map<String, String> headers,
                               Map<String, String> params,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method,
                serviceUrl,
                listener, errorListener);

        if(null != params)
            this.params=params;

        this.headers.put("Accept", "application/json; charset=utf-8");
        if(null != headers)
            this.headers.putAll(headers);

        VolleyLog.d("Adding request: %s",
                serviceUrl);
    }

    VolleyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() {
        if(null != params)
            this.headers.put("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        return params;
    }
}
