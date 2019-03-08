package com.samset.mvvm.mvvmsampleapp.remote.service.repository;

import android.util.Log;
import android.view.View;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.ResponseObserver;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.ProjectResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class ProjectRepository {
    private GitHubService gitHubService;
    private CompositeDisposable disposable;

    //private NetworkResponse networkview;

    @Inject
    public ProjectRepository(GitHubService gitHubService, CompositeDisposable disposablec) {
        this.gitHubService = gitHubService;
        this.disposable = disposablec;
        //  this.networkview=networkResponse;
    }

    public LiveData<ProjectResponse> getProjectList(String userId, NetworkResponse networkResponse) {
        final MutableLiveData<ProjectResponse> data = new MutableLiveData<>();


        gitHubService.getProjectList(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<ProjectResponse>>(disposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        networkResponse.onNetworkError();
                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        networkResponse.onServerError();
                    }

                    @Override
                    public void onNext(Response<ProjectResponse> response) {
                        data.setValue(response.body());
                    }
                });

        return data;
    }

    public LiveData<Project> getProjectDetails(String userID, String projectName) {
        final MutableLiveData<Project> data = new MutableLiveData<>();

        gitHubService.getProjectDetails(userID, projectName).enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                simulateDelay();
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                // TODO better error handling in part #2 ...
                data.setValue(null);
            }
        });

        return data;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
