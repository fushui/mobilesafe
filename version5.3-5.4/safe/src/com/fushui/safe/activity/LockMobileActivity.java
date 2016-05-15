package com.fushui.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;
import com.fushui.safe.utils.MD5;
import com.fushui.safe.utils.T;

public class LockMobileActivity extends Activity {

	private LockMobileActivity TAG = LockMobileActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.nor_dialog);

		// showFirstDialog();
		checkPassword();
	}

	private void checkPassword() {
		// TODO Auto-generated method stub
		// 判断sp中是否已经存在密码
		if (TextUtils
				.isEmpty(SavePreferences.getString(TAG, Constant.PASSWORD))) {
			showFirstDialog();
		} else {
			showNormalDialog();
		}
	}

	private void showNormalDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.nor_dialog, null);
		builder.setView(view);
		final Dialog d = builder.create();
		d.show();

		final EditText et_input = (EditText) view
				.findViewById(R.id.id_et_input);

		view.findViewById(R.id.id_bt_input).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 获取文本内容
						// MD5加密
						String pass = MD5.getString(et_input.getText()
								.toString());
						String right = SavePreferences.getString(TAG,
								Constant.PASSWORD);
						// 判断密码是否一致
						if (!TextUtils.isEmpty(pass) && pass.equals(right)) {

							// 关闭对话框和当前活动
							d.dismiss();
							TAG.finish();
							// 进入向导
							loadGuide();
						} else {
							T.makeTest(TAG, "请确认密码是否正确");
						}

					}
				});
		view.findViewById(R.id.id_bt_cancel2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
						TAG.finish();
					}
				});
	}

	private void showFirstDialog() {

		// 创建对话框

		AlertDialog.Builder builder = new Builder(this);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.fir_dialog, null);
		builder.setView(view);

		final Dialog d = builder.create();
		d.show();
		// 获取edittext
		final EditText et_pass = (EditText) view.findViewById(R.id.id_et_pass);
		final EditText et_confirm = (EditText) view
				.findViewById(R.id.id_et_confirm);

		view.findViewById(R.id.id_bt_confirm).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 获取文本内容
						String pass = et_pass.getText().toString();
						String confirm = et_confirm.getText().toString();

						// 判断密码是否一致
						if (TextUtils.isEmpty(pass)
								|| TextUtils.isEmpty(confirm)) {
							T.makeTest(TAG, "密码不能为空");
						} else if (pass.equals(confirm)) {
							pass = MD5.getString(pass);
							SavePreferences.save(TAG, Constant.PASSWORD, pass);
							d.dismiss();
							finish();
						} else {
							T.makeTest(TAG, "请确认密码是否一致");
						}

					}
				});

		view.findViewById(R.id.id_bt_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						d.dismiss();
						TAG.finish();
					}
				});
	}

	// 设置向导页面，如果已经设置完成，直接进入防盗界面
	private void loadGuide(){
		boolean is_setup = SavePreferences.getBoolean(TAG, "is_setup");
		if(is_setup){
			//直接进入防盗界面
			Intent intent = new Intent(LockMobileActivity.this, ProtectedActivity.class);
			startActivity(intent);
		}else{
			//进入向导界面
			Intent intent = new Intent(LockMobileActivity.this, FirstSetupActivity.class);
			startActivity(intent);
		}
	}
}
