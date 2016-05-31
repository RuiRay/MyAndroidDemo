package com.ionesmile.variousdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ionesmile.variousdemo.utils.JsonFormatTool;

public class MaxAppMenoryActivity extends Activity {

	public static final String URL = "http://maps.google.com/maps/api/geocode/json?latlng=[latitude],[longitude]&language=zh-CN&sensor=true";
	private static final String TAG = MaxAppMenoryActivity.class.getSimpleName();
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_max_app_menory);
		
		getMaxMenory();
		

		float latitude = 22.552549f;
		float longitude = 113.951320f;
		
		url = URL.replace("[latitude]", String.valueOf(latitude));
		url = url.replace("[longitude]", String.valueOf(longitude));
		
	}
	
	public void getLocal(View v){
		
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
		             e.printStackTrace();
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
				 ((TextView)findViewById(R.id.localInfo)).setText(JsonFormatTool.formatJson(localInfo));
			}
		});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



	private void getMaxMenory() {
		ActivityManager activityManager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		int memorySize = activityManager.getMemoryClass();
		int largeMemorySize = activityManager.getLargeMemoryClass();
		
		String info = "--- memorySize = " + memorySize + "\r\n--- largeMemorySize = " + largeMemorySize;
		Log.i("myTag", info);
		((TextView)findViewById(R.id.tvShow)).setText(info);
	}

}
