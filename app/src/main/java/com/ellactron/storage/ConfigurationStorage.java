package com.ellactron.storage;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ji.wang on 2017-07-13.
 */

public class ConfigurationStorage extends PrivateStorage{
    static ConfigurationStorage configurationStorage = null;
    static JSONObject configJson = null;

    ConfigurationStorage(Context context, String storagePath) {
        super(context, storagePath);
    }

    public static synchronized ConfigurationStorage getConfigurationStorage(Context context) throws IOException, JSONException {
        if(null == configurationStorage){
            configurationStorage = new ConfigurationStorage(context, "data");
            try {
                String configString = new String(configurationStorage.read(), "UTF-8");
                configJson = new JSONObject(configString);
            }
            catch(FileNotFoundException e){
                Log.w(ConfigurationStorage.class.getName(), "configuration file has not been created yet.");
                configJson = new JSONObject();
            }
        }

        return configurationStorage;
    }

    public void set(String key, Object value) throws JSONException, IOException {
        configJson.put(key, value);
        save(configJson.toString().getBytes());
    }

    public JSONObject getConfiguration() {
        return configJson;
    }

    public Object get(String key) throws JSONException {
        Object value= null;
        try {
            value = configJson.get(key);
        }
        catch(JSONException e){
            if(e.getMessage().equals("No value for " + key))
                Log.d(this.getClass().getName(), e.getMessage());
            else
                throw e;
        }
        return value;
    }

    public void remove(String key) throws IOException {
        configJson.remove(key);
        save(configJson.toString().getBytes());
    }
}
