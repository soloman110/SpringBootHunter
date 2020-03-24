package com.zinnaworks.nxpgtool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.zinnaworks.nxpgtool.entity.MetricsInfo;

public interface MonitorService {
	List<Map<String, Object>> mappings(String server, boolean isCacheMode);
	List<Map<String, Object>> threads(String server);
	String getActuatorByName(String server, String actuatorName);
	List<Map<String, Object>> getBeans(String server, boolean isCacheMode);
	Map<String, Object> getHealth(String server);
	Map<String, Object> getEnv(String server);
	Map<String, Object> getMetrics(String server) throws IOException;
}
