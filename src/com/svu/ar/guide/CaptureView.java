/*
 ********** SVU **********
 ********** Barea_27786 **********
 *********** Capture View **********
 *********** Draw red Rectangle on Screen **********
 */

package com.svu.ar.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CaptureView extends View {

	private Paint paint;
	private int left, right, top, bottom;

	public CaptureView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
	}

	// Set dimensions of the rectangle
	public void Update(int width, int height) {
		int centerX = width / 2;
		int centerY = height / 2;
		int i = (centerX * 2) / 3;
		int j = (centerY * 2) / 3;
		left = centerX - i;
		right = centerX + i;
		top = centerY - j;
		bottom = centerY + j;
		invalidate();
	}

	public int getCaptureLeft() {
		return left;
	}

	public int getCaptureTop() {
		return top;
	}

	public int getcaptureWidth() {
		return right - left;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(left, top, right, bottom, paint);
	}
}
