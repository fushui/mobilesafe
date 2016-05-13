package com.fushui.safe.biz;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;



/**
 * 往SharePreference存放数据，包括boolean和string
 * @author jianfeng
 *
 */
public class SavePreferences {

	//全局数据，用context
	public static void save(Context context,String key, Object value) {
		
		//拿到SharePreference
		SharedPreferences sp = context.getSharedPreferences(Constant.PFNAME, 0);
		
		//根据value存放数据
		if(value instanceof String){
			sp.edit().putString(key, (String)value).commit();
		}else if(value instanceof Boolean){
			sp.edit().putBoolean(key, (Boolean)value).commit();
		}
	}
	
	//返回数据，boolean和string
	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(Constant.PFNAME, 0);
		return sp.getString(key, "");
	}
	
	public static Boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(Constant.PFNAME, 0);
		return sp.getBoolean(key,false);
	}
}
