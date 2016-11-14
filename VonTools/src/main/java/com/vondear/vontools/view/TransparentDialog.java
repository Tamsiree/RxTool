package com.vondear.vontools.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.vondear.vontools.R;


public class TransparentDialog extends Dialog {
	private LayoutParams attr;
	
	public LayoutParams getAttr() {
		return attr;
	}

	public TransparentDialog(Context context, int themeResId) {
		super(context, themeResId);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = 1f;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = Gravity.CENTER;
		}
	}

	public TransparentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = 1f;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = Gravity.CENTER;
		}
	}

	public TransparentDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(
				R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = 1f;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = Gravity.CENTER;
		}
	}
	
	public TransparentDialog(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(
				R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = 1f;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = Gravity.CENTER;
		}
	}

	/**
	 * 
	 * @param context
	 * @param alpha 透明度 0.0f--1f(不透明)
	 * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
	 */
	public TransparentDialog(Context context, float alpha, int gravity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(
				R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = alpha;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = gravity;
		}
	}

	/**
	 *
	 * @param context
	 * @param alpha 透明度 0.0f--1f(不透明)
	 * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
	 */
	public TransparentDialog(Context context, float alpha, int gravity, boolean isOnScreen) {
		super(context, android.R.style.Theme);
		setOwnerActivity((Activity)context);
		// TODO Auto-generated constructor stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.alpha = alpha;
		window.setAttributes(lp);
		attr = window.getAttributes();
		if (attr != null) {
			attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			attr.gravity = gravity;
		}
	}

	/**
	 * 隐藏头部导航栏状态栏
	 */
	public void skipTools() {
		if (Build.VERSION.SDK_INT < 19) {
			return;
		}
		getWindow().setFlags(
				LayoutParams.FLAG_FULLSCREEN,
				LayoutParams.FLAG_FULLSCREEN);
	}
}
