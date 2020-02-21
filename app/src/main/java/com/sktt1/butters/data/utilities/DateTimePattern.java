package com.sktt1.butters.data.utilities;

public enum DateTimePattern{
    FULL_DATETIME("yyyy-mm-dd hh:mm:ss"),
    TIME("hh:mm a"),
    DATE("M/dd/yyyy"),
    SHORT_DATETIME("yyyy-mm-dd hh:mm");

    private  String action;

    public String getAction(){
        return this.action;
    }

    DateTimePattern(String action){
        this.action = action;
    }
}
