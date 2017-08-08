package com.ellactron.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ellactron.helpers.ParameterredCallback;
import com.ellactron.http.security.HttpSecurityContext;
import com.ellactron.services.UserService;
import com.ellactron.services.auth.FacebookSignIn;
import com.ellactron.storage.ConfigurationStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private final int SIGN_UP_ACTIVITY_RETURN_CODE = 1;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    Context context = null;
    UserService userService = null;

    private void init() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, UnrecoverableKeyException {
        context = this.getApplication().getApplicationContext();
        userService = new UserService(getApplicationContext());
        HttpSecurityContext.InitSSLContext();
    }

    public void setCredential(String email, String password) {
        if (null != mEmailView) {
            mEmailView.setText(email);
        }
        if (null != mPasswordView) {
            mPasswordView.setText(password);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            init();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
            System.exit(1);
        }
        initialOAuth2Sdk();

        setContentView(R.layout.activity_login);

        // 注册登录按钮
        registerLogInButon();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        TextView mSignUp = (TextView) findViewById(R.id.textSignUp);
        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpWindow();
            }
        });

        try {
            showMainWindow();
        } catch (Exception e) {
            Log.d(this.getClass().getName(), e.getMessage());
            return;
        }
    }

    public void showSignUpWindow() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("activity", this.getClass().getCanonicalName());
        startActivityForResult(intent, SIGN_UP_ACTIVITY_RETURN_CODE);
    }


    // 添加 Facebook login 回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case SIGN_UP_ACTIVITY_RETURN_CODE:
                try {
                    JSONObject credentialObject = new JSONObject(intent.getStringExtra("credential"));
                    JSONObject accountObject = credentialObject.getJSONObject("account");
                    setCredential(accountObject.getString("username"), accountObject.getString("password"));
                    // TODO: Automatically login
                    attemptLogin();
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }
            default:
                if (null != fb)
                    fb.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * OAuth2: Facebook
     */
    static FacebookSignIn fb;

    private void initialOAuth2Sdk() {
        if (null == fb) {
            fb = new FacebookSignIn();
        }
        fb.initialFacebookSdk(this);
    }

    /*private boolean isUserLoggedIn() {
        return (null == fb) ? false : (null != fb.getFacebookProfile());
    }*/

    private void registerLogInButon() {
        // 注册登录成功后回调函数
        if (null != fb) {
            fb.registerSignInButton(new ParameterredCallback<String, Void>() {
                @Override
                public Void call(String accessToken) throws IOException, JSONException, InterruptedException {
                    showMainWindow();
                    return null;
                }
            });
        }
    }

    private void storeSiteToken(String siteToken) throws IOException, JSONException {
        ConfigurationStorage storage = ConfigurationStorage.getConfigurationStorage(context);
        storage.set("token", siteToken);
    }

    public void getTokenByOAuth2(String accessToken,
                                 final ParameterredCallback<String, Void> onAuthenticationSuccess,
                                 final ParameterredCallback<Exception, Void> onAuthenticationFailed) throws InterruptedException {
        userService.getSiteTokenByOAuth2Token(accessToken,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(this.getClass().getName(), (response).toString());
                        try {
                            String siteToken = response.getString("token");
                            onAuthenticationSuccess.call(siteToken);
                        } catch (Exception e) {
                            Log.d(this.getClass().getName(), (response).toString());
                            Log.d(this.getClass().getName(), e.getMessage());
                            try {
                                onAuthenticationFailed.call(e);
                            } catch (Exception e1) {
                                Log.d(this.getClass().getName(), e1.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            onAuthenticationFailed.call(error);
                        } catch (Exception e) {
                            Log.d(this.getClass().getName(), e.getMessage());
                        }
                    }
                });
    }

    public void showMainWindow() throws IOException, JSONException, InterruptedException {
        Object lock;
        String token = (String) ConfigurationStorage.getConfigurationStorage(context).get("token");
        if (null == token) {
            String accessToken = (null == fb) ? null : fb.getAccessToken();
            if (null != accessToken) {
                getTokenByOAuth2(accessToken,
                        new ParameterredCallback<String, Void>() {
                            @Override
                            public Void call(String siteToken) throws Exception {
                                storeSiteToken(siteToken);
                                showMainWindow();
                                return null;
                            }
                        },
                        new ParameterredCallback<Exception, Void>() {
                            @Override
                            public Void call(Exception exception) {
                                mEmailView.setError(exception.getMessage());
                                mEmailView.requestFocus();
                                return null;
                            }
                        });
            }
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        final JSONObject[] registerResponse = {null};
        final Exception[] exceptions = {null};

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                login(mEmail, mPassword);
            } catch (InterruptedException e) {
                Log.d(this.getClass().getName(), e.getMessage());
                exceptions[0] = e;
            } catch (JSONException e) {
                Log.d(this.getClass().getName(), e.getMessage());
                exceptions[0] = e;
            }
            return null == exceptions[0];
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                try {
                    storeSiteToken((String) registerResponse[0].get("token"));
                    showMainWindow();
                    finish();
                    return;
                } catch (IOException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                    exceptions[0] = e;
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                    exceptions[0] = e;
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                    exceptions[0] = e;
                }
            }

            if (exceptions[0] instanceof VolleyError) {
                mEmailView.setError(getString(R.string.error_general_login_failure));
                mEmailView.requestFocus();
            } else {
                mEmailView.setError(getString(R.string.error_incorrect_password));
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        protected void login(String email, String password) throws InterruptedException, JSONException {
            final Object lock = new Object();
            final UserService userService = new UserService(getApplication().getApplicationContext());
            userService.getToken(email, password,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            registerResponse[0] = response;
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(this.getClass().getName(), null == error.getMessage() ? error.toString() : error.getMessage());
                            error.printStackTrace();
                            exceptions[0] = error;
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    });

            synchronized (lock) {
                lock.wait();
            }
        }
    }
}