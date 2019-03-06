package com.mvvm.sample.views


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.mvvm.sample.R
import com.mvvm.sample.utils.Constants.generateRandomColor
import com.mvvm.sample.viewmodels.MyViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var myViewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        defaultCallViewModel()
        mainlayout.setBackgroundColor(myViewModel.getColorResource())




        btncustom.setOnClickListener(this)
        btnsubmit.setOnClickListener(this)

    }

    override fun onClick(view: View?) {

        if (btnsubmit == view) {
            defaultCallViewModel()
        } else if (btncustom == view) {
            startActivity(Intent(this, FactoryActivity::class.java))
        }

    }

    private fun defaultCallViewModel() {
        val color = generateRandomColor()
        mainlayout.setBackgroundColor(color)
        myViewModel.setColorResource(color)
    }


}
