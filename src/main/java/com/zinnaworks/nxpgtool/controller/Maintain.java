package com.zinnaworks.nxpgtool.controller;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zinnaworks.nxpgtool.config.Servers;
import com.zinnaworks.nxpgtool.entity.ServerInfo;
import com.zinnaworks.nxpgtool.util.ExecuteShellUtil;
import com.zinnaworks.nxpgtool.util.HttpUtils;

@Controller
@RequestMapping("/maintain")
public class Maintain {
	// sample
	String sampleUrl = "v5/menu/gnb?IF=IF-NXPG-001&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVUH2V500";	
	// dev
	String devUrl = "https://xpg-nxpg-dev.skb-doj-dev01.mybluemix.net/";
	// stg
	String stgUrl = "https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/";
	// prd
	String prdSuyUrl = "https://xpg-nxpg-svc.skb-suy-prd01.mybluemix.net/";
	String prdSsuUrl = "https://xpg-nxpg-svc.skb-ssu-prd02.mybluemix.net/";
	
	@Autowired
	Servers servers;
	
	@RequestMapping("/redis")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		List<ServerInfo> serverInfos = servers.getServers();
		model.addAttribute("servers", serverInfos);
		return "tiles/thymeleaf/redis";
	}
	@RequestMapping("/instance")
	public String instanceCheck(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/checkInstance";
	}
	@RequestMapping("/urltrans")
	public String urlTrans(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/urlTransform";
	}
	
	@RequestMapping("/gecheck")
	public String gecheck(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/geCheck";
	}
	
	@RequestMapping("/fullCheck")
	@ResponseBody
	public List<String> fullCheck(@RequestBody(required=true) Map<String,Object> map) throws FileNotFoundException {
		ExecuteShellUtil executor = ExecuteShellUtil.getInstance();
		List<String> result = null;
		
		String type = (String)map.get("type");
		String date = (String)map.get("date");
		
		String host = "stg".equals(type) ? "10.10.223.30" : "10.10.213.9";
		String user = "root";
		String password = "stg".equals(type) ? "I@mg2pw!!stg" : "I@mg2pw!!dev";
		
		System.out.println("CMD: " + buildCmd(date));
		try {
			executor.init(host, 22, user, password);
			result = executor.remoteExecute(buildCmd(date));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.close();
		}
		return result;
	}
	
	private String buildCmd(String date) {
		if(date == null || "".equals(date)) {
			return "cat /home/xpg/logs/catalina.log | grep \"END_FILE_FULL\"";
		}
		return "cat /home/xpg/logs/catalina." + date + "* | grep \"END_FILE_FULL\"";
	}
	
	public String getToday() {
		Locale currLocale = new Locale("KOREAN","KOREA");  
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currLocale);
		return formatter.format(new Date());
	}
		
	@RequestMapping(value = "/checkInstance")
	@ResponseBody
	public String checkInstance(HttpServletRequest request, HttpServletResponse response) {
		String resultValue = "";
		String suyUrl = "", ssuUrl = "";
		int instCount = StringToInt(request.getParameter("instCount")); //Instance Count
		String urlType = request.getParameter("urlType"); //all, suy, ssu
		String serverType = request.getParameter("type"); //dev, stg, prd
		String result = request.getParameter("txtResult"); //result:0000
		String subUrl = request.getParameter("txtSubUrl"); //v5/menu/gnb?IF=IF-NXPG-001&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVUH2V500
		
		String suyAppId = "";
		String ssuAppId = "";
		
		if (urlType == null || instCount == 0) {
			return "필수값 없음";
		}
		
		if ("".equals(subUrl) || subUrl == null) {
			subUrl = sampleUrl;
		}
		
		// 서버 확인 후 url 정보 넣기
		if ("dev".equals(serverType)) {
			suyUrl = devUrl;
			ssuUrl = devUrl;
		} else if ("stg".equals(serverType)) {
			suyUrl = stgUrl;
			ssuUrl = stgUrl;
		} else {
			suyUrl = prdSuyUrl;
			ssuUrl = prdSsuUrl;
		}
		// url 생성..
		suyUrl = suyUrl + subUrl;
		ssuUrl = ssuUrl + subUrl;
		// appId 가지고 오기(all 일 경우에는 둘다 가지고 온다) // suy
		if (!"ssu".equals(urlType)) {
			suyAppId = HttpUtils.getAppId(suyUrl);
			if (suyAppId == null) {
				return "Application Id 못불러옴";
			}
		}
		// ssu(prd에서만 사용)
		if (!"suy".equals(urlType) && "prd".equals(serverType)) {
			ssuAppId = HttpUtils.getAppId(ssuUrl);
			if (ssuAppId == null) {
				return "Application Id 못불러옴";
			}
		}
		
		for(int i = 0; i < instCount; i++) {
			try {
				// suy
				if (!"ssu".equals(urlType)) {
					String strTemp = HttpUtils.getData(suyUrl, suyAppId, i);
					if (strTemp != null) {
						JSONObject jobj = new JSONObject(strTemp);
						if (CheckResult(strTemp, result, jobj.getString("result")))
							resultValue = resultValue + "suy inst : " + i + " Success";
						else 
							resultValue = resultValue + "suy inst : " + i + " Error " + jobj.getString("result");
					} else {
						resultValue = resultValue + "suy inst : " + i + " Error";
					}
				}
				// ssu(prd에서만 사용)
				if (!"suy".equals(urlType) && "prd".equals(serverType)) {
					String strTemp = HttpUtils.getData(ssuUrl, ssuAppId, i);
					if (strTemp != null) {
						JSONObject jobj = new JSONObject(strTemp);
						boolean isSuccess = CheckResult(strTemp, result, jobj.getString("result"));
						if (isSuccess)
							resultValue = resultValue + "ssu inst : " + i + " Success";
						else 
							resultValue = resultValue + "ssu inst : " + i + " Error " + jobj.getString("result");
					} else {
						resultValue = resultValue + "ssu inst : " + i + " Error";
					}
				}
			} catch (Exception e) {
				System.out.println("inst : " + i + "error : " + e.toString());
			}
		}
		return resultValue;
	}
	
	@RequestMapping(value = "/checkApi", method = RequestMethod.POST)
	@ResponseBody
	public String checkApi(HttpServletRequest request, HttpServletResponse response) {
		String resultValue = "";
		String suyUrl = "", ssuUrl = "";

		String url = request.getParameter("url");
		String urlType = request.getParameter("urlType");
		String serverType = request.getParameter("type");

		// 서버 확인 후 url 정보 넣기
		if ("dev".equals(serverType)) {
			suyUrl = devUrl;
			ssuUrl = devUrl;
		} else if ("stg".equals(serverType)) {
			suyUrl = stgUrl;
			ssuUrl = stgUrl;
		} else {
			suyUrl = prdSuyUrl;
			ssuUrl = prdSsuUrl;
		}
		// url 필터링
		String[] urlList = url.split("\n");
		
		for(int i = 0; i < urlList.length; i++) {
			try {
				url = getUrlPath(urlList[i]);
				if (url == null) continue;
				// suy
				if (!"ssu".equals(urlType)) {
					String strTemp = HttpUtils.getData(suyUrl + url);
					
					if (strTemp != null) {
						JSONObject jobj = new JSONObject(strTemp);
						if (StringEqueals("0000", jobj.getString("result"))) 
							resultValue = resultValue + "suy " + suyUrl + url + "</br>";
						else 
							resultValue = resultValue + "suy " + suyUrl + url + " Error " + jobj.getString("result") +"</br>";
					} else {
						resultValue = resultValue + "suy " + suyUrl + url + "</br>";
					}
				}

				// ssu(prd에서만 사용)
				if (!"suy".equals(urlType) && "prd".equals(serverType)) {
					String strTemp = HttpUtils.getData(ssuUrl + url);
					
					if (strTemp != null) {
						JSONObject jobj = new JSONObject(strTemp);
						if (StringEqueals("0000", jobj.getString("result"))) 
							resultValue = resultValue + "ssu " + ssuUrl + url + "</br>";
						else 
							resultValue = resultValue + "ssu " + ssuUrl + url + " Error " + jobj.getString("result") + "</br>";
					} else {
						resultValue = resultValue + "ssu " + ssuUrl + url + "</br>";
					}
				}
				
			} catch (Exception e) {
				System.out.println("inst : " + url + "error : " + e.toString());
				resultValue = resultValue + url;
			}
		}
		
		return resultValue;
	}
	
	@RequestMapping("/grid")
	public String grid(Model model) throws FileNotFoundException {
		return "tiles/thymeleaf/grid";
	}
	
	//==============================================================================
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
		String replacement = value.replace("\"", "");
		return replacement.indexOf(value2) != -1;
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
