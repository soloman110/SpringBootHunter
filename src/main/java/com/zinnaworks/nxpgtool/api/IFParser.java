package com.zinnaworks.nxpgtool.api;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zinnaworks.nxpgtool.exception.DataNotValidException;


public class IFParser {

	public Map<String, Object> parse(String testUrl) throws FileNotFoundException, DataNotValidException {
		Map<String, Object> data = IF.getData(testUrl);
		if(data.get("result" ) == null || !data.get("result").equals("0000")) {
			throw new DataNotValidException(testUrl);
		}
		return walk(data, 1, Field.getRoot());
	}

	public Map<String, Object> walk(Object o, int depth, Field parent) {
		Field.Type t = getType(o);
		if (t == Field.Type.String)
			throw new RuntimeException("좋은하루 되세요...");

		Map<String, Object> toReturn = new HashMap<>();

		if (t == Field.Type.Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				String name = entry.getKey();
				Field f = buildField(depth, name, entry.getValue());
				f.setParentId(parent.getId());
				parent.setChild(f.getId());

				Object v = entry.getValue();
				if (getType(v) != Field.Type.String) {
					toReturn.put(name, walk(entry.getValue(), depth + 1, f));
				} else {
					// field명만 알아내됨. //toReturn.put(name, f);
					toReturn.put(name, f);
				}
			}
		} else if (t == Field.Type.List) {
			@SuppressWarnings("unchecked")
			ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) o;
			for (Map<String, Object> map : list) {
				return walk(map, depth, parent);
			}
		}
		return toReturn;
	}

	public Field buildField(int depth, String name, Object v) {
		Field.Type type = getType(v);
		Field f = (type == Field.Type.List || type == Field.Type.Map) ? new CompositeField() : new LeafField();
		return f.setType(type).setDepth(depth).setName(name).setId(Field.buildId(depth, name));
	}

	public Field.Type getType(Object o) {
		if (o instanceof Map)
			return Field.Type.Map;
		else if (o instanceof ArrayList)
			return Field.Type.List;
		else
			return Field.Type.String;
	}
}