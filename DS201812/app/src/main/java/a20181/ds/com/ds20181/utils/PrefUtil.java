package a20181.ds.com.ds20181.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PrefUtil {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void savePreferences(Context context, String key, Set<String> stringSet) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public static void savePreferences(Context context, String key, boolean content) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, content);
        editor.apply();
    }

    public static void savePreferences(Context context, String key, String content) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, content);
        editor.apply();
    }

    public static void savePreferences(Context context, String key, int content) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(key, content);
        editor.apply();
    }

    public static boolean getPreferences(Context context, String key) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static String getPreferences(Context context, String key, String defVal) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(key, defVal);
    }

    public static int getPreferences(Context context, String key, int defVal) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(key, defVal);
    }

    public static Set<String> getPreferences(Context context, String key, Set<String> stringSet) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getStringSet(key, stringSet);
    }
}
