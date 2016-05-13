package com.fushui.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.receiver.MyAdmin;
import com.fushui.safe.utils.T;

public class ProtectedActivity extends Activity {

	private static final int MENU_CHANGE_NAME_ID = 0;
	ProtectedActivity TAG = ProtectedActivity.this;
	CheckBox ck_isopen;
	TextView tv_isopen;
	TextView tv_save;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protect);
		findViewById();
		setConfig();
	}

	// 根据SharePreference中数据保存配置
	private void setConfig() {
		// TODO Auto-generated method stub
		boolean is_open = SavePreferences.getBoolean(getApplicationContext(),
				Constant.IS_OPEN_PROTECTED);
		if (is_open) {
			ck_isopen.setChecked(true);
			tv_isopen.setText("防盗保护已经开启");
		} else {
			ck_isopen.setChecked(false);
			tv_isopen.setText("防盗保护没有开启");
		}
		String number = SavePreferences.getString(getApplicationContext(),
				Constant.SAVE_NUMBER);
		tv_save.setText(number);
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		ck_isopen = (CheckBox) findViewById(R.id.id_ck_is_open);
		tv_isopen = (TextView) findViewById(R.id.id_tv_id_tv_is_open);
		tv_save = (TextView) findViewById(R.id.id_tv_saveNum);
	}

	public void reset(View v) {
		Intent intent = new Intent(ProtectedActivity.this,
				FirstSetupActivity.class);
		startActivity(intent);
		finish();

	}

	public void open(View v) {
		// 选择框点击事件
		boolean is_open_protected = SavePreferences.getBoolean(TAG,
				Constant.IS_OPEN_PROTECTED);
		if (!is_open_protected) {
			ck_isopen.setChecked(true);
			tv_isopen.setText("防盗保护已经开启");
			SavePreferences.save(TAG, Constant.IS_OPEN_PROTECTED, true);
			
			activeAdmin();
		} else {
			ck_isopen.setChecked(false);
			tv_isopen.setText("防盗保护没有开启");
			SavePreferences.save(TAG, Constant.IS_OPEN_PROTECTED, false);
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
	//新建菜单选项，用于更改防盗界面名字
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,MENU_CHANGE_NAME_ID,0,"更改防盗界面名字");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		  int id = item.getItemId();
		  switch (id) {
		case MENU_CHANGE_NAME_ID:
			final View view = View.inflate(this,R.layout.change_name, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("修改手机防盗的名称");
			builder.setView(view);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//获取输入的名称，并保存起来
					EditText et_change_name = (EditText)view.findViewById(R.id.id_et_change_name);
					String change_name = et_change_name.getText().toString();
					if(TextUtils.isEmpty(change_name)){
						T.makeTest(TAG, "名称不能为空！");
					}else{
						SavePreferences.save(TAG, Constant.PROTECTED_NAME, change_name);
					}
				}
			});
			
			//取消不做操作
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.create().show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	  
	    
	}
}
