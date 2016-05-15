package com.fushui.safe.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import com.fushui.safe.domain.SmsInfo;

public class SmsInfoService {

	private Context context;
	private List<SmsInfo> infos;

	// 用于获取短信的类
	public SmsInfoService(Context context) {
		infos = new ArrayList<SmsInfo>();
		this.context = context;
	}

	/**
	 * @return
	 */
	public List<SmsInfo> getSmsInfos() {

		// 访问系统提供者
		Uri uri = Uri.parse("content://sms");

		Cursor c = context.getContentResolver().query(uri,
				new String[] { "address", "date", "type", "body" }, null, null,
				null);
		SmsInfo info = new SmsInfo();

		while (c.moveToNext()) {
			String date = c.getString(c.getColumnIndex("date"));
			String address = c.getString(c.getColumnIndex("address"));
			String type = c.getString(c.getColumnIndex("type"));
			String body = c.getString(c.getColumnIndex("body"));

			info.setAddress(address);
			info.setBody(body);
			info.setType(type);
			info.setDate(date);

			
			infos.add(info);
			
			//info = null;
		}
		return infos;
	}

	public List<SmsInfo> getSmsInfosFromXml() throws Exception {
		List<SmsInfo> infos = null;
		SmsInfo info = null;
		// 获取解析器
		XmlPullParser parser = Xml.newPullParser();
		// 获取输入流
		File file = new File("data/data/" + context.getPackageName()
				+ "/smsbackup.xml");
//		 File file = new File(Environment.getExternalStorageDirectory(),
//				 "smsbackup.xml");
		FileInputStream is = new FileInputStream(file);

		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();

		//判断标签
		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("smsInfos".equals(parser.getName())) {
					infos = new ArrayList<SmsInfo>();
				} else if ("smsInfo".equals(parser.getName())) {
					info = new SmsInfo();
				} else if ("address".equals(parser.getName())) {
					info.setAddress(parser.nextText());
				} else if ("date".equals(parser.getName())) {
					info.setDate(parser.nextText());
				} else if ("type".equals(parser.getName())) {
					info.setType(parser.nextText());
				} else if ("body".equals(parser.getName())) {
					info.setBody(parser.nextText());
				}
				break;

			case XmlPullParser.END_TAG:
				if ("smsInfo".equals(parser.getName())){
					infos.add(info);
					//info = null;
				}
				break;
			}
		}

		return infos;

	}

	// 把短信数据写入xml中
	public void createXml(List<SmsInfo> infos) throws Exception {

		// 获取xml解析器
		XmlSerializer serializer = Xml.newSerializer();

		// 获取输出流
		File file = new File("data/data/" + context.getPackageName()
				+ "/smsbackup.xml");

//		 File file = new File(Environment.getExternalStorageDirectory(),
//		 "smsbackup.xml");
		FileOutputStream os = new FileOutputStream(file);

		serializer.setOutput(os, "UTF-8");
		serializer.startDocument("UTF-8", true);

		serializer.startTag(null, "smsInfos");

		for (SmsInfo info : infos) {
			serializer.startTag(null, "smsInfo");

			// address
			serializer.startTag(null, "address");
			serializer.text(info.getAddress());
			serializer.endTag(null, "address");

			// date
			serializer.startTag(null, "date");
			serializer.text(info.getDate());
			serializer.endTag(null, "date");

			// type

			serializer.startTag(null, "type");
			serializer.text(info.getType());
			serializer.endTag(null, "type");

			// body
			serializer.startTag(null, "body");
			serializer.text(info.getBody());
			serializer.endTag(null, "body");

			serializer.endTag(null, "smsInfo");

		}

		serializer.endTag(null, "smsInfos");

		serializer.endDocument();

	}
}
