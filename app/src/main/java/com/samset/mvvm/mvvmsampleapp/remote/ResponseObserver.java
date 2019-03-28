package com.samset.mvvm.mvvmsampleapp.remote;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Response;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 08/03/19 at 11:53 AM for Mvvm-android-master .
 */

public abstract class ResponseObserver<T> implements Observer<Response<T>> {

    private int statusCode;

    private CompositeDisposable disposable;

    public ResponseObserver(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(Response<T> response) {
        Log.e("TAG", "Response header code is " + response.code());
        if (response.code() == HTTP_OK) {
            onSuccess(response.body());
        } else if (response.code() == HTTP_NO_CONTENT) {
            onNoData();
        } else {
            onServerError(new HttpException(response), response.code());
        }

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            statusCode = ((HttpException) e).response().code();
            onServerError(e, statusCode);
        } else {
            onNetworkError(e);
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T response);

    public abstract void onNoData();


    public abstract void onNetworkError(Throwable e);

    /*called when api Http_code is not 200*/
    public abstract void onServerError(Throwable e, int code);
}
