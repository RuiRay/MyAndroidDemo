package com.ionesmile.variousdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ionesmile.variousdemo.service.LaunchTestService;

public class LaunchServerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch_server);
	}
	
	public void launchServer(View v){
		Intent intent = new Intent(this, LaunchTestService.class);
		startService(intent);
	}
	
	public void stopServer(View v){
		Intent intent = new Intent(this, LaunchTestService.class);
		stopService(intent);
	}

}
