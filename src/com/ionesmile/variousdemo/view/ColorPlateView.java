package com.ionesmile.variousdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import com.ionesmile.variousdemo.R;

public class ColorPlateView extends ImageView {
	
    private static final float MAX_SWEEP_ANGLE = 270;
    private static final float START_ANGLE = 135;
	private static final String TAG = ColorPlateView.class.getSimpleName();
	
	private Paint progressBgPaint;
	private RectF arcRect;
	
	public ColorPlateView(Context context) {
		this(context, null);
	}

	public ColorPlateView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorPlateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}
	
	// 是否是白灯，true白灯，false彩灯
	private boolean isColdWarmLamp = false;
	
	// 初始化渲染
    protected void onInitCircle(){
        if (!isColdWarmLamp) {
        	// 颜色值的角度
        	int[] colorAngle = new int[]{360, 330, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 0};
        	// 每个角度对应的位置，生成方式见最底部
//        	float[] positions = new float[]{0.125f, 0.1875f, 0.25f, 0.3125f, 0.375f, 0.4375f, 0.5f, 0.5625f, 0.625f, 0.6875f, 0.75f, 0.8125f, 0.875f};
        	// 每个角度对应的位置，生成方式见最底部
//        	float[] positions = new float[]{0.125f, 0.1875f, 0.25f, 0.3125f, 0.375f, 0.4375f, 0.5f, 0.5717592f, 0.64351857f, 0.7152778f, 0.787037f, 0.8587963f, 0.9305556f};
        	float[] positions = new float[]{0.125f, 0.1875f, 0.25f, 0.3125f, 0.375f, 0.4375f, 0.5f, 0.5648148f, 0.6296296f, 0.6944444f, 0.7592593f, 0.8240741f, 0.8888889f};
            int[] colors = new int[colorAngle.length];
            float hsv[] = new float[] { 0f, 1f, 1f };
            for (int i = 0; i < colors.length; i++) {
                // 根据HSV色盘，旋转的角度取色
                hsv[0] = colorAngle[i];
                colors[i] = Color.HSVToColor(hsv);
            }
            SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, positions);
            progressBgPaint.setShader(sweepGradient);
        } else {
        	// 当有冷暖灯时，绘制从白到黄渐变，否者处理为灰色的不可用色圈
        	if (hasWarmLamp) {
        		int wramWhite1 = Color.HSVToColor(new float[]{49, 1, 30});
                int wramWhite2 = Color.HSVToColor(new float[]{49, 1, 60});
                int wramWhite3 = Color.HSVToColor(new float[]{49, 1, 96});
                int[] colors = new int[]{wramWhite3, wramWhite3, Color.WHITE, wramWhite1, wramWhite2}; 
                float[] positions = new float[]{0.0f, 0.25f, 0.375f, 0.75f, 1f};
                SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, positions);
                progressBgPaint.setShader(sweepGradient);
			} else {
				progressBgPaint.setShader(null);
				progressBgPaint.setColor(0xFFFFFBE5);
			}
        }
    }
	
    // 是否有冷暖灯
    private boolean hasWarmLamp = false;
    
	public boolean isHasWarmLamp() {
		return hasWarmLamp;
	}

	/**
	 * 设置是否有冷暖灯
	 * @param hasWarmLamp	true 有冷暖灯			false 无
	 * @param triggerListener	是否触发onColorChangeEnd()监听
	 */
	public void setHasWarmLamp(boolean hasWarmLamp, boolean triggerListener) {
		this.hasWarmLamp = hasWarmLamp;
		if (isColdWarmLamp) {
			onInitCircle();
			mArcBitmap = getArcBitmap(getWidth(), getHeight());
			// 当没有冷暖灯，并且显示为白灯时，设置不可用色盘的图片
			if (!hasWarmLamp && isColdWarmLamp) {
				setImageResource(R.drawable.img_lamp_circle_knob_unable);
			} else {
				setImageResource(R.drawable.img_lamp_circle_knob);
			}
			invalidate();
			if (triggerListener) {
				onColorChangeEnd(true);
			}
		}
	}

	/**
	 * 设置是彩灯还是白灯
	 * @param flag	true 白灯		false 彩灯
	 * @param triggerListener	是否触发onColorChangeEnd()监听
	 */
	public void setIsColdWarmLamp(boolean flag, boolean triggerListener){
		 if (flag != isColdWarmLamp) {
			isColdWarmLamp = flag;
			onInitCircle();
			// 创建一个渲染后的Bitmap
			mArcBitmap = getArcBitmap(getWidth(), getHeight());
			// 当没有冷暖灯，并且显示为白灯时，设置不可用色盘的图片
			if (!hasWarmLamp && isColdWarmLamp) {
				setImageResource(R.drawable.img_lamp_circle_knob_unable);
			} else {
				setImageResource(R.drawable.img_lamp_circle_knob);
			}
			invalidate();
			if (triggerListener) {
				onColorChangeEnd(true);
			}
		}
	 }
	 
	 public boolean isColdWarmLamp(){
		 return isColdWarmLamp;
	 }

    private Bitmap mArcBitmap;
    private Matrix mMatrix = new Matrix();
    private Paint mBasePaint = new Paint();
    
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mArcBitmap == null || mArcBitmap.getWidth() != getWidth()) {
			mArcBitmap = getArcBitmap(getWidth(), getHeight());
		}
		canvas.drawBitmap(mArcBitmap, mMatrix, mBasePaint);
    }
	
	private Bitmap getArcBitmap(int width, int height){
		// 判断是白灯还是彩灯，彩灯时需要多次转换
		if (isColdWarmLamp) {
			Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
	        Canvas canvas = new Canvas(bmp); 
	        canvas.drawArc(arcRect, START_ANGLE, MAX_SWEEP_ANGLE, false, progressBgPaint);
	        return bmp;
		} else {
			// 绘制一个渲染的圆
			Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
			Canvas canvas = new Canvas(bmp); 
			canvas.drawArc(arcRect, 0, 360, false, progressBgPaint);
			// 创建一个结果Bitmap
			Bitmap tagBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas tagCanvas = new Canvas(tagBitmap);
			Paint paint = new Paint();   
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			Matrix matrix = new Matrix();
			// 将Bitmap旋转90°，处理0°角颜色无法融为一体的样式问题
			matrix.setRotate(90, centerX, centerY);
			tagCanvas.drawBitmap(bmp, matrix, paint);
			// 绘制颜色环的外形，从135°开始，绘制270°
			Bitmap arcBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas arcCanvas = new Canvas(arcBitmap);
			paint.setStrokeWidth(progressWidth);
			paint.setStyle(Style.STROKE);
			paint.setStrokeCap(Cap.ROUND);
			arcCanvas.drawArc(arcRect, START_ANGLE, MAX_SWEEP_ANGLE, false, paint);
			// 设置合成模式，以bmp为底，arcBitmap为覆盖，生成一个arcBitmap形状的bmp图形
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));  
			tagCanvas.drawBitmap(arcBitmap, new Matrix(), paint);
			return tagBitmap;
		}
	}
    
	 private float centerX, centerY;
	 private float radius;
	 private float progressWidth = 10;
	 private float knobInterval = 50;
	 private float radius2;
	    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (wheelHeight == 0 || wheelWidth == 0) {
            wheelHeight = h;
            wheelWidth = w;
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            imageOriginal = ((BitmapDrawable) drawable).getBitmap();
            int drawableWidth = imageOriginal.getWidth();
            int drawableHeight = imageOriginal.getHeight();
            centerX = wheelWidth / 2;
            centerY = wheelHeight / 2;
            int drawableRadius = Math.min(drawableHeight, drawableWidth) / 2;
            radius = drawableRadius + progressWidth / 2f;
            progressBgPaint.setStrokeWidth(progressWidth);

            float translateX = (wheelWidth - drawableWidth) / 2;
            float translateY = (wheelHeight - drawableHeight) / 2;
            matrix.postTranslate(translateX, translateY);
            // this.setImageBitmap(imageOriginal);
            this.setImageMatrix(matrix);
            matrix.postRotate(-45, wheelWidth / 2, wheelHeight / 2);
            setImageMatrix(matrix);
            if (listener != null) {
                listener.onRoateChange(totalDegree, true);
            }
            radius2 = radius + knobInterval;
            arcRect = new RectF(centerX - radius2, centerY - radius2, centerX + radius2, centerY + radius2);
            onInitCircle();
        }
	}
	
    private int wheelHeight, wheelWidth;
    private Bitmap imageOriginal;
    private Matrix matrix;
    private RotateChangeListener listener;
    private float totalDegree;
    private Paint paint;

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.colorKnobView);
        progressWidth = mTypedArray.getDimension(R.styleable.colorKnobView_colorRingWidth, progressWidth);
        knobInterval = mTypedArray.getDimension(R.styleable.colorKnobView_knobInterval, knobInterval);
        isColdWarmLamp = mTypedArray.getBoolean(R.styleable.colorKnobView_iswhiteLight, isColdWarmLamp);
		mTypedArray.recycle();
    	
        this.setScaleType(ScaleType.MATRIX);
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        this.setOnTouchListener(new WheelTouchListener());


        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#e8e8e8"));

        progressBgPaint = new Paint();
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setColor(Color.GRAY);
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);
       
		progressBgPaint = new Paint();
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setDither(true);
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void setRotateChangeListener(RotateChangeListener listener) {
        this.listener = listener;
    }

    private float sweepAngle = 0;

    private boolean touchable = true;

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    /**
     * 对图片进行旋转
     *
     * @param degrees 旋转的角度
     */
    private void rotateWheel(float degrees) {
        float temp = degrees;
        // 在90度时出现问题
        if (degrees > MAX_SWEEP_ANGLE) {
            degrees = degrees - 360;
        } else if (degrees < -MAX_SWEEP_ANGLE) {
            degrees = 360 + degrees;
        }
        float sum = totalDegree + degrees;
        if(sum > MAX_SWEEP_ANGLE || sum < 0){
            return;
        }
        matrix.postRotate(temp, wheelWidth / 2, wheelHeight / 2);
        setImageMatrix(matrix);

        totalDegree = sum;
        totalDegree %= 360;
        if (totalDegree < 0) {
            totalDegree += 360;
        }
        sweepAngle = totalDegree;
        invalidate();
        if (listener != null) {
            listener.onRoateChange(totalDegree, true);
        }
        onColorChangeEnd(false);
    }

	private void onColorChangeEnd(boolean isEnd) {
		if(mColorListener != null && mArcBitmap != null){
        	/*PointF point = BrightView.ChartUtil.calcArcEndPointXY(centerX, centerY, radius2, getProgress() + 135);
        	int pixel = mArcBitmap.getPixel((int)point.x, (int)point.y);*/
			int pixel = getColor();
        	//获取颜色
        	int redValue = Color.red(pixel);
        	int blueValue = Color.blue(pixel);
        	int greenValue = Color.green(pixel);
        	if (isEnd) {
        		mColorListener.onColorChangeEnd(redValue, greenValue, blueValue);
			} else {
				mColorListener.onColorChange(redValue, greenValue, blueValue);
			}
        }
	}
	
	public int getColor(){
		float angle = sweepAngle;	// 135 ~ (360+45),  0-270
		// 以180°为中心点，乘以两边压缩比，色盘角度45-335的角度
		if (angle >= 135) {
			angle = (angle-135)*(180/140f) + 180;
		} else {
			angle = angle*(180/135f);
		}
		angle = 360 - angle;
		//angle = (angle + 270)%360;
		float[] hsv = new float[]{angle, 1, 1};
    	return Color.HSVToColor(hsv);
	}
	
	public int getBrightByWhiteLamp255(){
		int result = Math.round((255 - sweepAngle/MAX_SWEEP_ANGLE * 255));
    	// SimpleLog.printLog(this, "getBrightByWhiteLamp255() result = " + result);
		// 加上纯白或纯黄的缓冲区（255白，0黄）
		if (result > 252) {
			result = 255;
		} else if (result < 3) {
			result = 0;
		}
		return result;
	}
	
	public int getBrightByWhiteLamp16(){
		int bright1 = ((int) (255 - sweepAngle/MAX_SWEEP_ANGLE * 255) + 8)*16/255;
		return bright1;
	}
	
    /**
     * 此方法不会触发角度变化监听
     *
     * @param angle 取值范围0 -- 360
     */
    private void setAngle(float angle, boolean triggerListener) {
        setAngle(angle, false, triggerListener);
    }

    /**
     * @param angle    取值范围0 -- 360
     * @param callback 是否触发角度变化监听
     */
    private void setAngle(float angle, boolean callback, boolean triggerListener) {
        float delta = Math.abs(angle) % 360 - totalDegree;
        totalDegree = Math.abs(angle) % 360;
        if (wheelWidth == 0 || wheelHeight == 0) {
            return;
        }
        matrix.postRotate(delta, wheelWidth / 2, wheelHeight / 2);
        setImageMatrix(matrix);
        if (triggerListener) {
	    	 if (listener != null && callback) {
	             listener.onRoateChange(totalDegree, false);
	         }
	         onColorChangeEnd(false);
		}
    }

    /**
     * 触屏事件角度
     */
    private double getAngle(double x, double y) {
        x = x - (wheelWidth / 2d);
        y = wheelHeight - y - (wheelHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    /**
     * 根据点获取象限
     *
     * @return 1, 2, 3, 4
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    public interface RotateChangeListener {
        /**
         * 顺时针，0 ---> 360度，最顶点为0
         *
         * @param degress
         * @param fromUser 是否通过旋转的方式使角度改变,而不是通过{@link KnobView#setAngle(float)}}方法
         */
        public void onRoateChange(float degress, boolean fromUser);

        void onRotateChangeStart(float degree);

        void onRotateChangeEnd(float degree);
    }

    private class WheelTouchListener implements OnTouchListener {
        private double startAngle;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (!isTouchWheel((int) event.getX(), (int) event.getY())) {
                        return false;
                    }
                    startAngle = getAngle(event.getX(), event.getY());
                    if (listener != null) {
                        listener.onRotateChangeStart(totalDegree);
                    }
                    if (!touchable) {
                        return false;
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateWheel((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;
                case MotionEvent.ACTION_UP:
                    if (listener != null) {
                        listener.onRotateChangeEnd(totalDegree);
                    }
                    onColorChangeEnd(true);
                    break;
            }
            requestDisallowInterceptTouchEvent();
            return true;
        }
    }

    private boolean isTouchWheel(int x, int y) {
    	if (!hasWarmLamp && isColdWarmLamp) return false;
        double d = getTouchRadius(x, y);
        if (d < imageOriginal.getWidth() / 2) {
            return true;
        }
        return false;
    }

    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    private void requestDisallowInterceptTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private int mMax = (int) MAX_SWEEP_ANGLE;
    /**
     * 设置最大进度值
     */
    public void setMax(int max){
        mMax = max;
    }

    /**
     * 获取最大进度值
     * @return
     */
    public int getMax() {
        return mMax;
    }
    
    /**
     * 设置色盘冷暖色
     * @param brightness
     */
    public void setWhiteBright(int brightness){
        float ratio = 1 - brightness/255.0f;
    	sweepAngle = ratio * MAX_SWEEP_ANGLE;
        setAngle(sweepAngle, false);
        invalidate();
    }
    
    /**
     * 设置色盘颜色，更新UI
     * @param color
     */
    public void setColorful(int color){
    	float angle = getColorAngle(color);
    	sweepAngle = angle;
    	Log.i(TAG, "setColor() color = " + color + "    angle = " + angle);
    	setAngle(angle, false);
    	invalidate();
    }

    // 将颜色转换为角度
    private float getColorAngle(int color) {
    	// 第一步，转为HSV模型
    	float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		// 获取角度H（0-360）
		float angle = hsv[0];
		/*
		 * 修改最左边的颜色值与最右边颜色值一致，导致底部颜色（红色）没有一个固定角度的问题
		 * 现在处理为损失（0-(180/140f * 5)）这个角度的颜色，左半部分不变压缩为135°，右半部分压缩为140°（色盘上135°-140°颜色无法获取到，对应HSV的（0-6.428））
		 */
		if (angle <= 5) {
			angle = 360;
		}
		// 以180°为中心点，乘以两边压缩比，色盘角度45-335的角度
		if (angle >= 180) {
			angle = (angle-180)*(135/180f) + 180;
		} else {
			angle = (angle-180)*(140/180f) + 180;
		}
		// 转成0-270°
		angle -= 45;
		// 反转，与显示的图片对应
		angle = 270 - angle;
    	return angle;
    }

	/**
     * 获取当前进度
     * @return	0-270
     */
    public int getProgress() {
        float ratio = sweepAngle / MAX_SWEEP_ANGLE;
        int progress = (int)(mMax * ratio);
        return progress;
    }

    private OnColorChangeListener mColorListener;

    /**
     * 设置进度变化监听
     * @param listener
     */
    public void setOnColorChangeListener(OnColorChangeListener listener) {
        this.mColorListener = listener;
    }

    /**
     * 进度变化监听
     */
    public interface OnColorChangeListener{
    	/**
		 * 颜色改变
		 * @param red
		 * @param green
		 * @param blue
		 */
		void onColorChange(int red, int green, int blue);

		/**
		 * 颜色改变结束回调
		 * @param red
		 * @param green
		 * @param blue
		 */
		void onColorChangeEnd(int red, int green, int blue);
    }
    

	/*private static void printPlateAngleAndOffsetPlus() {
		int partNum = 13;	// 生成12 + 1个代表点
		int compressPoint = 180;	// 以中心点作为压缩中心
		// 生成角度原（[360, 330, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 0]）
		int[] sourceAngle = new int[partNum];
		for (int i = 0; i < sourceAngle.length; i++) {
			sourceAngle[i] = 360 - i*(360/(partNum-1));
		}
		System.out.println(Arrays.toString(sourceAngle));
		
		float percentLeft = 135/180f;
		float percentRight = 140/180f;
		int[] targetAngle = new int[partNum];
		for (int i = 0; i < sourceAngle.length; i++) {
			if (sourceAngle[i] >= 180) {
				targetAngle[i] = (int) ((sourceAngle[i]-compressPoint)*percentLeft + compressPoint);
			} else {
				targetAngle[i] = (int) ((sourceAngle[i]-compressPoint)*percentRight + compressPoint);
			}
		}
		System.out.println(Arrays.toString(targetAngle));
		
		
		float[] percentAngle = new float[partNum];
		for (int i = 0; i < percentAngle.length; i++) {
			if (sourceAngle[i] >= 180) {
				percentAngle[i] = 1 - (((sourceAngle[i]-compressPoint)*percentLeft + compressPoint)/360);
			} else {
				percentAngle[i] = 1 - (((sourceAngle[i]-compressPoint)*percentRight + compressPoint)/360);
			}
		}
		System.out.println(toStringF(percentAngle));
	}
	
	public static String toStringF(float[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "{}";

        StringBuilder b = new StringBuilder();
        b.append('{');
        for (int i = 0; ; i++) {
            b.append(a[i]).append("f");
            if (i == iMax)
                return b.append('}').toString();
            b.append(", ");
        }
    }*/
}
