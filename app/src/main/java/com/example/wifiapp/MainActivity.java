package com.example.wifiapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.thanosfisherman.wifiutils.WifiUtils;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ScanResult> results;
    private WifiManager wifiManager;
    private final ArrayList<WifiNetwork> wifiIsAvaliable = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void makeSureWifiIsOn() {
        if (wifiManager.isWifiEnabled()) {
            scanWifi();
            return;
        }
        enableWifi();
    }

    private void enableWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dialog = showWifiNeeded();
            return;
        }
        WifiUtils.withContext(getApplicationContext()).enableWifi();
    }

    private final ActivityResultLauncher<Intent> wifiEnabling = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), results -> makeSureWifiIsOn());

    private AlertDialog showWifiNeeded() {
        closeDialog();
        return new AlertDialog.Builder(this)
                .setTitle(R.string.warning).setMessage(R.string.enable_wifi)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> wifiEnabling.launch(new Intent(Settings.Panel.ACTION_WIFI)))
                .setCancelable(false).show();
    }

    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
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