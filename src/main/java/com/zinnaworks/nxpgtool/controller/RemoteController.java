package com.zinnaworks.nxpgtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.rest.RestClient_BadCode;

@RequestMapping("/remote")
@Controller
public class RemoteController {
	
	@Autowired
	RestClient_BadCode client;
	
	@ResponseBody
	@RequestMapping("/apicall")
	public Object info(@RequestParam String url) {
		String data =  client.apacheGet(url, null);
		return data;
	}
}

