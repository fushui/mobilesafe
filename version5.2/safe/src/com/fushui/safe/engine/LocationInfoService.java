package com.fushui.safe.engine;

import com.fushui.safe.biz.Constant;
import com.fushui.safe.biz.SavePreferences;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

//使用单例模式，只有一个实例
public class LocationInfoService {

	public static LocationInfoService instance;
	private LocationManager locationManager;
	private Context context;

	private LocationInfoService(Context context) {
		// TODO Auto-generated constructor stub

		// 获取定位服务
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		this.context = context;
	}

	public static LocationInfoService getInstance(Context context) {
		if (instance == null) {
			instance = new LocationInfoService(context);
		}
		return instance;

	}

	// 取消监听
	public void unregister() {
		locationManager.removeUpdates(getListener());
	}

	// 注册监听
	/**
	 * 
	 */
	public void registerLocListener() {

		// 设置查询条件
		Criteria criteria = new Criteria();

		// 设置精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否关注海拔
		criteria.setAltitudeRequired(false);
		// 设置是否关注周边事物
		criteria.setBearingRequired(false);
		// 设置是否支持收费查询
		criteria.setCostAllowed(true);
		// 设置是否耗电
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 设置是否关注速度
		criteria.setSpeedRequired(false);

		// 根据查询条件，获取最好的定位方式
		String provider = locationManager.getBestProvider(criteria, true);

		// 注册监听
		locationManager.requestLocationUpdates(provider, 60000, 0,
				getListener());
	}

	// 得到定位监听
	private MyLocationListener listener;

	private MyLocationListener getListener() {
		if (listener == null) {
			listener = new MyLocationListener();
		}
		return listener;
	}

	public String getLastLocation() {
		// 获取位置信息
		return SavePreferences.getString(context, Constant.LAST_LOCATION);
	}

	class MyLocationListener implements LocationListener {

		// 位置改变
		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			// 获取经纬度
			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();
			String location = "latitude: " + latitude + " longitude: "
					+ longitude;
			// 保存位置信息
			SavePreferences.save(context, Constant.LAST_LOCATION, location);
		}

		// 有一个gps找不到
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		// 某个位置被打开
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		// 某个位置被关闭
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}
}
