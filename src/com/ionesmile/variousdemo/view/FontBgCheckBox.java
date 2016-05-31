package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.ionesmile.variousdemo.R;

public class FontBgCheckBox extends CheckBox {

	private static final String STANDARD_WORD = "五";
	private Paint mBgPaint;
	private Paint mFontPaint;

	private int mFontCheckColor, mFontUncheckColor, mBgCheckColor;
	private int mPadding;

	public FontBgCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);

		mFontCheckColor = 0xFFFFFFFF;
		mFontUncheckColor = getResources().getColor(R.color.common_gray);
		mBgCheckColor = getResources().getColor(R.color.common_theme);
		mPadding = getResources().getDimensionPixelSize(R.dimen.common_padding_small);

		mBgPaint = new Paint();
		mBgPaint.setColor(mBgCheckColor);
		mBgPaint.setAntiAlias(true);

		mFontPaint = new Paint();
		mFontPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.common_font));
		mFontPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		if (isChecked()) {
			canvas.drawCircle(centerX, centerY, centerX-mPadding, mBgPaint);
			mFontPaint.setColor(mFontCheckColor);
			String text = STANDARD_WORD;
			Rect fontRect = new Rect();
			mFontPaint.getTextBounds(text, 0, text.length(), fontRect);
			canvas.drawText(getText().toString(), centerX-fontRect.width()/2, centerY+fontRect.height()/2, mFontPaint);
		} else {
			mFontPaint.setColor(mFontUncheckColor);
			String text = STANDARD_WORD;
			Rect fontRect = new Rect();
			mFontPaint.getTextBounds(text, 0, text.length(), fontRect);
			canvas.drawText(getText().toString(), centerX-fontRect.width()/2, centerY+fontRect.height()/2, mFontPaint);
		}
	}
	  
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

}
