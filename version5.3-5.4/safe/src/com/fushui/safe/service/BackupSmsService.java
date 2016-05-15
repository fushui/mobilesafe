package com.fushui.safe.service;

import java.util.List;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

import com.fushui.safe.R;
import com.fushui.safe.activity.MainActivity;
import com.fushui.safe.domain.SmsInfo;
import com.fushui.safe.engine.SmsInfoService;
import com.fushui.safe.utils.T;

public class BackupSmsService extends Service {

	
	SmsInfoService infoService ;
	
	NotificationManager nm;
	public BackupSmsService() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		infoService = new SmsInfoService(this);
		
		//获取通知管理器
		nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		new Thread(){
			public void run() {
				//获取短信，然后生成xml保存下来
				List<SmsInfo> infos = infoService.getSmsInfos();
				
				try {
					infoService.createXml(infos);
					
					

					//发送通知，告诉用户备份完成
					Notification notification = new Notification(R.drawable.ic_launcher, "短信备份完成", System.currentTimeMillis());
					
					//启动主界面
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);
					
					notification.setLatestEventInfo(getApplicationContext(), "提示信息", "短信备份完成", contentIntent);
					
					//设置消息消失
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					
					//显示通知
					nm.notify(100,notification);
					

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					//相当于handler,looper是一个消息泵，从消息队列(MessageQueue)获取消息,把消息交给handle
					Looper.prepare();
					T.makeTest(getApplicationContext(),"短信备份失败");
				    Looper.loop();
				    

					
				}
				

				//停止服务
				stopSelf();
				
			};
		}.start();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
