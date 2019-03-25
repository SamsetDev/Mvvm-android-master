package com.samset.mvvm.mvvmsampleapp.remote.vo

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 08/03/19 at 4:38 PM for Mvvm-android-master .
 */


data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }
}
