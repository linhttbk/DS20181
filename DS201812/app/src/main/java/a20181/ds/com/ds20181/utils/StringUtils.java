package a20181.ds.com.ds20181.utils;

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

    private static String timeConversion(int totalSeconds) {
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;
        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
    }
}
