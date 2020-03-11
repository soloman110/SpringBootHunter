package com.zinnaworks.nxpgtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.zinnaworks.nxpgtool.entity.Config;
import com.zinnaworks.nxpgtool.entity.Language;

@RequestMapping("/nxpgtool")
@Controller
public class HelloController {

	@Autowired
	Config config;

	@Autowired
	Language language;

	@RequestMapping("/index")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/excel";
	}

	@RequestMapping("/jsp-layout")
	public String jspViewTest() {
		return "tiles/jsp/j3";
	}

	@RequestMapping("/demo")
	public String thymeleaf() {
		return "tiles/thymeleaf/demo";
	}

}
