package ir.sharif.mobile.project.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static final DateFormat longDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
    private static final DateFormat shortDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private static final DateFormat intDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    public static final int LONG = 1;
    public static final int SHORT = 2;

    public static String formatDate(Date date, int state) {
        if (state == LONG)
            return longDateFormat.format(date);
        return shortDateFormat.format(date);
    }

    public static String formatDate(int year, int monthOfYear, int dayOfMonth, int state) {
        try {
            Date date = intDateFormat.parse("" + dayOfMonth + "-" + monthOfYear + "-" + year);
            return formatDate(date, state);
        } catch (ParseException e) {
            Log.e("DATE", "Unknown date pattern");
            return "";
        }
    }

    public static Date parseDate(String source) {
        try {
            return longDateFormat.parse(source);
        } catch (ParseException e) {
            Log.e("DATE", "Unknown date pattern: " + source);
        }
        return null;
    }

}
