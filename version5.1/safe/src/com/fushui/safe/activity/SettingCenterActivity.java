package com.fushui.safe.activity;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.utils.T;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingCenterActivity extends Activity {

	private CheckBox ck;
	private CheckBox ck_auto_call;
	private SettingCenterActivity TAG = SettingCenterActivity.this;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingcenter);
		findViewById();
		setOnClickListener();
        check();
	}

	
	private void check() {
		// TODO Auto-generated method stub
		boolean is_ip_call = SavePreferences.getBoolean(getApplicationContext(), Constant.IS_IP_CALL);
		if(is_ip_call){
			ck_auto_call.setChecked(true);
		}else{
			ck_auto_call.setChecked(false);
		}
	}


	private void setOnClickListener() {
		// TODO Auto-generated method stub
		ck.setOnClickListener(new View.OnClickListener() {

			// 判断checkbox选中状态，并存放到sharePreferences中
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ck.isChecked()) {
					ck.setChecked(true);
					SavePreferences.save(TAG, Constant.IS_UPDATE_DATA, true);
				} else {
					ck.setChecked(false);
					SavePreferences.save(TAG, Constant.IS_UPDATE_DATA, false);
				}
			}
		});
		ck_auto_call.setOnClickListener(new View.OnClickListener() {

			// 判断checkbox选中状态，并存放到sharePreferences中
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ck_auto_call.isChecked()) {
					ck_auto_call.setChecked(true);
					SavePreferences.save(TAG, Constant.IS_IP_CALL, true);
				} else {
					ck_auto_call.setChecked(false);
					SavePreferences.save(TAG, Constant.IS_IP_CALL, false);
				
				}
			}
			
		});
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		ck = (CheckBox) findViewById(R.id.id_cb_isupdata);
		ck_auto_call = (CheckBox) findViewById(R.id.id_cb_is_auto_ipcall);
	}

}
