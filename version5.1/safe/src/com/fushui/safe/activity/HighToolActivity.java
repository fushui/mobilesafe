package com.fushui.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fushui.safe.R;

public class HighToolActivity extends Activity {
    
	TextView tv_add_ipcall;
    TextView tv_address_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.hightools);
	    findViewById();
	    setOnClickListener();
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_add_ipcall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			   Intent intent = new Intent(HighToolActivity.this,AddIpCallctivity.class);
			   startActivity(intent);
			}
		});
		tv_address_query.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    Intent intent = new Intent(HighToolActivity.this,AddressQueryActivity.class);	
			    startActivity(intent);
			}
		});
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		tv_add_ipcall = (TextView)findViewById(R.id.id_tv_add_ipcall);
		tv_address_query = (TextView)findViewById(R.id.id_tv_aaddress_query);
	}

	
}
