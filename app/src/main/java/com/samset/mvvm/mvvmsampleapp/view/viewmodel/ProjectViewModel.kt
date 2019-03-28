package com.samset.mvvm.mvvmsampleapp.view.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository
import com.samset.mvvm.mvvmsampleapp.utils.CommonUtils.REPO_USERNAME
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.baseVM.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProjectViewModel
@Inject
constructor(projectRepository: ProjectRepository, application: Application, disposable: CompositeDisposable) : BaseViewModel(projectRepository, application, disposable) {

    private val TAG = ProjectViewModel::class.java.name
    val observableProject: MutableLiveData<Project> = MutableLiveData()
    var projectID: MutableLiveData<String> = MutableLiveData()

    var project = ObservableField<Project>()

    fun setProject(project: Project) {
        this.project.set(project)
    }

    fun setProjectID(id: String) {
        Log.e("TAG", " sender id-11 " + id)
        this.projectID.setValue(id)
        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        Log.e("TAG", " sender id-2 " + projectID.value)
        projectRepository.getProjectDetails(REPO_USERNAME, projectID.value.toString(), observableProject, getUiStateLiveData())
    }


}
