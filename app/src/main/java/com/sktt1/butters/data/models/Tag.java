package com.sktt1.butters.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Tag implements Parcelable {
    private int id;
    private String name;
    private String macAddress;
    private int lastSeenLocationId;
    private Date lastSeenTime;
    private boolean isConnected;
    private int alarm;

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }


    public Tag() {
    }

    protected Tag(Parcel in) {
        id = in.readInt();
        name = in.readString();
        macAddress = in.readString();
        lastSeenLocationId = in.readInt();
        isConnected = in.readByte() != 0;
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(macAddress);
        parcel.writeInt(lastSeenLocationId);
        parcel.writeByte((byte) (isConnected ? 1 : 0));
    }
}
