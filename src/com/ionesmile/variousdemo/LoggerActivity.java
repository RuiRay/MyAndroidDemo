package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;

public class LoggerActivity extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PropertyConfigurator.getConfigurator(this).configure();
		setContentView(R.layout.activity_logger);
	}
	
	public void createVerbose(View v){
		logger.debug("createVerbose()..........");
	}
	
	public void createInfo(View v){
		logger.info("createInfo()..........");
	}
	
	public void createError(View v){
		try {
			throw new NullPointerException();
		} catch (Exception e) {
			logger.error("createError()....", e);
		}
	}

}
