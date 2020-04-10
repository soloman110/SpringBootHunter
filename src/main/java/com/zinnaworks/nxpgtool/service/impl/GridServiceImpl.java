package com.zinnaworks.nxpgtool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.zinnaworks.nxpgtool.common.NXPGCommon;
import com.zinnaworks.nxpgtool.redis.RedisClient;
import com.zinnaworks.nxpgtool.service.GridService;
import com.zinnaworks.nxpgtool.util.CastUtil;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@Service
public class GridServiceImpl implements GridService {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	private ThreadPoolTaskExecutor gridServiceExecutor;

	@Override
	@Cacheable(value = "gridCache", condition="#isCache == true", key="#root.targetClass")
	@CachePut(value = "gridCache",condition="#isCache == false", key="#root.targetClass")
	public List<Pair<String, Integer>> searchVodPkg(boolean isCache) throws InterruptedException, ExecutionException, TimeoutException {
		
		Cursor<Entry<String, Object>> cursor = redisClient.hscan(NXPGCommon.GRID_CONTENTS);
		List<Future<Pair<String, Integer>>> futureCol = new ArrayList<Future<Pair<String, Integer>>>();
		List<Pair<String, Integer>> menuIAndTypeList = new ArrayList<>();
		try {
			if (cursor != null) {
				cursor.forEachRemaining(new Consumer<Map.Entry<String, Object>>() {
					@Override
					public void accept(Entry<String, Object> entry) {
						Future<Pair<String, Integer>> future = gridServiceExecutor.submit(new gridTask(entry));
						futureCol.add(future);
					}
				});
				for(Future<Pair<String, Integer>> f : futureCol) {
					Pair<String, Integer> result = f.get(3, TimeUnit.SECONDS);
					if (result != null) {
						menuIAndTypeList.add(result);
					}
				}
			}
		} finally {
			try {
				cursor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return menuIAndTypeList;
	}
	
	private class gridTask implements Callable<Pair<String, Integer>> {
		Entry<String, Object> entry;
		public gridTask(Entry<String, Object> entry) {
			super();
			this.entry = entry;
		}

		@Override
		public Pair<String, Integer> call() throws Exception {
			Pair<String, Integer> toReturn = null;
			String menuId = entry.getKey();
			final Object obj = entry.getValue();
			if(!Objects.isNull(obj)) {
				try {
					Map<String, Object> gridMap = JsonUtil.jsonToObjectHashMap(obj.toString(), String.class, Object.class);
					if (gridMap != null) {
						List<Map<String, Object>> list = CastUtil.getObjectToMapList(gridMap.get("contents"));
						int t = findVodAndPkgProduct(list);
						if(t <=0)
							return null;
						else {
							toReturn = new ImmutablePair<String,Integer>(menuId, Integer.valueOf(t));
						}
					} 
				} catch (Exception e2) {
					e2.printStackTrace();
					return null;
				}
			}
			return toReturn;
		}
	}

	@Override
	public Map<String, Object> getGridByMenuId(String menuId) {
		String json = (String) redisClient.hget(NXPGCommon.GRID_CONTENTS, menuId);
		Map<String, Object> gridMap = JsonUtil.jsonToObjectHashMap(json, String.class, Object.class);
		return gridMap;
	}
	
	/**
	 * 
	 * 그리드 리스트 둥에 게이트웨이 또는 VOD + 관련상품 시놉시스가 존재한지 확인
	 * 존재하면 해당 타입를 리턴한다. (VOD only, gateway only, VOD And gateway)
	 * 
	 * @param list 그리드 리스트
	 * @return 0 없음; 1 게이트웨이 시놉시스 만 존재; 2 VOD + 관련상품 시놉시스 만 존재 3 게이트웨이 및 VOD 둘다 존대 
	 */
	
	//03==> 게이트웨이 시놉시스; 04==>VOD + 관련상품 시놉시스
	private int findVodAndPkgProduct(List<Map<String, Object>> list) {
		int result = 0;
		boolean isGwExist = false;
		boolean isVodExist = false;
		if (list == null || list.size() == 0) {
			return 0;
		}
		ListIterator<Map<String, Object>> it = list.listIterator();
		while (it.hasNext()) {
			Map<String, Object> map = it.next();
			String synon_typ_cd = (String) map.get("synon_typ_cd");
			if("03".equals(synon_typ_cd) && !isGwExist) {
				isGwExist = true;
				result += 1;
			}
			else if("04".equals(synon_typ_cd) && !isVodExist) {
				isVodExist = true;
				result +=2;
			}
		}
		return result;
	}
}
