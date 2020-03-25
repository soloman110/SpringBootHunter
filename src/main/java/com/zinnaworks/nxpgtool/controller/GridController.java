package com.zinnaworks.nxpgtool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.nxpgtool.service.GridService;

@RequestMapping("/grid")
@Controller
public class GridController {

	@Autowired
	private GridService gridService;
	
	@RequestMapping("/grid")
	@ResponseBody
	public Map<String, Object> test(@RequestParam String menuId) throws Exception {
		Map<String, Object> result = gridService.getGridByMenuId(menuId);
		return result;
	}
	
	@RequestMapping("/search/vodpkg")
	@ResponseBody
	public List<Map<String, String>> search() throws Exception {
		List<String> menuList = gridService.searchVodPkg();
		List<Map<String, String>> mapList = menuList.parallelStream().map(new Function<String, Map<String, String>>() {
			@Override
			public Map<String, String> apply(String menuId) {
				Map<String, String> map = new HashMap<>();
				map.put("menuId", menuId);
				return map;
			}
		}).collect(Collectors.toList());
		
		return mapList;
	}
}
