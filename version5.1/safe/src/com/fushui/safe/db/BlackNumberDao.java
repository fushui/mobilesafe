package com.fushui.safe.db;

import java.util.ArrayList;
import java.util.List;

import com.fushui.safe.biz.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDao {

	private SQLiteOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = BlackNumberDBHelper.getInstance(context);
	}

	// 根据号码返回回id
	public int queryId(String number) {
		int _id = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(Constant.DB_TABLE_NAME, new String[] { "_id"},
					"number = ?", new String[] { number }, null, null, null);
			if (c.moveToFirst()) {
				_id = c.getInt(0);
			}

			c.close();
		}

		db.close();

		return _id;
	}

	// 根据id,修改黑名单，执行修改语句
	public void update(int id, String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.update(Constant.DB_TABLE_NAME, values, "_id = ?",
					new String[] { id + "" });
			// db.update("blacknumber", values, " _id = ? ", new String[] { id
			// + "" });
			db.close();
		}
	}

	// 添加黑名单，执行插入语句
	public void add(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.insert(Constant.DB_TABLE_NAME, "_id", values);
			db.close();
		}
	}

	// 删除黑名单，执行删除语句
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {

			db.delete(Constant.DB_TABLE_NAME, "number = ?",
					new String[] { number });

			db.close();
		}
	}

	// 返回所有黑名单
	public List<String> findAll() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(Constant.DB_TABLE_NAME,
					new String[] { "number" }, null, null, null, null, null);
			while (c.moveToNext()) {
				String number = c.getString(0);
				list.add(number);
			}
			c.close();
			db.close();
		}
		return list;
	}

	// 判断是否在数据库内
	public boolean isBlackNumber(String number) {
		boolean isExist = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(Constant.DB_TABLE_NAME, null, "number = ?",
					new String[] { number }, null, null, null);
			if (c.moveToFirst()) {
				isExist = true;
			}
			c.close();
			db.close();
		}
		return isExist;
	}
}
