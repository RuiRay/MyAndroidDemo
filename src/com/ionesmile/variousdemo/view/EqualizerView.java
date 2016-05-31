package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.PixelUtil;

public class EqualizerView extends View {

	private static final float ANIM_DRAW_FRAME = 20.0f;
	private final static int BAND_COUNT = 3;
	private final static String DB_UNIT = " db";
	private int mTopPadding, mBottomPadding;
	private int mMaxProgress = 30, mHalfProgress = mMaxProgress/2;
	private int[] mCurValues;
	private String[] mVoiceGradeText;
	private float mLeftRightPaddingSacle = 0.6f;
	private int mFontHeight;
	private int mBandLineWidth, mLinkWidth;
	private int mBandLineColor, mBandLineBackgroundColor, mLinkColor;
	private int mTextColor;
	private int mTextSize;
	

	private Interpolator mInterpolator;
	private Paint mBandLinePaint, mBandLineBackgroundPaint;
	private Paint mLinkPaint;
	private Paint mTextPaint;
	
	private Drawable mThumbNor, mThumbDown, mThumbClose;
	
	public EqualizerView(Context context) {
		this(context, null);
	}

	public EqualizerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EqualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mTopPadding = PixelUtil.dp2px(20, context);
		mBottomPadding = PixelUtil.dp2px(12, context);
		mCurValues = new int[]{0, 0, 0};
		mInterpolator = new DecelerateInterpolator();
		
		// initializer Line color and width
		mBandLineWidth = PixelUtil.dp2px(2, context);
		mLinkWidth = PixelUtil.dp2px(6, context);
		mTextSize = getResources().getDimensionPixelOffset(R.dimen.common_font);
		mBandLineColor = getResources().getColor(R.color.common_theme);
		mBandLineBackgroundColor = getResources().getColor(R.color.common_gray_press);
		mLinkColor = getResources().getColor(R.color.common_theme_press);
		mTextColor = getResources().getColor(R.color.common_gray);
		mVoiceGradeText = new String[]{getResources().getString(R.string.short_voice), getResources().getString(R.string.middle_voice), getResources().getString(R.string.high_voice)};
		
		// initializer Paint
		mBandLinePaint = new Paint();
		mBandLinePaint.setColor(mBandLineColor);
		mBandLinePaint.setStrokeWidth(mBandLineWidth);
		mBandLinePaint.setStyle(Style.STROKE);
		
		mBandLineBackgroundPaint = new Paint();
		mBandLineBackgroundPaint.setColor(mBandLineBackgroundColor);
		mBandLineBackgroundPaint.setStrokeWidth(mBandLineWidth);
		mBandLineBackgroundPaint.setStyle(Style.STROKE);
		
		mLinkPaint = new Paint();
		mLinkPaint.setColor(mLinkColor);
		mLinkPaint.setStrokeWidth(mLinkWidth);
		mLinkPaint.setStyle(Style.STROKE);
		
		mTextPaint = new Paint();
		mTextPaint.setColor(mTextColor);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setStyle(Style.STROKE);
		mFontHeight = (int) (mTextPaint.measureText("AA") * 3);
		
		// initializer Thumb Drawable
		mThumbNor = getResources().getDrawable(R.drawable.btn_fun_eq_slider_nor);
	    int thumbHalfheight = mThumbNor.getIntrinsicHeight() / 2;  
	    int thumbHalfWidth = mThumbNor.getIntrinsicWidth() / 2;  
	    mThumbNor.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    mThumbDown = getResources().getDrawable(R.drawable.btn_fun_eq_slider_down);
	    thumbHalfheight = mThumbDown.getIntrinsicHeight() / 2;  
	    thumbHalfWidth = mThumbDown.getIntrinsicWidth() / 2;  
	    mThumbDown.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    mThumbClose = getResources().getDrawable(R.drawable.btn_fun_eq_slider_close);
	    thumbHalfheight = mThumbClose.getIntrinsicHeight() / 2;  
	    thumbHalfWidth = mThumbClose.getIntrinsicWidth() / 2;  
	    mThumbClose.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	}
	
	// 绘制切换动画
	private boolean isDrawProgress = false;
	private int drawProgess = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x01) {
				invalidate();
			}
		};
	};
	
	@Override
	protected void onDraw(Canvas canvas) {
		// calculate BandY progress Position
		if (isDrawProgress) {
			mHandler.removeMessages(0x01);
			mHandler.sendEmptyMessageDelayed(0x01, 20);
			drawProgess ++;
			mAnimValues = getValuesByAnim(mStartAnimValues, mEndAnimValues, drawProgess);
			if (drawProgess > ANIM_DRAW_FRAME) {
				drawProgess = 0;
				isDrawProgress = false;
			}
			for (int i = 0; i < BAND_COUNT; i++) {
				mBandYProgressPosition[i] = mAnimValues[i] + mTopPadding;
			}
		} else {
			for (int i = 0; i < BAND_COUNT; i++) {
				mBandYProgressPosition[i] = (mMaxProgress - mCurValues[i]) * mBandLineHeight / mMaxProgress + mTopPadding;
			}
		}
		// draw progress line
		for (int i = 0; i < BAND_COUNT; i++) {
			// draw background
			canvas.drawLine(mBandXPosition[i], mTopPadding, mBandXPosition[i], mBandLineYEndPostion, mBandLineBackgroundPaint);
			// draw progress
			canvas.drawLine(mBandXPosition[i], mBandYProgressPosition[i], mBandXPosition[i], mBandLineYEndPostion, mBandLinePaint);
			// draw thumb
			// Log.i("myTag", "mBandYProgressPosition[" + i + "] = " + mBandYProgressPosition[i]);
			canvas.save();  
            canvas.translate(mBandXPosition[i], mBandYProgressPosition[i]);  
			if (mEnableTouch) {
				if (mTouchedBandIndex == i) {
					 mThumbDown.draw(canvas);
				} else {
					mThumbNor.draw(canvas);
				}
			} else {
				mThumbClose.draw(canvas);
			}
            canvas.restore();  
    		// draw text
            String text = (mCurValues[i] - mHalfProgress) + DB_UNIT;
            Rect rect = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), rect);
            int textWidth = rect.width(), textHeight = rect.height();
    		canvas.drawText(text, mBandXPosition[i] - textWidth/2, mBandLineYEndPostion+mBottomPadding+textHeight, mTextPaint);
    		// draw Voice Grade Word
    		text = mVoiceGradeText[i];
    		mTextPaint.getTextBounds(text, 0, text.length(), rect);
            textWidth = rect.width();
            textHeight = rect.height();
    		canvas.drawText(text, mBandXPosition[i] - textWidth/2, mBandLineYEndPostion+mBottomPadding+textHeight*2.5f, mTextPaint);
		}
		// draw link line
		for (int i = 0; i < BAND_COUNT-1; i++) {
			canvas.drawLine(mBandXPosition[i], mBandYProgressPosition[i], mBandXPosition[i+1], mBandYProgressPosition[i+1], mLinkPaint);
		}
		canvas.drawLine(0, mBandYProgressPosition[0], mBandXPosition[0], mBandYProgressPosition[0], mLinkPaint);
		canvas.drawLine(mBandXPosition[BAND_COUNT-1], mBandYProgressPosition[BAND_COUNT-1], getWidth(), mBandYProgressPosition[BAND_COUNT-1], mLinkPaint);
	}
	
	protected int[] getValuesByAnim(int[] startVals, int[] endVals, int progress) {
		int length = startVals.length;
		int[] result = new int[length];
		for (int i = 0; i < length; i++) {
			result[i] = getAnimValue(startVals[i], endVals[i], progress, mInterpolator);
		}
		return result;
	}

	private int getAnimValue(int startVal, int endVal, int progress, Interpolator interpolator) {
		float percent = interpolator.getInterpolation(progress/ANIM_DRAW_FRAME);
		return (int) ((endVal - startVal) * percent + startVal);
	}

	private float mIntervalBand;
	private float mBandLineHeight, mBandLineYEndPostion;
	private float[] mBandXPosition = new float[BAND_COUNT];
	private float[] mBandYProgressPosition = new float[BAND_COUNT];
	private float mValidTouchRadius;
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		mIntervalBand = (width - (BAND_COUNT - 1)*mBandLineWidth)/((BAND_COUNT - 1) + 2*mLeftRightPaddingSacle);
		mValidTouchRadius = mIntervalBand * 2 / 7;
		for (int i = 0; i < BAND_COUNT; i++) {
			mBandXPosition[i] = mIntervalBand*mLeftRightPaddingSacle + i*(mIntervalBand+mBandLineWidth) + mBandLineWidth/2;
		}
		mBandLineHeight = height - mTopPadding - mBottomPadding - mFontHeight;
		mBandLineYEndPostion = mBandLineHeight + mTopPadding;
	}
	
	private boolean mEnableTouch = true;
	//private boolean[] mBandTouched = new boolean[BAND_COUNT];
	private int mTouchedBandIndex = -1;
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            if ((mTouchedBandIndex = isTouchedBand(x, y)) != -1) {  
                updateBandPositon(x, y);  
                return true;  
            }
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (mTouchedBandIndex != -1) {  
                updateBandPositon(x, y);
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
        	mTouchedBandIndex = -1;
            invalidate();  
            if (mOnChangeListener != null) {  
            	mOnChangeListener.onChanged(mCurValues);
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }

	private void updateBandPositon(int x, int y) {
		y = (y < mTopPadding ? mTopPadding : y);
		y = (int) (y > mBandLineYEndPostion ? mBandLineYEndPostion : y);
		mCurValues[mTouchedBandIndex] = (int) (mMaxProgress - (y-mTopPadding)/mBandLineHeight * mMaxProgress);
		invalidate();
		if (mOnChangeListener != null) {  
        	mOnChangeListener.onChange(mCurValues);
        } 
	}

	private int isTouchedBand(int x, int y) {
		if (mEnableTouch && y > mTopPadding && y < mBandLineYEndPostion) {
			for (int i = 0; i < BAND_COUNT; i++) {
				if (Math.abs(mBandXPosition[i] - x) < mValidTouchRadius) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private int[] mStartAnimValues, mEndAnimValues, mAnimValues;
	public void setValues(int[] values){
		if (!isDrawProgress) {
			mStartAnimValues = copyAndConvertAnimValues(mCurValues);
			mEndAnimValues = copyAndConvertAnimValues(values);
			mCurValues = copyOfArrs(values);
			isDrawProgress = true;
			invalidate();
			if (mOnChangeListener != null) {  
	        	mOnChangeListener.onChanged(mCurValues);
	        }
		}
	}
	
	private int[] copyAndConvertAnimValues(int[] values) {
		int length = values.length;
		int[] result = new int[length];
		for (int i = 0; i < length; i++) {
			result[i] = (int) (mBandLineHeight - mBandLineHeight * values[i] / mMaxProgress);
		}
		return result;
	}

	public void setMaxProgress(int maxProgress){
		this.mMaxProgress = maxProgress;
		this.mHalfProgress = maxProgress / 2;
		invalidate();
	}
	
	public void setEnableTouch(boolean flag){
		if (flag != mEnableTouch) {
			mEnableTouch = flag;
		}
	}
	
	private OnChangeListener mOnChangeListener;

	public void setOnChangeListener(OnChangeListener mOnChangeListener) {
		this.mOnChangeListener = mOnChangeListener;
	}

	public static interface OnChangeListener {
		void onChange(int[] values);
		void onChanged(int[] values);
	}

    public int[] copyOfArrs(int[] original) {
    	int[] result = new int[original.length];
    	System.arraycopy(original, 0, result, 0, original.length);
    	return result;
    }
}
