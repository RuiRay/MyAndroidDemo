package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ionesmile.variousdemo.view.SlidingLayer;

public class BlurActivity extends Activity {


	private LinearLayout mPlayListBll;
	private SlidingLayer mSlidingLayer;
	private View rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blur);

		mPlayListBll = (LinearLayout) findViewById(R.id.blurll_playlist);
		mSlidingLayer = (SlidingLayer) findViewById(R.id.playlist_layer);

		rootView = findViewById(R.id.layout_root);
	}
	
	public void closeLayout(View v){
		mSlidingLayer.closeLayer(true);
	}
	
	public void openLayout(View v){
		mSlidingLayer.openLayer(true);
	}
	
	public void setBackground(View v){
		mPlayListBll.setBackgroundResource(R.drawable.img_lamp_bg);
	}
	
	@Override
	public void onBackPressed() {
		if (mSlidingLayer.isOpened()) {
			mSlidingLayer.closeLayer(true);
		} else {
			super.onBackPressed();
		}
	}

}
