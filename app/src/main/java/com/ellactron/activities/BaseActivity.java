package com.ellactron.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ellactron.storage.ConfigurationStorage;

import static com.ellactron.activities.LoginActivity.fb;

/**
 * Created by ji.wang on 2017-07-18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_item_logout:
                logout();
                break;
            case R.id.main_menu_item_about:
                Toast.makeText(this, "Selected options menu item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    protected String getSiteToken() {
        try {
            return (String) ConfigurationStorage.getConfigurationStorage(getApplicationContext()).get("token");
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
            return null;
        }
    }

    protected abstract int getActivityId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityId());
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.activity_home);

        getSupportActionBar().setIcon(R.mipmap.ic_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Toast.makeText(this, "Navigate back button clicked.", Toast.LENGTH_SHORT).show();
        return true;
    }

    protected void logout() {
        YesOrNoDialog.show(this,
                R.string.dialog_logout_message,
                R.string.dialog_logout_title,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doLogout();
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    private void doLogout() {
        try {
            ConfigurationStorage.getConfigurationStorage(getApplicationContext()).remove("token");
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

        if(null != fb && null != fb.getFacebookProfile())
            fb.getFacebookProfile().setCurrentProfile(null);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}