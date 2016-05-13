package com.fushui.safe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.db.BlackNumberDao;

public class BlackNumberService extends Service {

	private TelephonyManager tm;

	private MyPhoneStateListener listener;

	private BlackNumberDao dao;

	boolean is_open_abort_number;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 判断是否开启拦截服务
		is_open_abort_number = SavePreferences.getBoolean(this,
				Constant.IS_ABORT_NUMBER);

		
		dao = new BlackNumberDao(this);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		// 实例化监听器
		listener = new MyPhoneStateListener();

		// 设置监听事件
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		

		
	}

	// 监听电话的呼叫状态
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			
			
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 正在接听
				
				if (is_open_abort_number) {
					boolean is_black_number = dao.isBlackNumber(incomingNumber);
                    if(is_black_number){
                    	endcall(incomingNumber);
                    }
				}
				// 判断是否黑名单
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			}
		}

	
	}
	private void endcall(String incomingNumber) {
		
				try {
					
					//通过反射技术
					Class clazz = Class.forName("android.os.ServiceManager");
					Method method = clazz.getMethod("getService", String.class);
					IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
					ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
					iTelephony.endCall();
					
					
					//相当于访问系统数据库
					//删除通话记录 通话记录的保存是一个异步的操作，需要使用ContentObserver技术来实现
					Uri uri = Calls.CONTENT_URI;
					getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler(),incomingNumber));
//					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	
	private final class MyContentObserver extends ContentObserver{

		private String incomingNumber;
		public MyContentObserver(Handler handler, String incomingNumber) {
			super(handler);
			// TODO Auto-generated constructor stub
			this.incomingNumber = incomingNumber;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			Uri uri = Calls.CONTENT_URI;
			String where = Calls.NUMBER + " = ?";
			String[] selectionArgs = new String[]{incomingNumber};
			getContentResolver().delete(uri, where, selectionArgs);
			
			//解除监听
			getContentResolver().unregisterContentObserver(this);
		}
	}
	// 用于挂断黑名单号码的电话
	public BlackNumberService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
