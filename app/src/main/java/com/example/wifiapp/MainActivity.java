package com.example.wifiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.thanosfisherman.wifiutils.WifiUtils;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ScanResult> results;
    private WifiManager wifiManager;
    private final ArrayList<WifiNetwork> wifiIsAvaliable = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanWifi() {
        WifiUtils.withContext(getApplicationContext()).scanWifi(this :: getScanResults).start();
    }

    private void getScanResults(@NonNull final List<ScanResult> scanResults) {
        this.results = scanResults;

        if (!scanResults.isEmpty()) {
            showResults();
        }
    }

    private void showResults() {
        wifiIsAvaliable.clear();
        for (ScanResult result : results) {
            if (!result.SSID.contains(getString(R.string.vivo)) && !result.SSID.contains(getString(R.string.fibra))) {
                continue;
            }
            String signalLevel = checkSignalLevel(result);
            String mac = getMacFromSSID(result);
            String password = mac.substring(4);
            wifiIsAvaliable.add(new WifiNetwork(result.SSID, mac,  password, result.capabilities.contains("WPA2"), signalLevel));
        }
    }

    private String getMacFromSSID(ScanResult result) {
        String[] SSIDparts = result.SSID.split("_");
        return SSIDparts[SSIDparts.length - 1];
    }

    private String checkSignalLevel(ScanResult result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return String.valueOf(wifiManager.calculateSignalLevel(result.level));
        }

        return String.valueOf(WifiManager.calculateSignalLevel(result.level, 5));
    }
}