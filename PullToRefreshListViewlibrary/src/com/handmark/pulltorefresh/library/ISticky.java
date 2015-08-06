package com.handmark.pulltorefresh.library;

public interface ISticky {
	void onScrollChanged(int scrollY);

	void onDownMotionEvent();

	void onUpOrCancelMotionEvent();
}
