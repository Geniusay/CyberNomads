package io.github.geniusay.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(timestamp * 1000));
    }
}