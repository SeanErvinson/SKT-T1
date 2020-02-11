package com.sktt1.butters.data.models;

public class User {
    private String name;
    private String nickname;
    private int totalDevices;
    private int activeDevices;
    private int inactiveDevices;
    private int findMyPhoneAlarm;

    public int getFindMyPhoneAlarm() {
        return findMyPhoneAlarm;
    }

    public void setFindMyPhoneAlarm(int findMyPhoneAlarm) {
        this.findMyPhoneAlarm = findMyPhoneAlarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(int totalDevices) {
        this.totalDevices = totalDevices;
    }

    public int getActiveDevices() {
        return activeDevices;
    }

    public void setActiveDevices(int activeDevices) {
        this.activeDevices = activeDevices;
    }

    public int getInactiveDevices() {
        return inactiveDevices;
    }

    public void setInactiveDevices(int inactiveDevices) {
        this.inactiveDevices = inactiveDevices;
    }
}
