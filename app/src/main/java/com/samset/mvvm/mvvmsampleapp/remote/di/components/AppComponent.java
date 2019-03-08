package com.samset.mvvm.mvvmsampleapp.remote.di.components;

import android.app.Application;

import com.samset.mvvm.mvvmsampleapp.AppApplication;
import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.di.MainActivityModule;
import com.samset.mvvm.mvvmsampleapp.remote.di.module.AppModule;
import com.samset.mvvm.mvvmsampleapp.remote.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, MainActivityModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);


        AppComponent build();

        //NetworkResponse getNetwork();
    }

    void inject(AppApplication appApplication);

    //void inject(NetworkResponse networkResponse);
}
