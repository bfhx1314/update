package com.limn.update.module;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import com.example.update.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.limn.update.common.RequstClient;
import com.limn.update.config.StaticHttp;
import com.limn.update.listener.ClickFolderListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	private RelativeLayout mianView = null;
	
	private PullToRefreshListView listView = null;
	private ListView refreshableView = null;
	private MainAdapter mainAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("主页");
		
		setContentView(R.layout.activity_main);

		//创建第三方的listView
		listView = (PullToRefreshListView) findViewById(R.id.listView);
		refreshableView = listView.getRefreshableView();
		
		mianView = (RelativeLayout) findViewById(R.id.main);
		
		//设置适配器
		mainAdapter = new MainAdapter(MainActivity.this);
		refreshableView.setAdapter(mainAdapter);
		
		//刷新事件
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MainActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				requestType();
				
			}
		});
		
		//请求
		requestType();

	}

	private void requestType() {
		//加载动画
		showRoundProcessDialog(this, R.layout.loading_process_dialog_anim);
		
		RequstClient.post(StaticHttp.INDEX, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String res = new String(arg2, "UTF-8");

					JSONTokener jsonParser = new JSONTokener(res); 
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
			   
					JSONArray appType = (JSONArray) jsonObject.get("appType");
					
					ArrayList<String> list = new ArrayList<String>();
					//文件夹布局
					for (int i =0 ; i <= appType.length() - 1; i++) {
						list.add(appType.getString(i));		
					}
					
					mainAdapter.setList(list, mDialog);
					mainAdapter.notifyDataSetChanged();
					mianView.setOnClickListener(null);
					
				} catch (JSONException e) {
					Toast.makeText(MainActivity.this, "无数据", Toast.LENGTH_SHORT).show();
					mianView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							requestType();
						}
					});
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(MainActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
					mianView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							requestType();
						}
					});
				}
				mDialog.cancel();
				listView.onRefreshComplete();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(MainActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
				mianView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						requestType();
					}
				});
				mDialog.cancel();
				listView.onRefreshComplete();
			}
		});
	}

	private Dialog mDialog;
	
    public void showRoundProcessDialog(Context mContext, int layout){
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setCancelable(true); 
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

}
