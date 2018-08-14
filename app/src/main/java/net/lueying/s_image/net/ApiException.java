package net.lueying.s_image.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.lueying.s_image.constant.CommonConstant;
import net.lueying.s_image.core.App;
import net.lueying.s_image.ui.auth.LoginActivity;
import net.lueying.s_image.utils.Encryption;
import net.lueying.s_image.utils.LogUtil;
import net.lueying.s_image.utils.ToastUtil;

import java.util.Properties;

public class ApiException extends RuntimeException {
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static final int CODE_TOCKEN_FAILED = 20;
    public static final int CODE_TOCKEN_ERROR = 22;//失效
    public static final int CODE_TOCKEN_REFRESH = 21;//更新

    private Context context;

    private int code;

    public int getCode() {
        return code;
    }

    public ApiException(int code, String message, String data) {
        super(message);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.code = code;
        handleException(code, message, data);
        context = App.getApplication();
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 处理服务器返回异常
     *
     * @param code
     * @param msg
     */
    private void handleException(int code, String msg, String data) {
        switch (code) {
            case CODE_TOCKEN_FAILED:
                App.getApplication().logout();
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
            case CODE_TOCKEN_ERROR:
                App.getApplication().logout();
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
            case CODE_TOCKEN_REFRESH:
                //更新tocken,不需要重新登录
                if (!TextUtils.isEmpty(data)) {
                    JSONObject jsonObject = JSON.parseObject(data);
                    String tocken = jsonObject.getString(CommonConstant.TOKEN);
                    //更新tocken
                    //本地化用户登录信息
                    App.getApplication().setConfigs(new Properties() {{
                        try {
                            setProperty(CommonConstant.TOKEN, Encryption.encrypt(tocken, CommonConstant.IV));
                        } catch (Exception e) {
                            LogUtil.e("加密异常:" + e.getMessage());
                        }
                    }});
                    LogUtil.i("tocken信息更新");
                } else {
                    App.getApplication().logout();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

                break;
            default:
//                ToastUtil.showShort(App.getContext(), msg);
                LogUtil.e("出错了:" + msg);
                break;
        }
    }

}
