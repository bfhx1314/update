package com.limn.update.module;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.example.update.R;
import com.limn.update.common.FileMD5;
import com.limn.update.config.StaticHttp;
import com.limn.update.listener.ClickFileListener;
import com.limn.update.listener.ClickFolderListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DownLoadFileAdapter extends BaseAdapter {
	public ArrayList<DownLoadFileBean> downLoadFileBean = new ArrayList<DownLoadFileBean>();
	Context mContext;
	
	public DownLoadFileAdapter(Context context){
		this.mContext = context;
	}

	public void setDownLoadFileList(ArrayList<DownLoadFileBean> person){
		downLoadFileBean = person;
	}
	
	
	@Override
	public int getCount() {
		return (downLoadFileBean==null)?0:downLoadFileBean.size();
	}

	@Override
	public Object getItem(int position) {
		return downLoadFileBean.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
    public class ViewHolder {  
        Button down_install;  
        Button apkName;  
        TextView updateTime;  
    } 
    
	/**
	 * 查找是否存在此文件
	 * 
	 * @param fileName
	 * @param MD5
	 * @return
	 */
	private boolean findAPK(String fileName, String MD5) {
		boolean isFind = false;
		File file = new File(StaticHttp.APK_FILES_PATH + "/" + fileName);
		if (file.exists()) {
			try {
				String md5 = FileMD5.getMd5ByFile(file);

				if (md5.equalsIgnoreCase(MD5)) {
					isFind = true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			isFind = false;
		}
		return isFind;
	}
    
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		DownLoadFileBean dow = downLoadFileBean.get(position);
		
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_download1, null);
			viewHolder = new ViewHolder();
			viewHolder.apkName = (Button) convertView.findViewById(R.id.apkName);
			viewHolder.updateTime = (TextView) convertView.findViewById(R.id.updateTime);
			viewHolder.down_install = (Button) convertView.findViewById(R.id.down_install);
			
			convertView.setTag(viewHolder);  
					
		    
		}else {  
			viewHolder = (ViewHolder) convertView.getTag();  
        } 
		String apkName = dow.getApkName();
		viewHolder.apkName.setText(apkName);
		viewHolder.updateTime.setText(dow.getUpdateTime());
		
		if(dow.getMD5().isEmpty()){
			viewHolder.down_install.setText("...");
			// 给按钮添加监听事件
			viewHolder.down_install.setOnClickListener(new ClickFolderListener(dow.getPath()+ "/" + apkName,mContext));
			viewHolder.apkName.setOnClickListener(new ClickFolderListener(dow.getPath()+ "/" + apkName,mContext));

		}else if(findAPK(dow.getPath() + "/" +apkName,dow.getMD5())){
			viewHolder.down_install.setText("安装");
			// 给按钮添加监听事件
			viewHolder.down_install.setOnClickListener(new ClickFileListener(dow.getPath()+ "/" + apkName,mContext));

		}else{
			viewHolder.down_install.setText("下载");
			// 给按钮添加监听事件
			viewHolder.down_install.setOnClickListener(new ClickFileListener(dow.getPath()+ "/" + apkName,mContext));

		}
		
		return convertView;
	}

}
