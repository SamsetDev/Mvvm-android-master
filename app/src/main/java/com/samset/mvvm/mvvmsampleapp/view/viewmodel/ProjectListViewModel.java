package com.samset.mvvm.mvvmsampleapp.view.viewmodel;

import android.app.Application;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.ProjectResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProjectListViewModel extends BaseViewModel {

    private final LiveData<ProjectResponse> projectListObservable;
    private NetworkResponse response=null;

    @Inject
    public ProjectListViewModel(@NonNull ProjectRepository projectRepository, @NonNull Application application) {
        super(projectRepository, application);
        // If any transformation is needed, this can be simply done by Transformations class ...
        projectListObservable = projectRepository.getProjectList("Google",response);

    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<ProjectResponse> getProjectListObservable() {
        return projectListObservable;
    }



}
