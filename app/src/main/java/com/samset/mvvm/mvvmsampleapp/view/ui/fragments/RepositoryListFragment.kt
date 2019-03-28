package com.samset.mvvm.mvvmsampleapp.view.ui.fragments

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 25/03/19 at 4:43 PM for Mvvm-android-master .
 */


import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.test.mvvmsampleapp.R
import com.samset.mvvm.mvvmsampleapp.remote.di.Injectable
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.utils.UI_STATUS
import com.samset.mvvm.mvvmsampleapp.view.adapter.WithBaseAdapter
import com.samset.mvvm.mvvmsampleapp.view.callback.ProjectClickCallback
import com.samset.mvvm.mvvmsampleapp.view.ui.base.BaseRecyclerviewFragment
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectListViewModel
import java.util.*
import javax.inject.Inject

class RepositoryListFragment : BaseRecyclerviewFragment<Project>(), Injectable {


    private lateinit var viewModel: ProjectListViewModel

    // private var projectAdapter: NormalAdapter? = null
    private var projectAdapter: WithBaseAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getFragmentLayout(): Int {
        return R.layout.base_recycler_fragment_view
    }

    override fun initView(view: View) {

        //projectAdapter = NormalAdapter(projectClickCallback)
        projectAdapter = WithBaseAdapter(projectClickCallback)
        getRecyclerView()?.adapter = projectAdapter

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectListViewModel::class.java)
        fetchDataFromServer(0)
    }


    private fun observeViewModel(viewModel: ProjectListViewModel) {
        // Update the list when the data changes
        viewModel.getProjectListdata().observe(this, Observer<ArrayList<Project>> { projects ->

            if (projects == null || projects?.size <= 0) {
                showNoData()
                return@Observer
            }

            projects?.let {
                onShowContent(mRecyclerView)
                //projectAdapter?.setProjectList(projects)  // enable this line when u use normal adapter
                projectAdapter?.setData(projects)
                projectAdapter?.notifyDataSetChanged()


            }
        })


        viewModel.getUiStateLiveData().observe(this, Observer<UI_STATUS> {
            when (it) {
                UI_STATUS.SUCCESS -> {
                    onShowContent(mRecyclerView)
                }
                UI_STATUS.NO_DATA -> {
                   showNoData()
                }
                UI_STATUS.LOADING -> {
                    showLoading()
                }
                UI_STATUS.NETWORK_ERROR -> {
                    showNetworkError()

                }
                UI_STATUS.SERVER_ERROR -> {
                    showServerError()
                }
            }
        })

    }

    override fun fetchDataFromServer(page: Int) {
        viewModel.getDataFromServer()
        observeViewModel(viewModel)

    }


    private val projectClickCallback = ProjectClickCallback { project ->
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
           // (activity as MainActivity).show(project)
            actionNavigate(project.name)
        }
    }


    private fun actionNavigate(id:String) {

        val action = RepositoryListFragmentDirections.action_Details()
        action.setRepo_id(id.toString())
        Log.e("TAG"," sender id "+id)
        findNavController().navigate(action)


    }

}
