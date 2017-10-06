package com.ellactron.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeff on 2017-10-04.
 */

abstract public class WifiTool {
    private Context context;

    public WifiTool(Context context) {
        this.context = context;
    }

    WifiManager mainWifi;
    WifiReceiver receiverWifi = new WifiReceiver();

    public void scan()
    {
        mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(mainWifi.isWifiEnabled()==false)
        {
            mainWifi.setWifiEnabled(true);
        }

        doInback();
    }

    private void doInback()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                doInback();
            }
        }, 1000);
    }

    public abstract void getWifiConnections(List<String> connections);

    public  class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {
            ArrayList<String> connections=new ArrayList<String>();

            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); i++)
            {
                String ssid = wifiList.get(i).SSID.trim();
                if(ssid.length() > 0) connections.add(ssid);
            }

            getWifiConnections(connections);
        }
    }
}
