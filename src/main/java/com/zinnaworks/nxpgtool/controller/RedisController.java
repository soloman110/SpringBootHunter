package com.zinnaworks.nxpgtool.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.rest.RestClient_BadCode;

@RequestMapping("/redis")
@Controller
public class RedisController {
	
	@Autowired
	RestClient_BadCode client;
	
	@ResponseBody
	@RequestMapping("/info")
	public Object info(@RequestParam String url) {
		String data =  client.apacheGet(url, null);
		return data;
	}
	
	@ResponseBody
	@PostMapping("/save")
    public Object test(@RequestBody(required=true) Map<String,Object> map) throws ClientProtocolException, IOException{
		String json = (String) map.get("data");
		String url = (String)map.get("url");
		
		client.post(url, json);
		return "ok";
	}
}

