package com.fushui.safe.view;

import java.util.List;

import com.fushui.safe.R;
import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;

import android.R.raw;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	public Context context;
	public String[] string;
	public int[] image;

	public MainAdapter(Context context,String[] string,int[] image ) {
		this.context = context;
		this.string = string;
		this.image = image;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return string.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		//两种填充布局的方式
//		 LinearLayout view = new LinearLayout(context);
//		 view.setOrientation(LinearLayout.VERTICAL);
		
		 LayoutInflater inflater = LayoutInflater.from(context);  
		 View view = inflater.inflate(R.layout.item,null);

		 ImageView imageView = (ImageView)view.findViewById(R.id.id_iv_widget);
		 imageView.setImageResource(image[position]);
		 
		 
		
		 TextView textView =(TextView)view.findViewById(R.id.id_tv_widget);
		 
		 if(position == 0){
			 String name = SavePreferences.getString(context, Constant.PROTECTED_NAME);
			 if(!TextUtils.isEmpty(name)){
				 string[position] = name; 
			 }
		 }
		 textView.setText(string[position]);
		 textView.setTextColor(0xff000000);
		 return view;
	}

}
