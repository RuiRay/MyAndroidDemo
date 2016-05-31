package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ionesmile.variousdemo.FMActivity;
import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.PixelUtil;

public class FMView extends View {

	private static final int SCALE_INTERVAL = 3 * 1000;
	private int minFmFrequency = FMActivity.RADIO_FMS[0][0];
	private int maxFmFrequency = FMActivity.RADIO_FMS[0][1];
	private int minFreqencySpan = FMActivity.RADIO_FMS[0][2];
	private int fmFrequencySpan = maxFmFrequency - minFmFrequency;
	private final static int SCALE_COUNT = 6;
	
	private int mFrequency = minFmFrequency;
	
	private int mPaddingLeft, mPaddingRight;
	private int mLineWidth;
	private int mLineLongHeight, mLineShortHeight;
	private int mLineColor;
	private int mFontTopPadding;
	
	private Paint mScalePaint;
	private Paint mFontPaint;
	
	private Drawable mThumbNor;
	
	public FMView(Context context) {
		this(context, null);
	}

	public FMView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FMView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mPaddingLeft = PixelUtil.dp2px(20, context);
		mPaddingRight = mPaddingLeft;
		mLineWidth = PixelUtil.dp2px(0.6f, context);
		mLineColor = getResources().getColor(R.color.common_gray);
		
		mScalePaint = new Paint();
		mScalePaint.setStrokeWidth(mLineWidth);
		mScalePaint.setColor(mLineColor);
		mScalePaint.setStyle(Style.STROKE);
		
		mFontPaint = new Paint();
		mFontPaint.setColor(mLineColor);
		mFontPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.common_font));
		mFontTopPadding = getResources().getDimensionPixelSize(R.dimen.common_padding_small);
		
		// initializer Thumb Drawable
		mThumbNor = getResources().getDrawable(R.drawable.img_fun_fm_toggle);
	    int thumbHalfheight = mThumbNor.getIntrinsicHeight() / 2;  
	    int thumbHalfWidth = mThumbNor.getIntrinsicWidth() / 2;  
	    mThumbNor.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  

		mLineLongHeight = mThumbNor.getIntrinsicHeight() / 3;  
		mLineShortHeight = mLineLongHeight / 3;
	}
	
	private int textDrawIndex = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		textDrawIndex = 0;
		for (int i = 0; i < sumScaleNum; i++) {
			if ((i-scaleLeftOffset)%SCALE_COUNT == 0) {
				canvas.drawLine(mScaleXPosition[i], mLongStartY, mScaleXPosition[i], mLongEndY, mScalePaint);
				String textStr = String.valueOf((startFreq + textDrawIndex++*SCALE_INTERVAL)/1000);
				Rect textRect = new Rect();
				mFontPaint.getTextBounds(textStr, 0, textStr.length(), textRect);
				canvas.drawText(textStr, mScaleXPosition[i]-textRect.width()/2, mLongEndY + mFontTopPadding + textRect.height(), mFontPaint);
			} else {
				canvas.drawLine(mScaleXPosition[i], mShortStartY, mScaleXPosition[i], mShortEndY, mScalePaint);
			}
		}
		canvas.save();  
		float progress = (mFrequency-minFmFrequency) * mScaleWidth/fmFrequencySpan + mPaddingLeft;
        canvas.translate(progress, centerY);  
        mThumbNor.draw(canvas);  
        canvas.restore();
	}
	
	private float[] mScaleXPosition;
	private int mScaleWidth;
	private int sumScaleNum;
	private int mShortStartY, mLongStartY;
	private int mShortEndY, mLongEndY;
	private int centerY, scaleCenterY;
	private int scaleLeftOffset;
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		initScaleData();
		
		centerY = height/2;
		scaleCenterY = centerY - centerY/14;
		mShortStartY = scaleCenterY - mLineShortHeight/2;
		mShortEndY = scaleCenterY + mLineShortHeight/2;
		mLongStartY = scaleCenterY - mLineLongHeight/2;
		mLongEndY = scaleCenterY + mLineLongHeight/2;
	}
	
	private boolean mEnableTouch = true;
	private boolean isTouched = false;
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            if (isTouchedScale(x, y)) {  
            	isTouched = true;
                updateScalePositon(x, y);  
                return true;  
            }
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (isTouched) {  
                updateScalePositon(x, y);
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
        	isTouched = false;
            invalidate();  
            if (mOnChangeListener != null) {  
            	mOnChangeListener.onChanged(mFrequency);
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }
	
	private void updateScalePositon(int x, int y) {
		x = (x < mPaddingLeft ? mPaddingLeft : x);
		x = x > (mScaleWidth+mPaddingLeft) ? (mScaleWidth+mPaddingLeft) : x;
		mFrequency = (x-mPaddingLeft)*fmFrequencySpan/mScaleWidth + minFmFrequency;
		mFrequency = ((mFrequency+(minFreqencySpan/2))/minFreqencySpan) * minFreqencySpan;
		invalidate();
		if (mOnChangeListener != null) {  
        	mOnChangeListener.onChange(mFrequency);
        } 
	}

	private boolean isTouchedScale(int x, int y) {
		return mEnableTouch;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 设置高度为Thumb的高度
		int height = mThumbNor.getIntrinsicHeight();
		setMeasuredDimension(widthMeasureSpec, height);
	}
	
	public void setFrequency(int frequency){
		this.mFrequency = frequency;
		invalidate();
	}
	
	private int startFreq;
	public void setFrequencySpan(int minFreqency, int maxFreqency, int minFreqencySpan) {
		this.minFmFrequency = minFreqency;
		this.maxFmFrequency = maxFreqency;
		this.minFreqencySpan = minFreqencySpan;
		fmFrequencySpan = maxFmFrequency - minFmFrequency;
		
		initScaleData();
		invalidate();
	}

	// 初始化尺的数据
	private void initScaleData() {
		// {87500, 108000, 100}
		// 获取第一个大刻度的值，以1000为最小整数，结果如89000
		startFreq = getStartFreq(minFmFrequency, maxFmFrequency, SCALE_INTERVAL, 1000);
		// 尺上刻度的总数 = (前半段值)/(最小刻度值) + (后半段值)/(最小刻度值) + 一个刻度数
		sumScaleNum = (startFreq - minFmFrequency)/(SCALE_INTERVAL/SCALE_COUNT) + (maxFmFrequency - startFreq)/(SCALE_INTERVAL/SCALE_COUNT) + 1;
		// 刻度偏移数
		scaleLeftOffset = (startFreq - minFmFrequency)/(SCALE_INTERVAL/SCALE_COUNT);
		
		mScaleWidth = getWidth() - mPaddingLeft - mPaddingRight;
		// 每一个刻度的宽度 = 尺的可用宽度 / ((表示的长度)/最小长度单位 * (最小刻度拥有的最小长度个数))
		//float perScale = mScaleWidth / ((maxFreqency-minFreqency)/((maxFreqency-minFreqency)/minFreqencySpan) * (SCALE_INTERVAL/SCALE_COUNT/minFreqencySpan));
		float perScale = mScaleWidth * (SCALE_INTERVAL/SCALE_COUNT) / (maxFmFrequency-minFmFrequency + 0.0f);
		// 第一个刻度的位置  = ((前半段值)%(最小的刻度值)/(最小刻度值) * 平均刻度值)  =  (多余出来的比率 * 平均刻度值)
		float firstXPosition = ((startFreq-minFmFrequency)%(SCALE_INTERVAL/SCALE_COUNT)/(SCALE_INTERVAL/SCALE_COUNT + 0.0f) * perScale) + mPaddingLeft;
		
		mScaleXPosition = new float[sumScaleNum];
		for (int i = 0; i < sumScaleNum; i++) {
			mScaleXPosition[i] = firstXPosition + i*perScale;
		}
	}

	private static int getStartFreq(int minFreqency, int maxFreqency, int scaleInterval, int unit) {
		int yuNum = (maxFreqency - minFreqency) % scaleInterval;
		int num2 = unit - minFreqency%unit;
		num2 = (num2 == unit ? 0 : num2);
		if (yuNum > num2) {
			return minFreqency + num2 + scaleInterval/unit/2*unit;
		}
		return minFreqency;
	}

	private OnChangeListener mOnChangeListener;
	
	public OnChangeListener getOnChangeListener() {
		return mOnChangeListener;
	}

	public void setOnChangeListener(OnChangeListener mOnChangeListener) {
		this.mOnChangeListener = mOnChangeListener;
	}

	public static interface OnChangeListener {
		void onChange(int values);
		void onChanged(int values);
	}

}
