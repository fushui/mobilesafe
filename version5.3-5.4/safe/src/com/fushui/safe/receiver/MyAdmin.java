package com.fushui.safe.receiver;

import android.app.admin.DeviceAdminReceiver;


//让该广播接收者去申请管理员权限，操作系统给广播接受者授权（激活系统的授权组件），用户自己激活
public class MyAdmin extends DeviceAdminReceiver {

	public MyAdmin() {
		// TODO Auto-generated constructor stub
	}

}
