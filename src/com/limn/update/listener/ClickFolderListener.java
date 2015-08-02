package com.limn.update.listener;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ClickFolderListener implements OnClickListener {

	private String btn;
	private Context act;
	public ClickFolderListener(String btn, Context mContext) {
		this.btn = btn;// 将引用变量传递给实体变量
		this.act = mContext;
	}
	
	public void onClick(View v) {
		
		Intent intent = new Intent("android.intent.action.mydialog1");
		intent.putExtra("APK_TYPE", btn);
		act.startActivity(intent);
	}
}
