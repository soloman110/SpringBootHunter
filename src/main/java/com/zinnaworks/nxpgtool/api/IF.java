package com.zinnaworks.nxpgtool.api;

import java.io.FileNotFoundException;
import java.util.Map;

import com.zinnaworks.nxpgtool.util.HttpClient;
import com.zinnaworks.nxpgtool.util.JsonUtil;

public class IF {
	public static Map<String, Object> getData(String url) throws FileNotFoundException {
		String json2 = HttpClient.doGet(url);
		Map<String, Object> t = JsonUtil.jsonToObjectHashMap(json2, String.class, Object.class);
		return t;
	}
}
