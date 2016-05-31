package com.ionesmile.variousdemo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class FontTestActivity extends Activity {

	private TextView tvContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_font_test);
		tvContent = (TextView) findViewById(R.id.tv_content);
		
		//将字体文件保存在assets/fonts/目录下，创建Typeface对象
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/numbers.otf");

		//使用字体
		tvContent.setTypeface(typeFace);
		
		/*TextView textView =((TextView)findViewById(R.id.tv_marquee));
		textView.setFocusable(true);
		textView .setEllipsize(TextUtils.TruncateAt.MARQUEE);
		textView .setSingleLine(true);
		textView .setMarqueeRepeatLimit(6);*/

	}
	
	

}
