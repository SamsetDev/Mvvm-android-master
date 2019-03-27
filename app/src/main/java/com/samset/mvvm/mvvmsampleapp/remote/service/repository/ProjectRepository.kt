package com.samset.mvvm.mvvmsampleapp.remote.service.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samset.mvvm.mvvmsampleapp.remote.ResponseObserver
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.remote.vo.UI_STATUS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository
@Inject
constructor(private val gitHubService: GitHubService, private val disposable: CompositeDisposable) {


    fun getProjectListWithErrorHandle(userId: String,data:MutableLiveData<ArrayList<Project>>, status: MutableLiveData<UI_STATUS>){

        status.value=UI_STATUS.LOADING
        gitHubService.getProjectList(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResponseObserver<Response<ArrayList<Project>>>(disposable) {

                    override fun onNetworkError(e: Throwable) {
                        status.value=UI_STATUS.NETWORK_ERROR
                    }

                    override fun onServerError(e: Throwable, code: Int) {
                        status.value=UI_STATUS.SERVER_ERROR
                    }

                    override fun onNext(response: Response<ArrayList<Project>>) {
                        status.value=UI_STATUS.SUCCESS
                        data.postValue(response.body())
                    }
                })

    }

    fun getProjectDetails(userID: String, projectName: String): LiveData<Project> {
        val data = MutableLiveData<Project>()

        gitHubService.getProjectDetails(userID, projectName).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                simulateDelay()
                data.value = response.body()
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                // TODO better error handling in part #2 ...
                data.value = null
            }
        })

        return data
    }

    private fun simulateDelay() {
        try {
            Thread.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}
