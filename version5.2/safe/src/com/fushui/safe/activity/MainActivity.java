package com.fushui.safe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.view.MainAdapter;

public class MainActivity extends Activity {

	TextView tv_splash_version;
	GridView gv_main;
	MainAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById();
		initAdapter();
		gv_main.setAdapter(adapter);
		setOnItemClickListener();

	}

	// 利用activity的生命周期,调用Adapter.notifyDataSetChanged(),实质为调用getView()方法
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private void setOnItemClickListener() {
		// TODO Auto-generated method stub
		gv_main.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Intent intent_LockMobile = new Intent(MainActivity.this,
							LockMobileActivity.class);
					startActivity(intent_LockMobile);
					break;
				case 1:
					Intent intent_blackNumber = new Intent(MainActivity.this,BlackNumberActivity.class);
					startActivity(intent_blackNumber);
					break;
				case 7:
					Intent intent_hightools = new Intent(MainActivity.this,
							HighToolActivity.class);
					startActivity(intent_hightools);
					break;
				case 8:
					Intent intent_SettingCenter = new Intent(MainActivity.this,
							SettingCenterActivity.class);
					startActivity(intent_SettingCenter);
				default:
					break;
				}

			}
		});
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		// tv_splash_version = (TextView)
		// findViewById(R.id.id_tv_splash_version);
		// tv_splash_version.setText("version:" + ViewHelper.getVersion(this));
		// Toast.makeText(this, this.getPackageName(),
		// Toast.LENGTH_LONG).show();
		gv_main = (GridView) findViewById(R.id.id_gv_main);
	}

	public String[] getData() {

		String[] data = { "手机防盗", "通讯卫士", "软件管理", "任务管理", "流量管理", "手机杀毒",
				"系统优化", "高级工具", "设置中心" };
		// List<String> data_list = new ArrayList<String>(Arrays.asList(data));
		return data;

	}

	public int[] getImage() {

		int[] data = { R.drawable.widget03, R.drawable.widget02,
				R.drawable.widget01, R.drawable.widget07, R.drawable.widget05,
				R.drawable.widget04, R.drawable.widget06, R.drawable.widget08,
				R.drawable.widget09 };
		// List<String> data_list = new ArrayList<String>(Arrays.asList(data));
		return data;

	}

	private void initAdapter() {
		adapter = new MainAdapter(this, getData(), getImage());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
