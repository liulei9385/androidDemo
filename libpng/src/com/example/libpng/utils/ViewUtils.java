package com.example.libpng.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

public class ViewUtils {

	/**
	 * 转化dip为px
	 * 
	 * @param context
	 * @param value
	 * @return
	 */
	public static float dpToPx(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
				getScreenMetric(context));
	}

	/**
	 * 获取屏幕相关的参数
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenMetric(Context context) {
		return context.getResources().getDisplayMetrics();
	}

	/**
	 * 显示toast消息
	 * 
	 * @param context
	 * @param text
	 * @param isLong
	 */
	public static void showToast(Context context, String text, boolean isLong) {
		int duration = Toast.LENGTH_LONG;
		if (!isLong)
			duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * 判断view的id是否存在
	 * 
	 * @param activity
	 * @param resId
	 * @return
	 */
	public static boolean isViewExists(Activity activity, int resId) {
		return activity != null && activity.findViewById(resId) != null;
	}

	/**
	 * 获取颜色
	 * 
	 * @param context
	 * @param colorResId
	 * @return
	 */
	public static int getResColor(Context context, int colorResId) {
		return context.getResources().getColor(colorResId);
	}

}
