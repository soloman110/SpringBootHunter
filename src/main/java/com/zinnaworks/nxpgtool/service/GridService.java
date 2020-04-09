package com.zinnaworks.nxpgtool.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.tuple.Pair;

public interface GridService {
	public List<Pair<String, Integer>> searchVodPkg(boolean isCache) throws InterruptedException, ExecutionException, TimeoutException;
	public Map<String, Object> getGridByMenuId(String menuId);
}
