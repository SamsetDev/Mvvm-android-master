package com.samset.mvvm.mvvmsampleapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.app.ActivityCompat.requestPermissions;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 27/03/19 at 10:45 AM for Mvvm-android-master .
 */


public class PermissionUtils {

    /**
     * check permission for location
     */
    public static boolean checkStoragePermission(Activity context) {
        int read_storage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_storage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return (read_storage == PackageManager.PERMISSION_GRANTED && write_storage == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * request permission for location
     */
    public static void requestStoragePermission(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);

        } else {
            requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);

        }
    }
}
