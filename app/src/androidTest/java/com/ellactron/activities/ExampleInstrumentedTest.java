package com.ellactron.activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ellactron.services.UserService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    final Object lock = new Object();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ellactron.android", appContext.getPackageName());
    }

    @Test
    public void testRegister() throws InterruptedException, JSONException {
        final Object lock = new Object();
        final UserService userService = new UserService(InstrumentationRegistry.getTargetContext());
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
        final UserService userService = new UserService(InstrumentationRegistry.getTargetContext());
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
}
