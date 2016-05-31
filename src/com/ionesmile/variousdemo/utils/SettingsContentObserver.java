package com.ionesmile.variousdemo.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

/**
 * http://stackoverflow.com/questions/6896746/android-is-there-a-broadcast-action-for-volume-changes
 * @author Administrator
 *
 */
public class SettingsContentObserver extends ContentObserver
{
	int previousVolume;
	Context context;
	
	public SettingsContentObserver(Context c, Handler handler)
	{
		super(handler);
		context = c;

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		Log.i("myTag", "SettingsContentObserver previousVolume = " + previousVolume);
	}

	@Override
	public boolean deliverSelfNotifications()
	{
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange)
	{
		Log.i("myTag", "SettingsContentObserver onChange() selfChange = " + selfChange);
		super.onChange(selfChange);

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

		Log.d("myTag", currentVolume + " currentVolume");
		
		int delta = previousVolume - currentVolume;

		if (delta > 0)
		{
			Log.i("myTag", delta + " Decreased");
			previousVolume = currentVolume;
		}
		else if (delta < 0)
		{
			Log.i("myTag", delta + " Increased");
			previousVolume = currentVolume;
		}
	}
}
