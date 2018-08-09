package net.lueying.s_image.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import net.lueying.s_image.core.App;


public class NetworkUtil {

	public static final int NETTYPE_NO = 0;
	public static final int NETTYPE_WIFI = 1;
	public static final int NETTYPE_2G = 2;
	public static final int NETTYPE_3G = 3;
	public static final int NETTYPE_4G = 4;
	public static final int NETTYPE_UNKOWN = -1;
	
	public static int TYPE_NO = 0;
	public static int TYPE_MOBILE_CMNET = 1;
	public static int TYPE_MOBILE_CMWAP = 2;
	public static int TYPE_MOBILE_CTWAP = 3; // 移动梦网代理
	public static int TYPE_WIFI = 4;
	
	private static NetworkInfo mNetworkInfo = null;
	
	private static final String TAG = "NetworkUtil";

	public static boolean hasInternet() {
		boolean flag;
		if (((ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}
	
	public static int getNetworkState() {
		try {
			ConnectivityManager cm = (ConnectivityManager) App.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获得当前网络信息
			mNetworkInfo = cm.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				int currentNetWork = mNetworkInfo.getType();
				// 手机网络
				if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
					if (mNetworkInfo.getExtraInfo() != null
							&& mNetworkInfo.getExtraInfo().equals("cmwap")) {
						return TYPE_MOBILE_CMWAP;
					} else if (mNetworkInfo.getExtraInfo() != null
							&& mNetworkInfo.getExtraInfo().equals("uniwap")) {
						return TYPE_MOBILE_CMWAP;
					} else if (mNetworkInfo.getExtraInfo() != null
							&& mNetworkInfo.getExtraInfo().equals("3gwap")) {
						return TYPE_MOBILE_CMWAP;
					} else if (mNetworkInfo.getExtraInfo() != null
							&& mNetworkInfo.getExtraInfo().contains("ctwap")) {
						return TYPE_MOBILE_CTWAP;
					} else {
						return TYPE_MOBILE_CMNET;
					}
				} else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
					return TYPE_WIFI;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TYPE_NO;
	}
	
//	public static HttpHost getHttpHostProxy() {
//		int netType = getNetworkState();
//		HttpHost proxy = null;
//		if (netType == TYPE_MOBILE_CTWAP) { // 移动梦网
//			proxy = new HttpHost("10.0.0.200", 80);
//		} else { // cmwap uniwap 3gwap
//			proxy = new HttpHost("10.0.0.172", 80);
//		}
//
//		return proxy;
//	}
	
	public static NetworkInfo getAvailableNetWorkInfo() {
		NetworkInfo activeNetInfo = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) App.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
			activeNetInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (activeNetInfo != null && activeNetInfo.isAvailable()) {
			return activeNetInfo;
		} else {
			return null;
		}
	}
	
	public static boolean isWifi() {
		NetworkInfo networkInfo = getAvailableNetWorkInfo();
		if (networkInfo != null) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean is3G(){
		TelephonyManager telephonyManager = (TelephonyManager) App.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
		int netWorkType = telephonyManager.getNetworkType();
		switch (netWorkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_EDGE:

			return false;
		default:
			return true;
		}
	}
	
	/**
     * 判断sim卡网络为几代网络
     */
    public static int getNetGeneration(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETTYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NETTYPE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:

                    return NETTYPE_4G;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return NETTYPE_UNKOWN;
                default:
                    return NETTYPE_UNKOWN;
            }
        }
        else {
            return NETTYPE_NO;
        }
    }
}
