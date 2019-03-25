package com.samset.mvvm.mvvmsampleapp.remote.service.repository

import androidx.databinding.ObservableField

import com.samset.mvvm.mvvmsampleapp.remote.ResponseObserver
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.remote.vo.Resource

import io.reactivex.android.schedulers.AndroidSchedulers

import javax.inject.Inject
import javax.inject.Singleton

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Singleton
class ProjectRepository
@Inject
constructor(private val gitHubService: GitHubService, private val disposable: CompositeDisposable) {

    //lateinit var progresbar: ObservableField<Boolean>()
     public var success : ObservableField<String>
    public var progresbar: ObservableField<Boolean>

    init {
        progresbar = ObservableField<Boolean>()
        success = ObservableField<String>()
    }

    fun getProjectList(userId: String): LiveData<List<Project>> {
        val data = MutableLiveData<List<Project>>()

        progresbar.set(true)
        success.set("Loading..")
        gitHubService.getProjectList(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResponseObserver<Response<List<Project>>>(disposable) {
                    override fun onNetworkError(e: Throwable) {
                        //networkResponse.onNetworkError()
                        progresbar.set(false)
                    }

                    override fun onServerError(e: Throwable, code: Int) {
                        //networkResponse.onServerError()
                        progresbar.set(false)
                    }

                    override fun onNext(response: Response<List<Project>>) {
                        progresbar.set(false)
                        success.set("Result")
                        data.value = response.body()

                    }
                })

        return data
    }

    fun getProjectListWithErrorHandle(userId: String): LiveData<Resource<List<Project>>> {
        val data = MutableLiveData<Resource<List<Project>>>()
       // val error = MutableLiveData<Throwable>()

        gitHubService.getProjectList(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResponseObserver<Response<List<Project>>>(disposable) {

                    override fun onNetworkError(e: Throwable) {
                        data.setValue(Resource.error(e.localizedMessage.toString()))
                    }

                    override fun onServerError(e: Throwable, code: Int) {
                        data.setValue(Resource.error(e.localizedMessage.toString()))
                    }

                    override fun onNext(response: Response<List<Project>>) {
                        //data.postValue(response.body())
                        data.setValue(Resource.success(response.body()))
                    }
                })

        return data
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
