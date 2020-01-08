package com.sktt1.butters.data.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    public static String getFormattedDate(Date date,DateTimePattern pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern.getAction(), Locale.ENGLISH);
        return formatter.format(date);
    }
}

