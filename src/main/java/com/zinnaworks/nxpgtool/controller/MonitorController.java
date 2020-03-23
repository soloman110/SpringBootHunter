package com.zinnaworks.nxpgtool.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.common.ServerEnum;
import com.zinnaworks.nxpgtool.service.MonitorService;
import com.zinnaworks.nxpgtool.util.CastUtil;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

	@Autowired
	MonitorService monitor;

	@RequestMapping("/index")
	public String index(Model model) throws IOException {
		Map<String, Object> obj = monitor.getMetricsMapTest(null);
		Map<String, Object> health = monitor.getHealthTest(ServerEnum.getServer("STG"));

		model.addAttribute("metrics", obj);
		model.addAttribute("health", health);

		return "tiles/thymeleaf/monitor";
	}

	@RequestMapping("/metrics")
	@ResponseBody
	public Map<String, Object> metrics(@RequestParam("server") String server) throws IOException {
		Map<String, Object> obj = monitor.getMetricsMapTest(ServerEnum.getServer(server));
		// Map<String, Object> obj =
		// monitor.getMetricsMap(ServerEnum.getServer(server));
		// getMetricsMap
		return obj;
	}

	@RequestMapping("/health")
	@ResponseBody
	public Map<String, Object> health(@RequestParam("server") String server) throws FileNotFoundException {
		Map<String, Object> obj = monitor.getHealthTest(ServerEnum.getServer(server));
		return obj;
	}

	@RequestMapping("/env")
	@ResponseBody
	public Map<String, Object> env(@RequestParam("server") String server) throws FileNotFoundException {
		Map<String, Object> obj = monitor.getEnvTest(ServerEnum.getServer(server));
		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			logger.info("Key %s", entry.getKey());
			System.out.println("VV: " + entry.getValue());
		}
		return obj;
	}

	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail(@RequestParam("server") String server) throws IOException {
		Map<String, Object> health = monitor.getHealthTest(ServerEnum.getServer(server));
		Map<String, Object> metrics = monitor.getMetricsMapTest(ServerEnum.getServer(server));
		Map<String, Object> result = new HashMap<>();
		result.put("health", health);
		result.put("metrics", metrics);
		return result;
	}

	@RequestMapping("/beans")
	@ResponseBody
	public List<Map<String, Object>> beans(@RequestParam("server") String server) throws FileNotFoundException {
		boolean isCache = true;
		List<Map<String, Object>> beansList = monitor.getBeans(ServerEnum.getServer(server), isCache);
		if(beansList!= null && beansList.size() > 0) {
			Map<String, Object> map = beansList.get(0);
			return CastUtil.getObjectToMapList(map.get("beans"));
		}
		return Collections.emptyList();
	}
	
	@RequestMapping("/mappings")
	@ResponseBody
	public List<Map<String, Object>> mappings(@RequestParam("server") String server) throws FileNotFoundException {
		boolean isCache = true;
		List<Map<String, Object>> list = monitor.mappings(ServerEnum.STG, isCache);
		
		return list;
	}

	@RequestMapping("/thread")
	@ResponseBody
	public List<Map<String, Object>> threads(@RequestParam("server") String server) throws FileNotFoundException {
		List<Map<String, Object>> beansList = monitor.threads(ServerEnum.getServer(server));
		return beansList;
	}

	@RequestMapping("/actuator/{item}")
	@ResponseBody
	public Object info(@RequestParam("server") String server, @PathVariable("item") String item) throws FileNotFoundException {
		String json = monitor.getActuatorByName(ServerEnum.STG, item);
		logger.info(json);
		return json;
	}
}
