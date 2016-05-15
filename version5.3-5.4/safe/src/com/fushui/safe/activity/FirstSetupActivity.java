package com.fushui.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fushui.safe.R;

public class FirstSetupActivity extends Activity {

	/**
	 * @param args
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_setup);
	}
	public void next(View v){
		Intent intent = new Intent(FirstSetupActivity.this, SecondSetupActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}

}
