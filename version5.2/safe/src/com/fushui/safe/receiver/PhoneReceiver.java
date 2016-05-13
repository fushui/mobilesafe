package com.fushui.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.fushui.safe.activity.ProtectedActivity;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;

public class PhoneReceiver extends BroadcastReceiver {

	public PhoneReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
        Log.i("i","拦截到外部电话");
        
        //清除数据，启动Activity
        //获取所拨打的电话号码
        String number = getResultData();
	    if("20121212".equals(number)){
	    	setResultData(null);
	    	
	    	//receiver不存在于任务栈里，在里面启动Activity必须设置flag,FLAG_ACTIVITY_NEW_TASK
	    	Intent i = new Intent(context,ProtectedActivity.class);
	    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(i);
	    }else{
	    	//判断是否开启ip拨号
	    	boolean is_auto_call = SavePreferences.getBoolean(context,Constant.IS_IP_CALL);
	    	if(is_auto_call){
	    		//如果开启，获得ip号码
	    		String ip_number = SavePreferences.getString(context, Constant.IP_NUMBER);
	    	    if(!TextUtils.isEmpty(ip_number))
	    		setResultData(ip_number + number); 
	    	}
	    }
	 }

}
