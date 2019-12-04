package com.zinnaworks.nxpgtool.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.rest.RestClient;

@RequestMapping("/redis")
@Controller
public class RedisController {
	
	@Autowired
	RestClient client;
	
	@ResponseBody
	@RequestMapping("/info")
	public Object info(@RequestParam String url) {
		System.out.println("URL: " + url);
		String data =  client.apacheGet(url, null);
		return data;
	}
	
	@ResponseBody
	@PostMapping("/save")
    public Object test(@RequestBody(required=true) Map<String,Object> map) throws ClientProtocolException, IOException{
		String json = (String) map.get("data");
		String url = (String)map.get("url");
		System.out.println("Data: " + json);
		System.out.println("URL: " + url);
		
		client.post(url, json);
		return "ok";
	}
}

