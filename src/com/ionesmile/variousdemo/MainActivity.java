package com.ionesmile.variousdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void RotateAnimActivity(View v){
		startActivity(RotateAnimActivity.class);
	}
	
	public void WeatherActivity(View v){
		startActivity(WeatherActivity.class);
	}
	
	public void ShakeActivity(View v){
		startActivity(ShakeActivity.class);
	}
	
	public void ColorTipImageViewActivity(View v){
		startActivity(ColorTipImageViewActivity.class);
	}
	
	public void GetLocalActivity(View v){
		startActivity(GetLocalActivity.class);
	}
	
	public void FMActivity(View v){
		startActivity(FMActivity.class);
	}
	
	public void BlurActivity(View v){
		startActivity(BlurActivity.class);
	}
	
	public void HorizontalPickerActivity(View v){
		startActivity(HorizontalPickerActivity.class);
	}
	
	public void TextViewLinkActivity(View v){
		startActivity(TextViewLinkActivity.class);
	}
	
	public void EqualizerViewActivity(View v){
		startActivity(EqualizerViewActivity.class);
	}
	
	public void ColorSeekBarActivity(View v){
		startActivity(ColorSeekBarActivity.class);
	}
	
	public void BrightViewActivity(View v){
		startActivity(BrightViewActivity.class);
	}
	
	public void AudioEqualizerActivity(View v){
		startActivity(AudioEqualizerActivity.class);
	}
	
	public void MarketActivity(View v){
		startActivity(MarketActivity.class);
	}
	
	public void VibratorActivity(View v){
		startActivity(VibratorActivity.class);
	}
	
	public void SyllabusActivity(View v){
		startActivity(SyllabusActivity.class);
	}
	
	public void DeviceInfoActivity(View v){
		startActivity(DeviceInfoActivity.class);
	}
	
	public void VolumeActivity(View v){
		startActivity(VolumeActivity.class);
	}
	
	public void VolumeObserverActivity(View v){
		startActivity(VolumeObserverActivity.class);
	}
	
	public void MaxAppMenoryActivity(View v){
		startActivity(MaxAppMenoryActivity.class);
	}
	
	public void LaunchServerActivity(View v){
		startActivity(LaunchServerActivity.class);
	}
	
	public void LoggerActivity(View v){
		startActivity(LoggerActivity.class);
	}
	
	public void FontTestActivity(View v){
		startActivity(FontTestActivity.class);
	}
	
	public void WebViewActivity(View v){
		startActivity(WebViewActivity.class);
	}
	
	public void PhoneGapActivity(View v){
		startActivity(PhoneGapActivity.class);
	}
	
	public void CodeLayoutActivity(View v){
		startActivity(CodeLayoutActivity.class);
	}

	private void startActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

}
