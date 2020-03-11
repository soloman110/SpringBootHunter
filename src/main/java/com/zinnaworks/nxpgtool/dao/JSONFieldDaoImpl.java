package com.zinnaworks.nxpgtool.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zinnaworks.nxpgtool.api.Field;

public class JSONFieldDaoImpl implements FieldDao {

	// Field Id로 빠른접근하기 위함
	private Map<Integer, Field> fieldMap = new HashMap<>();
	// 모든 Filed를 저장하는 List
	private List<Field> allList = new ArrayList<Field>();

	@Override
	public void insert(String ifId, Field f) {
		fieldMap.put(f.getId(), f);
		Field parent = fieldMap.get(f.getParentId());

		if (parent != null) {
			parent.setChild(f.getId());
			fieldMap.put(f.getParentId(), parent);
		}
		allList.add(f);
	}

	@Override
	public List<Field> getAllList(String ifId) {
		return allList;
	}

	@Override
	public Map<Integer, Field> getAllMap(String ifId) {
		return fieldMap;
	}
}