package com.fushui.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.receiver.MyAdmin;

public class ForthSetupActivity extends Activity {

	private CheckBox ck_isOpen;
	private TextView tv_isOpen;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forth_setup);
		findViewById();
		check();
	}

	private void check() {
		// TODO Auto-generated method stub
		boolean is_open_protected = SavePreferences.getBoolean(
				ForthSetupActivity.this, Constant.IS_OPEN_PROTECTED);
		if (is_open_protected) {
			ck_isOpen.setChecked(true);
			tv_isOpen.setText("防盗保护已经开启");
			SavePreferences.save(ForthSetupActivity.this,
					Constant.IS_OPEN_PROTECTED, true);
		} else {
			ck_isOpen.setChecked(false);
			tv_isOpen.setText("防盗保护没有开启");
			SavePreferences.save(ForthSetupActivity.this,
					Constant.IS_OPEN_PROTECTED, false);
		}
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		ck_isOpen = (CheckBox) findViewById(R.id.id_ck_open);
		tv_isOpen = (TextView) findViewById(R.id.id_tv_open);
	}

	public void previous(View v) {
		Intent intent = new Intent(ForthSetupActivity.this,
				ThirdSetupActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}

	public void ck_open(View v) {
		// 选择框点击事件
		boolean is_open_protected = SavePreferences.getBoolean(
				ForthSetupActivity.this, Constant.IS_OPEN_PROTECTED);
		if (!is_open_protected) {
			ck_isOpen.setChecked(true);
			tv_isOpen.setText("防盗保护已经开启");
			SavePreferences.save(ForthSetupActivity.this,
					Constant.IS_OPEN_PROTECTED, true);
			
			activeAdmin();
		} else {
			ck_isOpen.setChecked(false);
			tv_isOpen.setText("防盗保护没有开启");
			SavePreferences.save(ForthSetupActivity.this,
					Constant.IS_OPEN_PROTECTED, false);
		}
	}

	public void activeAdmin(){
    	//设备安全服务，2.2之前不对外暴露
    			DevicePolicyManager devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
    			
    			//为组件申请权限,为授权
    			ComponentName componentName = new ComponentName(this,MyAdmin.class);
    			
    			//判断组件是否具有管理员权限
    			boolean isAdmin = devicePolicyManager.isAdminActive(componentName);
    			if(isAdmin){
    				//锁屏
    				//devicePolicyManager.lockNow();
    				
    				//重设密码
    				//devicePolicyManager.resetPassword("1234", 0);
    				
    				//恢复出厂设置    //模拟器不支持
    				//devicePolicyManager.wipeData(0);
    			}else{
    				Intent intent = new Intent();
    				
    				//指定动作
    				intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    				
    				//指定给组件授权
    				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
    				
    				startActivity(intent);
    			}
    }

	public void next(View v) {
		// Intent intent = new Intent(ThirdSetupActivity.this,
		// ForthSetupActivity.class);
		// startActivity(intent);
		// finish();
		// 判断是否开启防盗保护
		boolean is_open_protected = SavePreferences.getBoolean(
				ForthSetupActivity.this, Constant.IS_OPEN_PROTECTED);
		if (is_open_protected) {
			SavePreferences.save(ForthSetupActivity.this, Constant.IS_SETUP,
					true);
			finish();
		} else {
			// 为开启弹出对话框
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// 设置标题和信息
			builder.setTitle("强烈建议");
			builder.setMessage("请开启防盗保护");

			// 开启与取消按钮事件监听
			builder.setPositiveButton("开启",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							// 选中选择框和修改文本
							ck_isOpen.setChecked(true);
							tv_isOpen.setText("防盗保护已经开启");
							// 保存设置信息
							SavePreferences.save(ForthSetupActivity.this,
									Constant.IS_OPEN_PROTECTED, true);
							SavePreferences.save(ForthSetupActivity.this,
									Constant.IS_SETUP, true);
							
							
							activeAdmin();
							finish();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							// 表明设置完成
							SavePreferences.save(ForthSetupActivity.this,
									Constant.IS_SETUP, true);
							finish();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}
