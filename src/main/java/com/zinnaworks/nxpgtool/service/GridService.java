package com.zinnaworks.nxpgtool.service;

import java.util.List;
import java.util.Map;

public interface GridService {
	public List<String> searchVodPkg();
	public Map<String, Object> getGridByMenuId(String menuId);
}
