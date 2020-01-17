package com.sktt1.butters.data.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Tag {
    private String id;
    private String name;
    private Location lastSeenLocation;
    private String macAddress;
    private Date lastSeenTime;

    public Location getLastSeenLocation() {
        return lastSeenLocation;
    }

    public void setLastSeenLocation(Location lastSeenLocation) {
        this.lastSeenLocation = lastSeenLocation;
    }

    public Date getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(Date lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
