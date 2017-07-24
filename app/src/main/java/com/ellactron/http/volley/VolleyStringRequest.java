package com.ellactron.http.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ellactron.activities.R;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by ji.wang on 2017-07-11.
 */

public class VolleyStringRequest extends StringRequest {
    final static String PROTOCOL_SCHEME="https://";
    final static String SERVICE_PORT="8443";

    static Context context;
    Map<String, String> params;
    Map<String, String> headers = new HashMap<String, String>();;

    /*public String getBaseRestUrl() {
        return getBaseUrl() + Resources.getSystem().getString(R.string.rest_base);
    }*/

    /*public String getRestUrl() {
        return Resources.getSystem().getString(R.string.base_url);
    }*/

    private static String getBaseUrl(Context context) {
        VolleyStringRequest.context = context;
        return  PROTOCOL_SCHEME + context.getString(R.string.hostname)+ ":" +SERVICE_PORT;
    }

    public static String getBaseUrl() {
        return getBaseUrl(VolleyStringRequest.context);
    }

    VolleyStringRequest(Context context,
                               int method,
                               String serviceUrl,
                               Map<String, String> headers,
                               Map<String, String> params,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method,
                getBaseUrl(context) + serviceUrl,
                listener, errorListener);

        if(null != params)
            this.params=params;

        this.headers.put("Accept", "application/json; charset=utf-8");
        if(null != headers)
            this.headers.putAll(headers);

        VolleyLog.d("Adding request: %s",
                getBaseUrl() + serviceUrl);
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
