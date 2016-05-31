package com.ionesmile.variousdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 手机震动
 * @author iOnesmile
 *
 */
public class VibratorActivity extends Activity {

	public static final int DELAY_TIME = 10 * 1000;
	private Vibrator vibrator;
	private RadioGroup rgLanuage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vibrator);
		
		initRadioGroup();

		//delayCancelAlarm(DELAY_TIME);
		//startVibrator();
	}

	private void initRadioGroup() {
		rgLanuage = (RadioGroup) findViewById(R.id.rg_lanuage);
		rgLanuage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.i("GroupTest", "onCheckedChanged() group = " + group.getId() + "   checkedId = " + (checkedId == R.id.rb_chinese ? "chinese" : "english"));
			}
		});
	}
	
	public void checkChinese(View v){
		rgLanuage.check(R.id.rb_chinese);
	}
	
	public void checkEnglish(View v){
		rgLanuage.check(R.id.rb_english);
	}
	
	public void checkChineseBtn(View v){
		((RadioButton)findViewById(R.id.rb_chinese)).setChecked(true);
	}
	
	public void checkEnglishBtn(View v){
		((RadioButton)findViewById(R.id.rb_english)).setChecked(true);
	}
	
	public void checkClear(View v){
		rgLanuage.clearCheck();
	}

	@Override
	protected void onPause() {
		if (vibrator != null) {
			vibrator.cancel();
		}
		super.onPause();
	}

	private void startVibrator() {
		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		//long[] pattern = { 300, 300, 300, 300 }; // 停止 开启 停止 开启
		int stopTime = 240;
		int runTime = 480;
		int length = (DELAY_TIME / (stopTime + runTime) + 2) * 2;
		long[] pattern = new long[length];
		for (int i = 0; i < length; i+=2) {
			pattern[i] = stopTime;
			pattern[i + 1] = runTime;
		}
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}

	private void delayCancelAlarm(int delayTime) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, delayTime);
	}
}
