package com.zinnaworks.nxpgtool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MonitorService {
	List<Map<String, Object>> mappings(String server, boolean isCache);
	List<Map<String, Object>> threads(String server, boolean isCache);
	String getActuatorByName(String server, String actuatorName, boolean isCache);
	List<Map<String, Object>> getBeans(String server, boolean isCache);
	Map<String, Object> getHealth(String server, boolean isCache);
	Map<String, Object> getEnv(String server, boolean isCache);
	Map<String, Object> getMetrics(String server, boolean isCache) throws IOException;
}
