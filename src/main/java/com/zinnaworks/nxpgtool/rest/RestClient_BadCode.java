package com.zinnaworks.nxpgtool.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class RestClient_BadCode {

	private int socktimeout = 5000;

	private int conntimeout = 5000;

	private int connreqtimeout = 5000;

	public String apacheGet(String url, Map<String, String> reqparam) {

		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {

				HttpClient client = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(url);

				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socktimeout)
						.setConnectTimeout(conntimeout).setConnectionRequestTimeout(connreqtimeout).build();

				request.setConfig(requestConfig);

				String encoding;
				try {
					encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes("UTF-8"));
					request.addHeader("Authorization", "Basic " + encoding);
				} catch (Exception e2) {
				}

				HttpResponse response = null;
				BufferedReader rd = null;
				StringBuffer result = new StringBuffer();

				try {
					if (client == null)
						return null;
					response = client.execute(request);
					if (response == null)
						return null;
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
				} catch (IOException e1) {
				} catch (UnsupportedOperationException e) {
				} finally {
					try {
						if (rd != null)
							rd.close();
					} catch (IOException e) {
					}
				}
				System.out.println("Result: " + result.toString());
				return result.toString();

			}
		});

		threadPool.execute(task);
		String result = "";
		try {
			try {
				result = task.get(conntimeout + 100, TimeUnit.MILLISECONDS);

			} catch (TimeoutException e) {
			} finally {
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return result;

	}

	public String doPost(String url, List<NameValuePair> parameters) throws Exception {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		// 设置2个post参数，一个是scope、一个是q
		// List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
		// 构造一个form表单式的实体
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
		// 将请求实体设置到httpPost对象中
		httpPost.setEntity(formEntity);
		// 伪装浏览器
		// httpPost.setHeader("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like
		// Gecko) Chrome/56.0.2924.87 Safari/537.36");
		CloseableHttpResponse response = null;
		String content = "";
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				content = EntityUtils.toString(response.getEntity(), "UTF-8");
				// 内容写入文件
				System.out.println("内容长度：" + content.length());
			}
		} finally {
			if (response != null) {
				response.close();
			}
			httpclient.close();
		}
		return content;
	}

	public void post(String postEndpoint, String data) throws ClientProtocolException, IOException {


		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(postEndpoint);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");


		//StringEntity stringEntity = new StringEntity(data);
		

	    if (!StringUtils.isEmpty(data)) {
	    	httpPost.setEntity(new StringEntity(data, "utf-8"));
	    }
		//httpPost.setEntity(stringEntity);

		System.out.println("Executing request " + httpPost.getRequestLine());

		HttpResponse response = httpclient.execute(httpPost);

		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		// Throw runtime exception if status code isn't 200
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		// Create the StringBuffer object and store the response into it.
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println("Response : \n" + result.append(line));
		}
	}
}
