package com.samset.mvvm.mvvmsampleapp.view.viewmodel;

import android.app.Application;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.ProjectResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

public class ProjectListViewModel extends BaseViewModel {

    ObservableField<Boolean> observableField=new ObservableField<>();

    private final LiveData<List<Project>> projectListObservable;
    private NetworkResponse response=null;

    @Inject
    public ProjectListViewModel(@NonNull ProjectRepository projectRepository, @NonNull Application application) {
        super(projectRepository, application);
        // If any transformation is needed, this can be simply done by Transformations class ...
        projectListObservable = projectRepository.getProjectList("Google");
      //  projectListObservable = projectRepository.getProjectList("Google",response);


    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<List<Project>> getProjectListObservable() {
        return projectListObservable;
    }



}
