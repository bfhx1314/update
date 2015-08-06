package com.limn.update.common;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RequstClient {
	/**
	 * 定义一个异步网络客户端 默认超时未20秒 当超过，默认重连次数为5次 默认最大连接数为10个
	 */
	private static AsyncHttpClient mClient = new AsyncHttpClient();

	static {
		mClient.setTimeout(10000);
	}

	public static String post(String url, RequestParams params) {
		mClient.post(url, params, new LoadAsyncHttpResponseHandler());
		return url;
	}

	public static void get(String url, RequestParams params) {
		mClient.get(url, params, new LoadAsyncHttpResponseHandler());
	}
	
	public static String post(String url) {
		mClient.post(url, new LoadAsyncHttpResponseHandler());
		return url;
	}

	public static void get(String url) {
		mClient.get(url, new LoadAsyncHttpResponseHandler());
	}
	
	public static String post(String url, AsyncHttpResponseHandler handler) {
		mClient.post(url, handler);
		return url;
	}

	public static void get(String url, AsyncHttpResponseHandler handler) {
		mClient.get(url, handler);
	}
	
	public static String post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		mClient.post(url, params, handler);
		return url;
	}

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		mClient.get(url, params, handler); 
	}
	
}