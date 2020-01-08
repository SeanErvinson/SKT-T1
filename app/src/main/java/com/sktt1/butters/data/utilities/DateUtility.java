package com.sktt1.butters.data.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public enum DateTimePattern{
    FULL_DATETIME("yyyy-mm-dd hh:mm:ss"),
    TIME("hh:mm"),
    DATE("yyyy-mm-dd"),
    SHORT_DATETIME("yyyy-mm-dd hh:mm");

    private  String action;

    public String getAction(){
        return this.action;
    }

    DateTimePattern(String action){
        this.action = action;
    }
}

public class DateUtility {
    public static String getFormattedDate(Date date,DateTimePattern pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern.getAction(), Locale.ENGLISH);
        return formatter.format(date);
    }
}

