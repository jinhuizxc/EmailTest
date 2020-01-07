package com.example.emailtest.email;


import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashReport.initCrashReport(getApplicationContext(), "900031508", false);
        sContext = getApplicationContext();
    }
}
