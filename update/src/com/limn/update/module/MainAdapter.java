package com.limn.update.module;

import java.util.ArrayList;

import com.example.update.R;
import com.limn.update.listener.ClickFolderListener;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MainAdapter extends BaseAdapter {
	
	
	public ArrayList<String> mainList = new ArrayList<String>();
	private Context mContext;
	private Dialog mdialog;
	
	public MainAdapter(Context mContext){
		this.mContext = mContext;
	}
	
	
	public void setList(ArrayList<String> mainList, Dialog mdialog){
		this.mainList = mainList;
		this.mdialog = mdialog;
	}
	
	
	
	@Override
	public int getCount() {
		return (mainList==null)?0:mainList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mainList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	
    public class ViewHolder {  
        Button type;  
    } 
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		if (null == convertView) {
			Button button = new Button(mContext);
			button.setText(mainList.get(position));
			convertView = button;
		}
		
		convertView.setOnClickListener(new ClickFolderListener(mainList.get(position), mContext));
		
		if(getCount() == position + 1){
			mdialog.cancel();
		}
		
		return convertView;
	}

}
