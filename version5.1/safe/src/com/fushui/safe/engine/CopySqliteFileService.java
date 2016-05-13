package com.fushui.safe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fushui.safe.utils.T;

public class CopySqliteFileService {

	private Context mcontext;
	private File file;
	private File dir;

	public CopySqliteFileService(Context context) {
		this.mcontext = context;
	}

	/**
	 * 见assets目录下的文件拷贝到sd上
	 * 
	 * @return 存储数据库的地址
	 */

	// 复制和加载区域数据库中的数据
	public String CopySqliteFileFromRawToDatabases(String SqliteFileName)
			throws IOException {

		// 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

		dir = new File("data/data/" + mcontext.getPackageName() + "/databases");
		Log.i("i", "!dir.exists()=" + !dir.exists());
		Log.i("i", "!dir.isDirectory()=" + !dir.isDirectory());

		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}

		file = new File(dir, SqliteFileName);
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// 通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
		if (!file.exists()) {
			try {
				file.createNewFile();

				inputStream = mcontext.getClass().getClassLoader()
						.getResourceAsStream("assets/" + SqliteFileName);
				outputStream = new FileOutputStream(file);

				// InputStream inputStream =
				// AddressQueryActivity.class.getClassLoader().getResourceAsStream("assets/activity_main.xml");
				if (inputStream != null) {
					T.makeTest(mcontext, "write success");
					byte[] buffer = new byte[1024];
					int len;

					while ((len = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, len);
					}
				} else {
					T.makeTest(mcontext, "write failed");

				}
				// byte[] buffer = new byte[1024];
				// int len ;
				//
				// while ((len = inputStream.read(buffer)) != -1) {
				// outputStream.write(buffer,0,len);
				// }

			} catch (IOException e) {
				e.printStackTrace();

			}

			finally {

				if (outputStream != null) {

					outputStream.flush();
					outputStream.close();

				}
				if (inputStream != null) {
					inputStream.close();
				}

			}

		}

		return file.getPath();

	}

	public String query(String number) {
		String address = null;
		// 以只读方式打开数据库
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
				null, SQLiteDatabase.OPEN_READONLY);

		// 判断是否打开
		if (db.isOpen()) {
			// 判断输入的号码是否是电话号码
			String expression = "^1[358]\\d{9}$";
			boolean isPhone = number.matches(expression);
			if (isPhone) {
				// 以前八位为查询条件
				String prefix_number = number.substring(0, 7);
				Cursor c = db.query("info", new String[] { "city" },
						"mobileprefix = ?", new String[] { prefix_number },
						null, null, null);
				if (c.moveToFirst()) {
					address = c.getString(0);
				}
				c.close();
			} else {

				if (number.length() == 10) {
					// 3位区号 + 7位号码
					String area = number.substring(0, 3);
					Cursor c = db
							.query("info", new String[] { "city" }, "area = ?",
									new String[] { area }, null, null, null);
					if (c.moveToFirst()) {
						address = c.getString(0);
					}
					c.close();

				} else if (number.length() == 11) {
					// 3位区号 + 8位号码
					String area = number.substring(0, 3);
					Cursor c = db
							.query("info", new String[] { "city" }, "area = ?",
									new String[] { area }, null, null, null);
					if (c.moveToFirst()) {
						address = c.getString(0);
					}
					c.close();
					// 4位区号 + 7位号码
					String area1 = number.substring(0, 4);
					Cursor c1 = db
							.query("info", new String[] { "city" }, "area = ?",
									new String[] { area1 }, null, null, null);
					if (c1.moveToFirst()) {
						address = c1.getString(0);
					}
					c1.close();
				} else if (number.length() == 12) {
					// 4位区号 + 8位号码
					String area = number.substring(0, 4);
					Cursor c = db
							.query("info", new String[] { "city" }, "area = ?",
									new String[] { area }, null, null, null);
					if (c.moveToFirst()) {
						address = c.getString(0);
					}
					c.close();
				}

			}

			db.close();
		}

		if (address == null) {
			address = "未知号码";
		}

		return address;
	}

}
