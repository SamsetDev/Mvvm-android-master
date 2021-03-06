package com.samset.mvvm.mvvmsampleapp.view.ui.base


import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.example.test.mvvmsampleapp.R
import com.samset.mvvm.mvvmsampleapp.utils.CommonUtils


/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 *
 *
 * Created by samset on 25/03/19 at 6:24 PM for Mvvm-android-master .
 */


abstract class BaseFragment : Fragment(), BaseView, View.OnClickListener {

    lateinit var btnRetry: AppCompatButton

    lateinit var mEmptyview: LinearLayout

    lateinit var mProgressView: LinearLayout

    lateinit var mNetworkErrorView: LinearLayout

    lateinit var mServerErrorView: LinearLayout

    private lateinit var mActivity: BaseActivity

    private val TAG = BaseFragment::class.java.getSimpleName()

    private lateinit var munbinder: Unbinder


    @LayoutRes
    protected abstract fun getFragmentLayout(): Int

    protected abstract fun initView(view: View)

    protected abstract fun fetchDataFromServer(page: Int)


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val baseactivity = context as BaseActivity
            this.mActivity = baseactivity
            baseactivity?.onFragmentAttached()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getFragmentLayout(), container, false)
        munbinder=ButterKnife.bind(this,view)
        mServerErrorView = view.findViewById(R.id.ServerError)
        mNetworkErrorView = view.findViewById(R.id.networkError)
        mProgressView = view.findViewById(R.id.progressView)
        mEmptyview = view.findViewById(R.id.empty_view)
        btnRetry = view.findViewById(R.id.retry)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }



    protected fun getLayoutManager(): LinearLayoutManager {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return layoutManager
    }


    override fun openActivityOnTokenExpire() {}

    override fun showErrorHeader(statusCode: Int) {}

    override fun showError(@StringRes resId: Int) {
        mActivity?.showError(resId)
    }

    override fun showError(message: String?) {
        mActivity?.showError(message)
    }

    override fun showLoading() {
        mEmptyview?.visibility = View.GONE
        mNetworkErrorView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mProgressView?.setVisibility(View.VISIBLE)

    }

    override fun showMessage(message: String?) {
        mEmptyview?.visibility = View.GONE
        mProgressView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mNetworkErrorView?.setVisibility(View.GONE)
    }

    override fun showMessage(@StringRes resId: Int) {
        mActivity?.showError(resId)
        mEmptyview?.visibility = View.GONE
        mProgressView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mNetworkErrorView?.setVisibility(View.GONE)

    }

    override fun onShowContent(view: View?) {
        mEmptyview?.visibility = View.GONE
        mProgressView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mNetworkErrorView?.setVisibility(View.GONE)
    }

    override fun showServerError() {
        mEmptyview?.visibility = View.GONE
        mProgressView?.setVisibility(View.GONE)
        mNetworkErrorView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.VISIBLE)
        btnRetry.setOnClickListener(this)
    }

    override fun showNetworkError() {
        mEmptyview?.visibility = View.GONE
        mProgressView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mNetworkErrorView?.setVisibility(View.VISIBLE)
        btnRetry.setOnClickListener(this)
    }

    override fun showNoData() {
        mNetworkErrorView?.setVisibility(View.GONE)
        mServerErrorView?.setVisibility(View.GONE)
        mProgressView?.setVisibility(View.GONE)
        mEmptyview?.visibility = View.VISIBLE
    }

    override fun onRetry() {
        Log.e("TAG"," retry")
            fetchDataFromServer(0)
    }

    override fun isNetworkConnected(): Boolean {
        return CommonUtils.isNetworkConnected(context)
    }

    override fun hideKeyboard() {

    }

    private fun errorItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            (context?.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (context?.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(500)
        }
    }


    override fun onClick(v: View) {
        when (btnRetry) {
            btnRetry -> onRetry()
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        munbinder?.unbind()
    }

    override fun onDetach() {
        super.onDetach()
    }


    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

}
