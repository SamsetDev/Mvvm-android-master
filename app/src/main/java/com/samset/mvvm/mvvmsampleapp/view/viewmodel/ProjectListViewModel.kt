package com.samset.mvvm.mvvmsampleapp.view.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.samset.mvvm.mvvmsampleapp.view.model.Project
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository
import com.samset.mvvm.mvvmsampleapp.utils.CommonUtils.REPO_USERNAME
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.baseVM.BaseViewModel
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.baseVM.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProjectListViewModel
@Inject
constructor(projectRepository: ProjectRepository, application: Application, disposable: CompositeDisposable) : BaseViewModel(projectRepository, application, disposable) {
    var projectListObservable = SingleLiveEvent<ArrayList<Project>>()


    init {
        // If any transformation is needed, this can be simply done by Transformations class ...
        // projectListObservable = projectRepository.getProjectList("Google")
        projectRepository.getProjectListWithErrorHandle(REPO_USERNAME, projectListObservable, getUiStateLiveData())


    }

    public fun getDataFromServer() {
        return projectRepository.getProjectListWithErrorHandle(REPO_USERNAME, projectListObservable, getUiStateLiveData())
    }


    public fun getProjectListdata(): LiveData<ArrayList<Project>> {
        return projectListObservable
    }

}
