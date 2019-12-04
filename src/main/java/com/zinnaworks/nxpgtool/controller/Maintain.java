package com.zinnaworks.nxpgtool.controller;

import java.io.FileNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/maintain")
public class Maintain {	
	@RequestMapping("/redis")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {
		model.addAttribute("name", name);
		return "tiles/thymeleaf/redis";
	}

}
