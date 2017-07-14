package com.ellactron.activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ellactron.services.UserService;
import com.ellactron.storage.ConfigurationStorage;
import com.facebook.internal.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationTest {
    final Object lock = new Object();
    final Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.ellactron.android", appContext.getPackageName());
    }

    @Test
    public void getFacebookAppId() throws Exception {
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getTargetContext();
        final String appId = Utility.getMetadataApplicationId(appContext);
        assertNotNull(appId);
    }

    @Test
    public void testRegister() throws InterruptedException, JSONException {
        final Object lock = new Object();
        final UserService userService = new UserService(appContext);
        userService.register("newuser@domain.com","pa55w0rd",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Assert.assertNotNull(response.getInt("Id"));
                        } catch (JSONException e) {
                            Log.d(this.getClass().getName(),(response).toString());
                            Log.d(this.getClass().getName(),e.getMessage());
                            fail();
                        }
                        synchronized(lock) {
                            lock.notify();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(this.getClass().getName(), error.getMessage());
                        fail();
                        synchronized(lock) {
                            lock.notify();
                        }
                    }
                });

        synchronized(lock) {
            lock.wait();
        }
    }

    @Test
    public void testGetToken() throws InterruptedException {
        getToken("username@domain.com", "pa55w0rd", new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), error.getMessage());
                fail();
                synchronized(lock) {
                    lock.notify();
                }
            }
        });
    }

    @Test
    public void testGetInvalidToken() throws InterruptedException {
        getToken("invalid@domain.com", "pa55w0rd", new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject errorObject = new JSONObject(new String(error.networkResponse.data, "UTF-8"));
                    Assert.assertEquals("Invalid credential",errorObject.getString("message"));
                } catch (Exception e) {
                    Log.d(this.getClass().getName(), error.getMessage());
                }

                synchronized(lock) {
                    lock.notify();
                }
            }
        });
    }

    public void getToken(String username, String password, Response.ErrorListener errorListener) throws InterruptedException {
        final UserService userService = new UserService(appContext);
        userService.getToken(username,password,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(this.getClass().getName(),(response).toString());
                        try {
                            Assert.assertNotNull(response.getString("token"));
                        } catch (JSONException e) {
                            Log.d(this.getClass().getName(),(response).toString());
                            Log.d(this.getClass().getName(),e.getMessage());
                            fail();
                        }
                        synchronized(lock) {
                            lock.notify();
                        }
                    }
                },
                errorListener);

        synchronized(lock) {
            lock.wait();
        }
    }

    @Test
    public void testConfigurationStorage() throws IOException, JSONException {
        ConfigurationStorage configurationStorage = ConfigurationStorage.getConfigurationStorage(appContext);
        configurationStorage.set("name1","value1");
        configurationStorage.set("name2","value2");

        Assert.assertTrue(configurationStorage.getConfiguration().has("name1"));
        Assert.assertTrue(configurationStorage.getConfiguration().has("name2"));
        Assert.assertEquals("value1",configurationStorage.getConfiguration().get("name1"));

        configurationStorage.remove("name1");
        Assert.assertFalse(configurationStorage.getConfiguration().has("name1"));

        configurationStorage.set("name2", "value2_changed");
        Assert.assertEquals("value2_changed",configurationStorage.getConfiguration().get("name2"));

        Log.d(this.getClass().getName(), configurationStorage.getConfiguration().toString());

        configurationStorage.delete();
    }
}
