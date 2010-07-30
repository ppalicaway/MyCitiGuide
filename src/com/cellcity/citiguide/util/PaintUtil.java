package com.cellcity.citiguide.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;

public class PaintUtil {
	public static Paint FontBold(int size, int color, boolean shadow){
		Paint textPaint = new Paint();
		textPaint.setTextSize(size);
		textPaint.setColor(color);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		if(shadow)
			textPaint.setShadowLayer(2, 2, 2, color+15);
		return textPaint;
	}
	
	public static Paint FontDefault(int size, int color, boolean shadow){
		Paint textPaint = new Paint();
		textPaint.setTextSize(size);
		textPaint.setColor(color);

		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		if(shadow)
			textPaint.setShadowLayer(2, 2, 2, color+15);
		return textPaint;
	}
	
	public static Paint FontCustom(Context context, int size, int color, String path) {
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(size);
		textPaint.setColor(color);
		Typeface mFace = Typeface.createFromAsset(context.getAssets(), path);
		textPaint.setTypeface(mFace);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setAntiAlias(true);
		return textPaint;
	}
}
