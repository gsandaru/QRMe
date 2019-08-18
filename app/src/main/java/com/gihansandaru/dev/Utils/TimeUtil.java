package com.gihansandaru.dev.Utils;



import com.gihansandaru.dev.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtil {
    public static String getFormattedDateString(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return new SimpleDateFormat(AppConstants.COMMON_DATE_FORMAT,
                Locale.ENGLISH).format(calendar.getTime());
    }

    public static String getFormattedDateString(long milliseconds, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return new SimpleDateFormat(format, Locale.ENGLISH).format(calendar.getTime());
    }
}
