package com.markss.rfidtemplate.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.markss.rfidtemplate.application.Application;
import com.markss.rfidtemplate.modules.login.model.UserModel;

public class PreferenceUtil {
    public static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Application.getContext());
    public static UserModel userModel = null;

    public static boolean isUserLoggedIn(){
        return preferences.getBoolean("is_logged_in", false);
    }

    public static void setUserLoggedIn(boolean loggedIn){
        preferences.edit().putBoolean("is_logged_in", loggedIn).apply();
    }

    public static UserModel getUser()
    {
        if (userModel == null) {
            userModel = new Gson().fromJson(preferences.getString("user", null), UserModel.class);
        }
        return userModel;
    }

    public static void clearUserData() {
        if (userModel != null) {
            userModel = null;
        }
        preferences.edit().remove("user").apply();
    }

    public static void setUser(UserModel userDetailsModel) {
        preferences.edit().putString("user", new Gson().toJson(userDetailsModel)).apply();
    }

    public static void clearAll() {
        userModel = null;
        preferences.edit().clear().apply();
    }

    public static void clear() {
        userModel = null;
    }

    public static String getIPAddress(){
        return preferences.getString("IPAddress", URLs.DEFAULT_IP_ADDRESS);
    }

    public static void setIPAddress(String newIP){
        preferences.edit().putString("IPAddress", newIP).apply();
    }
}
