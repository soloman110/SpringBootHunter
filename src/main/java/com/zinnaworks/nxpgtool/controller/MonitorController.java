package com.zinnaworks.nxpgtool.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.service.MonitorService;
import com.zinnaworks.nxpgtool.util.CastUtil;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

	ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	MonitorService monitor;

	@RequestMapping("/index")
	public String index(Model model) throws IOException {
		return "tiles/thymeleaf/monitor";
	}
	
	@RequestMapping("/guide")
	public String guide(Model model) throws IOException {
		model.addAttribute("poc", "BTVWEBV513");
		return "tiles/thymeleaf/guide513";
	}

	@RequestMapping("/metrics")
	@ResponseBody
	public Map<String, Object> metrics(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws IOException {
		Map<String, Object> obj = monitor.getMetrics(server, isCache);
		return obj;
	}

	@RequestMapping("/health")
	@ResponseBody
	public Map<String, Object> health(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws FileNotFoundException {
		Map<String, Object> obj = monitor.getHealth(server, isCache);
		return obj;
	}

	@RequestMapping("/env")
	@ResponseBody
	public Map<String, Object> env(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws FileNotFoundException {
		Map<String, Object> obj = monitor.getEnv(server, isCache);
		return obj;
	}

	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws IOException {
		Future<Map<String, Object>> healthFuture = executor.submit(() -> {return monitor.getHealth(server, isCache);});
		Future<Map<String, Object>> metircsFuture = executor.submit(() -> {return monitor.getMetrics(server, isCache);});
		
		Map<String, Object> health = Collections.emptyMap();
		try {
			health = healthFuture.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}
		
		Map<String, Object> metrics = Collections.emptyMap();
		try {
			metrics = metircsFuture.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> metricsFiltered = metrics.entrySet().stream()
				.filter(x -> !x.getKey().startsWith("gauge"))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		//주의...Copy를 했다..
		Map<String, Object> healthCopy = new HashMap<>(health);
		Map<String, Object> result = new LinkedHashMap<>();
		Object redis = healthCopy.get("redis");
		healthCopy.remove("redis");
		
		result.put("Health", healthCopy);
		result.put("Redis", redis);
		result.put("Metrics", metricsFiltered);
		
		return result;
	}

	@RequestMapping("/beans")
	@ResponseBody
	public List<Map<String, Object>> beans(@RequestParam("server") String server) throws FileNotFoundException {
		boolean isCache = true;
		List<Map<String, Object>> beansList = monitor.getBeans(server, isCache);
		if(beansList!= null && beansList.size() > 0) {
			Map<String, Object> map = beansList.get(0);
			return CastUtil.getObjectToMapList(map.get("beans"));
		}
		return Collections.emptyList();
	}
	
	@RequestMapping("/mappings")
	@ResponseBody
	public List<Map<String, Object>> mappings(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws FileNotFoundException {
		List<Map<String, Object>> list = monitor.mappings(server, isCache);
		return list;
	}

	@RequestMapping("/thread")
	@ResponseBody
	public List<Map<String, Object>> threads(@RequestParam("server") String server, @RequestParam(defaultValue="true") boolean isCache) throws FileNotFoundException {
		List<Map<String, Object>> beansList = monitor.threads(server, isCache);
		return beansList;
	}

	@RequestMapping("/actuator/{item}")
	@ResponseBody
	public Object info(@RequestParam("server") String server, @PathVariable("item") String item, @RequestParam(defaultValue="true") boolean isCache) throws FileNotFoundException {
		String json = monitor.getActuatorByName(server, item, isCache);
		return json;
	}
}
