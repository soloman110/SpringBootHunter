package com.zinnaworks.nxpgtool.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.common.ResponseCommon;
import com.zinnaworks.nxpgtool.config.Servers;
import com.zinnaworks.nxpgtool.entity.ServerInfo;
import com.zinnaworks.nxpgtool.exception.DataNotValidException;
import com.zinnaworks.nxpgtool.service.IFService;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@RequestMapping("/nxpgtool")
@Controller
public class IFController {
	private static final Logger logger = LoggerFactory.getLogger(IFController.class);
	
	@Autowired
	IFService ifService;
	

	@Autowired
	Servers servers;
	
	@RequestMapping("/if")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		List<ServerInfo> serverInfos = servers.getServers();
		model.addAttribute("servers", serverInfos);
		return "tiles/thymeleaf/if";
	}
	
	@ResponseBody
	@RequestMapping(value="/if/checkfield", method = {RequestMethod.GET,RequestMethod.POST})
	public Map<String, String> checkField(@RequestParam String url, @RequestParam String ifname) throws FileNotFoundException {
		
		Map<String, Object> ifTree = null;
		try {
			ifTree = ifService.toTree(url);
		} catch (DataNotValidException e) {
			return ResponseCommon.IFResponse("해당 데이터가 없음..Interface 호출하여 데이터 확인하세요.");
		}
		Map<String, Object> apiTree = ifService.getFieldTree(ifname);
		logger.info(JsonUtil.objectToJson(apiTree));
		
		String diff = ifService.compare(ifTree, apiTree);
		return ResponseCommon.IFResponse(diff);
	}
	
	@RequestMapping("/jiraissue")
	public String jiraIssue(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/jiraIssue";
	}
}
