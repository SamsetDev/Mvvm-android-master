package com.samset.mvvm.mvvmsampleapp.view.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.test.mvvmsampleapp.R
import com.samset.mvvm.mvvmsampleapp.remote.di.Injectable
import com.samset.mvvm.mvvmsampleapp.utils.UI_STATUS
import com.samset.mvvm.mvvmsampleapp.view.ui.base.BaseFragment
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject


class DetailsFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_details
    }

    override fun initView(view: View) {


    }

    override fun fetchDataFromServer(page: Int) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectViewModel::class.java)
        val id = DetailsFragmentArgs.fromBundle(getArguments()).repo_id.toString()
        viewModel.setProjectID(id)


        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: ProjectViewModel) {
        // Observe project data
        viewModel.observableProject.observe(this, Observer { project ->
            if (project != null) {
                viewModel.setProject(project)
                setData(viewModel)
            }
        })

        viewModel.getUiStateLiveData().observe(this, Observer<UI_STATUS> {
            when (it) {

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


    private fun setData(viewModel: ProjectViewModel) {
        val project = viewModel.project.get()
        name.text = project?.name
        project_desc.text = project?.description
        languages.text = project?.language
        project_watchers.text = project?.watchers.toString()
        project_open_issues.text = project?.open_issues.toString()
        project_created_at.text = project?.created_at.toString()
        project_updated_at.text = project?.updated_at.toString()
        clone_url.text = project?.clone_url

    }
}
