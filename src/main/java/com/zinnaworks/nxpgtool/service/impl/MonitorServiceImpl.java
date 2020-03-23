package com.zinnaworks.nxpgtool.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zinnaworks.nxpgtool.common.ServerEnum;
import com.zinnaworks.nxpgtool.controller.IFController;
import com.zinnaworks.nxpgtool.entity.MetricsInfo;
import com.zinnaworks.nxpgtool.service.MonitorService;
import com.zinnaworks.nxpgtool.util.CastUtil;
import com.zinnaworks.nxpgtool.util.HttpUtils;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@Service
public class MonitorServiceImpl implements MonitorService{
	private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
	
	private static Map<String, String> testFiles = new HashMap<>();
	static {
		testFiles.put("health", "src/health.json"); // health
		testFiles.put("metrics", "src/metrics.json"); //ok
		testFiles.put("beans", "src/beans.json");
		testFiles.put("env", "src/env.json"); //ok
		testFiles.put("mappings", "src/mappings.json");
		testFiles.put("dump", "src/dump.json");
	}
	
	@Override
	public MetricsInfo getMetrics(ServerEnum server) {
		try {
			String url = server.getUri();
			String envUrl = url.substring(0, url.indexOf(".net")+4)+"/metrics";
			logger.info(envUrl);
			String strTemp = getActuatorInfo(server, "metrics");;
			JSONObject jobj = new JSONObject(strTemp);
			
			int men = (int) jobj.get("mem");
			logger.info(String.valueOf(men));
			return null;
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	@Override
	public Map<String, Object> getMetricsMap(ServerEnum server) {
		try {
			String strTemp = getActuatorInfo(server, "metrics");
			Map<String, Object> map1 = CastUtil.StringToJsonMap(strTemp);
			return map1;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	@Override
	public Map<String, Object> getMetricsMapTest(ServerEnum server) throws IOException {
		StringBuilder strTemp = readJsonFile("src/metrics.json");
		Map<String, Object> map1 = CastUtil.StringToJsonMap(strTemp.toString());
		return map1;
	}
	
	@Override
	public Map<String, Object> getEnv(ServerEnum server) {
		try {
			String strTemp = getActuatorInfo(server, "env");
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	@Override
	public Map<String, Object> getHealth(ServerEnum server) {
		try {
			String strTemp = getActuatorInfo(server, "health");
			//Map<String, String> map = JsonUtil.jsonToObjectHashMap(strTemp, String.class, String.class);
			saveJson(strTemp, testFiles.get("health"));
			Map<String, Object> map1 = CastUtil.StringToJsonMap(strTemp);
			return map1;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	private String getActuatorInfo(ServerEnum server, String type) {
		String url = server.getUri();
		String envUrl = url.substring(0, url.indexOf(".net")+4)+"/" + type;
		logger.info(envUrl);
		return HttpUtils.getData(envUrl);
	}
	
	@Override
	public Map<String, Object> getHealthTest(ServerEnum server) {
		try {
			String strTemp = readJsonFile(testFiles.get("health")).toString();
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	@Override
	public Map<String, Object> getEnvTest(ServerEnum server) {
		try {
			//String strTemp = getActuatorInfo(server, "env");
			String strTemp = readJsonFile(testFiles.get("env")).toString();
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	private void saveJson(String strTemp, String fstr) throws IOException {
		FileWriter f = new FileWriter(new File(fstr));
		BufferedWriter writer = new BufferedWriter(f);
		writer.write(strTemp);
		writer.close();
		f.close();
	}
	@Override
	public List<Map<String, Object>> getBeans(ServerEnum server, boolean isCacheMode) {
		List<Map<String, Object>> beanList = Collections.emptyList();
		String strTemp = "";
		try {
			if (isCacheMode) {
				strTemp = readJsonFile(testFiles.get("beans")).toString();
			} else {
				strTemp = getActuatorInfo(ServerEnum.STG, "beans");
			} 
		} catch (Exception e) {
			logger.error(e.toString());
		}
		beanList = CastUtil.StringToJsonListMap(strTemp);
		return beanList;
	}
	
	@Override
	public String getActuatorByName(ServerEnum server, String actuatorName) {
		String strJson = "";
		try {
			String fn = testFiles.get(actuatorName);
			File beansFile = new File(fn);
			if(!beansFile.exists()) {
				strJson = getActuatorInfo(ServerEnum.STG, actuatorName);
				saveJson(strJson, testFiles.get(actuatorName));
			} else {
				strJson = readJsonFile(testFiles.get(actuatorName)).toString();
			}
			return strJson;
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return strJson;
	}
	
	
	
	private StringBuilder readJsonFile(String fstr) throws FileNotFoundException, IOException {
		StringBuilder strTemp = new StringBuilder();
		
		FileReader f = new FileReader(new File(fstr));
		BufferedReader reader = new BufferedReader(f);
		
		String temp;
		while((temp = reader.readLine()) != null) {
			strTemp.append(temp);
		}
		reader.close();
		f.close();
		return strTemp;
	}
	//너무 복잡하다..
	@Override
	public List<Map<String, Object>> mappings(ServerEnum server, boolean isCacheMode) {
		Map<String, Object> mappingsMap = Collections.emptyMap();
		String strTemp = "";
		try {
			if (isCacheMode) {
				strTemp = readJsonFile(testFiles.get("mappings")).toString();
			} else {
				strTemp = getActuatorInfo(ServerEnum.STG, "mappings");
			} 
		} catch (Exception e) {
			logger.error(e.toString());
		}
		mappingsMap = CastUtil.StringToJsonMap(strTemp);
		
		List<Map<String, Object>> list = new ArrayList<>();
		for(Map.Entry<String, Object> entry : mappingsMap.entrySet()) {
			String key = entry.getKey();
			Object v = entry.getValue();
			Map<String, Object> vMap = CastUtil.getObjectToMap(v);
			vMap.putAll(parseMappingsInfo(key));
			list.add(vMap);
		}
		return list;
	}
	private Map<String, String> parseMappingsInfo(String mappinginfo) {
		if(StringUtils.isEmpty(mappinginfo)) {
			return Collections.emptyMap();
		}
		
		Map<String, String> map = new HashMap<>();
		
		mappinginfo = StringUtils.removeEnd(mappinginfo, "}");
		mappinginfo = StringUtils.removeStart(mappinginfo, "{");

		String[] strs = mappinginfo.split(",");
		String uri = "";
		String httpMethod = "";
		String produces = "";
		if(strs.length > 0) {
			uri = strs[0];
			map.put("uri", uri);
		}
		if(strs.length >1) {
			for(int i =1; i<strs.length; i++) {
				String kv = strs[i];
				String[] kvMap = kv.split("=");
				if("methods".equals(kvMap[0])) {
					httpMethod = kvMap[1];
					map.put("httpMethod", httpMethod);
				}
				if("produces".equals(kvMap[0])) {
					produces = kvMap[1];
					map.put("produces", produces);
				}
			}
		}
		return map;
	}
	@Override
	public List<Map<String, Object>> threads(ServerEnum server) {
		List<Map<String, Object>> beanList = Collections.emptyList();
		String strTemp = "";
		try {
			//strTemp = getActuatorInfo(ServerEnum.STG, "dump");
			strTemp = readJsonFile(testFiles.get("dump")).toString();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		beanList = CastUtil.StringToJsonListMap(strTemp);
		return beanList;
	}
}
