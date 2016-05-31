package com.ionesmile.variousdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WifiLampColorView extends View {
	public static String TAG = "ColorPicker";
	/**
	 * Customizable display parameters (in percents)
	 */
	private final int paramOuterPadding = 0; // 弧形的外边距占控件的百分比
	private final int paramInnerPadding = 40; // 内弧的外边距占控件的百分比
	private final int paramValueSliderWidth = 6;// 外圆的宽度百分比
	private final int paramValueInnerWidth = 4; // 内面半圆的宽度百分比
	protected Paint valueSliderPaint;// 绘制拖动条

	private Path valueInnerSliderPath; // 内部滑动条的绘制路径
	private Paint valueInnerSliderPaint; // 内部滑动条的笔

	private Paint outerEdgePaint; // 最外面描边的笔

	private int outerPadding;

	private int paddingOuterThumb; // 外圆Thumb的padding
	private int paddingInnerThumb; // 内圆Thumb的padding

	private RectF outerWheelRect;

	private int mThumbXPos, mThumbYPos; // 外圆色盘Thumb Position
	private int mInnerThumbXPos, mInnerThumbYPos; // 内环明暗Thumb Position
	private float thumbRadius;// 滑动的半径
	private float thumbInnerRadius;// 滑动的半径

	/** Currently selected color */
	protected float[] colorHSV = new float[] { 0, 1f, 1f };

	private boolean isColdWarmLamp = false; // 是否是冷白

	public WifiLampColorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WifiLampColorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WifiLampColorView(Context context) {
		super(context);
		init();
	}

	private void init() {
		valueSliderPaint = new Paint();
		valueSliderPaint.setAntiAlias(true);
		valueSliderPaint.setDither(true);
		valueSliderPaint.setStyle(Paint.Style.STROKE);

		outerEdgePaint = new Paint();
		outerEdgePaint.setAntiAlias(true);
		outerEdgePaint.setDither(true);
		outerEdgePaint.setStyle(Paint.Style.STROKE);
		outerEdgePaint.setColor(0xAAFFFFFF);
		outerEdgePaint.setStrokeWidth(1);

		valueInnerSliderPaint = new Paint();
		valueInnerSliderPaint.setAntiAlias(true);
		valueInnerSliderPaint.setDither(true);

		outerWheelRect = new RectF();
		valueInnerSliderPath = new Path();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);// 设定自定义组建大小
		radius();// //设定进度条弧度
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(Color.BLACK);

		// drawing outer circle colors dish
		canvas.drawArc(outerWheelRect, 0, 360, false, valueSliderPaint);

		canvas.drawCircle(centerX, centerY,
				thumbRadius + paddingOuterThumb / 2, outerEdgePaint);

		// drawing inner arc
		canvas.drawPath(valueInnerSliderPath, valueInnerSliderPaint);
	}

	@Override
	public void invalidate() {
		updateInnerSliderPaintGradient();
		super.invalidate();
	}

	// 修改内部滑动条的大小
	private void updateInnerSlidePath() {
		valueInnerSliderPath.reset();
		int outerWheelRadius = (int) (thumbRadius * (100 - paramInnerPadding) / 100);
		int innerWheelRadius = (int) (outerWheelRadius - (thumbRadius
				* paramValueInnerWidth / 100));
		thumbInnerRadius = (outerWheelRadius + innerWheelRadius) / 2;
		// 画一个外面的半圆，从180度，画180度（到360度）
		RectF outerWheelRect = new RectF(centerX - outerWheelRadius, centerY
				- outerWheelRadius, centerX + outerWheelRadius, centerY
				+ outerWheelRadius);
		valueInnerSliderPath.arcTo(outerWheelRect, 180, 180);
		// 画一个内半圆，从0度逆时针画180度，以四个角形成一个完整的弧
		RectF innerWheelRect = new RectF(centerX - innerWheelRadius, centerY
				- innerWheelRadius, centerX + innerWheelRadius, centerY
				+ innerWheelRadius);
		valueInnerSliderPath.arcTo(innerWheelRect, 0, -180);

		minValidateTouchInnerArcRadius = (int) (thumbInnerRadius - paddingOuterThumb);
		maxValidateTouchInnerArcRadius = (int) (thumbInnerRadius + paddingOuterThumb);
		maxYInnerArcRange = centerY + paddingOuterThumb;

		updateInnerSliderPaintGradient();
	}

	// 修改内部选择笔的SweepGradient
	private void updateInnerSliderPaintGradient() {
		if (!isColdWarmLamp) {
			float[] hsv = new float[] { colorHSV[0], colorHSV[1], 1f };
			SweepGradient sweepGradient = new SweepGradient(
					centerX,
					centerY,
					new int[] { Color.BLACK, Color.BLACK, Color.HSVToColor(hsv) },
					null);
			valueInnerSliderPaint.setShader(sweepGradient);
		} else {
			SweepGradient sweepGradient = new SweepGradient(centerX, centerY,
					new int[] { Color.BLACK, Color.BLACK, Color.WHITE }, null);
			valueInnerSliderPaint.setShader(sweepGradient);
		}
	}

	private int centerX, centerY;

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		centerX = width / 2;
		centerY = height / 2;
		int min = Math.min(centerX, centerY);

		outerPadding = (paramOuterPadding * width / 100);

		thumbRadius = min - outerPadding - paddingOuterThumb;

		outerWheelRect.set(centerX - thumbRadius, centerY - thumbRadius,
				centerX + thumbRadius, centerY + thumbRadius);

		float arcWidth = thumbRadius * paramValueSliderWidth / 100;
		valueSliderPaint.setStrokeWidth(arcWidth);

		minValidateTouchArcRadius = (int) (thumbRadius - paddingOuterThumb);
		maxValidateTouchArcRadius = (int) (thumbRadius + paddingOuterThumb);

		updateInnerSlidePath();

		onInitCircle();
		radius();
	}

	private int colors[];

	protected void onInitCircle() {
		if (!isColdWarmLamp) {
			int colorCount = 12;
			int colorAngleStep = 360 / 12;
			colors = new int[colorCount + 1];
			float hsv[] = new float[] { 0f, 1f, 1f };
			for (int i = 0; i < colors.length; i++) {
				// 根据HSV色盘，旋转的角度取色
				// hsv[0] = (-i * colorAngleStep + 270 + 360) % 360;
				hsv[0] = (i * colorAngleStep + 180) % 360;
				colors[i] = Color.HSVToColor(hsv);
			}
			SweepGradient sweepGradient = new SweepGradient(getWidth() / 2,
					getHeight() / 2, colors, null);
			valueSliderPaint.setShader(sweepGradient);
		} else {
			float[] hsv = { 49, 1, 96 };
			LinearGradient linearGradient = new LinearGradient(0, 0, 0,
					getHeight(),
					new int[] { Color.HSVToColor(hsv), Color.WHITE }, null,
					Shader.TileMode.REPEAT);
			valueSliderPaint.setShader(linearGradient);
		}
	}

	private void radius() {
		updateThumbPosition();
		updateInnerThumbPosition();
	}

	private boolean downOnArc = false;
	private boolean downOnInnerArc = false;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (isTouchArc(x, y)) {
				downOnArc = true;
				updateArc(event, x, y);
				return true;
			} else if (isTouchInnerArc(x, y)) {
				downOnInnerArc = true;
				updateInnerArc(event, x, y);
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (downOnArc) {
				updateArc(event, x, y);
				return true;
			} else if (downOnInnerArc) {
				updateInnerArc(event, x, y);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downOnArc = false;
			downOnInnerArc = false;
			invalidate();
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 更新色盘
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 */
	private void updateWheelColor(int x, int y) {
		int cx = x - getWidth() / 2;
		int cy = y - getHeight() / 2;
		// colorHSV[0] = (float) (Math.toDegrees(Math.atan2(cy, cx)) + 360) %
		// 360;
		colorHSV[0] = (float) (Math.toDegrees(Math.atan2(cy, cx)) + 180f);
		colorHSV[1] = 1;
		colorHSV[2] = 255;
	}

	/**
	 * 更新色盘
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 */
	private void updateInnerWheelColor(int x, int y) {
		int cx = x - getWidth() / 2;
		int cy = y - getHeight() / 2;
		cy = Math.abs(Math.min(0, cy));
		colorHSV[2] = (float) Math.max(0,
				Math.min(1, 1 - Math.atan2(cy, cx) / Math.PI));
	}

	/**
	 * 更新进度
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 */
	private void updateArc(MotionEvent event, int x, int y) {
		updateWheelColor(x, y);
		updateThumbPosition();
		invalidate();
	}

	/**
	 * 更新内圆进度
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 */
	private void updateInnerArc(MotionEvent event, int x, int y) {
		updateInnerWheelColor(x, y);
		updateInnerThumbPosition();
		invalidate();
	}

	private void updateThumbPosition() {
		float hueAngle = (float) Math.toRadians(colorHSV[0]);
		mThumbXPos = (int) (Math.cos(hueAngle) * thumbRadius);
		mThumbYPos = (int) (Math.sin(hueAngle) * thumbRadius);
	}

	private void updateInnerThumbPosition() {
		float hueAngle = (float) ((colorHSV[2]) * Math.PI);
		mInnerThumbXPos = (int) (Math.cos(hueAngle) * thumbInnerRadius);
		mInnerThumbYPos = (int) (Math.sin(hueAngle) * thumbInnerRadius);
	}

	private int minValidateTouchArcRadius, maxValidateTouchArcRadius; // 外圆最小、最大有效点击半径
	private int minValidateTouchInnerArcRadius, maxValidateTouchInnerArcRadius; // 内圆最小、最大有效点击半径

	private boolean isTouchArc(int x, int y) {
		double d = getTouchRadius(x, y);
		if (d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius) {
			return true;
		}
		return false;
	}

	private int maxYInnerArcRange;

	private boolean isTouchInnerArc(int x, int y) {
		double d = getTouchRadius(x, y);
		if (y < maxYInnerArcRange && d >= minValidateTouchInnerArcRadius
				&& d <= maxValidateTouchInnerArcRadius) {
			return true;
		}
		return false;
	}

	/**
	 * 点击点距离中心点的距离
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private double getTouchRadius(int x, int y) {
		int cx = x - getWidth() / 2;
		int cy = y - getHeight() / 2;
		return Math.hypot(cx, cy);
	}

	/**
	 * 设置颜色
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		Color.colorToHSV(color, colorHSV);
		updateThumbPosition();
		invalidate();
	}

	/**
	 * 获取当前颜色
	 * 
	 * @return
	 */
	public int getColor() {
		return Color.HSVToColor(colorHSV);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle state = new Bundle();
		state.putFloatArray("color", colorHSV);
		state.putParcelable("super", super.onSaveInstanceState());
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			colorHSV = bundle.getFloatArray("color");
			super.onRestoreInstanceState(bundle.getParcelable("super"));
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	public boolean isColdWarmLamp() {
		return isColdWarmLamp;
	}

	public void setColdWarmLamp(boolean isColdWarmLamp) {
		this.isColdWarmLamp = isColdWarmLamp;
		onInitCircle();
		invalidate();
	}

	private OnColorChangeListener mChangeListener;

	public void setOnColorChangeListener(OnColorChangeListener listener) {
		this.mChangeListener = listener;
	}

	public static interface OnColorChangeListener {
		/**
		 * 颜色改变
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 */
		void onColorChange(boolean fromUser, int red, int green, int blue);

		/**
		 * 颜色改变结束回调
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 */
		void onColorChangeEnd(boolean fromUser, int red, int green, int blue);
	}

}
