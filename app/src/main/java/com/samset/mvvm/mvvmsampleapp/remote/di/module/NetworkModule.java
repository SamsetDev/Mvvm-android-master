package com.samset.mvvm.mvvmsampleapp.remote.di.module;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 08/03/19 at 12:19 PM for Mvvm-android-master .
 */


@Module
public class NetworkModule {
    public NetworkResponse response;

    public NetworkModule(NetworkResponse networkResponse) {
        this.response = networkResponse;
    }

    @Provides
    @Singleton
    public NetworkResponse getResponse() {
        return response;
    }


}
