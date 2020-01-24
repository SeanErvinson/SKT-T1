package com.sktt1.butters.data.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Tag {
    private int id;
    private String name;
    private String macAddress;
    private int lastSeenLocationId;
    private Date lastSeenTime;
    private boolean isConnected;


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public int getLastSeenLocationId() {
        return lastSeenLocationId;
    }

    public void setLastSeenLocationId(int lastSeenLocationId) {
        this.lastSeenLocationId = lastSeenLocationId;
    }

    public Date getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(Date lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

}
