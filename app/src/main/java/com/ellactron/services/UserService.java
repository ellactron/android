package com.ellactron.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.ellactron.activities.R;
import com.ellactron.configuration.AppConfiguration;
import com.ellactron.http.volley.RestRequestFactory;
import com.ellactron.http.volley.VolleyStringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ji.wang on 2017-07-10.
 */

public class UserService extends RestService {
    public UserService(Context context) {
        super(context);
    }

    protected String getUserServiceURL(int serviceId) {
        return appConfiguration.getUserServiceBaseUrl() +
                context.getString(serviceId);
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
                getUserServiceURL(R.string.register_service),
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
                getUserServiceURL(R.string.token_service),
                null,
                params,
                listener,
                errorListener);
        jsonRequest.setTag("REQUEST_TAG");

        this.mQueue.add(jsonRequest);
    }

    public void getSiteTokenByOAuth2Token(String oauth2token,
                                          final Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener) {
        final VolleyStringRequest jsonRequest = requestFactory.getStringRequest(
                Request.Method.PUT,
                getUserServiceURL(R.string.facebook_auth) + "/" + oauth2token,
                null,
                null,
                listener,
                errorListener);
        jsonRequest.setTag("REQUEST_TAG");

        this.mQueue.add(jsonRequest);
    }
}
