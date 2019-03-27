package com.samset.mvvm.mvvmsampleapp.remote.service.repository;

import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.ProjectResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    String HTTPS_API_GITHUB_URL = "https://api.github.com/";

    @GET("users/{user}/repos")
    Observable<Response<ArrayList<Project>>> getProjectList(@Path("user") String user);

    @GET("/repos/{user}/{reponame}")
    Call<Project> getProjectDetails(@Path("user") String user, @Path("reponame") String projectName);
}
