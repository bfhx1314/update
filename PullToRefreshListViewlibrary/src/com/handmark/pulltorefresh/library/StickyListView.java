/*
 * Copyright 2013 Lars Werkman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class StickyListView extends ListView {

	private int mItemCount;
	private int mItemOffsetY[];
	private boolean scrollIsComputed = false;
	private int mHeight;

	public StickyListView(Context context) {
		super(context);
	}

	public StickyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public int getListHeight() {
		return mHeight;
	}

	public void computeScrollY() {
		mHeight = 0;
		// if(isLoadMoreViewVisible) {
		// mItemCount = getAdapter().getCount()+1;
		// } else {
		mItemCount = getAdapter().getCount();
		Log.e("ItemCount()=============>", mItemCount + "");
		// }

		// if (mItemOffsetY == null) {
		mItemOffsetY = new int[mItemCount];
		// }
		for (int i = 0; i < mItemCount; ++i) {
			View view = null;
			try {
				view = getAdapter().getView(i, null, this);
			} catch (Exception e) {
				return;
			}
			if (view == null)
				return;
			try {
				view.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			} catch (Exception e) {
				continue;
			}
			/** start */
			try {
				mItemOffsetY[i] = mHeight;
			} catch (Exception e) {
				scrollIsComputed = true;
				break;
			}
			/** end */
			mHeight += view.getMeasuredHeight();
		}
		scrollIsComputed = true;
	}

	public boolean scrollYIsComputed() {
		return scrollIsComputed;
	}

	public void setScrollYIsComputed(boolean scrollIsComputed) {
		this.scrollIsComputed = scrollIsComputed;
	}

	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = view.getTop();
		// Log.e("FirstTop=======>", nItemY+"");
		// Log.e("getFirstVisiblePosition()=============>", pos+"");
		// try {
		// nScrollY = mItemOffsetY[pos] - nItemY;
		// }catch (Exception e) {
		//
		// return mItemOffsetY[pos-1] - nItemY;
		// }
		nScrollY = getnScrollY(pos);
		return nScrollY;
	}

	private int getnScrollY(int pos) {

		View view = getChildAt(0);
		int nItemY = view.getTop();
		int nScrollY = 0;
		try {
			nScrollY = mItemOffsetY[pos] - nItemY;
			// Log.e("pos Right=====>", pos+"");
		} catch (Exception e) {
			getnScrollY(pos - 1);
		}
		return nScrollY;
	}
}