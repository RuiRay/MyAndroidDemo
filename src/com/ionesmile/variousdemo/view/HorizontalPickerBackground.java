package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.PixelUtil;

public class HorizontalPickerBackground extends View {

	public HorizontalPickerBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(compositeImages(getWidth(), getHeight()), new Matrix(), new Paint());
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Bitmap compositeImages(int width, int height){   
		Log.i("myTag", "compositeImages width = " + width + "   height = " + height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
        Paint paint = new Paint();   
        Canvas canvas = new Canvas(bmp); 
        canvas.setBitmap(bmp);
        canvas.drawColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        // 绘制这个三角形,你可以绘制任意多边形  
        int sideLen = PixelUtil.dp2px(10, getContext());
        int centerX = width / 2;
        Path path = new Path();  
        path.moveTo(centerX - sideLen, 0);// 此点为多边形的起点  
        path.lineTo(centerX + sideLen, 0);
        path.lineTo(centerX, sideLen);
        path.close(); // 使这些点构成封闭的多边形  
        canvas.drawPath(path, paint);  
        
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        paint.setColor(getResources().getColor(R.color.common_theme));
        int x = width / 2;
        int y = height - PixelUtil.dp2px(8, getContext());
        int radius = PixelUtil.dp2px(3.6f, getContext());
        canvas.drawCircle(x, y, radius, paint);
        return bmp;   
  }  

}
