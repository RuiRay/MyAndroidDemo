package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.PixelUtil;


public class ColorSeekBarView extends View {

	private int maxProgress = 255, curProgress = 0;
	private Paint mBackgroundPaint;
	private Drawable mThumb;
	private boolean enableSeek = true;
	private int mSeekbarType = SeekBarType.COLORFUL;
	private int mPadding;
	
	public ColorSeekBarView(Context context) {
		this(context, null);
	}

	public ColorSeekBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorSeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSeekBarView);
		mSeekbarType = mTypedArray.getInt(R.styleable.ColorSeekBarView_seekBarType, SeekBarType.COLORFUL);
		float lineWidth = mTypedArray.getInt(R.styleable.ColorSeekBarView_csbv_line_width, PixelUtil.dp2px(4, context));
		mTypedArray.recycle();
		
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setStyle(Style.STROKE);
		mBackgroundPaint.setStrokeWidth(lineWidth);
		mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
		
		mThumb = getResources().getDrawable(R.drawable.btn_fun_model_brightness);
	    int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;  
	    int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;  
	    mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    mPadding = thumbHalfWidth;
	}
	
	private void renderSeekBar(int type) {
		switch (type) {
		case SeekBarType.COLORFUL:
			int angleCount = 12;
			int[] colorful = new int[angleCount];
			float hsv[] = new float[] { 0f, 1f, 1f };
            for (int i = 0; i < angleCount; i++) {
                // 根据HSV色盘，旋转的角度取色
                hsv[0] = 360 - i * 30;
                colorful[i] = Color.HSVToColor(hsv);
            }
			LinearGradient colorfulLG = new LinearGradient(0, 0, getWidth(), getHeight(), colorful, null, TileMode.REPEAT);
			mBackgroundPaint.setShader(colorfulLG);
			break;

		case SeekBarType.WHITE:
			LinearGradient whiteLG = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{Color.HSVToColor(new float[]{49, 1, 96}), Color.WHITE}, null, TileMode.REPEAT);
			mBackgroundPaint.setShader(whiteLG);
			break;
			
		case SeekBarType.BRIGHT:
			LinearGradient brightLG = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{Color.BLACK, Color.WHITE}, null, TileMode.REPEAT);
			mBackgroundPaint.setShader(brightLG);
			break;
			
		default:
			
			break;
		}
	}

	private int centerY;
	private Bitmap mSeekBarBitmap;
    private Matrix mMatrix = new Matrix();
    private Paint mBasePaint = new Paint();
    private int mProgressX;
    
	@Override
	protected void onDraw(Canvas canvas) {
		centerY = getHeight() / 2;
		
		// draw Background
		if (mSeekBarBitmap == null || mSeekBarBitmap.getWidth() != getWidth()) {
			mSeekBarBitmap = getSeekBarBitmap(getWidth(), getHeight());
		}
		canvas.drawBitmap(mSeekBarBitmap, mMatrix, mBasePaint);
		// draw Thumb
		mProgressX = (maxProgress != 0 ? (curProgress * (getWidth()-2*mPadding) / maxProgress + mPadding) : mPadding);
		Log.i("myTag", "curProgress = " + curProgress + "   mProgressX = " + mProgressX);
		canvas.save();  
		canvas.translate(mProgressX, centerY);  
		mThumb.draw(canvas);  
		canvas.restore();
	}
	
	private Bitmap getSeekBarBitmap(int width, int height){   
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
        Canvas canvas = new Canvas(bmp); 
        canvas.drawLine(mPadding, height/2, width-mPadding, height/2, mBackgroundPaint);
        return bmp;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO
		renderSeekBar(mSeekbarType);
		mSeekBarBitmap = getSeekBarBitmap(getWidth(), getHeight());
	}
	
	private boolean downOnProgress = false;
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            if (isTouchProgress(x, y)) {  
            	downOnProgress = true;  
                updateProgress(x, y);  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (downOnProgress) {  
            	updateProgress(x, y);  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
        	downOnProgress = false;  
            invalidate();  
            if (mOnProgressListener != null) {  
            	mOnProgressListener.onProgressChanged(mSeekbarType, getValue());
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }
	
	

	private void updateProgress(int x, int y) {
		if (x < mPadding) {
			x = mPadding;
		} else if (x > getWidth()-mPadding) {
			x = getWidth()-mPadding;
		}
		x = x - mPadding;
		curProgress = x * maxProgress / (getWidth()-2*mPadding);
		invalidate();
		if (mOnProgressListener != null) {  
			mOnProgressListener.onProgressChange(mSeekbarType, getValue());
		}
	}
	
	public void setSeekBarType(int type){
		this.mSeekbarType = type;
		renderSeekBar(type);
		if (getWidth() > 0 && getHeight() > 0) {
			mSeekBarBitmap = getSeekBarBitmap(getWidth(), getHeight());
		}
		invalidate();
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public int getCurProgress() {
		return curProgress;
	}
	
	/** Currently selected color */
	private float[] colorHSV = new float[] { 0f, 0f, 1f };
	
	public void setProgress(int value){
		switch (mSeekbarType) {
		case SeekBarType.COLORFUL:
			Color.colorToHSV(value, colorHSV);
			Log.i("myTag", "colorHSV[0] = " + colorHSV[0] + "  maxProgress " + maxProgress);
			curProgress = (int) ((360-colorHSV[0]) * maxProgress / 360);
			break;

		case SeekBarType.WHITE:
			curProgress = value;
			break;
			
		case SeekBarType.BRIGHT:
			curProgress = value*maxProgress/16;
			break;
		}
		invalidate();
	}

	private boolean isTouchProgress(int x, int y) {
		return enableSeek;
	}
	
	public boolean isEnableSeek() {
		return enableSeek;
	}

	public void setEnableSeek(boolean enableSeek) {
		this.enableSeek = enableSeek;
	}

	public int getSeekbarType() {
		return mSeekbarType;
	}
	
	public int getValue(){
		if (mSeekbarType == SeekBarType.COLORFUL && mSeekBarBitmap != null) {
			int color = mSeekBarBitmap.getPixel(mProgressX, centerY);
			return color;
		} else {
			if (mSeekbarType == SeekBarType.BRIGHT) {
				return (curProgress+8)/16;
			}
			return curProgress;
		}
	}

	private OnProgressListener mOnProgressListener;
	
	public OnProgressListener getOnProgressListener() {
		return mOnProgressListener;
	}

	public void setOnProgressListener(OnProgressListener mOnProgressListener) {
		this.mOnProgressListener = mOnProgressListener;
	}

	public interface OnProgressListener {
		void onProgressChange(int type, int val);
		void onProgressChanged(int type, int val);
	}
	
	public static interface SeekBarType {
		int COLORFUL = 0;
		int WHITE = 1;
		int BRIGHT = 2;
	}
}
