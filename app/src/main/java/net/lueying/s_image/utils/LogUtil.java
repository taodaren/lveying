package net.lueying.s_image.utils;

import android.util.Log;

import net.lueying.s_image.core.App;

/**
 * log工具类
 */

public class LogUtil {

    private static boolean isOpen = App.isDebug;
    private static String tag = "LY";

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
