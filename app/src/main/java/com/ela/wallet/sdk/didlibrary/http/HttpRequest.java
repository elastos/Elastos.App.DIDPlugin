package com.ela.wallet.sdk.didlibrary.http;

import com.ela.wallet.sdk.didlibrary.utils.LogUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @version 2015.3.17
 * @author maxiaoxu
 */
public class HttpRequest {
	
	/**
	 * 使用HttpURLConnection get方式打开链接
	 * 
	 * @param url
	 * 			需要访问的网络地址。
	 * @param listener
	 * 			访问网络响应状态回调。返回的响应在onFinish()方法。
	 */
	public static void sendRequestWithHttpURLConnection(final String url, final
			HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL strUrl = new URL(url);
					LogUtil.v("url=" + url);
					connection = (HttpURLConnection) strUrl.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(15000);
					connection.setReadTimeout(15000);
					connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
					connection.setRequestProperty("accept","application/json");

					int code = connection.getResponseCode();
					LogUtil.i("http response code=" + code);
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder response = new StringBuilder();
					String line;
					
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
					
				} catch (Exception e) {
					LogUtil.e("[http]...sendRequestWithHttpURLConnection..." + e.getMessage());
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}

	public static void sendRequestWithHttpURLConnection(final String url, final String data, final
		HttpCallbackListener listener) {
		sendRequestWithHttpURLConnection(url, null, data, listener);
	}
	
	/**
	 * 使用HttpURLConnection post方式打开链接
	 * 
	 * @param url
	 * 			待访问网络的目标地址。
	 * @param data
	 * 			每条数据都要以键值对的形式存在，数据与数据之间用&符号隔开
	 * @param listener
	 * 			访问网络响应状态回调。返回的响应在onFinish()方法。
	 */
	public static void sendRequestWithHttpURLConnection(final String url, final HashMap<String, String> header, final String data, final
			HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL strUrl = new URL(url);
					connection = (HttpURLConnection) strUrl.openConnection();
					connection.setConnectTimeout(15000);
					connection.setReadTimeout(15000);
					connection.setRequestMethod("POST");
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
					connection.setRequestProperty("accept","application/json");
					if (header != null && header.size() > 0) {
						Iterator iterator = header.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry entry = (Map.Entry) iterator.next();
							connection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
						}
					}
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					out.writeBytes(data);

//					LogUtil.d("[http]...sendRequestWithHttpURLConnection..." + connection.getResponseCode());
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder response = new StringBuilder();
					String line;
					
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
					
				} catch (Exception e) {
					LogUtil.e("[http]...sendRequestWithHttpURLConnection..." + e.getMessage());
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}
	
	/**
	 * http请求回调接口。
	 * 需要实现onFinish(String response)和onError(Exception e)方法。
	 */
	public interface HttpCallbackListener {
		
		void onFinish(String response);
		
		void onError(Exception e);
	}
}
