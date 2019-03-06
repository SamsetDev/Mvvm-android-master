package com.mvvm.sample.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Copyright (C) ViewModel-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 06/03/19 at 5:05 PM for ViewModel-master .
 */


public class Constants {

    public static int generateRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
