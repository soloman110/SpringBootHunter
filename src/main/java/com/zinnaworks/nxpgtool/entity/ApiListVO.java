package com.zinnaworks.nxpgtool.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zinnaworks.nxpgtool.util.FileUtils;

public class ApiListVO {
	
	private List<ApiVO> listApi = new ArrayList<ApiVO>();
	
	// url에 get param중 원하는 값을 가지고 온다.
	public String FindValue(String url, String name) {
		String result = "";
		if (url == null) return result;
		if (name == null) return result;
		
		// 원하는 문자를 찾는다.
		int index = url.indexOf(name);
		if (index < 0) return result;
		
		// 그 이전 글자는 자르고 가지고 온다.
		// +1을 하는 이유는 = 을 빼기 위해서.
		result = url.substring(index + 1 + name.length(), url.length());	
		
		// 나머지 부분을 자른다
		index = result.indexOf("&");
		if (index >= 0) 
			result = result.substring(0, index);
		
		// url 인코딩 한다
		try {
			result = URLDecoder.decode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public ApiListVO(int type) {
		if (type == 1) {
			JSONObject json = FileUtils.getFileToJson("definition_service.json");
			ApiVO apivo;
			
			JSONArray arr = (JSONArray) json.get("fields");
			
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = (JSONObject) arr.get(i);
				apivo = new ApiVO();
				apivo.setService(obj.get("service").toString());
				apivo.setEnName(obj.get("enName").toString());
				apivo.setKoName(obj.get("koName").toString());
				apivo.setUri(obj.get("uri").toString());
				apivo.setSampleUrl(obj.get("sampleUrl").toString());
				apivo.setConId(FindValue(apivo.getSampleUrl(), "con_id"));
				apivo.setStbId(FindValue(apivo.getSampleUrl(), "stb_id"));
				listApi.add(apivo);
			}
		} else if (type == 2) {
			JSONObject json = FileUtils.getFileToJson("compare_service.json");
			ApiVO apivo;
			
			JSONArray arr = (JSONArray) json.get("fields");
			
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = (JSONObject) arr.get(i);
				apivo = new ApiVO();
				apivo.setService(obj.get("service").toString());
				apivo.setKoName(obj.get("koName").toString());
				apivo.setUri(obj.get("uri").toString());
				apivo.setAnotherUrl(obj.get("anotherUrl").toString());
				apivo.setChkValue(obj.get("chkValue").toString());
				listApi.add(apivo);
			}
		}
		
	}
	
	public List<ApiVO> getListApi() {
		return listApi;
	}

	public void setListApi(List<ApiVO> listApi) {
		this.listApi = listApi;
	}
	
	
}
