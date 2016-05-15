package com.fushui.safe.activity;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.utils.T;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class ThirdSetupActivity extends Activity {

	/**
	 * @param args
	 */
	EditText et_number;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_setup);
		findViewById();
		check();
	}
	private void check() {
		// TODO Auto-generated method stub
		String num = SavePreferences.getString(getApplicationContext(), Constant.SAVE_NUMBER);
		if(!TextUtils.isEmpty(num)){
			et_number.setText(num);
		}
	}
	private void findViewById() {
		// TODO Auto-generated method stub
		et_number = (EditText)findViewById(R.id.id_et_safe_number);
	}
	public void previous(View v) {
		Intent intent = new Intent(ThirdSetupActivity.this,
				SecondSetupActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	public void next(View v) {
		//判断安全密码是否设置
		String number = et_number.getText().toString();
		if(TextUtils.isEmpty(number)){
			T.makeTest(this, "安全密码不能为空");
		}else{
			//不为空，即开启下一活动,并保存SharePreference
			SavePreferences.save(ThirdSetupActivity.this, Constant.SAVE_NUMBER, number);
			Intent intent = new Intent(ThirdSetupActivity.this,
					ForthSetupActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
		}
	}
	public void choose(View v) {
		Intent intent = new Intent(ThirdSetupActivity.this,
				ContactActivity.class);
		//请求码100,启动另一活动
		startActivityForResult(intent,100);
	   //finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
			if(data != null){
				String number = data.getStringExtra("number");
				//T.makeTest(getApplicationContext(), number);
				et_number.setText(number);
			}
		}
	}
}
