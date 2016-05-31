package com.ionesmile.variousdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.PixelUtil;

public class BrightView extends View {
	
    private int maxProgress = 360;
    private Paint bgArcPaint;
    private Paint progressPaint;

    private float arcWidth;
    private float progressWidth;
    private int progressColor;
    
    private int mLightLevel = 16;
    private Paint mLightDotPaint;
    
    private Drawable mThumb;
    private Drawable mHighIcon, mLowIcon;
    
    private float verticalOffset = 100;
	
	public BrightView(Context context) {
		this(context, null);
	}

	public BrightView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BrightView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}
	
	private int mBackground = 0xFFEFEFEF;
	private float mDotRadius = 5;
	
	private void init(Context context, AttributeSet attrs) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.brightView);
		arcRadius = mTypedArray.getDimension(R.styleable.brightView_radianRadius, getResources().getDisplayMetrics().heightPixels/3*2);
		mDotRadius = mTypedArray.getDimension(R.styleable.brightView_dotRadius, mDotRadius);
        mLightLevel = mTypedArray.getInteger(R.styleable.brightView_dotNumber, mLightLevel);
        mBackground = mTypedArray.getColor(R.styleable.brightView_viewBackground, mBackground);
		mTypedArray.recycle();
		
        arcWidth = PixelUtil.dp2px(6, getContext()) + 10;
        progressWidth = arcWidth;
		
		bgArcPaint = new Paint();
        int color = Color.TRANSPARENT;
        bgArcPaint.setColor(color);
        bgArcPaint.setAntiAlias(true);
        bgArcPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        bgArcPaint.setStyle(Paint.Style.FILL);
        bgArcPaint.setStrokeWidth(2);
        
        
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        
        mLightDotPaint = new Paint();
        mLightDotPaint.setAntiAlias(true);
        mLightDotPaint.setColor(0xFF9A9A9A);
        mLightDotPaint.setStyle(Style.FILL_AND_STROKE);
        mLightDotPaint.setStrokeWidth(10);
        
        // 加载拖动图标  
	    mThumb = getResources().getDrawable(R.drawable.img_lamp_brightness);// 圆点图片  
	    int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;  
	    int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;  
	    mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    
	    mHighIcon = getResources().getDrawable(R.drawable.img_lamp_brightness_high);
	    thumbHalfheight = mHighIcon.getIntrinsicHeight() / 2;  
	    thumbHalfWidth = mHighIcon.getIntrinsicWidth() / 2;  
	    mHighIcon.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	    
	    mLowIcon = getResources().getDrawable(R.drawable.img_lamp_brightness_low);
	    thumbHalfheight = mLowIcon.getIntrinsicHeight() / 2;  
	    thumbHalfWidth = mLowIcon.getIntrinsicWidth() / 2;  
	    mLowIcon.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);  
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(compositeImages(getWidth(), getHeight()), new Matrix(), new Paint());
		
		drawLightDots(canvas);
		
		drawLightIcon(canvas);
		
		drawThumb(canvas);
    }
	
	private void drawThumb(Canvas canvas) {
		canvas.save();  
		PointF point = ChartUtil.calcArcEndPointXY(centerX, 0, arcRadius, 360 * currentProgress / maxProgress, 270);
        canvas.translate(point.x, point.y + arcOffset - arcRadius + verticalOffset);  
        mThumb.draw(canvas);  
        canvas.restore();  
	}

	private void drawLightIcon(Canvas canvas) {
		canvas.save();  
        canvas.translate(getWidth() - mHighIcon.getIntrinsicWidth(), mHighIcon.getIntrinsicHeight());  
        mHighIcon.draw(canvas);  
        canvas.restore();  
        
        canvas.save();  
        canvas.translate(mLowIcon.getIntrinsicWidth(), mLowIcon.getIntrinsicHeight());  
        mLowIcon.draw(canvas);  
        canvas.restore();  
	}

	private void drawLightDots(Canvas canvas) {
		float perDegree = sweepDegree / (mLightLevel + 1);
		int dotCount = (int) ((sweepDegree - (360 * currentProgress / maxProgress - startDegree - 90))/sweepDegree * (mLightLevel + 1));
		for (int i = 0; i < dotCount; i++) {
			PointF point = ChartUtil.calcArcEndPointXY(centerX, 0, arcRadius + 30, startDegree + sweepDegree - (i+1)*perDegree);
			//Log.i("myTag", "drawLightDots() perDegree = " + perDegree + "     x = " + point.x + "    y = " + point.y + "   sweepDegree = " + sweepDegree + "    startDegree = " + startDegree);
			canvas.drawCircle(point.x, point.y + arcOffset - arcRadius + verticalOffset, mDotRadius, mLightDotPaint);
			//Log.i("myTag", "drawLightDots() arcOffset = " + arcOffset + "     getHeight = " + getHeight() + "    bottom = " + (point.y - arcOffset));
		}
	}

	private Bitmap compositeImages(int width, int height){   
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
        Paint paint = new Paint();   
       Canvas canvas = new Canvas(bmp); 
        canvas.setBitmap(bmp);
        canvas.drawColor(mBackground);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));   
        canvas.drawArc(fermodeRect, startDegree, sweepDegree, false, paint);
        
        canvas.drawRect(new RectF(0, 0, width, verticalOffset), paint);
        return bmp;   
  }  
	
	private boolean downOnArc = false; 
	
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            if (isTouchArc(x, y)) {  
                downOnArc = true;  
                updateArc(x, y);  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (downOnArc) {  
                updateArc(x, y);  
                return true;  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
            downOnArc = false;  
            invalidate();  
            if (changeListener != null) {  
            	changeListener.onBrightChangeEnd((int) (255 - (currentProgress - startDegree - 90)/sweepDegree * 255), false);
            }  
            break;  
        }  
        return super.onTouchEvent(event);  
    }  
    

    // 根据点的位置，更新进度  
    private void updateArc(int x, int y) {  
        int cx = (int) (x - centerX);  
        int cy = (int) (y + arcRadius - arcOffset - verticalOffset);
        
        // 计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
	    double angle = Math.atan2(cy, cx)/Math.PI;
	   // Log.i("myTag", "updateArc() angle = " + angle + "   x = " + x + "   y = " + y);
	    // 将角度转换成（0->2）之间的值，然后加上90°的偏移量
	    angle = ((2 + angle)%2 + (90/180f))%2;
	    // 用（0->2）之间的角度值乘以总进度，等于当前进度 
	    currentProgress = (int) (angle * maxProgress/2);
	    if (currentProgress > startDegree + sweepDegree + 90) {
			currentProgress = (int) (startDegree + sweepDegree + 90);
		}
	    if (currentProgress < startDegree + 90) {
			currentProgress = (int) (startDegree + 90);
		}
	   // Log.i("myTag", "updateArc() after angle = " + angle + "   progress = " + currentProgress);
        
        if (changeListener != null) {
        	changeListener.onBrightChange((int) (255 - (currentProgress - startDegree - 90)/sweepDegree * 255), false);
        }  
        invalidate();  
    }  
  
    private int minValidateTouchArcRadius; // 最小有效点击半径  
    private int maxValidateTouchArcRadius; // 最大有效点击半径  
    // 判断是否按在圆边上  
    private boolean isTouchArc(int x, int y) {  
        double d = getTouchRadius(x, y);  
        Log.i("myTag", "isTouchArc() d = " + d + "     minValidateTouchArcRadius = " + minValidateTouchArcRadius + "   maxValidateTouchArcRadius = " + maxValidateTouchArcRadius);
        if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {  
            return true;  
        }  
        return false;  
    }  
      
    // 计算某点到圆点的距离  
    private double getTouchRadius(int x, int y) {  
        int cx = x - getWidth() / 2;  
        //int cy = y - getHeight() / 2;  
        int cy = (int) (-y + arcOffset - arcRadius + verticalOffset);
        cy = Math.abs(cy);
        Log.i("myTag", "getTouchRadius() cy = " + cy);
        return Math.hypot(cx, cy);  
    } 
	
	private float centerX, arcRadius;
	private float startDegree, sweepDegree;
	private double radians = -1;
    private RectF fermodeRect;
	
    private float arcOffset;
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		centerX = getWidth()/2;
		
		radians = Math.asin(centerX / arcRadius);
        float degree = (float) Math.toDegrees(radians);
        startDegree = 90 - degree;
        sweepDegree = 2 * degree;
        currentProgress = (int) (startDegree + 90);
        
        float left = centerX - arcRadius;
        float right = centerX + arcRadius;
        
        arcOffset = (float)(arcRadius - Math.cos(radians)* arcRadius);
        
        float top = arcOffset + verticalOffset - 2* arcRadius;
        float bottom = arcOffset + verticalOffset;
        //arcRect = new RectF(left + halfArcWidth, top + halfArcWidth, right - halfArcWidth, bottom - halfArcWidth);
        
        double bigRadius = Math.hypot(centerX, arcRadius);
        float offset = (float)(bigRadius - arcRadius);
        offset = 0;
        //Log.i("myTag", "bigRadius = " + bigRadius + "      centerX = " + centerX + "    offset = " + offset);
        fermodeRect = new RectF(left-offset, top-offset, right+offset, bottom+offset);
        
        minValidateTouchArcRadius = (int) (arcRadius - mThumb.getIntrinsicWidth()*0.8f);  
        maxValidateTouchArcRadius = (int) (arcRadius + mThumb.getIntrinsicWidth()*0.8f);  
	}
	
	 private int currentProgress;
    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress){
        progress = Math.min(progress, maxProgress);
        currentProgress = Math.max(0, progress);
        invalidate();
    }
    
    public void setBright(int bright){
    	if (bright < 0) {
			bright = 0;
		}
    	if (bright > 255) {
			bright = 255;
		}
    	currentProgress = (int) (bright/255.0f*sweepDegree + startDegree);
    }
    
    public static class ChartUtil {  
        
        /** 
         * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标   
         * @param cirX 
         * @param cirY 
         * @param radius 
         * @param cirAngle 
         * @return 
         */  
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle){    
            float posX = 0.0f;    
            float posY = 0.0f;    
            //将角度转换为弧度          
            float arcAngle = (float) (Math.PI * cirAngle / 180.0);    
            if (cirAngle < 90)    
            {    
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;    
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;    
            }    
            else if (cirAngle == 90)    
            {    
                posX = cirX;    
                posY = cirY + radius;    
            }    
            else if (cirAngle > 90 && cirAngle < 180)    
            {    
                arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);    
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;    
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;    
            }    
            else if (cirAngle == 180)    
            {    
                posX = cirX - radius;    
                posY = cirY;    
            }    
            else if (cirAngle > 180 && cirAngle < 270)    
            {    
                arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);    
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;    
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;    
            }    
            else if (cirAngle == 270)    
            {    
                posX = cirX;    
                posY = cirY - radius;    
            }    
            else   
            {    
                arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);    
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;    
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;    
            }    
            return new PointF(posX, posY);          
        }    
          
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle){  
            cirAngle = (orginAngle + cirAngle) % 360;  
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);  
        }  
    }

    
    
    private OnBrightChangeListener changeListener;  
  
    public void setOnBrightChangeListener(OnBrightChangeListener changeListener) {  
        this.changeListener = changeListener;  
    }
  
    public interface OnBrightChangeListener {  
        void onBrightChange(int bright, boolean fromUser);  
  
        void onBrightChangeEnd(int bright, boolean fromUser);  
    }  
}
