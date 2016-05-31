package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.ionesmile.variousdemo.R;

public class ColorTipImageView extends ImageView {

	
	private int mPaddingTop, mPaddingBottom;
	private int mRadius, mYOffset;
	
	private Paint mShadowPaint;
	private Paint mIconPaint;
	
	private Drawable mThumbNor;
	
	public ColorTipImageView(Context context) {
		this(context, null);
	}

	public ColorTipImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorTipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mShadowPaint = new Paint();
		mShadowPaint.setStrokeWidth(1);
		mShadowPaint.setColor(0x66FFFFFF);
		mShadowPaint.setStyle(Style.FILL);
		
		mIconPaint = new Paint();
		mIconPaint.setStyle(Style.FILL);
		
		// initializer Thumb Drawable
		mThumbNor = getResources().getDrawable(R.drawable.ic_fun_pic_color_bottom);
	    thumbHalfheight = mThumbNor.getIntrinsicHeight() / 2;  
	    thumbHalfWidth = mThumbNor.getIntrinsicWidth() / 2;  
	    mThumbNor.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    
	    int extraOffset = thumbHalfWidth;
	    mMaxLeftOffset = thumbHalfWidth + extraOffset;
	    mMaxTopOffset = mThumbNor.getIntrinsicHeight();

	    mRadius = thumbHalfWidth * 4 / 5;
	    mYOffset = thumbHalfWidth - thumbHalfheight;
	}
	
	private int thumbHalfheight, thumbHalfWidth;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Drawable drawable = getDrawable() ;  
        if (drawable == null) {
            return; // couldn't resolve the URI
        }
        
        if (getWidth() == 0 || getHeight() == 0) {
        	return;     // nothing to draw (empty bounds)
        }
		
		if(imageOriginal != null) {
			canvas.save();  
	        canvas.translate(mPointX, mPointY);
	        mThumbNor.draw(canvas);  
	        canvas.restore(); 
	        
        	mIconPaint.setColor(curColor);
	        canvas.drawCircle(mPointX, mPointY+mYOffset, mRadius, mIconPaint);
		}
		

		canvas.drawRect(0, 0, getWidth(), mPaddingTop, mShadowPaint);
		canvas.drawRect(0, getHeight()-mPaddingBottom, getWidth(), getHeight(), mShadowPaint);

		// 画指示点
		/*mIconPaint.setColor(Color.BLACK);
		canvas.drawCircle(mPointX, mPointY, 10, mIconPaint);*/
		
		// 画颜色点
	/*	mIconPaint.setColor(0xFFFF0000);
		canvas.drawCircle(mPointX, mPointY+thumbHalfheight, 10, mIconPaint);*/
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		initBitmap(getWidth(), getHeight());
	}
	
	private int mPointX, mPointY;
	private Bitmap imageOriginal;
	
	public int curColor;
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);


		mPaddingTop = width / 9;
		mPaddingBottom = mPaddingTop;
		initBitmap(width, height);
		
		if (width != oldw || height != oldh) {
			setPosition(thumbHalfWidth, thumbHalfheight + mPaddingTop, true);
		}
	}

	private void initBitmap(int width, int height) {
		Drawable drawable = getDrawable();
         if (drawable == null) {
             return;
         }
         imageOriginal = ((BitmapDrawable) drawable).getBitmap();
         curColor = getCurrentColor(imageOriginal, width, height);
	}
	
	private int getCurrentColor(Bitmap bitmap, int width, int height) {
		if (width > 0 && height > 0) {
			int bmpWidth = bitmap.getWidth();
			int bmpHeight = bitmap.getHeight();
			int pty = mPointY+thumbHalfheight;
			int x = mPointX * bmpWidth / width;
			int y = pty * bmpHeight / height;
			// IllegalArgumentException: x must be < bitmap.width()
			x = x > bmpWidth-1 ? bmpWidth-1 : x;
			curColor = bitmap.getPixel(x, y);
			return curColor;
		} else {
			return 0xFFFFFFFF;
		}
	}

	private boolean mEnableTouch = true;
	private boolean isTouched = false;
	
	// 记录在按下时与所选点的偏移值，根据偏移值来相对移动
	private int mLeftOffset, mTopOffset; 
	// 最大偏移值,只有点击在偏移范围内，才跟随相对移动
	private int mMaxLeftOffset, mMaxTopOffset;	
	
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            if (isTouched(x, y)) {  
            	isTouched = true;
            	mLeftOffset = mPointX - x;
            	mTopOffset = mPointY - y;
            	// 当偏移量左右超过mMaxLeftOffset，或者上大于mMaxTopOffset，就清空偏移（即左右超过图片两边，上超过图片顶部，下超过点）
            	if (mMaxLeftOffset < Math.abs(mLeftOffset) || mMaxTopOffset < mTopOffset || mTopOffset < 0) {
            		mLeftOffset = 0;
            		//mTopOffset = 0;
            		mTopOffset = -thumbHalfheight;
            	}
                updatePositon(x, y);
                return true;
            }
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (isTouched) {  
                updatePositon(x, y);
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
        	isTouched = false;
    		curColor = getCurrentColor(imageOriginal, getWidth(), getHeight());
            invalidate();  
            if (mOnChangeListener != null) { 
            	mOnChangeListener.onChanged(curColor);
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
	
	private void updatePositon(int x, int y) {
		y = (y < mPaddingBottom-thumbHalfheight-mTopOffset ? mPaddingBottom-thumbHalfheight-mTopOffset : y);
		y = y > (getHeight()-mPaddingBottom-thumbHalfheight-mTopOffset) ? (getHeight()-mPaddingBottom-thumbHalfheight-mTopOffset) : y;
		x = (x < -mLeftOffset ? -mLeftOffset : x);
		x = (x > getWidth()-mLeftOffset ? getWidth()-mLeftOffset : x);
		mPointX = x + mLeftOffset;
		mPointY = y + mTopOffset;
		curColor = getCurrentColor(imageOriginal, getWidth(), getHeight());
		invalidate();
		if (mOnChangeListener != null) {  
			mOnChangeListener.onChange(curColor);
        } 
	}

	private boolean isTouched(int x, int y) {
		return mEnableTouch && imageOriginal != null && (y >= mPaddingTop && y <= getHeight()-mPaddingBottom);
	}
	
	/**
	 * 设置点的位置
	 * @param x
	 * @param y
	 * @param callback 是否触发onChanged()回调
	 */
	public void setPosition(int x, int y, boolean callback){
		y = (y < mPaddingBottom-thumbHalfheight-mTopOffset ? mPaddingBottom-thumbHalfheight-mTopOffset : y);
		y = y > (getHeight()-mPaddingBottom-thumbHalfheight-mTopOffset) ? (getHeight()-mPaddingBottom-thumbHalfheight-mTopOffset) : y;
		x = (x < -mLeftOffset ? -mLeftOffset : x);
		x = (x > getWidth()-mLeftOffset ? getWidth()-mLeftOffset : x);
		mPointX = x + mLeftOffset;
		mPointY = y + mTopOffset;
		curColor = getCurrentColor(imageOriginal, getWidth(), getHeight());
		invalidate();
		if (mOnChangeListener != null && callback) {  
			mOnChangeListener.onChanged(curColor);
        } 
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
