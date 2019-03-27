package com.samset.mvvm.mvvmsampleapp.view.ui.activities

import android.os.Bundle

import com.example.test.mvvmsampleapp.R
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.view.ui.base.BaseActivity
import com.samset.mvvm.mvvmsampleapp.view.ui.fragments.DetailsFragment

import javax.inject.Inject

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

import androidx.navigation.findNavController;

class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    private var navController: NavController? = null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    override fun getActivityLayout(): Int {
        return R.layout.activity_main
    }

    override fun getFragmentContainer(): Int {
        return 0
    }

    override fun initView(bundle: Bundle?) {
        navController = findNavController(R.id.homeNavigationFrag);
        if (bundle == null) {
            startTransaction(R.id.actionlist)
        }

    }

    private fun startTransaction(fragment: Int) {

        //NavigationUI.setupActionBarWithNavController(this, navController)
        navController?.navigate(fragment)
    }


    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }


}
