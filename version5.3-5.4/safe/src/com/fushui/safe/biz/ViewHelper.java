package com.fushui.safe.biz;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ViewHelper {
    public static String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = (PackageInfo)pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    	
    	return null;
	}
}
