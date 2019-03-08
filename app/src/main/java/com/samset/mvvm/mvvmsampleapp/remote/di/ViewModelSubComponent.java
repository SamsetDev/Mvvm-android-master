package com.samset.mvvm.mvvmsampleapp.remote.di;

import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectListViewModel;
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectViewModel;
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectViewModelFactory;

import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link ProjectViewModelFactory}.
 */
@Subcomponent
public interface ViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    ProjectListViewModel projectListViewModel();
    ProjectViewModel projectViewModel();
}
