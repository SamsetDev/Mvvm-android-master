package com.samset.mvvm.mvvmsampleapp;

import android.app.Activity;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.samset.mvvm.mvvmsampleapp.remote.di.AppInjector;
import com.samset.mvvm.mvvmsampleapp.remote.di.components.AppComponent;

import javax.inject.Inject;

import androidx.multidex.MultiDex;
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
        MultiDex.install(this);
        AppInjector.init(this);
        debugToolsInitilization();
    }


    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void debugToolsInitilization() {
        Stetho.initializeWithDefaults(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
