package com.fushui.safe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;

import android.util.Log;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.db.BlackNumberDao;
import com.fushui.safe.engine.LocationInfoService;

public class SMSReceiver extends BroadcastReceiver {

	
	BlackNumberDao dao;
	public SMSReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		dao = new BlackNumberDao(context);
		// TODO Auto-generated method stub
		Log.i("i", "拦截到短信");
		// 判断是否开启保护
		boolean is_protected = SavePreferences.getBoolean(context,
				Constant.IS_OPEN_PROTECTED);

		// 设备安全服务，2.2之前不对外暴露
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		if (is_protected) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");

			for (Object pdu : pdus) {
				// 转换为短信

				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				// 拦截接受的短信
				String body = smsMessage.getMessageBody();
				
				
				//拦截接受的短信号码
				String number = smsMessage.getDisplayOriginatingAddress();

				if ("#*location*#".equals(body)) {
					// 获取位置
					LocationInfoService service = LocationInfoService
							.getInstance(context);
					service.registerLocListener();
					String last_location = service.getLastLocation();
					Log.i("i", last_location);
					// 发送短信
					SmsManager sms = SmsManager.getDefault();
					String save_num = SavePreferences.getString(context,
							Constant.SAVE_NUMBER);
					sms.sendTextMessage(save_num, null, "location->"
							+ last_location, null, null);

					// 中断广播
					abortBroadcast();
				
				} else if ("#*lockscreen*#".equals(body)) {

					//锁屏和重设密码
					//devicePolicyManager.lockNow();
					//devicePolicyManager.resetPassword("1234", 0);
					

					// 中断广播
					abortBroadcast();
				}else if("#*delete*#".equals(body)){
					//恢复出厂设置
					//devicePolicyManager.wipeData(0);
					

					// 中断广播
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//发出报警音乐
//					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
//					
//					//设置声音
//					mediaPlayer.setVolume(1.0f, 1.0f);
//					
//					mediaPlayer.start();
//					

					// 中断广播
					abortBroadcast();
				}else if(body.contains("cctv")){

					// 中断广播
					abortBroadcast();
				}
				
				
				boolean is_black_number = dao.isBlackNumber(number);
				if(is_black_number){
					abortBroadcast();
				}
			}
		}
	}

}
