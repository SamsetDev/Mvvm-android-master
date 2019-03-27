package com.samset.mvvm.mvvmsampleapp.view.viewmodel.baseVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository
import com.samset.mvvm.mvvmsampleapp.remote.vo.UI_STATUS
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 25/03/19 at 3:58 PM for Mvvm-android-master .
 */


open class BaseViewModel
@Inject
constructor(projectRepository: ProjectRepository, app: Application,composit: CompositeDisposable) : AndroidViewModel(app) {

      var disposable : CompositeDisposable
     var applications: Application
     var projectRepository: ProjectRepository
    private var uiStateLiveData: MutableLiveData<UI_STATUS>? = null


    init {
        this.applications = app
        this.disposable=composit
        this.projectRepository = projectRepository

    }


     fun getUiStateLiveData(): MutableLiveData<UI_STATUS> {
        if (uiStateLiveData == null) {
            uiStateLiveData = MutableLiveData<UI_STATUS>().apply {
                //Need to confirm about this removal
                uiStateLiveData?.value = UI_STATUS.SUCCESS
            }
        }
        return uiStateLiveData as MutableLiveData<UI_STATUS>
    }


    protected fun getApiService(): ProjectRepository {
        return projectRepository
    }


    protected fun getDispose(): CompositeDisposable {
        return disposable
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}
