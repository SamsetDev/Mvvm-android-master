package com.samset.mvvm.mvvmsampleapp.remote.di.module;

import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.di.ViewModelSubComponent;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.GitHubService;
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = ViewModelSubComponent.class)
public class AppModule {

    @Singleton
    @Provides
    GitHubService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl(GitHubService.HTTPS_API_GITHUB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubService.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {

        return new ProjectViewModelFactory(viewModelSubComponent.build());
    }

}
