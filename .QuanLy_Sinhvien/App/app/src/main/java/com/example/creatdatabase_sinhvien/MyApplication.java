package com.example.creatdatabase_sinhvien;

import android.app.Application;

/**
 * Application class để lưu context global
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}

