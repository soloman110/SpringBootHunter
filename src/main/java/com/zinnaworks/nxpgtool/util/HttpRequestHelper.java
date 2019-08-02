package com.zinnaworks.nxpgtool.util;

public class HttpRequestHelper {
	public static String buildParams(String[][] pairs) {
		if (pairs.length == 0)
			throw new IllegalArgumentException();
		StringBuilder params = new StringBuilder();
		for (int i = 0; i < pairs.length; i++) {
			String[] pair = pairs[i];
			params.append(pair[0] + "=" + pair[1] + "&");
		}
		return params.substring(0, params.lastIndexOf("&"));
	}
}
