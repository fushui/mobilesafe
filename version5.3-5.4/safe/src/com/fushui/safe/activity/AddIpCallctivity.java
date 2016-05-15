package com.fushui.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.utils.T;

public class AddIpCallctivity extends Activity {

	EditText et_add_ipcall;
	Button bt_ipcall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_ipcall);
		findViewById();
		setOnClickListener();
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		bt_ipcall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				String ip_call = et_add_ipcall.getText().toString();
				if(!TextUtils.isEmpty(ip_call)){
					//保存号码
					SavePreferences.save(AddIpCallctivity.this, Constant.IP_NUMBER, ip_call);
					
					T.makeTest(getApplicationContext(), "ip号码设置完成!");
					finish();
				}else{
					T.makeTest(getApplicationContext(), "号码不能为空!");
				}
			}
		});
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		et_add_ipcall = (EditText) findViewById(R.id.id_et_ipcall);
		bt_ipcall = (Button) findViewById(R.id.id_bt_ipcall);
	}

}
