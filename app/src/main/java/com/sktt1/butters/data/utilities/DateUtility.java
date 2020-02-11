package com.sktt1.butters.data.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    public static String getFormattedDate(Date date,DateTimePattern pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern.getAction(), Locale.ENGLISH);
        return formatter.format(date);
    }
    public static Date getStringDate(String value, DateTimePattern pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern.getAction(), Locale.ENGLISH);
        return formatter.parse(value);
    }
}

