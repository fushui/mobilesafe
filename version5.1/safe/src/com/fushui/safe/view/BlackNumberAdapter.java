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

public class BlackNumberAdapter extends BaseAdapter {

	Context context;
	List<String> black_lists = new ArrayList<String>();

	public void setBlack_lists(List<String> black_lists) {
		this.black_lists = black_lists;
	}

	public BlackNumberAdapter(Context context, List<String> black_lists) {
		this.black_lists = black_lists;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return black_lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return black_lists.get(arg0);
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
		View view = inflater.inflate(R.layout.black_number_item, null);

		TextView tv_black_number = (TextView) view
				.findViewById(R.id.id_tv_black_item);

		tv_black_number.setText(black_lists.get(position));
		return view;
	}

}
