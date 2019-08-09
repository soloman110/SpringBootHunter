package com.zinnaworks.nxpgtool.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.api.Field;
import com.zinnaworks.nxpgtool.entity.Config;
import com.zinnaworks.nxpgtool.entity.Language;
import com.zinnaworks.nxpgtool.entity.User;

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

	@ResponseBody
	@RequestMapping("/info")
	public Map<String, Object> info(@Valid @ModelAttribute("user") User user, Errors errors) {

		Map<String, Object> map = new HashMap<>();
		if (errors.hasErrors()) {
			map.put("error", errors.getAllErrors());
		} else {
			map.put("user", user);
		}

		map.put("config", config);
		map.put("language", language);
		return map;
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
