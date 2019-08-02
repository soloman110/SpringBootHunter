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
		return "index";
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

	@RequestMapping("/user")
	public String user(@Valid @ModelAttribute("user") User user, Errors errors, Model model, String xss,
			@RequestParam(defaultValue = "true") boolean injection) {
		if (injection) {
			xss = StringEscapeUtils.escapeHtml4(xss);
		}
		model.addAttribute("xss", xss);
		if (errors.hasErrors()) {
			model.addAttribute("error", errors.getAllErrors());
		} else {
//            model.addAttribute("user",user);
		}

		return "user";
	}

	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		int result1 = Field.buildId(1, "cacbro_cd");
		return String.valueOf(result1);
	}
}
