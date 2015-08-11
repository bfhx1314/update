package com.limn.update.config;

public class StaticHttp {
	
	//请求地址
	public static String REQUEST_IP = "http://192.168.0.103:8080/update";
	
	
	
	
	public static String DOWNLOAD_IP = REQUEST_IP + "/apk/";
	
	public static String INDEX = REQUEST_IP + "/index.do";
	
	public static String GETVERSION = REQUEST_IP + "/getVersion.do";
	
	public static String APK_FILES_PATH = "sdcard/apkDownLoad";

}
