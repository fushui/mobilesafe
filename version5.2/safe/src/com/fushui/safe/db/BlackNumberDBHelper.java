package com.fushui.safe.db;

import com.fushui.safe.biz.Constant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBHelper extends SQLiteOpenHelper {

	// 创建表
	private static SQLiteOpenHelper instance;

	// private final static String name = "blacknumber.db";
	public BlackNumberDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// 首次创建版本号为1的表
	public static SQLiteOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new BlackNumberDBHelper(context, Constant.DB_NAME, null,
					1);
		}

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 执行数据库语句
		db.execSQL("create table " + Constant.DB_TABLE_NAME
				+ "(_id integer primary key autoincrement,number text)");
		// db.execSQL("create table blacknumber(_id integer primary key autoincrement,number text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
