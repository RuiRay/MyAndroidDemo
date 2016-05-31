package com.ionesmile.variousdemo;

import org.apache.cordova.DroidGap;

import android.os.Bundle;

public class PhoneGapActivity extends DroidGap {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //加载assets/www目录下的的index.html
        super.loadUrl("file:///android_asset/www/phoneGap.html");
    }

}
