package com.ionesmile.variousdemo;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ionesmile.variousdemo.view.EqualizerView;
import com.ionesmile.variousdemo.view.HorizontalPicker;

public class EqualizerViewActivity extends Activity implements EqualizerView.OnChangeListener {

	private static final int CUSTOM_INDEX = 1;
	private EqualizerView mEqualizerView;
	private HorizontalPicker mHorizontalPicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equalizer_view);
		
		initUI();
	}
	
	 private static final int EQUALIZER_DATA[][];
	    
	    private static final int EQUALIZER_SOURCE_DATA[][] = {
	    	{0, 0, 0},
	    	{0, 0, 0},
	    	{0, 2, 2},
	    	{10, 0, 12},
	    	{-6, 8, -6},
	    	{5, -8, 6},
	    	{1, 3, -2},
	    	{0, 6, 3},
	    	{5, 8, -7},
	    	{-10, 2, 11}
	    };
	    
	    static {
	    	// 转换原始数据
	    	EQUALIZER_DATA = new int[EQUALIZER_SOURCE_DATA.length][EQUALIZER_SOURCE_DATA[0].length];
	    	for (int i = 1; i < EQUALIZER_SOURCE_DATA.length; i++) {
				for (int j = 0; j < EQUALIZER_SOURCE_DATA[i].length; j++) {
					EQUALIZER_DATA[i][j] = EQUALIZER_SOURCE_DATA[i][j] + 15;
				}
			}
	    }
	    
		private int curHorizontalIndex = 1;
		
		public void initUI() {
			mEqualizerView = (EqualizerView) findViewById(R.id.equalizer_view);
			mEqualizerView.setOnChangeListener(this);
			
			mHorizontalPicker = (HorizontalPicker) findViewById(R.id.horizontalpicker);
			mHorizontalPicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
				
				@Override
				public void onItemSelected(int index) {
					Log.i("myTag", "setOnItemSelectedListener onItemSelected index = " + index);
					curHorizontalIndex = index;
					if (index == 0) {
						mEqualizerView.setEnableTouch(false);
					} else {
						mEqualizerView.setEnableTouch(true);
					}
					// 当不是自定义值时，设置
					mEqualizerView.setValues(EQUALIZER_DATA[index]);
				}
			});
			mHorizontalPicker.setSelectedItem(CUSTOM_INDEX);
		}
		
		@Override
		public void onChange(int[] values) {
		}

		private int[] lastSetValues;
		
		@Override
		public void onChanged(int[] values) {
			if (!Arrays.equals(values, lastSetValues)) {
				lastSetValues = copyOfArrs(values);
				// 当进度改变后，如果不是自定义模式，则跳转到自定义模式
			 	if (curHorizontalIndex != CUSTOM_INDEX && !Arrays.equals(values, EQUALIZER_DATA[curHorizontalIndex])) {
			 		mHorizontalPicker.setSelectedItem(CUSTOM_INDEX);
			 		EQUALIZER_DATA[CUSTOM_INDEX] = copyOfArrs(values);
			 		mEqualizerView.setValues(EQUALIZER_DATA[CUSTOM_INDEX]);
			 		curHorizontalIndex = CUSTOM_INDEX;
				}
			}
		}
	    
	    public static int[] copyOfArrs(int[] original) {
	    	int[] result = new int[original.length];
	    	System.arraycopy(original, 0, result, 0, original.length);
	    	return result;
	    }

}
