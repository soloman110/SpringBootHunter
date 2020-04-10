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
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zinnaworks.nxpgtool.common.NXPGCommon;
import com.zinnaworks.nxpgtool.config.Servers;
import com.zinnaworks.nxpgtool.service.MonitorService;
import com.zinnaworks.nxpgtool.util.CastUtil;
import com.zinnaworks.nxpgtool.util.HttpUtils;

@Service
public class MonitorServiceImpl implements MonitorService{
	private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
	
	@Autowired
	Servers servers;
	
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
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public Map<String, Object> getMetrics(String server, boolean isCache) throws IOException {
		if(NXPGCommon.isTestMode) {
			return getMetricsMapTest();
		}
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
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public Map<String, Object> getEnv(String server, boolean isCache) {
		if(NXPGCommon.isTestMode) {
			return getEnvTest();
		}
		try {
			String strTemp = getActuatorInfo(server, "env");
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	//monitorCache
	@Override
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public Map<String, Object> getHealth(String server, boolean isCache) {
		if(NXPGCommon.isTestMode) {
			return getHealthTest();
		}
		try {
			String strTemp = getActuatorInfo(server, "health");
			//saveJson(strTemp, testFiles.get("health"));
			Map<String, Object> map1 = CastUtil.StringToJsonMap(strTemp);
			return map1;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	
	@Override
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public List<Map<String, Object>> getBeans(String server, boolean isCache) {
		String strTemp = "";
		List<Map<String, Object>> beanList = Collections.emptyList();
		
		if(NXPGCommon.isTestMode) {
			try {
				strTemp = readJsonFile(testFiles.get("beans")).toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CastUtil.StringToJsonListMap(strTemp);
		}
		try {
			strTemp = getActuatorInfo(server, "beans");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		beanList = CastUtil.StringToJsonListMap(strTemp);
		return beanList;
	}

	@Override
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public List<Map<String, Object>> threads(String server, boolean isCache) {
		if(NXPGCommon.isTestMode) {
			return threadsTest(server);
		}
		List<Map<String, Object>> threadList = Collections.emptyList();
		String strTemp = "";
		try {
			strTemp = getActuatorInfo(server, "dump");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		threadList = CastUtil.StringToJsonListMap(strTemp);
		return threadList;
	}
	
	//너무 복잡하다..
	@Override
	@Cacheable(value = "monitorCache", condition="#isCache == true", key="#root.targetClass + #root.methodName +  #root.args[0]")
	@CachePut(value = "monitorCache",condition="#isCache == false", key="#root.targetClass + #root.methodName + #root.args[0]")
	public List<Map<String, Object>> mappings(String server, boolean isCache) {
		String strTemp = "";
		Map<String, Object> mappingsMap = Collections.emptyMap();
		
		if(NXPGCommon.isTestMode) {
			try {
				strTemp = readJsonFile(testFiles.get("mappings")).toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				strTemp = getActuatorInfo(server, "mappings");
			} catch (Exception e) {
				logger.error(e.toString());
			}
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

	@Override
	public String getActuatorByName(String server, String actuatorName, boolean isCache) {
		String strJson = "";
		try {
			String fn = testFiles.get(actuatorName);
			File beansFile = new File(fn);
			if(!beansFile.exists()) {
				strJson = getActuatorInfo(server, actuatorName);
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
	private Map<String, Object> getMetricsMapTest() throws IOException {
		StringBuilder strTemp = readJsonFile("src/metrics.json");
		Map<String, Object> map1 = CastUtil.StringToJsonMap(strTemp.toString());
		return map1;
	}
	private Map<String, Object> getHealthTest() {
		try {
			String strTemp = readJsonFile(testFiles.get("health")).toString();
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	private Map<String, Object> getEnvTest() {
		try {
			String strTemp = readJsonFile(testFiles.get("env")).toString();
			Map<String, Object> map = CastUtil.StringToJsonMap(strTemp);
			return map;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
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
	private String getActuatorInfo(String server, String type) {
		String url = servers.getServerInfo(server).getUrl();
		String envUrl = url.substring(0, url.indexOf(".net")+4)+"/" + type;
		return HttpUtils.getData(envUrl);
	}
	private List<Map<String, Object>> threadsTest(String server) {
		List<Map<String, Object>> threadList = Collections.emptyList();
		String strTemp = "";
		try {
			strTemp = readJsonFile(testFiles.get("dump")).toString();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		threadList = CastUtil.StringToJsonListMap(strTemp);
		return threadList;
	}
	
	private void saveJson(String strTemp, String fstr) throws IOException {
		FileWriter f = new FileWriter(new File(fstr));
		BufferedWriter writer = new BufferedWriter(f);
		writer.write(strTemp);
		writer.close();
		f.close();
	}
}
