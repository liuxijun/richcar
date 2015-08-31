package com.fortune.mobile.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;
import com.fortune.util.ULog;

public class MyVerticalSeekBar extends SeekBar {
	public static final String TAG = MyVerticalSeekBar.class.getSimpleName();

	private Drawable mThumb;

	public interface OnSeekBarChangeListener {
		void onProgressChanged(MyVerticalSeekBar verticalSeekBar, int progress, boolean fromUser);

		void onStartTrackingTouch(MyVerticalSeekBar verticalSeekBar);

		void onStopTrackingTouch(MyVerticalSeekBar verticalSeekBar);
	}

	private OnSeekBarChangeListener mOnSeekBarChangeListener;

	public MyVerticalSeekBar(Context context) {
		this(context, null);
	}

	public MyVerticalSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.seekBarStyle);
	}

	public MyVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;
	}

	void onStartTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStartTrackingTouch(this);
		}
	}

	void onStopTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStopTrackingTouch(this);
		}
	}

	void onProgressRefresh(float scale, boolean fromUser) {
		Drawable thumb = mThumb;
		if (thumb != null) {

			setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
			invalidate();
		}
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
		}
	}

	private void setThumbPos(int h, Drawable thumb, float scale, int gap) {
		ULog.d("h = " + h + ", scale = " + scale + ", gap = " + gap);
		int available = h - getPaddingLeft() - getPaddingRight();
		int thumbWidth = thumb.getIntrinsicWidth();
		int thumbHeight = thumb.getIntrinsicHeight();

		available -= thumbWidth;
		// The extra space for the thumb to move on the track
		available += getThumbOffset() * 2;
		int thumbPos = (int) (scale * available);
		int topBound, bottomBound;
		if (gap == Integer.MIN_VALUE) {
			Rect oldBounds = thumb.getBounds();
			topBound = oldBounds.top;
			bottomBound = oldBounds.bottom;
		}
		else {
			topBound = gap;
			bottomBound = gap + thumbHeight;
		}

		// Canvas will be translated, so 0,0 is where we start drawing
		thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
	}

	@Override
	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	public void setThumb(Drawable thumb) {
		mThumb = thumb;
		super.setThumb(thumb);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setPressed(true);
			onStartTrackingTouch();
			trackTouchEvent(event);
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;

		case MotionEvent.ACTION_MOVE:
			trackTouchEvent(event);
			attemptClaimDrag();
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;

		case MotionEvent.ACTION_UP:
			trackTouchEvent(event);
			onStopTrackingTouch();
			setPressed(false);
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;

		case MotionEvent.ACTION_CANCEL:
			onStopTrackingTouch();
			setPressed(false);
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;
		}
		return true;
	}

	private void trackTouchEvent(MotionEvent event) {
		final int Height = getHeight();
		final int available = Height - getPaddingBottom() - getPaddingTop();
		int Y = (int) event.getY();
		float scale;
		float progress = 0;
		if (Y > Height - getPaddingBottom()) {
			scale = 0.0f;
		}
		else if (Y < getPaddingTop()) {
			scale = 1.0f;
		}
		else {
			scale = (float) (Height - getPaddingBottom() - Y) / (float) available;
		}

		final int max = getMax();
		progress = scale * max;

		setProgress((int) progress);
	}

	private void attemptClaimDrag() {
		if (getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
	}
}
