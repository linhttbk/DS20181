package a20181.ds.com.ds20181.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isContainWhiteSpace(String source) {
        return source.contains(" ");
    }

    public static boolean isAllCharacter(String source) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(source);
        return !m.find();
    }

}
