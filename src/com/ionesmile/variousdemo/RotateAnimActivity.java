package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

public class RotateAnimActivity extends Activity {

	private static final String TAG = RotateAnimActivity.class.getSimpleName();
	private ImageView ivRotate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rotate_anim);
		
		ivRotate = (ImageView) findViewById(R.id.iv_rotate);
		ViewHelper.setRotation(ivRotate, currentDegree);
	}
	
	private boolean isAnim = false;
	
	public void rotateImage(View v){
		isAnim = !isAnim;
		lpRotateAnim(isAnim);
	}
	
	private ValueAnimator animator;
	private boolean animStart = false;
	private static float currentDegree;
	
	/**
	 * 胶片旋转动画
	 * 
	 * @param start
	 */
	private void lpRotateAnim(boolean start) {
		Log.i(TAG,  "lpRotateAnim() start = " + start + "   animStart = " + animStart);
		if (start && !animStart) {
			animStart = true;
			if (animator != null) {
				animator.removeAllUpdateListeners();
			}
			animator = ValueAnimator.ofFloat(0f, 360f);
			animator.setDuration(10 * 1000);
			animator.setRepeatMode(ValueAnimator.RESTART);
			animator.setRepeatCount(ValueAnimator.INFINITE);
			animator.setInterpolator(new LinearInterpolator());
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					float degree = (Float) animator.getAnimatedValue();
					Log.v(TAG,  "onAnimationUpdate() degree = " + degree);
					if (animator.isRunning()) {
						ViewHelper.setRotation(ivRotate, degree + currentDegree);
					}
				}
			});
			animator.start();
		} else if (!start) {
			animStart = false;
			if (animator != null) {
				animator.removeAllUpdateListeners();
				animator.end();
				currentDegree = ViewHelper.getRotation(ivRotate);
			}
		}
	}
	
	/*private Animation operatingAnim;
	private void rotateMusicAnim(boolean start) {
		if (start && !animStart) {
			animStart = true;
			operatingAnim = AnimationUtils.loadAnimation(this, R.anim.music_rotate_anim);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			if (operatingAnim != null) {
				ivRotate.startAnimation(operatingAnim);
			}
		} else {
			//ViewHelper.setRotation(ivRotate, degree + ViewHelper.getRotation(ivRotate));
			ivRotate.clearAnimation();
			animStart = false;
		}
	}*/

}
