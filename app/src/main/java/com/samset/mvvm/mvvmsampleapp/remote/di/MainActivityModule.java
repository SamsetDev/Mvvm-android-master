package com.samset.mvvm.mvvmsampleapp.remote.di;

import com.samset.mvvm.mvvmsampleapp.view.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();



}
