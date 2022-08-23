package com.submeter.android.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.submeter.android.SubmeterApp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

	/**
	 * 
	 * 判断Network是否开启(包括移动网络和wifi)
	 */
	public static boolean isNetworkEnabled(Context mContext) {
		return (isNetEnabled(mContext) || isWIFIEnabled(mContext));
	}

	/**
	 * 判断Network是否连接成功(包括移动网络和wifi)
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context mContext) {
		return (isWifiContected(mContext) || isNetContected(mContext));
	}

	/**
	 * 
	 * 判断移动网络是否开启
	 * 
	 * @return
	 */
	public static boolean isNetEnabled(Context context) {
		boolean enable = false;
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
				enable = true;
				Log.i(Thread.currentThread().getName(), "isNetEnabled");
			}
		}

		return enable;
	}

	/**
	 * 判断wifi是否开启
	 */
	public static boolean isWIFIEnabled(Context context) {
		boolean enable = false;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			enable = true;
			Log.i(Thread.currentThread().getName(), "isWifiEnabled");
		}

		Log.i(Thread.currentThread().getName(), "isWifiDisabled");
		return enable;
	}

	/**
	 * 判断移动网络是否连接成功！
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetContected(Context context) {
		if(!isNetEnabled(context)) {
			return false;
		}
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetworkInfo.isConnected()) {

			Log.i(Thread.currentThread().getName(), "isNetContected");
			return true;
		}
		Log.i(Thread.currentThread().getName(), "isNetDisconnected");
		return false;
	}

	/**
	 * 判断wifi是否连接成功
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiContected(Context context) {
		if(!isWIFIEnabled(context)) {
			return false;
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {

			Log.i(Thread.currentThread().getName(), "isWifiContected");
			return true;
		}
		Log.i(Thread.currentThread().getName(), "isWifiDisconnected");
		return false;
	}

	/**
	 * 获取ip地址
	 * @return
	 */
	public static String getHostIP() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				Enumeration<InetAddress> enIp = ni.getInetAddresses();
				while (enIp.hasMoreElements()) {
					InetAddress inet = enIp.nextElement();
					if (!inet.isLoopbackAddress() && (inet instanceof Inet4Address)) {
						return inet.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "127.0.0.1";
	}

	public static String getNetworkType() {
		String strNetworkType = "";

		NetworkInfo networkInfo = ((ConnectivityManager) SubmeterApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = "WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();
				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: { //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					}

					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP: { //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					}

					case TelephonyManager.NETWORK_TYPE_LTE: {   //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					}

					default: {
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							strNetworkType = "3G";
						} else {
							strNetworkType = _strSubTypeName;
						}

						break;
					}
				}
			}
		}

		return strNetworkType;
	}
}