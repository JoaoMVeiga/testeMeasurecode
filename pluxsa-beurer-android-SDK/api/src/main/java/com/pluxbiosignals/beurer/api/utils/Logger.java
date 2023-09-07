/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.api.utils;

import android.util.Log;

public class Logger {
    protected static boolean LOG_ENABLED = true;

    protected static String MAIN_TAG = "[Logger]";

    public static void d(String tag, String msg) {
        if (LOG_ENABLED)
            Log.d(MAIN_TAG + tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_ENABLED)
            Log.d(MAIN_TAG + tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLED)
            Log.e(MAIN_TAG + tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_ENABLED)
            Log.e(MAIN_TAG + tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (LOG_ENABLED)
            Log.i(MAIN_TAG + tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_ENABLED)
            Log.i(MAIN_TAG + tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (LOG_ENABLED)
            Log.v(MAIN_TAG + tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_ENABLED)
            Log.v(MAIN_TAG + tag, msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        if (LOG_ENABLED)
            Log.w(MAIN_TAG + tag, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_ENABLED)
            Log.w(MAIN_TAG + tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (LOG_ENABLED)
            Log.w(MAIN_TAG + tag, msg);
    }
}