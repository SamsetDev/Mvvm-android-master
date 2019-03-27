
package com.samset.mvvm.mvvmsampleapp.view.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.mvvmsampleapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.samset.mvvm.mvvmsampleapp.utils.CommonUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static com.samset.mvvm.mvvmsampleapp.utils.PermissionUtils.checkStoragePermission;
import static com.samset.mvvm.mvvmsampleapp.utils.PermissionUtils.requestStoragePermission;


/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 27/03/19 at 10:45 AM for Mvvm-android-master .
 */


public abstract class BaseActivity extends AppCompatActivity implements BaseView, BaseFragment.Callback {

    private final int STARAGE_REQUEST = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initView(savedInstanceState);

        /* check permission */
        checkPermissionSetting();


    }

    private void checkPermissionSetting() {
        if (!checkStoragePermission(this)) {
            requestStoragePermission(this, STARAGE_REQUEST);
        } else {
            // Write your code
        }
    }


    @LayoutRes
    abstract protected int getActivityLayout();

    public abstract int getFragmentContainer();

    protected abstract void initView(Bundle bundle);


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {


    }


    @Override
    public void showError(String message) {
        if (message != null) {
            showSnackBar(message);
            errorItBaby();
        } else {
            showSnackBar(getString(R.string.internal_error));
        }
    }


    @Override
    public void showError(int resId) {
        showError(getString(resId));
    }


    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return CommonUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void showErrorHeader(int code) {
        setresponseMsg(code);
    }

    @Override
    public void showServerError() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void openActivityOnTokenExpire() {
        // startActivity(LoginActivity.getStartIntent(this));
        finish();
    }


    @Override
    public void onFragmentAttached() {
        Log.e("TAG", " Fragment attached");
    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onShowContent(View view) {
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    public boolean setresponseMsg(int code) {
        boolean status = true;
        if (code == 209) {
            status = false;
            showNoData();
        } else if (code >= 500) {
            status = false;
        }

        return status;
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void errorItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionSetting();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
