package com.example.wifiapp;

public class WifiNetwork {

    private String ssid;
    private String password;
    private boolean hasPassword;
    private String signalStrength;
    private String mac;

    public WifiNetwork(String ssid, String mac, String password, boolean hasPassword, String signalStrength) {
        this.ssid = ssid;
        this.mac = mac;
        this.password = password;
        this.hasPassword = hasPassword;
        this.signalStrength = signalStrength;
    }

    public WifiNetwork(String ssid, String mac, String password, boolean hasPassword) {
        this.ssid = ssid;
        this.mac = mac;
        this.password = password;
        this.hasPassword = hasPassword;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
