package com.fushui.safe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String getString(String str) {
		try {
			MessageDigest dig = MessageDigest.getInstance("md5");
			byte[] data = dig.digest(str.getBytes());
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				//16进制整数装换为字符串
				String result = Integer.toHexString(data[i] & 0xff);
				String temp = null;
				if (result.length() == 1) {
					temp = '0' + result;
				} else {
					temp = result;
				}

				builder.append(temp);
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
