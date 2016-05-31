package com.ionesmile.variousdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DeviceInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_test);
		
		//Log.i("myTag", "--- Installation SID = " + Installation.id(this));
		
		String jsonArrString = "['aaaaaa','bbbbbbb','ccccccc']";
		try {
			JSONArray jsonArray = new JSONArray(jsonArrString);
			jsonArray.getString(0);
		} catch (JSONException e) {
			Log.e("myTag", "", e);
		}
	}
	
	public void getDeviceInfo(View v){
		Log.i("myTag", "--- Installation SID = " + Installation.id(this) + "-abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
		Log.i("myTag", "--- Installation SID MD5 = " + Md5Util.getMd5(Installation.id(this) + "-abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"));
	}
	
	/**
	 * Md5 工具
	 */
	public static class Md5Util {

	    private static MessageDigest md5 = null;
	    static {
	        try {
	            md5 = MessageDigest.getInstance("MD5");
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    }

	    /**
	     * 用于获取一个String的md5值
	     * @param string
	     * @return
	     */
	    public static String getMd5(String str) {
	        byte[] bs = md5.digest(str.getBytes());
	        StringBuilder sb = new StringBuilder(40);
	        for(byte x:bs) {
	            if((x & 0xff)>>4 == 0) {
	                sb.append("0").append(Integer.toHexString(x & 0xff));
	            } else {
	                sb.append(Integer.toHexString(x & 0xff));
	            }
	        }
	        return sb.toString();
	    }

	    public static void main(String[] args) {
	        System.out.println(getMd5("hello world"));
	    }
	}
	
	public static class Installation {
	    private static String sID = null;
	    private static final String INSTALLATION = "INSTALLATION";
	    public synchronized static String id(Context context) {
	        if (sID == null) {  
	            File installation = new File(context.getFilesDir(), INSTALLATION);
	            try {
	                if (!installation.exists())
	                    writeInstallationFile(installation);
	                sID = readInstallationFile(installation);
	            } catch (Exception e) {
	                throw new RuntimeException(e);
	            }
	        }
	        return sID;
	    }
	    private static String readInstallationFile(File installation) throws IOException {
	        RandomAccessFile f = new RandomAccessFile(installation, "r");
	        byte[] bytes = new byte[(int) f.length()];
	        f.readFully(bytes);
	        f.close();
	        return new String(bytes);
	    }
	    private static void writeInstallationFile(File installation) throws IOException {
	        FileOutputStream out = new FileOutputStream(installation);
	        String id = UUID.randomUUID().toString();
	        out.write(id.getBytes());
	        out.close();
	    }
	}
}
