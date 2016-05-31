package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ionesmile.variousdemo.view.ColorSeekBarView;
import com.ionesmile.variousdemo.view.ColorSeekBarView.OnProgressListener;
import com.ionesmile.variousdemo.view.ColorSeekBarView.SeekBarType;

public class ColorSeekBarActivity extends Activity implements OnProgressListener {

	private ColorSeekBarView colorfulView, whiteView, brightView;
	private TextView showTv;
	
	private boolean isOpen = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_seek_bar);
		colorfulView = (ColorSeekBarView) findViewById(R.id.csv_colorful);
		whiteView = (ColorSeekBarView) findViewById(R.id.csv_white);
		brightView = (ColorSeekBarView) findViewById(R.id.csv_bright);
		showTv = (TextView) findViewById(R.id.tv_show);
		
		colorfulView.setOnProgressListener(this);
		whiteView.setOnProgressListener(this);
		brightView.setOnProgressListener(this);
		
		showTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = showTv.getText().toString().trim();
				if (text.contains("COLORFUL")) {
					colorfulView.setProgress(value);
				} else if (text.contains("WHITE")) {
					whiteView.setProgress(value);
				} else if (text.contains("BRIGHT")) {
					brightView.setProgress(value);
				}
			}
		});
		
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox_switch);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isOpen = isChecked;
			}
		});
		isOpen = checkBox.isChecked();
	}

	private int value;
	@Override
	public void onProgressChange(int type, int val) {
		Log.i("myTag", "onProgressChange = " + type + "    val = " + val);
		if (!isOpen) {
			return;
		}
		value = val;
		switch (type) {
		case SeekBarType.COLORFUL:
			showTv.setBackgroundColor(val);
			showTv.setText("COLORFUL   = " + val);
			break;
			
		case SeekBarType.WHITE:
			showTv.setText("WHITE   = " + val);
			break;
			
		case SeekBarType.BRIGHT:
			showTv.setText("BRIGHT   = " + val);
			break;
		}
	}

	@Override
	public void onProgressChanged(int type, int val) {
		onProgressChange(type, val);
	}
	
	

}
