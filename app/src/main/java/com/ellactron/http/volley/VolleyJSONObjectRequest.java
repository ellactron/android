package com.ellactron.http.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ellactron.activities.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ji.wang on 2017-06-08.
 */

public class VolleyJSONObjectRequest extends JsonObjectRequest {
    final static String PROTOCOL_SCHEME="https://";
    final static String SERVICE_PORT="8443";

    static Context context;
    Map<String, String> headers = new HashMap<String, String>();

    /*public String getBaseRestUrl() {
        return getRestUrl() + Resources.getSystem().getString(R.string.rest_base);
    }

    public String getRestUrl() {
        return Resources.getSystem().getString(R.string.base_url);
    }*/

    private static String getBaseUrl(Context context) {
        VolleyStringRequest.context = context;
        return  PROTOCOL_SCHEME + context.getString(R.string.hostname) + ":" +SERVICE_PORT;
    }

    public static String getBaseUrl() {
        return getBaseUrl(VolleyStringRequest.context);
    }

    VolleyJSONObjectRequest(Context context,
                            int method,
                            String serviceUrl,
                            Map<String, String> headers,
                            JSONObject params,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(method,
                getBaseUrl(context) + serviceUrl,
                params,
                listener,
                errorListener);

        this.headers.put("Accept", "application/json; charset=utf-8");
        if (null != headers)
            this.headers.putAll(headers);

        VolleyLog.d("Adding request: %s",
                getBaseUrl() + serviceUrl);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headers.putAll(super.getHeaders());
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}
