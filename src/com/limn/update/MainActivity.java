package com.limn.update;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import com.example.update.R;
import com.limn.update.common.RequstClient;
import com.limn.update.config.StaticHttp;
import com.limn.update.listener.ClickFolderListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private LinearLayout linearLayout = null;
	
	private RelativeLayout mianView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("主页");
		
		setContentView(R.layout.activity_main);

		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		
		mianView = (RelativeLayout) findViewById(R.id.main);
//		aa.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				requestType();
//				
//			}
//		});
		requestType();

	}

	private void requestType() {
		//加载动画
		showRoundProcessDialog(this, R.layout.loading_process_dialog_anim);
		
		RequstClient.post(StaticHttp.INDEX, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String res;
						res = new String(arg2, "UTF-8");

					JSONTokener jsonParser = new JSONTokener(res); 
					JSONObject jsonObject;
						jsonObject = (JSONObject) jsonParser.nextValue();
			   
					JSONArray appType = (JSONArray) jsonObject.get("appType");
					//文件夹布局
					for (int i =0 ; i <= appType.length() - 1; i++) {
						// 创建一个新的Button对象
						Button btnLesson = new Button(MainActivity.this);
						// 设置对象的id
//						btnLesson.set
						// 设置对象显示的值
						btnLesson.setText(appType.getString(i));
						// 给按钮添加监听事件
						btnLesson.setOnClickListener(new ClickFolderListener(appType.getString(i),MainActivity.this));
						// 将Button对象添加到LinearLayout中
						linearLayout.addView(btnLesson);
					}
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
