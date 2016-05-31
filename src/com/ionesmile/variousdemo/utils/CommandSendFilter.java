package com.ionesmile.variousdemo.utils;

import java.util.Timer;
import java.util.TimerTask;

public class CommandSendFilter {

	protected static final int COUNTER_MAX = 12;

	private static CommandSendFilter mInstance;
	
	private Timer mTimer;

	private boolean execRunnable;
	private Runnable mRunnable;
	
	private long sendCounter = 0;
	
	private CommandSendFilter() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (execRunnable) {
					execRunnable = false;
					if(mRunnable != null) mRunnable.run();
				}
			}
		}, 200);
	}
	
	public static CommandSendFilter getInstance(){
		if (mInstance == null) {
			mInstance = new CommandSendFilter();
		}
		return mInstance;
	}

	public Runnable getRunnable() {
		return mRunnable;
	}

	public void setRunnable(Runnable mRunnable) {
		this.mRunnable = mRunnable;
		sendCounter ++;
		if (sendCounter%COUNTER_MAX == 0) {
			if(mRunnable != null){
				execRunnable = false;
				mRunnable.run();
			} else {
				execRunnable = true;
			}
		}
	}

	private void cancelTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	
	public void cancel(){
		cancelTimer();
		mInstance = null;
	}
	
}
