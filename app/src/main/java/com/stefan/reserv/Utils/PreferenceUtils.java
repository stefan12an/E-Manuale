package com.stefan.reserv.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PreferenceUtils {
    public PreferenceUtils() {
    }

    public static boolean saveId(String id, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ID, id);
        editor.apply();
        return true;
    }

    public static String getId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_ID, null);
    }

    public static boolean saveEmail(String email, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_EMAIL, email);
        editor.apply();
        return true;
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_EMAIL, null);
    }

    public static boolean savePassword(String password, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_PASSWORD, password);
        editor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_PASSWORD, null);
    }

    public static boolean saveRole(String role, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ROLE, role);
        editor.apply();
        return true;
    }

    public static String getRole(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_ROLE, null);
    }

    public static boolean savePic(byte[] pic, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String saveThis = Base64.encodeToString(pic, Base64.NO_WRAP);
        editor.putString(Constants.KEY_PROFILE_PIC, saveThis);
        editor.apply();
        return true;
    }
    public static byte[] getPic(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String stringFromSharedPrefs = preferences.getString(Constants.KEY_PROFILE_PIC,null);
        if(stringFromSharedPrefs!=null) {
            return Base64.decode(stringFromSharedPrefs, Base64.DEFAULT);
        }
        return null;
    }

    public static boolean saveUsername(String username, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_USERNAME, username);
        editor.apply();
        return true;
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_USERNAME, null);
    }

    public static boolean saveGradeId(String id_grade, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ID, id_grade);
        editor.apply();
        return true;
    }

    public static String getGradeId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.KEY_ID, null);
    }
}
