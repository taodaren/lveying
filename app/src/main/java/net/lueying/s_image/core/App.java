package net.lueying.s_image.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import net.lueying.s_image.constant.UserConstant;
import net.lueying.s_image.entity.LoginUser;
import net.lueying.s_image.utils.Encryption;

import java.util.Properties;

public class App extends Application {
    private static App mApplication;
    public static boolean isDebug;
    //屏幕的宽和高
    public static int windowWidth = 0;
    public static int windowHeight = 0;
    private LoginUser loginUser;
    private boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        isDebug = checkDebug(mApplication);
//        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        initUser();
        initDisplay();
    }


    public static App getApplication() {
        return mApplication;
    }

    public static Context getContext() {
        return mApplication;
    }

    /**
     * 检测当前模式是否是Debug状态
     *
     * @param context
     * @return
     */
    private boolean checkDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    private void initDisplay() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (dm.widthPixels > dm.heightPixels) {
            windowWidth = dm.heightPixels;
            windowHeight = dm.widthPixels;
        } else {
            windowHeight = dm.heightPixels;
            windowWidth = dm.widthPixels;
        }
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    public String getConfig(String key) {
        String res = AppConfig.getInstance(this).get(key);
        return res;
    }

    /**
     * 单独获取加密过的值
     *
     * @param key
     * @return
     */
    public String getEncryptConfig(String key) {
        String res = "";
        try {
            res = Encryption.decrypt(AppConfig.getInstance(this).get(key),UserConstant.IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void initUser() {
        loginUser = getLoginInfo();
        if (!TextUtils.isEmpty(getConfig(UserConstant.TOKEN))) {
            isLogin = true;
        } else {
            logout();
        }
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * 获取用户的登录信息
     *
     * @return
     */
    public LoginUser getLoginInfo() {
        LoginUser user = new LoginUser();
        user.setToken(getConfig(UserConstant.TOKEN));
        return user;
    }

    /**
     * 登出
     */
    public void logout() {
        removeConfig(UserConstant.TOKEN
        );
        isLogin = false;
        loginUser = null;
    }

    /**
     * 清除用户信息缓存
     *
     * @param key
     */
    public void removeConfig(String... key) {
        AppConfig.getInstance(this).remove(key);
    }

    /**
     * 获取ANDROID_ID作为设备唯一序号
     */

    public String getAndroidID() {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void setConfigs(Properties ps) {
        AppConfig.getInstance(this).set(ps);
    }

}
