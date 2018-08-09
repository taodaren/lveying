package com.gongyujia.app.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.Stack;

/**
 * author ATao
 * version 1.0
 * created 2015/9/29
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;
	private static Context mContext;

	private AppManager() {
	}

	public Context getContext() {
		mContext = currentActivity().getApplicationContext();
		return mContext;
	}

	public static void setContext(Context context) {
		mContext = context;
	}

	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	public static Activity getActivity(Class<?> cls) {
		if (activityStack != null)
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					return activity;
				}
			}
		return null;
	}

	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	public void finishAllActivityExcept(Class<?> cls) {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i) && !activityStack.get(i).getClass().equals(cls)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	/**
	 * 判断一个Activity 是否存在
	 *
	 * @param clz
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static <T extends Activity> boolean isActivityExist(Class<T> clz) {
		boolean res;
		Activity activity = getActivity(clz);
		if (activity == null) {
			res = false;
		} else {
			if (activity.isFinishing() || activity.isDestroyed()) {
				res = false;
			} else {
				res = true;
			}
		}

		return res;
	}
}
