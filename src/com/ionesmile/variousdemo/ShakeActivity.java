package com.ionesmile.variousdemo;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ionesmile.variousdemo.utils.ShakeDetector;

public class ShakeActivity extends Activity implements ShakeDetector.Listener {

	/*
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_shake); }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		ShakeDetector sd = new ShakeDetector(this);
		sd.start(sensorManager);
		
		sd.stop();

		TextView tv = new TextView(this);
		tv.setGravity(CENTER);
		tv.setText("Shake me, bro!");
		setContentView(tv, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
	}

	public void hearShake() {
		Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();
	}

}
