package com.samset.mvvm.mvvmsampleapp;

import android.app.Activity;
import android.app.Application;

import com.samset.mvvm.mvvmsampleapp.remote.di.AppInjector;
import com.samset.mvvm.mvvmsampleapp.remote.di.components.AppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class AppApplication extends Application implements HasActivityInjector {
     private static AppApplication appApplication;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    public static AppApplication getInstance(){
        return appApplication==null ? new AppApplication() : appApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }


    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
