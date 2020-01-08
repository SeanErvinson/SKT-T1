package com.sktt1.butters.data.models;

import java.util.Date;

public class Activity {
    private int id;
    private String message;
    private Date notifiedOn;
    private boolean hasRead = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getNotifiedOn() {
        return notifiedOn;
    }

    public void setNotifiedOn(Date notifiedOn) {
        this.notifiedOn = notifiedOn;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
