package com.ellactron.services;

import android.content.Context;

/**
 * Created by ji.wang on 2017-07-24.
 */

public class UIService extends RestService {
    public UIService(Context context) {
        super(context);
    }

    public String getMainPage() {
        return appConfiguration.getUIServiceBaseUrl() +"/static/main.html";
    }
}
