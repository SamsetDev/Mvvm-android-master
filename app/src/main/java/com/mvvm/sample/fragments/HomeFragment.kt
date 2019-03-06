package com.mvvm.sample.fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.mvvm.sample.R
import com.mvvm.sample.viewmodels.MyViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var myViewModel: MyViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        mainframe.setBackgroundColor(myViewModel.getColorResource())
        btnsubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val color = generateRandomColor()
                mainframe.setBackgroundColor(color)
                myViewModel.setColorResource(color)
            }

        })


    }

    private fun generateRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }


}
