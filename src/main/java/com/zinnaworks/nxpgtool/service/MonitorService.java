package com.zinnaworks.nxpgtool.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.zinnaworks.nxpgtool.common.ServerEnum;
import com.zinnaworks.nxpgtool.entity.MetricsInfo;

public interface MonitorService {
	public  MetricsInfo getMetrics(ServerEnum server);
	public  Map<String, Object> getMetricsMap(ServerEnum server);
	public 	Map<String, Object> getEnv(ServerEnum server);
	public 	Map<String, Object> getEnvTest(ServerEnum server);
	public List<Map<String, Object>> getBeans(ServerEnum server, boolean isCache);
	Map<String, Object> getMetricsMapTest(ServerEnum server) throws FileNotFoundException, IOException;
	Map<String, Object> getHealth(ServerEnum server);
	Map<String, Object> getHealthTest(ServerEnum server);
	String getActuatorByName(ServerEnum server, String actuatorName);
	public List<Map<String, Object>> mappings(ServerEnum server, boolean isCache);
	public List<Map<String, Object>> threads(ServerEnum server);
}
