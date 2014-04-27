package com.allblacks.utils.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

    private static SharedPreferences preferences;
    
    private static Editor editor;

    public static void update(SharedPreferences preferences) {
        Preferences.preferences = preferences;
        Preferences.editor = preferences.edit();
    }

    public static String get(String key) {
        return preferences.getString(key, "");
    }

    public static void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static void put(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String get(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static Boolean isEnabled(String key) {
        return preferences.getBoolean(key, Boolean.FALSE);
    }

    public static Boolean isEnabled(String key, Boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static boolean isSet(String key) {

        if (preferences == null) {
            return false;
        }

        if (preferences.getString(key, null) == null) {
            return false;
        }

        if (preferences.getString(key, "").trim().equals("")) {
            return false;
        }

        return true;
    }
}
