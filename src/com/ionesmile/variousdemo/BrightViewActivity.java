package com.ionesmile.variousdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ionesmile.variousdemo.view.BrightView.OnBrightChangeListener;
import com.ionesmile.variousdemo.view.ColorPlateView;
import com.ionesmile.variousdemo.view.ColorPlateView.OnColorChangeListener;

public class BrightViewActivity extends Activity {

	protected static final String TAG = BrightViewActivity.class.getSimpleName();
	private ColorPlateView colorPlateView;
	private int mColor, mBrightness = 255;
	private boolean isRender = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bright_view);
		//setContentView(new DrawView(this));
		//setContentView(new WifiLampColorView(this));
		//setContentView(new BrightView(this));
		
		final TextView colorView = (TextView) findViewById(R.id.view_color);
		
		colorPlateView = (ColorPlateView) findViewById(R.id.color_plate_view);
		colorPlateView.setOnColorChangeListener(new OnColorChangeListener() {
			
			@Override
			public void onColorChangeEnd(int red, int green, int blue) {
				Log.v("myTag", "onColorChangeEnd()   red = " + red + "   green = " + green + "   blue = " + blue);
				if (isRender) {
					mColor = Color.argb(0xFF, red, green, blue);
					colorView.setBackgroundColor(mColor);
				}
			}
			
			@Override
			public void onColorChange(int red, int green, int blue) {
				Log.v("myTag", "onColorChange()   red = " + red + "   green = " + green + "   blue = " + blue);
				if (isRender) {
					mColor = Color.argb(0xFF, red, green, blue);
					colorView.setBackgroundColor(mColor);
				}
			}
		});
		final com.ionesmile.variousdemo.view.BrightView brightView = (com.ionesmile.variousdemo.view.BrightView) findViewById(R.id.brightView);
		brightView.setOnBrightChangeListener(new OnBrightChangeListener() {
			
			@Override
			public void onBrightChangeEnd(int bright, boolean fromUser) {
				Log.v("myTag", "onBrightChangeEnd()   bright = " + bright);
				if (isRender) {
					mBrightness = bright;
					colorView.setText(String.valueOf(bright));
				}
			}
			
			@Override
			public void onBrightChange(int bright, boolean fromUser) {
				Log.v("myTag", "onBrightChange()   bright = " + bright);
				if (isRender) {
					mBrightness = bright;
					colorView.setText(String.valueOf(bright));
				}
			}
		});
		
		CheckBox cbRender = (CheckBox) findViewById(R.id.cb_render);
		cbRender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isRender = isChecked;
			}
		});
		
		colorView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int compoundColor = getCompoundColor(mColor, mBrightness);
				
				colorPlateView.setColorful(compoundColor);
				
				brightView.setBright(getBrightess(compoundColor));
			}

			private int getBrightess(int color) {
//				float[] hsv = new float[] { 0f, 0f, 1f };
//				Color.colorToHSV(color, hsv);
//				int brightness = (int) (hsv[2] * 255);
//				Log.i(TAG, "getBrightess() color = " + Integer.toHexString(color) + "   hsv[2] = " + hsv[2] + "   brightness = " + brightness);
				int red = Color.red(color);
				int green = Color.green(color);
				int blue = Color.blue(color);
				int brightness = Math.max(Math.max(red, green), blue);
				Log.i(TAG, "getBrightess() color = " + Integer.toHexString(color) + "   brightness = " + brightness);
				return brightness;
			}

			private int getCompoundColor(int color, int brightness) {
				float[] hsv = new float[]{0, 0, 1};
				Color.colorToHSV(color, hsv);
				hsv[2] = (brightness + 0.0f) / 255;
				int resultColor = Color.HSVToColor(hsv);
				Log.i(TAG, "getCompoundColor() color = " + Integer.toHexString(color) + "   brightness = " + brightness + "    resultColor = " + Integer.toHexString(resultColor));
				return resultColor;
			}
		});
	}
	
	
	public void Colorful(View v){
		colorPlateView.setIsColdWarmLamp(false, false);
	}
	
	public void white(View v){
		colorPlateView.setIsColdWarmLamp(true, false);
	}
	
	public void whiteUnable(View v){
		colorPlateView.setHasWarmLamp(!colorPlateView.isHasWarmLamp(), false);
	}
	
	/*public class BrightView extends View {
		
		public BrightView(Context context) {
			this(context, null);
		}

		public BrightView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public BrightView(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			init();
		}
		
		private void init() {
			
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(compositeImages(getWidth(), getHeight()), new Matrix(), new Paint());
			//compositeImages(getWidth(), getHeight(), canvas);
		}
		
		 private Bitmap compositeImages(int width, int height){   
		        Bitmap bmp = null;   
		        //下面这个Bitmap中创建的函数就可以创建一个空的Bitmap   
		        bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
		        Paint paint = new Paint();   
		       Canvas canvas = new Canvas(bmp); 
		        canvas.setBitmap(bmp);
		        //首先绘制第一张图片，很简单，就是和方法中getDstImage一样   
		        //canvas.drawBitmap(srcBitmap, 0, 0, paint);      
		        canvas.drawColor(Color.BLUE);
		           
		        //在绘制第二张图片的时候，我们需要指定一个Xfermode   
		        //这里采用Multiply模式，这个模式是将两张图片的对应的点的像素相乘   
		        //，再除以255，然后以新的像素来重新绘制显示合成后的图像   
		        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));   
		        //canvas.drawBitmap(dstBitmap, 0, 0, paint); 
		        paint.setColor(Color.RED);
		        canvas.drawCircle(width/2, height/2, width/3, paint);
		        
		        return bmp;   
		  }   
	}*/

}
