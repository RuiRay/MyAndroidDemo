package com.ionesmile.variousdemo;

import lxz.utils.android.net.CustomerHttpClient;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	// 返回当前所在城市的天气预报--新浪协议
	public final static String CURRENT_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=jsunicode&ie=utf-8%22";
	private static final String TAG = WeatherActivity.class.getSimpleName();

	private String cityMsg = null;
	private TextView cityTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		cityTv = (TextView) findViewById(R.id.tv_city);
	}

	public void getWeather(View v) {
		new Thread() {
			public void run() {
				try {
					cityMsg = getCurrentCity();
					Log.i(TAG, "getWeather() =  cityMsg " + cityMsg);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							cityTv.setText(cityMsg);
						}
					});
				} catch (Exception e) {
					Log.e(TAG, "getWeather()", e);
				}
			};
		}.start();
	}

	// 返回当前所在地的城市名称
	public String getCurrentCity() throws Exception {
		String parameter = CustomerHttpClient.getContent(CURRENT_URL);
		Log.i(TAG, "getCurrentCity() =  parameter " + parameter);
		parameter = parameter.substring(parameter.indexOf("=") + 1, parameter.indexOf(";")).trim();
		JSONObject jsonObj = new JSONObject(parameter);
		return jsonObj.getString("city");
	}

}
