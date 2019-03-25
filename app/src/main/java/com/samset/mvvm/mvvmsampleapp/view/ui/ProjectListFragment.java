package com.samset.mvvm.mvvmsampleapp.view.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.mvvmsampleapp.R;
import com.example.test.mvvmsampleapp.databinding.FragmentProjectListBinding;
import com.samset.mvvm.mvvmsampleapp.AppApplication;
import com.samset.mvvm.mvvmsampleapp.listeners.NetworkResponse;
import com.samset.mvvm.mvvmsampleapp.remote.di.AppInjector;
import com.samset.mvvm.mvvmsampleapp.remote.di.Injectable;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.ProjectResponse;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.ProjectRepository;
import com.samset.mvvm.mvvmsampleapp.view.adapter.ProjectAdapter;
import com.samset.mvvm.mvvmsampleapp.view.callback.ProjectClickCallback;
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectListViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ProjectListFragment extends Fragment implements Injectable, NetworkResponse {
    public static final String TAG = "ProjectListFragment";
    private ProjectAdapter projectAdapter;
    private FragmentProjectListBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProjectRepository projectRepository;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false);

        projectAdapter = new ProjectAdapter(projectClickCallback);
        binding.projectList.setAdapter(projectAdapter);
        binding.setIsLoading(true);

       // Log.e("TAG"," isloading "+projectRepository.progresbar.get());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ProjectListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectListViewModel.class);
        observeViewModel(viewModel);


    }

    private void observeViewModel(ProjectListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProjectListObservable().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                if (projects != null) {
                    binding.setIsLoading(false);
                   // binding.nodata.setIsNodata(true);
                    projectAdapter.setProjectList(projects);
                }
            }
        });
    }

    private final ProjectClickCallback projectClickCallback = new ProjectClickCallback() {
        @Override
        public void onClick(Project project) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(project);
            }
        }
    };

    @Override
    public void onNoData() {

    }

    @Override
    public void onServerError() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onSuccess() {

    }
}
