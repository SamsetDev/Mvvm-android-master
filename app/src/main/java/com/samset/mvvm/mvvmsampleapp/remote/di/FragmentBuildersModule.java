package com.samset.mvvm.mvvmsampleapp.remote.di;

import com.samset.mvvm.mvvmsampleapp.view.ui.ProjectFragment;
import com.samset.mvvm.mvvmsampleapp.view.ui.ProjectListFragment2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProjectFragment contributeProjectFragment();


    @ContributesAndroidInjector
    abstract ProjectListFragment2 contributeProjectListFragment2();

}
