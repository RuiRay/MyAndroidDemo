package com.ionesmile.variousdemo;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class VolumeActivity extends Activity {

	private static final int DLNA_MAX_VOLUME = 80;
	private AudioManager mAudioManager;
	private int maxVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volume);
		
		//音量控制,初始化定义    
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);    
		//最大音量    
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.i("myTag", "keyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			onVolumeChangeBefore();
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,    
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
			onVolumeChange();
			return true;
			
		case KeyEvent.KEYCODE_VOLUME_UP:
			onVolumeChangeBefore();
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,    
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
			onVolumeChange();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int setVolume;
	// new int[]{0, 3, 8, 15, 22, 30, 38, 40, 50, 60, 70, 80, 90, 100};
	private void onVolumeChangeBefore() {
		// 获取DLNA当前的音量大小
		int curDlnaVolume = setVolume;
		// 计算对应的音量级别, 入最大的整数
		int volumeLevel = (int) Math.ceil(curDlnaVolume * maxVolume / (DLNA_MAX_VOLUME + 0.0f));
		Log.i("myTag", "-------------------------------------------------");
		Log.i("myTag", " volumeLevel = " + volumeLevel + "      orginVol = " + (curDlnaVolume * maxVolume / (DLNA_MAX_VOLUME + 0.0f)));
		// 调整当前手机的音量值
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
	}

	// 音量改变时监听
	private void onVolumeChange() {
		// 当前音量    
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
		Log.i("myTag", "--- maxVolume = " + maxVolume + "     currentVolume = " + currentVolume);
		// 根据当前手机的音量比，计算DLNA应该设置的音量
		setVolume = (int) (currentVolume / (maxVolume + 0.0f) * DLNA_MAX_VOLUME);
		Log.i("myTag", "- setVolume = " + setVolume + "    VolumeRate = " + (currentVolume / (maxVolume + 0.0f)));
	}
	
	class MyThread extends Thread {
		
		private int mMillis;
		private Runnable mRunnable;
		private boolean isInterrupe = false;
		private boolean isStop = false;
		
		public MyThread(int millis, Runnable runnable) {
			this.mMillis = millis;
			this.mRunnable = runnable;
		}
		
		public void run() {
			while (true) {
				try {
					Thread.sleep(mMillis);
					break;
				} catch (InterruptedException e) {
					if (isInterrupe) break;
				}
			}
			isStop = true;
			if(mRunnable != null && !isInterrupe){
				mRunnable.run();
			}
		}

		// 设置再执行的时间
		public void setmMillis(int mMillis) {
			this.mMillis = mMillis;
			interrupt();
		}

		public void setInterrupe(boolean isInterrupe) {
			this.isInterrupe = isInterrupe;
			if (isInterrupe) interrupt();
		}

		public boolean isStop(){
			return isStop;
		}
	}

}
