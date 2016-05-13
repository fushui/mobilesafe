package com.fushui.safe.activity;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fushui.safe.R;
import com.fushui.safe.engine.CopySqliteFileService;
import com.fushui.safe.utils.T;

public class AddressQueryActivity extends Activity {

	EditText et_addressquery;
	Button bt_addressquery;
	CopySqliteFileService serviece;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addressquery);
		findViewById();
		setOnClickListener();
		copy();

	}

	// 第一次运行时拷贝数据库进入手机
	private void copy() {
		// TODO Auto-generated method stub
		serviece = new CopySqliteFileService(AddressQueryActivity.this);
		try {
			serviece.CopySqliteFileFromRawToDatabases("address.db");
			T.makeTest(getApplicationContext(), "copy success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			T.makeTest(getApplicationContext(), "copy failed");
			e.printStackTrace();

		}
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		bt_addressquery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 获取号码
				String number = et_addressquery.getText().toString();
				
				if (TextUtils.isEmpty(number)) {
					T.makeTest(getApplicationContext(), "请输入手机号码");

				} else {
					T.makeTest(getApplicationContext(), serviece.query(number));
				} 
			}
		});
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		et_addressquery = (EditText) findViewById(R.id.id_et_address_query);
		bt_addressquery = (Button) findViewById(R.id.id_bt_addressquery);
	}

}
