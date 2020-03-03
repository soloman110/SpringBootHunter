package com.zinnaworks.nxpgtool.util;

public class StringUtils {

	public static String getUrlPath(String url) {
		String result = null;
		
		try {
			if (url == null) return null;
			url = url.trim();
			int idx = url.indexOf("http");
			if (idx == -1) return null;
			url = url.substring(idx);
			idx = url.indexOf("xpg");
			if (idx == -1) return null;
			url = url.substring(idx+4);
			
			result = url.trim();
		} catch (Exception e) {
			return null;
		}
		
		return result;
	}
	
	public static boolean CheckResult(String value, String value2, String result) {
		if (value == null && value2 == null) return true;
		if (value == null) return false;
		if (value2 == null || "".equals(value2)) {
			return "0000".equals(result);
		}
		
		return value.indexOf(value2) != -1;
	}
	
	public static boolean StringEqueals(String value, String value2) {
		if (value == null && value2 == null) return true;
		if (value == null) return false;
		if (value2 == null) return false;
		
		return value.equals(value2);
	}
	
	public static int StringToInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}
}
