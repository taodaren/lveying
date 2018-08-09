package com.gongyujia.app.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongyujia.app.R;

/**
 * Created by admin on 2017/1/4.
 */

public class ToastUtil {
    
    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    public static void showToast(Context context, String text) {
        
        showToast(context, text, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0, 180);
    }
    
    public static void showToast(Context context, String text, int duration, int gravity, int xOffset, int yOffset) {
        
        MyToast.getInstance(context).makeText(context, text, duration, gravity, xOffset, yOffset);
    }
    
    /**
     * Toast的内部类
     */
    private static class MyToast {
        
        private static MyToast mToast;
        private final Toast toast;
        
        private MyToast(Context context) {
            toast = new Toast(context);
        }
        
        //静态工厂方法
        public static MyToast getInstance(Context context) {
            if (mToast == null) {
                mToast = new MyToast(context);
            }
            return mToast;
        }
        
        public void makeText(Context context, String text, int duration, int gravity, int xOffset, int yOffset) {
            // 1:这一步是关键，不能重复的new出Toast，否则就会导致布局重复显示
            getInstance(context);
            // 3:重新初始化布局
            View view = LayoutInflater.from(context).inflate(R.layout.mytoast, null);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            // 4:设置布局的内容
            textView.setText(text);
            // 5:设置Toast的参数
            toast.setDuration(duration);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.setView(view);
            
            toast.show();
        }
    }
}
