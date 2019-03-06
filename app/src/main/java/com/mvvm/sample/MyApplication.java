package com.mvvm.sample;

import android.app.Application;


import androidx.multidex.MultiDex;

/**
 * Copyright (C) viewmodel - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * Created by samset on 28/01/19 at 3:28 PM for viewmodel .
 */


public class MyApplication extends Application {
    private static MyApplication myApplication;


    public static MyApplication getInstance() {
        return myApplication == null ? new MyApplication() : myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }


}
