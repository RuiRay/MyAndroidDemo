package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ionesmile.variousdemo.view.HorizontalPicker;

public class HorizontalPickerActivity extends Activity  implements HorizontalPicker.OnItemSelected, HorizontalPicker.OnItemClicked {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horizontal_picker);
		

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.picker);
        picker.setOnItemClickedListener(this);
        picker.setOnItemSelectedListener(this);
	}

    @Override
    public void onItemSelected(int index)    {
        Toast.makeText(this, "Item selected = " + index, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int index) {
        Toast.makeText(this, "Item clicked = " + index, Toast.LENGTH_SHORT).show();
    }

}
