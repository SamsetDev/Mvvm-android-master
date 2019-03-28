package com.samset.mvvm.mvvmsampleapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.mvvmsampleapp.R
import com.example.test.mvvmsampleapp.databinding.ProjectListItemBinding
import com.samset.mvvm.mvvmsampleapp.view.model.Project
import com.samset.mvvm.mvvmsampleapp.view.callback.ProjectClickCallback
import com.samset.mvvm.mvvmsampleapp.view.ui.base.BaseAdapter

class WithBaseAdapter(private val projectClickCallback: ProjectClickCallback?) : BaseAdapter<Project>() {

    internal var projectList: ArrayList<Project>? = null

    public fun setData(data: ArrayList<Project>) {
        this.projectList = data
    }

    override fun getDataList(): ArrayList<Project>? {
        return projectList
    }

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ProjectListItemBinding>(LayoutInflater.from(parent.context), R.layout.project_list_item,
                parent, false)

        binding.callback = projectClickCallback

        return ProjectViewHolder(binding)
    }

    override fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ProjectViewHolder)
            holder.binddata(position)

    }


    inner class ProjectViewHolder(val binding: ProjectListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        infix fun binddata(pos: Int) {
            binding.setProject(projectList?.get(pos))
            binding.executePendingBindings()
        }
    }
}
