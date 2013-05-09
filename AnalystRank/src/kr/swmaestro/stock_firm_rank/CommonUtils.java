package kr.swmaestro.stock_firm_rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class CommonUtils {
	//////// 서버 통신 모듈
	private static final int timeoutConnection = 3000;
	private static final int timeoutSocket = 3000;
	
	public static String requestWithPost(String url) {
		return requestWithPost(url, null);
	}
		
	public static String requestWithPost(String postURL, Map<String, String> m_params) {
		try
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        
	        if (m_params != null) {
	        	for (String key : m_params.keySet()) {
		        	params.add(new BasicNameValuePair(key, m_params.get(key)));
				}
	        }
			
		        HttpClient client = new DefaultHttpClient(httpParameters);  
		        HttpPost post = new HttpPost(postURL); 
		        
		        
		        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
		        post.setEntity(ent);
		        HttpResponse responsePOST = client.execute(post);  
		        HttpEntity resEntity = responsePOST.getEntity();
		        
		        if (resEntity != null)
		        {
		        	String result = EntityUtils.toString(resEntity);
		        	Log.d("NETWORK_RESULT", result);
		        	return result;
		        }
		}
		catch (Exception e)
		{
		        e.printStackTrace();
		}
		
		return null;
	}
	
	public static String requestWithGet(String getURL) {
		return requestWithGet(getURL, null);
	}
	public static String requestWithGet(String getURL, Map<String, String> m_params) {
		try
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			int index = 0;
	        if (m_params != null) {
	        	for (String key : m_params.keySet()) {
		        	if (index++ == 0)
		        		getURL += "?";
		        	else
		        		getURL += "&";
		        	
		        	getURL += key + "=" +  m_params.get(key);
				}
	        }

	        Log.d("NETWORK_RESULT", getURL);
	        
		        HttpClient client = new DefaultHttpClient(httpParameters);  
		        HttpGet get = new HttpGet(getURL);
		        
		        	        
		        
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		        
		        if (resEntityGet != null)
		        {  
		                // 결과를 처리합니다.
		        	String result = EntityUtils.toString(resEntityGet);
		        	Log.d("NETWORK_RESULT", result);
		            return result;
		        }
		}
		catch (Exception e)
		{
		        e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	//////// network state check
	public static boolean isNetworkEnable(Context context) {
		return isConnected3G(context) || isConnectedWiFi(context);
	}
	
	/**
	* Wi-Fi가 연결되어 있는가
	* @return true=연결됨
	*/
	public static boolean isConnectedWiFi(Context context)
	{
	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	/**
	* 3G가 연결되어 있는가
	* @return true=연결됨
	*/
	public static boolean isConnected3G(Context context)
	{
	boolean kResult = false;
	try
	{
	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	kResult = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	} catch (Exception e)
	{
	e.printStackTrace();
	}
	return kResult;
	}
	
	public static void debug(String string) {
		//Log.e("CommonUtils.debug", string);
		Log.d("CommonUtils.debug", string);
	}

}
