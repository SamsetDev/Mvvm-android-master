package com.samset.mvvm.mvvmsampleapp.listeners;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 07/03/19 at 6:37 PM for Mvvm-android-master .
 */


public interface NetworkResponse {

    void onNoData();
    void onServerError();
    void onNetworkError();
    void onSuccess();


}
