/**
 * 
 */
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author yjm
 * 
 */

public class EndlessListview extends ListView implements OnScrollListener {

	private PullLoadingListViewListener mLoadingListener;
	private EndlessListViewFooter mFooterView;
	private TextView endlessListview_text;
	private ProgressBar endlessListview_progressBar;

	private int mTotalCount = 0;
	private Context context;
	private boolean enableLoading = true;// true 代表刷新 false 代表不刷新

	private boolean isLoading = false;

	private boolean allLoadingComplete = false;

	boolean over_1_page_flag = true;

	float density = 2F;

	private OnScrollListener onScrollListener;

	public EndlessListview(Context context) {
		super(context);
		initView(context);
	}

	public EndlessListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ListViewAttrs);
		enableLoading = a.getBoolean(R.styleable.ListViewAttrs_enableLoading,
				false);
		a.recycle();

		initView(context);
	}

	public EndlessListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;

		density = context.getResources().getDisplayMetrics().density;

		// 判断是不是要刷新
		if (enableLoading == true) {
			setOnScrollListener(this);
		}

		mFooterView = new EndlessListViewFooter(context);
		endlessListview_text = (TextView) mFooterView
				.findViewById(R.id.endlessListview_text);
		endlessListview_progressBar = (ProgressBar) mFooterView
				.findViewById(R.id.endlessListview_progressBar);
		addFooterView(mFooterView);
		mFooterView.setVisiableHeight(0);
	}

	public void removeListener() {
		mLoadingListener = null;
	}

	public void setLoadingListener(PullLoadingListViewListener mLoadingListener) {
		this.mLoadingListener = mLoadingListener;
	}

	public interface PullLoadingListViewListener {
		public void onLoading();
	}

	public void loadingCompleted() {
		mFooterView.setVisiableHeight(0);
		isLoading = false;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public void setCustomOnScrollListener(OnScrollListener listener) {
		this.onScrollListener = listener;
	}

	public void allLoadingComplete() {
		mFooterView.setVisiableHeight(0);
		allLoadingComplete = true;
	}

	public void resetAllLoadingComplete() {
		// mFooterView.setVisiableHeight((int) (60 * density));
		mFooterView.setVisibility(View.VISIBLE);
		endlessListview_progressBar.setVisibility(View.VISIBLE);
		endlessListview_text.setText("正在加载更多");
		allLoadingComplete = false;
	}
	
	

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (onScrollListener != null) {
			onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
		
		mTotalCount = totalItemCount;

		over_1_page_flag = totalItemCount >= 10 ? true : false;
		
		if (over_1_page_flag && !allLoadingComplete) {
			if (mFooterView.getVisiableHeight() == 0) {
				mFooterView.setVisiableHeight((int) (60 * density));
			}
		}
	}
	
	
	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

		Log.e("allLoadingComplete", allLoadingComplete + "======>");
		if (allLoadingComplete)
			return;

		if (scrollState == SCROLL_STATE_IDLE) {
			Log.e("scrollState", scrollState + "");
			Log.e("mTotalCount", mTotalCount + "");
			Log.e("getFirstVisiblePosition", getFirstVisiblePosition() + "");
			Log.e("getLastVisiblePosition", getLastVisiblePosition() + "");
			// 因为有下拉刷新，所以需要减2
			if (getLastVisiblePosition() == mTotalCount - 1 || getLastVisiblePosition() == mTotalCount - 2) {

				if (mLoadingListener != null && isLoading == false
						&& over_1_page_flag) {

					Log.e("mLoadingListener != null && isLoading == false",
							"======>");

					isLoading = true;
					mLoadingListener.onLoading();
					endlessListview_progressBar.setVisibility(View.VISIBLE);
					endlessListview_text.setText("正在加载更多");
				}
			}
		}
	}

}
