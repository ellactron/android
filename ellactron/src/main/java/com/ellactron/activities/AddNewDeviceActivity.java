package com.ellactron.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ellactron.helpers.WifiTool;

import java.util.ArrayList;
import java.util.List;

public class AddNewDeviceActivity extends BaseActivity /*AppCompatActivity*/ implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected int getActivityId() {
        return R.layout.activity_add_new_device;
    }
    @Override
    protected void addNewDevice(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityId());

        ListView listConnections = (ListView) findViewById(R.id.list_wifi);
        listConnections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        "Click ListItem Number " + position,
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        Button mSearchButton = (Button) findViewById(R.id.button_search_device);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWaitingAnimation();
                        getWifiList();
                        Toast.makeText(getApplication().getApplicationContext(), "Searching...", Toast.LENGTH_SHORT).show();
                    }

                    // Add waiting animation
                    private void showWaitingAnimation() {
                        RelativeLayout layout = (RelativeLayout ) findViewById(R.id.layout_list_device);
                        ProgressBar pb = (ProgressBar) findViewById(R.id.search_progress);
                        pb.setX((layout.getWidth()-pb.getWidth())/2);
                        pb.setY((layout.getHeight()-pb.getHeight())/2);
                        findViewById(R.id.search_progress).setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    protected void getWifiList() {
        final AddNewDeviceActivity self = this;

        WifiTool wifiTool = new WifiTool(this.getApplicationContext()) {
            @Override
            public void getWifiConnections(final List<String> connections) {
                List<String> wifiList = new ArrayList<String>();
                List<String> deviceList = new ArrayList<String>();
                categoryConnection(connections, deviceList, wifiList);

                setupDeviceList(deviceList);
                setupWifiList(wifiList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.search_progress).setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        wifiTool.scan();
    }

    private void categoryConnection(List<String> connections, List<String> deviceList, List<String> wifiList){
        for(String connection : connections){
            if (connection.startsWith("wang")) {
                deviceList.add(connection);
            }
            else {
                wifiList.add(connection);
            }
        }
    }

    private void setupWifiList(List<String> connections) {
        final ArrayAdapter<String> listConnectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, connections);
        final ListView listConnections = (ListView) findViewById(R.id.list_wifi);
        listConnections.setAdapter(listConnectionAdapter);
        listConnectionAdapter.notifyDataSetChanged();
    }

    private void setupDeviceList(List<String> connections) {
        final ArrayAdapter<String> listConnectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, connections);
        final ListView listDevices = (ListView) findViewById(R.id.list_device);
        listDevices.setAdapter(listConnectionAdapter);
        listConnectionAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
