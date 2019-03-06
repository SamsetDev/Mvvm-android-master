package com.mvvm.sample.viewmodels

import androidx.lifecycle.ViewModel

/**
 * Copyright (C) ViewModelSample - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 06/03/19 at 1:05 PM for ViewModelSample .
 */


class MyViewModel : ViewModel() {
    private var colorResource: Int = 0xfff

    fun setColorResource(colorResource: Int) {
        this.colorResource = colorResource
    }

    fun getColorResource() = colorResource


    /*  When activity is destryed this automatically called
    * */
    override fun onCleared() {
        super.onCleared()

        // todo your logic
    }
}
