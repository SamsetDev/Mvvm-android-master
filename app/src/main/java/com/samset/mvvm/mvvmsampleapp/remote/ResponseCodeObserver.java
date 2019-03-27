package com.samset.mvvm.mvvmsampleapp.remote;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import retrofit2.Response;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 25/03/19 at 3:06 PM for Mvvm-android-master .
 */


public abstract class ResponseCodeObserver<T> implements Observer<Response<T>> {

    private final static String TAG = ResponseCodeObserver.class.getSimpleName();
    private int statusCode;

    private CompositeDisposable disposable;

    public ResponseCodeObserver(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (disposable != null) {
            disposable.add(d);
        }
    }

    @Override
    public void onNext(Response<T> value) {

        if (value != null) {
            updateBaseInfo(value.body());

            if (value.code() == HTTP_OK) {
                onSuccess(value.body());
            } else {
                onServerError(new HttpException(value), value.code());
            }
        } else {
            Log.e(TAG, "Response Value null");
        }
    }

    private void updateBaseInfo(T body) {

       /* if (body instanceof BaseModelInterface) {
            BaseModel model = ((BaseModelInterface) body).getBaseModel();
            AppApplication.getInstance().handleBaseInfo(model);
        }*/
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
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

    /*
     *
     * called when api Http_code is 200
     * */
    public abstract void onSuccess(T value);

    public abstract void onNetworkError(Throwable e);

    /*called when api Http_code is not 200*/
    public abstract void onServerError(Throwable e, int code);

}
