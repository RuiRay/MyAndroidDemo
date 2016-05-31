package com.ionesmile.variousdemo;

import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ionesmile.variousdemo.utils.CommandSendFilter;
import com.ionesmile.variousdemo.view.FMView;

public class FMActivity extends Activity {

	public final static int[][] RADIO_FMS = {
		{87500, 108000, 100},		// CN
		{76000, 90000, 100}, 		// JP
		{87000, 108000, 50}};	// EU
	
	private FMView mFmView;
	private Button mFreqBtn;
	private int mRadioIndex = 0;
	

	private final static DecimalFormat mDF[] = { new DecimalFormat("###.0"), new DecimalFormat("###.00") };
	private DecimalFormat mDecimalFormat = new DecimalFormat("###.0");
	private static final float[] mCoefficient = { 0.1f, 0.001f };

	protected static final String TAG = FMActivity.class.getName();
	
	protected static final int COUNTER_MAX = 16;
	private long sendCounter = 0;
	
	private boolean execRunnable;
	private Runnable mRunnable;
	
	private CommandSendFilter mCommandSendFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fm);
		mCommandSendFilter = CommandSendFilter.getInstance();
		
		mFmView = (FMView) findViewById(R.id.fmview);
		mFreqBtn = (Button) findViewById(R.id.btn_freq);
		
		mFmView.setOnChangeListener(new FMView.OnChangeListener() {
			
			@Override
			public void onChanged(int values) {
			}
			
			@Override
			public void onChange(int values) {
				mFreqBtn.setText(String.valueOf(mDecimalFormat.format(values * mCoefficient[1])));
				sendCounter ++;
				Log.v(TAG, "onChange() values = " + values + "    sendCounter = " + sendCounter);
				mRunnable = doSomething(sendCounter);
				mCommandSendFilter.setRunnable(mRunnable);
			}
		});
		
		mFreqBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mRadioIndex = ++mRadioIndex % RADIO_FMS.length;
				mFmView.setFrequencySpan(RADIO_FMS[mRadioIndex][0], RADIO_FMS[mRadioIndex][1], RADIO_FMS[mRadioIndex][2]);
			}
		});
	}
	
	private int doTimer = 0;
	private Runnable doSomething(final long counter) {
		return new Runnable() {
			
			@Override
			public void run() {
				doTimer ++;
				Log.d(TAG, "doSomething() doTimer = " + doTimer + "   counter = " + counter + "   time = " + new Date().getTime());
			}
		};
	}
	
	@Override
	protected void onDestroy() {
		mCommandSendFilter.cancel();
		super.onDestroy();
	}

}
