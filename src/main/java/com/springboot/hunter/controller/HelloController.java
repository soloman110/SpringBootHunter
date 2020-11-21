package com.springboot.hunter.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.hunter.config.properties.MailPropertiesExample;

@Controller
@RequestMapping("/deploy")
public class HelloController {

	ExecutorService executor = Executors.newCachedThreadPool();
	

	@Autowired
	MailPropertiesExample mail;
	
    public void doTest() {
    	List<String> servers = mail.getSmtpServers();
    	for (String string : servers) {
			System.out.println(string);
		}
    }

	@RequestMapping("/index")
	public String index(Model model) throws IOException {
		doTest();
		return "tiles/thymeleaf/index";
	}
}
