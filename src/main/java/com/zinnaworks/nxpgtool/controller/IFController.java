package com.zinnaworks.nxpgtool.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.zinnaworks.nxpgtool.exception.DataNotValidException;
import com.zinnaworks.nxpgtool.service.IFService;
import com.zinnaworks.nxpgtool.util.CommonUtils;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@RequestMapping("/nxpgtool")
@Controller
public class IFController {
	private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);
	
	@Autowired
	IFService ifService;
	
	@RequestMapping("/if")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "if2";
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
		//logger.info(JsonUtil.objectToJson(ifTree));
		try {
			CommonUtils.saveJson("/tmp/aaa.json", JsonUtil.objectToJson(ifTree));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> apiTree = ifService.getFieldTree(ifname);
		logger.info(JsonUtil.objectToJson(apiTree));
		
		String diff = ifService.compare(ifTree, apiTree);
		return ResponseCommon.IFResponse(diff);
	}
}