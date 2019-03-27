package com.samset.mvvm.mvvmsampleapp.view.ui.base;
import android.view.View;

import androidx.annotation.StringRes;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 27/03/19 at 10:45 AM for Mvvm-android-master .
 */


public interface BaseView {

    void showLoading();


    void openActivityOnTokenExpire();

    void showError(@StringRes int resId);

    void showErrorHeader(int statusCode);

    void showError(String message);

    void showMessage(String message);

    void showMessage(int resId);

    void onShowContent(View view);

    void showServerError();

    void showNetworkError();

    void showNoData();

    void onRetry();

    boolean isNetworkConnected();

    void hideKeyboard();
}
