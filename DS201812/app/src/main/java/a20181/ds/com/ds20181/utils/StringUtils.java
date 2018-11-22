package a20181.ds.com.ds20181.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    final static int MINUTES_IN_AN_HOUR = 60;
    final static int SECONDS_IN_A_MINUTE = 60;

    public static boolean isContainWhiteSpace(String source) {
        return source.contains(" ");
    }

    public static boolean isAllCharacter(String source) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(source);
        return !m.find();
    }

    public static String getHourFromTime(int totalSeconds) {
        return String.valueOf(totalSeconds / (MINUTES_IN_AN_HOUR * SECONDS_IN_A_MINUTE));

    }

    public static String getMinuteFromTime(int totalSeconds) {
        return String.valueOf((totalSeconds / SECONDS_IN_A_MINUTE) % MINUTES_IN_AN_HOUR);

    }

    public static String getSecondFromTime(int totalSeconds) {
        return String.valueOf(totalSeconds % SECONDS_IN_A_MINUTE);

    }


    public static String timeConversion(int totalSeconds) {
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;
        StringBuilder builder = new StringBuilder();
        return hours + ":" + minutes + ":" + seconds;
    }

    public static int convertTime(int hour, int min, int sec) {
        return hour * 3600 + min * 60 + sec;
    }

    public static long convertStringToLong(String source) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) return 0;
        long mills = date.getTime();
        return mills;
    }

    public static String formatLongToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String convertLongToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(new Date(time));
    }

    public static String formatLong2Date(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getTodayTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return format.format(new Date());
    }

    public static boolean isEmpty(String src) {
        return src == null || src.equals("");
    }

    public static boolean isDateValid(String source) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            formatter.parse(source);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
