package com.zinnaworks.nxpgtool.dao;

import java.util.List;
import java.util.Map;

import com.zinnaworks.nxpgtool.api.Field;

public interface FieldDao {
	public void insert(String ifId, Field f);
	//public void insertTreeMode(String ifId, Field f);
	public List<Field> getAllList(String ifId);
	//public List<Field> getListByDepth(String ifId, int depth);
	public Map<Integer, Field> getAllMap(String ifId);
	
}
