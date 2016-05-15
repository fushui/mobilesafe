package com.fushui.safe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fushui.safe.R;
import com.fushui.safe.domain.ContactInfo;
import com.fushui.safe.engine.ContactInfoService;
import com.fushui.safe.view.ContactsAdapter;



public class ContactActivity extends Activity {

	ListView lv_contacts;
	List<ContactInfo> infos = new ArrayList<ContactInfo>();
	ContactsAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.contacts);
	    findViewById();
	    getData();
	    setAdapter();
	    setOnItemClickListener();
	   
	}
	private void setOnItemClickListener() {
		// TODO Auto-generated method stub
		 lv_contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				//获取所选的item号码
				ContactInfo info = (ContactInfo)adapter.getItem(pos);
				String number = info.getPhoneNumber();
				//带数据放回上一活动，200为结果码
				Intent intent = new Intent();
				intent.putExtra("number",number);
				setResult(200, intent);
				finish();
			}
		
		 });
	}
	private void setAdapter() {
		// TODO Auto-generated method stub
		adapter = new ContactsAdapter(getApplicationContext(), infos);
		lv_contacts.setAdapter(adapter);
	}
	private void getData() {
		// TODO Auto-generated method stub
//		ContactInfo info = new ContactInfo();
//		info.setName("haha");
//		info.setPhoneNumber("545645");
//		infos.add(info);
//		infos.add(info);
		infos = new ContactInfoService(this).getContactInfo();
	}
	private void findViewById() {
		// TODO Auto-generated method stub
		lv_contacts = (ListView)findViewById(R.id.id_lv_contacts);
	}
	
	
}
