package com.fushui.safe.receiver;

import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

	//开机启动生效
	TelephonyManager tm;
	public BootCompletedReceiver() {
		// TODO Auto-generated constructor stub
	     
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub

		Log.i("i", "拦截到开机广播");
		tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		//判断是否开启保护
		boolean is_protected = SavePreferences.getBoolean(context, Constant.IS_OPEN_PROTECTED);
	
		if(is_protected){
			//获取现在的手机标识码
			String serial_num = tm.getSimSerialNumber();
			//获取以前的手机标识码
			String old_serial_num = SavePreferences.getString(context, Constant.SIM_SERIAL);
		
		    if(!serial_num.equals(old_serial_num)){
		    	
		    	//发送短信
		    	SmsManager smsManager = SmsManager.getDefault();
		    	
		    	String save_num = SavePreferences.getString(context, Constant.SAVE_NUMBER);
		    	smsManager.sendTextMessage(save_num, null, "你的手机被盗了", null, null);
		    	Log.i("i", "你的手机被盗了");
		    	abortBroadcast();
		    }
		}
		
	}

}
