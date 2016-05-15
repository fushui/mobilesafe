package com.fushui.safe.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.domain.SmsInfo;
import com.fushui.safe.engine.SmsInfoService;
import com.fushui.safe.service.BackupSmsService;
import com.fushui.safe.utils.T;

public class HighToolActivity extends Activity {

	TextView tv_add_ipcall;
	TextView tv_address_query;
	TextView tv_backup_sms;
	TextView tv_restore_sms;

	SmsInfoService infoService;

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
				Intent intent = new Intent(HighToolActivity.this,
						AddIpCallctivity.class);
				startActivity(intent);
			}
		});
		tv_address_query.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HighToolActivity.this,
						AddressQueryActivity.class);
				startActivity(intent);
			}
		});
		tv_backup_sms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HighToolActivity.this,
						BackupSmsService.class);

				startService(intent);
				// T.makeTest(getApplicationContext(), "hi");
			}
		});
		tv_restore_sms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				T.makeTest(getApplicationContext(), "hi");

				// 打开进度条对话框
				final ProgressDialog dialog = new ProgressDialog(
						HighToolActivity.this);
				// // 设置样式
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				//
				// // 设置标题
				dialog.setTitle("正在删除短信");

				dialog.show();

				// 使用线程
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							Uri uri = Uri.parse("content://sms");
							// //执行删除操作
							getContentResolver().delete(uri, null, null);

							dialog.setTitle("正在还原短信");

							// // 获取xml中的短信
							infoService = new SmsInfoService(
									getApplicationContext());

							 List<SmsInfo> infos = infoService
							 .getSmsInfosFromXml();
							//List<SmsInfo> infos = infoService.getSmsInfos();

							dialog.setMax(infos.size());

							for (SmsInfo info : infos) {
								ContentValues values = new ContentValues();
								values.put("address", info.getAddress());
								values.put("date", info.getDate());
								values.put("type", info.getType());
								values.put("body", info.getBody());

								getContentResolver().insert(uri, values);

								// 进程休眠2秒
								SystemClock.sleep(2000);
								// 进度条加1
								dialog.incrementProgressBy(1);
							}

							dialog.dismiss();

							Looper.prepare();
							T.makeTest(getApplicationContext(), "还原成功");
							Looper.loop();

						} catch (Exception e) {
							Looper.prepare();
							T.makeTest(getApplicationContext(), "还原失败");
							Looper.loop();
							e.printStackTrace();
							dialog.dismiss();
						}

					}
				}.start();

			}
		});

	}

	private void findViewById() {
		// TODO Auto-generated method stub
		tv_add_ipcall = (TextView) findViewById(R.id.id_tv_add_ipcall);
		tv_address_query = (TextView) findViewById(R.id.id_tv_aaddress_query);
		tv_backup_sms = (TextView) findViewById(R.id.id_tv_backupsms);
		tv_restore_sms = (TextView) findViewById(R.id.id_tv_restoresms);
	}

}
