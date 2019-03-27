package com.samset.mvvm.mvvmsampleapp.view.ui;

import android.os.Bundle;

import com.example.test.mvvmsampleapp.R;
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project;
import com.samset.mvvm.mvvmsampleapp.view.ui.base.BaseActivity;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    public int getFragmentContainer() {
        return 0;
    }

    @Override
    protected void initView(Bundle bundle) {

        if (bundle == null) {
            ProjectListFragment2 fragment = new ProjectListFragment2();

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, ProjectListFragment2.Companion.getTAG()).commit();
        }

    }

    /**
     * Shows the project detail fragment
     */
    public void show(Project project) {
        ProjectFragment projectFragment = ProjectFragment.forProject(project.name);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("project")
                .replace(R.id.fragment_container,
                        projectFragment, null).commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


}
