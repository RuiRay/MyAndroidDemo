package com.ionesmile.variousdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ionesmile.variousdemo.utils.JsonFormatTool;

public class GetLocalActivity extends Activity {

	private TextView postionView;

	private LocationManager locationManager;
	private String locationProvider;

	public static final int SHOW_LOCATION = 0;

	protected static final String TAG = GetLocalActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_local);

		// 获取显示地理位置信息的TextView
		postionView = (TextView) findViewById(R.id.positionView);
		// 获取地理位置管理器
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();  
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度  
        criteria.setAltitudeRequired(false);//无海拔要求  
        criteria.setBearingRequired(false);//无方位要求  
        criteria.setCostAllowed(true);//允许产生资费  
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗  
        
        // 获取最佳服务对象  
        locationProvider = locationManager.getBestProvider(criteria,true);  
		// 获取Location
		Location location = locationManager.getLastKnownLocation(locationProvider);

		if (location != null) {
			// 不为空,显示地理位置经纬度
			//showLocation(location);
			getLocal(location);
		} else {
			Toast.makeText(this, "location为空", Toast.LENGTH_SHORT).show();
		}
		// 监视地理位置变化
		locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
		//locationManager.requestSingleUpdate(provider, listener, looper)
		Log.i(TAG, "onCreate() ");

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_LOCATION:
				String position = (String) msg.obj;
				Log.i(TAG, "SHOW_LOCATION postion = " + position);
				postionView.setText(position);
				break;
			default:
				break;
			}
		}
	};
	
	
	// http://maps.google.com/maps/api/geocode/json?latlng=40.714224,-73.961452&language=zh-CN&sensor=true
	// http://maps.google.com/maps/api/geocode/json?latlng=35.7139098,139.7286330&language=zh-CN&sensor=true
	public static final String URL = "http://maps.google.com/maps/api/geocode/json?latlng=[latitude],[longitude]&language=zh-CN&sensor=true";
	private String url;
	
	public void getLocal(Location location){

		Log.i(TAG, "getLocal() Latitude = " + location.getLatitude() + "  Longitude = " + location.getLongitude());
		float latitude = (float) location.getLatitude();
		float longitude = (float) location.getLongitude();
		
		url = URL.replace("[latitude]", String.valueOf(latitude));
		url = url.replace("[longitude]", String.valueOf(longitude));
		
		new Thread(){
			public void run() {
				HttpGet httpGet = new HttpGet(url);
		         HttpClient httpClient = new DefaultHttpClient();

		         // 发送请求
		         try
		         {

		             HttpResponse response = httpClient.execute(httpGet);

		             // 显示响应
		             showResponseResult(response);// 一个私有方法，将响应结果显示出来

		         }
		         catch (Exception e)
		         {

						Log.e(TAG, "getLocal() ", e);
		         }
			};
		}.start();
		
		
	}
	
	/**
     * 显示响应结果到命令行和TextView
     * @param response
     */
    private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }

            final String localInfo = result;
           Log.i(TAG, result);
           runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				postionView.setText(JsonFormatTool.formatJson(localInfo));
			}
		});
        }
        catch (Exception e)
        {

			Log.e(TAG, "showResponseResult() ", e);
        }

    }

	/**
	 * 显示地理位置经度和纬度信息
	 * 
	 * @param location
	 */
	private void showLocation(final Location location) {
		Log.i(TAG, "showLocation() Latitude = " + location.getLatitude() + "  Longitude = " + location.getLongitude());
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 组装反向地理编码的接口位置
					StringBuilder url = new StringBuilder();
					url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
					url.append(location.getLatitude()).append(",");
					url.append(location.getLongitude());
					url.append("&sensor=false");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url.toString());
					httpGet.addHeader("Accept-Language", "zh-CN");
					HttpResponse response = client.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						String res = EntityUtils.toString(entity);
						// 解析
						JSONObject jsonObject = new JSONObject(res);
						// 获取results节点下的位置信息
						JSONArray resultArray = jsonObject
								.getJSONArray("results");
						if (resultArray.length() > 0) {
							JSONObject obj = resultArray.getJSONObject(0);
							// 取出格式化后的位置数据
							String address = obj.getString("formatted_address");

							Message msg = new Message();
							msg.what = SHOW_LOCATION;
							msg.obj = address;
							handler.sendMessage(msg);
						}
					}
				} catch (Exception e) {

					Log.e(TAG, "showLocation() ", e);
				}
			}
		}).start();
	}

	/**
	 * LocationListern监听器 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
	 */

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle arg2) {
			Log.i(TAG, "locationListener onStatusChanged() provider = " + provider + "   status = " + status);

		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(TAG, "locationListener onProviderEnabled() provider = " + provider);

		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(TAG, "locationListener onProviderDisabled() provider = " + provider);

		}

		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, "locationListener onLocationChanged() Latitude = " + location.getLatitude() + "  Longitude = " + location.getLongitude());
			// 如果位置发生变化,重新显示
			//showLocation(location);
			getLocal(location);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			// 移除监听器
			locationManager.removeUpdates(locationListener);
		}
	}

}