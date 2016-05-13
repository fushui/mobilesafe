package com.fushui.safe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fushui.safe.domain.ContactInfo;

public class ContactInfoService {

	private Context context;
	private List<ContactInfo> infos;

	public ContactInfoService(Context context) {
		// 初始化列表
		infos = new ArrayList<ContactInfo>();
		this.context = context;
	}

	// 获取手机联系人
	public List<ContactInfo> getContactInfo() {

		ContentResolver cr = context.getContentResolver();
		// 查询raw_contacts表获取联系人的_id和名字
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		// 游标查询
		Cursor c = cr.query(uri, new String[] { "_id", "display_name" }, null,
				null, null);
		// String[] names = c.getColumnNames();
		// for(String name:names){
		// Log.i("i", name);
		// }

		while (c.moveToNext()) {
			ContactInfo info = new ContactInfo();
			String _id = c.getString(c.getColumnIndex("_id"));
			String name = c.getString(c.getColumnIndex("display_name"));
			// 设置名字
			info.setName(name);

			//Log.i("name", name);
			// 根据id查询data表
			uri = Uri.parse("content://com.android.contacts/raw_contacts/"
					+ _id + "/data");
			Cursor c1 = cr.query(uri, new String[] { "data1", "mimetype" },
					null, null, null);
//			String[] names1 = c1.getColumnNames();
//			for (String name1 : names1) {
//				Log.i("i", name1);
//			}
			while (c1.moveToNext()) {
				String data1 = c1.getString(c1.getColumnIndex("data1"));
				String mimetype = c1.getString(c1.getColumnIndex("mimetype"));
				// 根据mimetype,获得电话号码
				if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					info.setPhoneNumber(data1);
					infos.add(info);
				}
			}
			c1.close();
		}
		c.close();
		return infos;
	}
}
