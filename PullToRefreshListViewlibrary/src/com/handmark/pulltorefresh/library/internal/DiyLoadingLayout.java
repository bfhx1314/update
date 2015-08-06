/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

@SuppressLint("ViewConstructor")
public class DiyLoadingLayout extends LoadingLayout {

	Context context;

	public DiyLoadingLayout(Context context, final Mode mode,
			final Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
		this.context = context;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			final int dHeight = imageDrawable.getIntrinsicHeight();
			final int dWidth = imageDrawable.getIntrinsicWidth();

			/**
			 * We need to set the width/height of the ImageView so that it is
			 * square with each side the size of the largest drawable dimension.
			 * This is so that it doesn't clip when rotated.
			 */
			ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
			lp.width = lp.height = Math.max(dHeight, dWidth);
			// int paddingRightLeft = (int)
			// context.getResources().getDimension(R.dimen.header_footer_left_right_padding);
			// mHeaderImage.setPadding(paddingRightLeft, 0, 0, 0);
			mHeaderImage.requestLayout();

			// reset();

			// final int dHeight = imageDrawable.getIntrinsicHeight();
			// final int dWidth = imageDrawable.getIntrinsicWidth();
			//
			// /**
			// * We need to set the width/height of the ImageView so that it is
			// square with each side the size of the
			// * largest drawable dimension. This is so that it doesn't clip
			// when rotated.
			// */
			// FrameLayout.LayoutParams lp = (LayoutParams)
			// mHeaderImage.getLayoutParams();
			// lp.width = lp.height = Math.max(dHeight, dWidth);
			//
			// int paddingRightLeft = (int)
			// context.getResources().getDimension(R.dimen.header_footer_left_right_padding);
			// lp.leftMargin = paddingRightLeft;
			// // mHeaderImage.setPadding(paddingRightLeft, 0, 0, 0);
			// mHeaderImage.requestLayout();
		}
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
	}

	@Override
	protected void pullToRefreshImpl() {
	}

	@Override
	protected void refreshingImpl() {

	}

	@Override
	protected void releaseToRefreshImpl() {
	}

	@Override
	protected void resetImpl() {
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_flip;
	}
}
