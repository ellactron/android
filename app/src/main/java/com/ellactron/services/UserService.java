package com.ellactron.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.ellactron.http.volley.RestRequestFactory;
import com.ellactron.http.volley.VolleyJSONObjectRequest;
import com.ellactron.http.volley.VolleyStringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ellactron.activities.R;
/**
 * Created by ji.wang on 2017-07-10.
 */

public class UserService extends RestService{
    private VolleyJSONObjectRequest jsonRequest;
    private RestRequestFactory requestFactory;

    public UserService(Context context) {
        super(context);
        requestFactory = new RestRequestFactory(context);
    }

    public void register(String username,
                         String password,
                         final Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        final VolleyStringRequest jsonRequest = requestFactory.getStringRequest(
                Request.Method.POST,
                context.getString(R.string.register_service),
                null,
                params,
                listener,
                errorListener);
        jsonRequest.setTag("REQUEST_TAG");

        this.mQueue.add(jsonRequest);
    }

    public void getToken(String username,
                         String password,
                         final Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        final VolleyStringRequest jsonRequest = requestFactory.getStringRequest(
                Request.Method.POST,
                context.getString(R.string.token_service),
                null,
                params,
                listener,
                errorListener);
        jsonRequest.setTag("REQUEST_TAG");

        this.mQueue.add(jsonRequest);
    }
}
