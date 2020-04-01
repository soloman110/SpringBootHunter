package com.zinnaworks.nxpgtool.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface GridService {
	public List<String> searchVodPkg() throws InterruptedException, ExecutionException, TimeoutException;
	public Map<String, Object> getGridByMenuId(String menuId);
}
