package com.mvvm.sample.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Copyright (C) ViewModel-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 06/03/19 at 4:26 PM for ViewModel-master .
 */

/*

  When creating an instance of a ViewModel with ViewModelProviders.of(this).get(SomeViewModel::class.java).
    In this scenario, we have a default constructor, what if we need the parameterize constructor for our ViewModel.
       For this kind of ViewModel, we need to create the custom ViewModelProvider.Factory
*/

class ViewModelFractory : ViewModelProvider.Factory {
    private lateinit var myViewModel: MyViewModel
    private var colorresource: Int = 0xfff

    public fun setColorResource(data: Int) {
        this.colorresource = data
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        myViewModel = MyViewModel()
        myViewModel.setColorResource(colorresource)

        Log.e("TAG", " factory color " + colorresource)
        return myViewModel as T
    }

    public fun getMyViewModel(): MyViewModel {
        return myViewModel
    }
}
