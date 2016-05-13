package com.fushui.safe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.db.BlackNumberDao;
import com.fushui.safe.utils.T;
import com.fushui.safe.view.BlackNumberAdapter;

public class BlackNumberActivity extends Activity {

	private static final int MENU_UPDATE_ID = 1;
	private static final int MENU_DELETE_ID = 2;
	TextView tv_empty;
	ListView lv_black_number_lists;
	Button bt_add_black_number;
	BlackNumberActivity TAG = BlackNumberActivity.this;
	BlackNumberDao dao;
	BlackNumberAdapter adapter;
	List<String> black_number_lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black_number);
		findViewById();
		setOnClickListener();
		// 当listview没有数据时显示
		lv_black_number_lists.setEmptyView(tv_empty);
		getData();

		lv_black_number_lists.setAdapter(adapter);

	}

	private void getData() {
		// TODO Auto-generated method stub
		dao = new BlackNumberDao(TAG);
		black_number_lists = dao.findAll();

		adapter = new BlackNumberAdapter(TAG, black_number_lists);

		// 给一个控件注册上下文菜单
		registerForContextMenu(lv_black_number_lists);
	}

	//菜单事件监听
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case MENU_UPDATE_ID:
			// 获得要更改的索引
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();
			int position = acmi.position;
			updateItem(position);
			break;

		case MENU_DELETE_ID:
			// 获得要更改的索引
			AdapterContextMenuInfo acmi1 = (AdapterContextMenuInfo) item
					.getMenuInfo();
			int position1 = acmi1.position;
			deleteItem(position1);
			break;
		}

		return super.onContextItemSelected(item);

	}

	private void deleteItem(int position) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(TAG);

		final int pos = position;
		// 对话框视图
		View view = LayoutInflater.from(TAG).inflate(
				R.layout.add_blacknumber_dialog, null);
		
		//修改标题
		TextView tv_title = (TextView)view.findViewById(R.id.id_tv_add_blacknumber);
		tv_title.setText("删除黑名单号码");
		
		builder.setView(view);
		
		final Dialog d = builder.create();
		d.show();

		Button bt_confirm = (Button) view
				.findViewById(R.id.id_bt_confirm_add_black);
		Button bt_cancel = (Button) view
				.findViewById(R.id.id_bt_cancel_add_black);
		final EditText et_number = (EditText) view
				.findViewById(R.id.id_et_add_black);
		
		//删除不需要用到编辑框，设置为不可见
		et_number.setVisibility(View.GONE);
		bt_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String number = (String) adapter.getItem(pos);
				T.makeTest(TAG, "删除成功");

				dao.delete(number);
				d.dismiss();

				// 刷新数据
				black_number_lists = dao.findAll();
				adapter.setBlack_lists(black_number_lists);
				adapter.notifyDataSetChanged();

			}
		});

		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
	}

	private void updateItem(int postion) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(TAG);

		final int pos = dao.queryId((String)adapter.getItem(postion));
		// 对话框视图
		View view = LayoutInflater.from(TAG).inflate(
				R.layout.add_blacknumber_dialog, null);
		
		//修改标题
		TextView tv_title = (TextView)view.findViewById(R.id.id_tv_add_blacknumber);
		tv_title.setText("更新黑名单号码");
		
		builder.setView(view);
		
		
		
		final Dialog d = builder.create();
		d.show();

		Button bt_confirm = (Button) view
				.findViewById(R.id.id_bt_confirm_add_black);
		Button bt_cancel = (Button) view
				.findViewById(R.id.id_bt_cancel_add_black);
		
	
		final EditText et_number = (EditText) view
				.findViewById(R.id.id_et_add_black);
		bt_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 获取输入的黑名单号码
				String input_number = et_number.getText().toString();
				if (!TextUtils.isEmpty(input_number)) {

					// 判断是否已存在该黑名单
					boolean is_exist = dao.isBlackNumber(input_number);
					if (is_exist) {
						T.makeTest(TAG, "输入号码已存在");
					} else {

						T.makeTest(TAG, "修改成功");
						dao.update(pos, input_number);
						d.dismiss();

						// 刷新数据
						black_number_lists = dao.findAll();
						adapter.setBlack_lists(black_number_lists);
						adapter.notifyDataSetChanged();

					}
				} else {
					T.makeTest(TAG, "输入号码为空");
				}

			}
		});

		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});

	}

	// 弹出菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_UPDATE_ID, 0, "更新黑名单号码");
		menu.add(0, MENU_DELETE_ID, 0, "删除黑名单号码");

	}

	private void setOnClickListener() {

		bt_add_black_number.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(TAG);

				// 对话框视图
				View view = LayoutInflater.from(TAG).inflate(
						R.layout.add_blacknumber_dialog, null);
				builder.setView(view);

				final Dialog d = builder.create();
				d.show();

				Button bt_confirm = (Button) view
						.findViewById(R.id.id_bt_confirm_add_black);
				Button bt_cancel = (Button) view
						.findViewById(R.id.id_bt_cancel_add_black);
				final EditText et_number = (EditText) view
						.findViewById(R.id.id_et_add_black);
				bt_confirm.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// 获取输入的黑名单号码
						String input_number = et_number.getText().toString();
						if (!TextUtils.isEmpty(input_number)) {

							// 判断是否已存在该黑名单
							boolean is_exist = dao.isBlackNumber(input_number);
							if (is_exist) {
								T.makeTest(TAG, "输入号码已存在");
							} else {
								T.makeTest(TAG, "添加成功");
								dao.add(input_number);
								d.dismiss();

								// 刷新数据
								black_number_lists = dao.findAll();
								adapter.setBlack_lists(black_number_lists);
								adapter.notifyDataSetChanged();
							}

						} else {
							T.makeTest(TAG, "输入号码为空");
						}

					}
				});

				bt_cancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						d.dismiss();
					}
				});

			}
		});
	}

	private void findViewById() {

		tv_empty = (TextView) findViewById(R.id.id_tv_empty);
		bt_add_black_number = (Button) findViewById(R.id.id_bt_add_blacknumber);
		lv_black_number_lists = (ListView) findViewById(R.id.id_lv_blacklists);
	}
}
