package com.ionesmile.variousdemo.service;

import java.io.IOException;
import java.net.ServerSocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LaunchTestService extends Service {

	private static final String TAG = "ServerTAG";

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "LaunchTestService onBind()...");
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "LaunchTestService onCreate()...");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "LaunchTestService onStart()" + "       startId = " + startId);
		super.onStart(intent, startId);
	}
	
	private ServerSocket mServerSocket;
	private Thread thread;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "LaunchTestService onStartCommand() flags = " + flags + "       startId = " + startId);
		try {
			Log.i(TAG, "LaunchTestService onStartCommand() mServerSocket.isClosed() = " + (mServerSocket != null ? mServerSocket.isClosed() : "null"));
			if (mServerSocket != null && mServerSocket.isBound()) {
				Log.i(TAG, "LaunchTestService onStartCommand() mServerSocket.isBound() = " + mServerSocket.isBound());
			} else {
				mServerSocket = new ServerSocket(10010);
				thread = new Thread(){
					public void run() {
						try {
							while(true)
								mServerSocket.accept();
						} catch (IOException e) {
							Log.w(TAG, "", e);
						}
					};
				};
				thread.setDaemon(true);
				thread.start();
			}
		} catch (Exception e) {
			Log.w(TAG, "", e);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "LaunchTestService onDestroy()...");
		if (mServerSocket != null) {
			try {
				mServerSocket.close();
				//mServerSocket = null;
				thread.join();
			} catch (IOException e) {
				Log.w(TAG, "", e);
			} catch (InterruptedException e) {
				Log.w(TAG, "", e);
			}
		}
		super.onDestroy();
	}

}
