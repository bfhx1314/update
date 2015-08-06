package com.limn.update.module;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.update.R;
import com.handmark.pulltorefresh.library.EndlessListview;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshEndlessListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.limn.update.common.RequstClient;
import com.limn.update.config.StaticHttp;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 下载APK界面
 * 
 * @author Administrator
 * 
 */
public class DownLoadFileActivity extends Activity {
	
	//定义Person
	private DownLoadFileAdapter adapter = null;
	private String type = null;
	private PullToRefreshListView listView = null;
	private ListView refreshableView = null;
	private RequestParams params = new RequestParams();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_view01);

		Intent intent = getIntent();
		type = intent.getStringExtra("APK_TYPE");
		
		params.put("version", type);
		
		setTitle("主页/" + type);
		
		listView = (PullToRefreshListView) findViewById(R.id.pull_guest_list);
		
		refreshableView = listView.getRefreshableView();
		
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(DownLoadFileActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				request();
				
			}
		});
		adapter = new DownLoadFileAdapter(this);
		refreshableView.setAdapter(adapter);
		request();
	}

	public void request(){
		//加载动画
		showRoundProcessDialog(this, R.layout.loading_process_dialog_anim);
		RequstClient.post(StaticHttp.GETVERSION, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String res = new String(arg2, "UTF-8");
					JSONTokener jsonParser = new JSONTokener(res);
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					JSONArray appType = (JSONArray) jsonObject.get("appType");
					JSONArray appList = (JSONArray) jsonObject.get("appList");

					ArrayList<DownLoadFileBean> listBean = new ArrayList<DownLoadFileBean>();
					//文件夹布局
					for (int i =0 ; i <= appType.length() - 1; i++) {

						String folderName = appType.get(i).toString();
						
						
						DownLoadFileBean df = new DownLoadFileBean();
						df.setApkName(folderName);
						df.setMD5("");
						df.setUpdateTime("");
						df.setPath(type);
						listBean.add(df);

						
					}
					//文件布局
					
					for (int i = 0; i <= appList.length() - 1; i++) {
						// 创建一个新的Button对象

						JSONObject data = (JSONObject) appList.get(i);
						String file = data.getString("fileName");
						String MD5 = data.getString("MD5");
						String updateTime = data.getString("updateTime");
						
						String fileName = file.substring(file.lastIndexOf("/") + 1);
						DownLoadFileBean df = new DownLoadFileBean();
						df.setApkName(fileName);
						df.setMD5(MD5);
						df.setUpdateTime("更新时间： " + updateTime);
						df.setPath(type);
						listBean.add(df);

					}
					
					adapter.setDownLoadFileList(listBean,mDialog);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					Toast.makeText(DownLoadFileActivity.this, "无数据", Toast.LENGTH_SHORT).show();
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(DownLoadFileActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				}
				
				if(mDialog.isShowing()){
					mDialog.cancel();
				}
				listView.onRefreshComplete();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(DownLoadFileActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
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
