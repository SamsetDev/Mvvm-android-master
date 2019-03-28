package com.samset.mvvm.mvvmsampleapp.remote.service.repository

import androidx.lifecycle.MutableLiveData
import com.samset.mvvm.mvvmsampleapp.remote.ResponseObserver
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.utils.UI_STATUS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository
@Inject
constructor(private val gitHubService: GitHubService, private val disposable: CompositeDisposable) {


    fun getProjectListWithErrorHandle(userId: String, data: MutableLiveData<ArrayList<Project>>, status: MutableLiveData<UI_STATUS>) {

        status.value = UI_STATUS.LOADING
        gitHubService.getProjectList(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResponseObserver<ArrayList<Project>>(disposable) {

                    override fun onSuccess(value: ArrayList<Project>?) {
                        status.value = UI_STATUS.SUCCESS
                        data.postValue(value)
                    }

                    override fun onNoData() {
                        status.value = UI_STATUS.NO_DATA
                    }

                    override fun onNetworkError(e: Throwable) {
                        status.value = UI_STATUS.NETWORK_ERROR
                    }

                    override fun onServerError(e: Throwable, code: Int) {
                        status.value = UI_STATUS.SERVER_ERROR
                    }

                })

    }

    fun getProjectDetails(username: String, reponame: String, data: MutableLiveData<Project>, status: MutableLiveData<UI_STATUS>) {

        status.value = UI_STATUS.LOADING
        gitHubService.getProjectDetails(username, reponame).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResponseObserver<Project>(disposable) {
                    override fun onSuccess(response: Project?) {
                        status.value = UI_STATUS.SUCCESS
                        data.postValue(response)
                    }

                    override fun onNoData() {
                        status.value = UI_STATUS.NO_DATA
                    }

                    override fun onNetworkError(e: Throwable) {
                        status.value = UI_STATUS.NETWORK_ERROR
                    }

                    override fun onServerError(e: Throwable, code: Int) {
                        status.value = UI_STATUS.SERVER_ERROR
                    }
                })

    }

}
