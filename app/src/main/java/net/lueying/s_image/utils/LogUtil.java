package com.gongyujia.app.util;

import android.util.Log;

import com.gongyujia.app.core.App;

/**
 * Created by XTF on 2017/5/5.
 */

public class LogUtil {
    
    private static boolean isOpen = App.isDebug;
    private static String tag = "GYJR";
    
    public static void e(String value) {
        if (isOpen) {
            Log.e(tag, value);
        }
    }
    
    public static void i(String value) {
        if (isOpen) {
            Log.i(tag, value);
        }
    }
}
