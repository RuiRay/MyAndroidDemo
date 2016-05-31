package com.ionesmile.variousdemo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.ionesmile.variousdemo.utils.SettingsContentObserver;

public class VolumeObserverActivity extends Activity {
	
	private AudioManager mAudioManager;
	private SettingsContentObserver mSettingsContentObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volume_observer);

		this.mAudioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		Log.d("max volume ",
				this.mAudioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC) + "");

		mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
		// content://settings/system/volume_music
		Uri uri = android.provider.Settings.System.getUriFor(Settings.System.VOLUME_MUSIC + Settings.System.APPEND_FOR_LAST_AUDIBLE);
		 //Uri uri = Settings.System.getUriFor(Settings.System.VOLUME_SETTINGS[AudioManager.STREAM_RING]);
		uri = android.provider.Settings.System.CONTENT_URI;
		Log.i("myTag", "Volume uri = " + uri.toString());
		getContentResolver().registerContentObserver(uri, true, mSettingsContentObserver);
		
		try {
			JSONObject jobj = new JSONObject("");
			JSONArray names = jobj.names();
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private class Brand{
		public String name;
		public List<Car> cars;
		public String logo;
	}
	private class Car{
		public int id;
		public String name;
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
	}
}
