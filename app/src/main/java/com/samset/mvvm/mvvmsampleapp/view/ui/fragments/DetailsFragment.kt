package com.samset.mvvm.mvvmsampleapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.test.mvvmsampleapp.R
import com.example.test.mvvmsampleapp.databinding.FragmentDetailsBinding
import com.samset.mvvm.mvvmsampleapp.remote.di.Injectable
import com.samset.mvvm.mvvmsampleapp.remote.service.model.Project
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.ProjectViewModel

import javax.inject.Inject
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


class DetailsFragment : Fragment(), Injectable {
    private var binding: FragmentDetailsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        // Create and set the adapter for the RecyclerView.
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectViewModel::class.java)
        val id = DetailsFragmentArgs.fromBundle(getArguments()).repo_id
        viewModel.setProjectID(id)

        binding?.projectViewModel = viewModel
        binding?.isLoading = true

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: ProjectViewModel) {
        // Observe project data
        viewModel.observableProject.observe(this, Observer { project ->
            if (project != null) {
                binding?.isLoading = false
                viewModel.setProject(project)
            }
        })
    }
}
