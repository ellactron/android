package com.ellactron.http.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ji.wang on 2017-07-11.
 */

public class RestRequestFactory extends HttpRequestFactory{
    public RestRequestFactory(Context context) {
        super(context);
    }

    private synchronized void setContext(Context context) {
        if (null == RestRequestFactory.context)
            RestRequestFactory.context = context;
    }

    public synchronized VolleyJSONObjectRequest getJSONObjectRequest(int method,
                                                                     String serviceName,
                                                                     Map<String, String> headers,
                                                                     JSONObject params,
                                                                     Response.Listener<JSONObject> listener,
                                                                     Response.ErrorListener errorListener) {
        return new VolleyJSONObjectRequest(
                context,
                method,
                serviceName,
                headers,
                params,
                listener,
                errorListener
        );
    }

    public synchronized VolleyStringRequest getStringRequest(int method,
                                                             String serviceName,
                                                             Map<String, String> headers,
                                                             Map<String, String> data,
                                                             final Response.Listener<JSONObject> listener,
                                                             Response.ErrorListener errorListener) {
        return new VolleyStringRequest(
                context,
                method,
                serviceName,
                headers,
                data,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            listener.onResponse(new JSONObject(response));
                        } catch (JSONException e) {
                            Log.d(this.getClass().getName(), e.getMessage());
                        }
                    }
                },
                errorListener
        );
    }
}
