package com.samset.mvvm.mvvmsampleapp.remote.di;

import com.samset.mvvm.mvvmsampleapp.view.ui.fragments.DetailsFragment;
import com.samset.mvvm.mvvmsampleapp.view.ui.fragments.RepositoryListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract DetailsFragment contributeProjectFragment();


    @ContributesAndroidInjector
    abstract RepositoryListFragment contributeProjectListFragment2();

}
