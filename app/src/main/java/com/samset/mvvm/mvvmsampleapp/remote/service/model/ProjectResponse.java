package com.samset.mvvm.mvvmsampleapp.remote.service.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 08/03/19 at 3:26 PM for Mvvm-android-master .
 */


public class ProjectResponse implements Serializable {

    @SerializedName("")
    private List<Project> project;


    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }




}
