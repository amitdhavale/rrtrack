package com.markss.rfidtemplate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class URLs {

    public static String BaseUrl = "/WelSpun_Dispatch/Service.svc/";    //client folder
    public static String DEFAULT_IP_ADDRESS = "172.20.204.13";  //client fix ip
    //    public static String DEFAULT_IP_ADDRESS = "192.168.1.30";  //client fix ip

    public static String mainServerIP;
    public static String SavedServerIP;
    public static String serverUrl = "http://" + DEFAULT_IP_ADDRESS + BaseUrl;
    public static SharedPreferences sharedPreferences;
    public Context context;

//    public static void changeIP(Context context) {
//        sharedPreferences = context.getSharedPreferences(com.markss.dispatchmanagement.common.URLs.ShrdPrfnc, Context.MODE_APPEND);
//        mainServerIP = sharedPreferences.getString("IPAddress", DEFAULT_IP_ADDRESS);
//        serverUrl = "http://" + mainServerIP + BaseUrl;
//        sharedPreferences.edit().putString("server_Middleware",SavedServerIP).apply();
//    }
}
