package com.fortune.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;

public class CheckTextView extends CompoundButton {
	public CheckTextView(Context context) {
		this(context, null);
	}

	public CheckTextView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public CheckTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(CheckTextView.class.getName());
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(CheckTextView.class.getName());
	}
}
