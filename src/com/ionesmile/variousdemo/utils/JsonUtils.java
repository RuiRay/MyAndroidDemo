package com.ionesmile.variousdemo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JsonUtils {
	
	public static List<CalenderBean> getData(Context context) throws IOException, JSONException{
		InputStream is = context.getResources().getAssets().open("Cal.json");
		List<CalenderBean> result = parseStringToCalBeans(getStringOfInputStream(is));
		return result;
	}

	private static String getStringOfInputStream(InputStream is) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null){
			sb.append(line);
		}
		return sb.toString();
	}
	
	private static List<CalenderBean> parseStringToCalBeans(String jsonObj) throws JSONException{
		JSONArray jsonArr = new JSONObject(jsonObj).getJSONArray("data");
		List<CalenderBean> result = new ArrayList<CalenderBean>();
		int len = jsonArr.length();
		for (int i = 0; i < len; i++) {
			JSONObject itemJson = jsonArr.getJSONObject(i);
			CalenderBean calbean = new CalenderBean();
            calbean.setYear(itemJson.getString("years"));
            // TODO ......
            result.add(calbean);
		}
		return result;
	}
}
