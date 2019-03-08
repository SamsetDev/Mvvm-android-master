package com.samset.mvvm.mvvmsampleapp.view.viewmodel;

import android.app.Application;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 08/03/19 at 11:53 AM for Mvvm-android-master .
 */


public class BaseViewModel extends AndroidViewModel {

    private ProjectRepository projectRepository;
    private Application application;

    @Inject
    public BaseViewModel(@NonNull ProjectRepository repository, @NonNull Application app) {
        super(app);
        this.application = app;
        this.projectRepository = repository;

    }


    protected ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    public Application getApplication() {
        return application;
    }


}
