package com.limn.update.module;

import com.limn.update.common.BaseBean;

public class DownLoadFileBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1843913802451660791L;
	
	
	
	private String apkName = null;
	
	private String updateTime = null;
	
	private String MD5 = null;
	
	private String path = null;
	
	public void setApkName(String apkName){
		this.apkName = apkName;	
	}
	
	public void setUpdateTime(String updateTime){
		this.updateTime = updateTime;	
	}
	
	public void setMD5(String MD5){
		this.MD5 = MD5;	
	}
	
	public void setPath(String path){
		this.path = path;	
	}
	
	public String getPath(){
		return path;	
	}
	
	public String getMD5(){
		return MD5;	
	}
	
	public String getApkName(){
		return apkName;	
	}
	public String getUpdateTime(){
		return updateTime;	
	}

}
