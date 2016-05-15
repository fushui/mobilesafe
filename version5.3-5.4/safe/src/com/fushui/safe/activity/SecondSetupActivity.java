package com.fushui.safe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;

public class SecondSetupActivity extends Activity {

	/**
	 * @param args
	 */
	ImageView iv_switch;
	TelephonyManager tm;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_setup);
		findViewById();
		//获得电话管理器
		tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		check();
	}

	private void check() {
		// TODO Auto-generated method stub
		//根据SharePreference改变图片内容
		String sim = SavePreferences.getString(getApplicationContext(),Constant.SIM_SERIAL);
		if(TextUtils.isEmpty(sim)){
			iv_switch.setImageResource(R.drawable.switch_off_normal);
		}else{
			iv_switch.setImageResource(R.drawable.switch_on_normal);
		}
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		iv_switch = (ImageView)findViewById(R.id.id_iv_switch);
	}

	public void is_switch(View v){
		//获得唯一标识码,注意权限的添加
		String sim_serial = tm.getSimSerialNumber();
		
		//存入SharePreference
		SavePreferences.save(SecondSetupActivity.this,Constant.SIM_SERIAL,sim_serial );
	    //改变图片
		iv_switch.setImageResource(R.drawable.switch_on_normal);
	}
	public void previous(View v) {
		Intent intent = new Intent(SecondSetupActivity.this,
				FirstSetupActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	public void next(View v) {
		Intent intent = new Intent(SecondSetupActivity.this,
				ThirdSetupActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}
}
