package com.badani.changesetting;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static int requestCodeBlueTooth = 201;
    final static int requestCodeWifi = 202;
    final static int requestCodeLocation = 203;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControlListener();
        //checkWifiState();
        // checkBluetoothState();
        // checkLocationState();
    }

    private void initControlListener() {
        findViewById(R.id.btn_bluetooth).setOnClickListener(this);
        findViewById(R.id.btn_wifi).setOnClickListener(this);
        findViewById(R.id.btn_location).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bluetooth:
                createIntent(Settings.ACTION_BLUETOOTH_SETTINGS, requestCodeBlueTooth);
                break;
            case R.id.btn_location:
                createIntent(Settings.ACTION_LOCATION_SOURCE_SETTINGS, requestCodeLocation);
                break;
            case R.id.btn_wifi:
                createIntent(Settings.ACTION_WIFI_SETTINGS, requestCodeWifi);
                break;
        }
    }


    private void createIntent(String actionType, int requestCode) {
        Intent intentOpenLocationSettings = new Intent(actionType);
        startActivityForResult(intentOpenLocationSettings, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requestCodeBlueTooth:
                checkBluetoothState();
                break;
            case requestCodeWifi:
                checkWifiState();
                break;
            case requestCodeLocation:
                checkLocationState();
                break;
        }
    }


    private void checkBluetoothState() {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            ((Switch) findViewById(R.id.sw_bluetooth)).setChecked(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
        } catch (Exception ex) {
        }
    }

    private void checkLocationState() {
        LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        ((Switch) findViewById(R.id.sw_location)).setChecked(lm != null && gps_enabled);

    }

    private void checkWifiState() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            ((Switch) findViewById(R.id.sw_wifi)).setChecked(cm != null &&  isWiFi);
        } catch (Exception ex) {
        }
    }
}