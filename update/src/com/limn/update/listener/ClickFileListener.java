package com.limn.update.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.limn.update.config.StaticHttp;
import com.limn.update.module.DownLoadFileActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ClickFileListener implements OnClickListener {
	private String fileDownLoadPath = null;

	// 是否在进行下载
	private boolean isLoading = false;

	private Context act = null;

	/*
	 * 一个构造方法, 将Button对象做为参数
	 */
	public ClickFileListener(String URL, Context mContext) {
		this.act = mContext;
		fileDownLoadPath = URL;
	}

	@SuppressLint("ShowToast")
	public void onClick(View v) {
		String name = (String) ((Button) v).getText();
		if (name.equals("安装")) {

			String filePath = StaticHttp.APK_FILES_PATH + "/"
					+ fileDownLoadPath;

			File file = new File(filePath);

			if (!file.exists()) {
				Toast.makeText(act, "文件不存在：" + fileDownLoadPath, 1);
			}

			openFile(filePath);
			return;
		}

		// 下载的进程
		if (!isLoading && name.equals("下载")) {
			isLoading = true;
			new Thread(new DownLoadFile(fileDownLoadPath, ((Button) v)))
					.start();

		}
	}

	/**
	 * 打开APK
	 * 
	 * @param file
	 */
	private void openFile(String fileDownLoadPath) {
		Intent intentInstall = new Intent();
		intentInstall.setAction(android.content.Intent.ACTION_VIEW);
		intentInstall.setDataAndType(Uri.fromFile(new File(fileDownLoadPath)),
				"application/vnd.android.package-archive");
		intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		act.startActivity(intentInstall);
	}

	/**
	 * 下载文件
	 * 
	 * @author Administrator
	 * 
	 */
	class DownLoadFile implements Runnable {

		private String fileDownLoadPath = null;
		private Button button = null;
		private int fileSize = 0;
		private int downLoadFileSize = 0;

		public DownLoadFile(String fileDownLoadPath, Button button) {
			this.fileDownLoadPath = fileDownLoadPath;
			this.button = button;
		}

		@Override
		public void run() {

			down_file(fileDownLoadPath,false);

		}

		/**
		 * 下载文件
		 * 
		 * @param url
		 * @param path
		 * @throws IOException
		 */
		public void down_file(String path, boolean isAgain) {

			String url = path;
			URL myURL = null;
			try {
				url = StaticHttp.DOWNLOAD_IP + encode(url, "UTF-8");
				myURL = new URL(url);
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}

			URLConnection conn = null;
			InputStream is = null;
			FileOutputStream fos = null;
			
			try{
				conn = myURL.openConnection();
				conn.connect();
				is = conn.getInputStream();
			}catch (IOException ex){
				
				Looper.prepare();
				Toast.makeText(act, "文件不存在,请重新刷新", Toast.LENGTH_SHORT).show();
			    Looper.loop();// 进入loop中的循环，查看消息队列
				return;
			}
			
			
			
			try {


				fileSize = conn.getContentLength();// 根据响应获取文件大小
				if (fileSize <= 0)
					throw new RuntimeException("无法获知文件大小 ");
				if (is == null)
					throw new RuntimeException("stream is null");

				path = StaticHttp.APK_FILES_PATH + "/" + path;

				File file = new File(path);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (file.exists()) {
					file.delete();
				}

				fos = new FileOutputStream(file.getAbsolutePath(), true);
				// 把数据存入路径+文件名
				byte buf[] = new byte[1024];
				downLoadFileSize = 0;
				sendMsg(0);
				do {
					// 循环读取
					int numread = is.read(buf);
					if (numread == -1) {
						break;
					}
					fos.write(buf, 0, numread);
					downLoadFileSize += numread;
					sendMsg(1);// 更新进度条
				} while (true);
				sendMsg(2);// 通知下载完成
				conn = null;
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException ex) {
				
				StaticHttp.APK_FILES_PATH = StaticHttp.APK_FILES_PATH + "1";
				if(!isAgain){
					down_file(fileDownLoadPath,true);
				}
				
			} finally {
				conn = null;
				try {
					if(fos!=null){
						fos.flush();
						fos.close();
					}
					if(is!=null){
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		private void sendMsg(int flag) {
			Message msg = new Message();
			msg.what = flag;
			handler.sendMessage(msg);
		}

		/**
		 * 替换字符串卷
		 * 
		 * @param str
		 *            被替换的字符串
		 * @param charset
		 *            字符集
		 * @return 替换好的
		 * @throws UnsupportedEncodingException
		 *             不支持的字符集
		 */
		public String encode(String str, String charset)
				throws UnsupportedEncodingException {

			String zhPattern = "[\\u4e00-\\u9fa5]";
			Pattern p = Pattern.compile(zhPattern);
			Matcher m = p.matcher(str);
			StringBuffer b = new StringBuffer();
			while (m.find()) {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			}
			m.appendTail(b);
			return b.toString().replace(" ", "%20");
		}

		@SuppressLint("HandlerLeak")
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 0:
					case 1:
						int result = downLoadFileSize * 100 / fileSize;
						button.setText(result + "%");
						break;
					case 2:
						button.setText("安装");
						break;
					case -1:
						break;
					}
				}
				super.handleMessage(msg);
			}
		};

	}

}
