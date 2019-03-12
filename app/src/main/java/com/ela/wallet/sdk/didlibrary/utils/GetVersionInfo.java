/**
 * Copyright (C) 2015 mvp dev demo
 * Company:  wondertek
 * Data:   2016.03.10
 * Description: 版本信息获取工具
 * Author: Jimsu
 * 
 * Fix history:
 **/

package com.ela.wallet.sdk.didlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;

/**
 * 获取当前应用版本号
 *
 */
public class GetVersionInfo {
	/**
	 * 获取当前应用程序的版本号
	 * 
	 * @return 版本名称
	 */
	public static String getAppVersion(@NonNull Context mContext) {
		// 获取手机的包管理者
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(mContext.getPackageName(),
					0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
     * 获取当前应用程序的版本号
     * 
     * @return 版本号
     */
	public static String getAppVersionCode(@NonNull Context mContext) {
		// 获取手机的包管理者
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(mContext.getPackageName(),
					0);
			return packInfo.versionCode+"";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1+"";
		}
	}
	
	/**
     * 获取当前应用程序的包名
     * 
     * @return 应用程序的包名
     */
	public static String getAppPackagName(@NonNull Context mContext) {
		// 获取手机的包管理者
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(mContext.getPackageName(),
					0);
			
			return packInfo.packageName+"";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getMetaData() {
		String msg = "1.0.0";
		try {
			ApplicationInfo appInfo = Utilty.getContext().getPackageManager()
                    .getApplicationInfo(getAppPackagName(Utilty.getContext()),
                            PackageManager.GET_META_DATA);
			msg = appInfo.metaData.getString("LIBRARY_VERSION");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return msg;
	}
}
