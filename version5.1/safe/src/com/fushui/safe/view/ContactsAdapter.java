package com.fushui.safe.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fushui.safe.R;
import com.fushui.safe.domain.ContactInfo;

public class ContactsAdapter extends BaseAdapter {

	Context context;
	List<ContactInfo> infos = new ArrayList<ContactInfo>();

	public ContactsAdapter(Context context, List<ContactInfo> infos) {
		this.infos = infos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.contacts_item, null);
	
		TextView tv_name = (TextView) view.findViewById(R.id.id_tv_name);
		TextView tv_number = (TextView) view.findViewById(R.id.id_tv_number);
		
		tv_name.setText(infos.get(position).getName());
		tv_number.setText(infos.get(position).getPhoneNumber());
		return view;
	}

}
