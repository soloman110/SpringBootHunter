package com.zinnaworks.nxpgtool.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.zinnaworks.nxpgtool.entity.ApiVO;

public class HttpUtils {

	// �������� AppId�� ������ �´�.
	public static String getAppId(String url) {
		try {
			String envUrl = url.substring(0, url.indexOf(".net")+4)+"/env";
			String strTemp = getData(envUrl);
			JSONObject jobj = new JSONObject(strTemp);
			jobj = jobj.getJSONObject("vcap");
			return jobj.getString("vcap.application.application_id");
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public static String getData(String url, String appId, int instance) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
	
		try {
			if (appId != null) 
				httpGet.addHeader("X-CF-APP-INSTANCE", appId+":"+instance);
			
			HttpResponse response = httpClient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() == 200) {
		        StringBuffer sb =  new StringBuffer();
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String body = "";
				while ((body = rd.readLine()) != null) 
				{
					sb.append(body);
				}
				return sb.toString();
			} else {
				JSONObject jobj = new JSONObject();
				jobj.put("result", Integer.toString(response.getStatusLine().getStatusCode()));
				return jobj.toString();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static String getPostData(ApiVO api) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(api.getSampleUrl());
	
		try {
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    postParameters.add(new BasicNameValuePair("url", api.getUri()));
		    postParameters.add(new BasicNameValuePair("anotherUrl", api.getAnotherUrl()));
		    postParameters.add(new BasicNameValuePair("chkValue", api.getChkValue()));

		    httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
		    
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200) {
		        StringBuffer sb =  new StringBuffer();
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String body = "";
				while ((body = rd.readLine()) != null) 
				{
					sb.append(body);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	 
	public static String getData(String strUrl) {
		try {
	        URL url = new URL(strUrl);
	
	        // open connection
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setReadTimeout(20000);
	        conn.setRequestMethod("GET");
	  
	        StringBuffer sb =  new StringBuffer();
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
	        for(;;){
	        	String line =  br.readLine();
	        	if(line == null) break;
	        	sb.append(line+"\n");
	        }
	        br.close();
	        conn.disconnect();
	        return sb.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}
