package com.zinnaworks.nxpgtool.common;

import java.util.HashMap;
import java.util.Map;

public class ResponseCommon {
	public static Map<String, String> IFResponse(String msg) {
		Map<String, String> result = new HashMap<>();
		if(msg== null || "".equals(msg)) {
			result.put("result", "success");
		} else {
			result.put("result", "fail");
			result.put("errormsg", msg);
		}
		return result;
	}
}
