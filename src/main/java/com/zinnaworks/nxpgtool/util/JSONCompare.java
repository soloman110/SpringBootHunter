package com.zinnaworks.nxpgtool.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zinnaworks.nxpgtool.entity.CompareVO;

public class JSONCompare {	

	// JSONArray 2개를 List로 만든다.
	public static List<CompareVO> getMenuListToJSONArray(
			String parentId, String parentKey, List<String> ruleOut,
			JSONArray jarrResult, JSONArray jarrAnotherResult) {
		List<CompareVO> resultList = new ArrayList<CompareVO>();
		CompareVO result = new CompareVO();
		
		if (jarrResult == null) {
			result.setKey(parentId);
			result.setValue("Array is NULL");
			result.setError(true);
			resultList.add(result);
			return resultList;
		}
		if (jarrAnotherResult == null) {
			result.setKey(parentId);
			result.setValue("Another Array is NULL");
			result.setError(true);
			resultList.add(result);
			return resultList;
		} 
		
		// 기존 리스트를 넣어준다.
        	for (int i = 0; i < jarrResult.length(); i++) {
        		try {
        			result = new CompareVO();

    		        result.setId(parentId + i);
    		        result.setKey(parentKey + i);
    		        result.setValue(parentKey + i);
    		        result.setAnotherKey(parentKey + i);
    		        result.setAnotherValue(parentKey + i);
    		       
        			if (jarrAnotherResult.length() < i) {
        				result.setValue("Another Array Index Out of Bounds");
        				result.setError(true);
        				resultList.add(result);
        				continue;
        			}
        			
		        Object obj = jarrResult.get(i);
		        Object anotherObj = jarrAnotherResult.get(i);
		        
		        // JSONObject 
		        if (obj instanceof JSONObject) {
		        		result.setChildList(getMenuListToJSONObject(parentId + i, ruleOut, (JSONObject) obj, (JSONObject) anotherObj));
		        }
		        
		        resultList.add(result);
		        
        		} catch (Exception e) {
        			result.setKey(parentKey);
        			result.setValue("getMenuListToJSONOArray - " + parentKey + " Error : " + e.toString());
        			result.setError(true);
        			resultList.add(result);
        		}
        	}
		
		return resultList;
	}
	// JSONObject 2개를 List로 만든다.
	public static List<CompareVO> getMenuListToJSONObject(String parentKey, List<String> ruleOut, 
			JSONObject jobjResult, JSONObject jobjAnotherResult) {
		List<CompareVO> resultList = new ArrayList<CompareVO>();
		CompareVO result = new CompareVO();
		
		if (jobjResult == null) {
			result.setKey(parentKey);
			result.setValue("Object is NULL");
			result.setError(true);
			resultList.add(result);
			return resultList;
		}
		if (jobjAnotherResult == null) {
			result.setKey(parentKey);
			result.setValue("Another Object is NULL");
			result.setError(true);
			resultList.add(result);
			return resultList;
		} 
		
		// 기존 리스트를 넣어준다.
        	for (Iterator<String> iterator = jobjResult.keySet().iterator(); iterator.hasNext();) {
        		try {
        			result = new CompareVO();
        			
	        		String key = iterator.next();
	        		
	        		boolean cancel = false;
	        		
	        		// 빼려는 키는 그냥 넘어간다. 
	        		for (int i = 0; i < ruleOut.size(); i++) {
	        			if (StringUtils.StringEqueals(ruleOut.get(i), key)) {
	        				cancel = true;
	        				break;
	        			}
	        		}
	        		
	        		if (cancel) continue;
	        		
		        Object obj = jobjResult.get(key);
		        Object anotherObj = jobjAnotherResult.get(key);
		        
		        result.setId(parentKey + "_" + key);
		        result.setKey(key);
		        result.setValue(obj);
		        result.setAnotherKey(key);
		        result.setAnotherValue(anotherObj);
		        
		        // JSONObject
		        if (obj instanceof JSONObject) {
		        		result.setChildList(getMenuListToJSONObject(result.getId(), ruleOut, (JSONObject) obj, (JSONObject) anotherObj));
		        }
		        // JSONArray
		        else if (obj instanceof JSONArray) {
	        			result.setChildList(getMenuListToJSONArray(result.getId(), key, ruleOut, (JSONArray) obj, (JSONArray) anotherObj));
		        }
		        
		        resultList.add(result);
		        
        		} catch (Exception e) {
        			result.setKey(parentKey);
        			result.setValue("getMenuListToJSONObject - " + parentKey + " Error : " + e.toString());
        			result.setError(true);
        			resultList.add(result);
        		}
        	}
		
		return resultList;
	}
	
	// 이 아이템이 들어있는지 확인한다.
	public static CompareVO findItem(List<CompareVO> list, String name, boolean isAnother) {
		if (list == null) return null;
		for(int i = 0; i < list.size(); i++) {
			CompareVO item = list.get(i);
			if (isAnother) {
				if (item.getAnotherKey().equals(name)) return item;
			} else {
				if (item.getKey().equals(name)) return item;
			}
		}
		
		return null;
	}
	
	// JSONObject 2개를 List로 만든다.
	public static List<CompareVO> getListToJSONObject(JSONObject jobjResult, JSONObject jobjAnotherResult) {
		
		List<CompareVO> resultList = new ArrayList<CompareVO>();

		// 맨 윗 정보를 가지고 오기.
		try {
			
			if (jobjResult != null) {
				// 기존 리스트를 넣어준다.
		        	for (Iterator<String> iterator = jobjResult.keySet().iterator(); iterator.hasNext();) {
			            String key = iterator.next();
			            CompareVO result = new CompareVO();
			            result.setKey(key);
			            result.setValue(jobjResult.get(key));
			            resultList.add(result);
		        	}
			}
        	
        	if (jobjAnotherResult != null) {
	        	// 비교 리스트를 넣어준다.
	        	for (Iterator<String> iterator = jobjAnotherResult.keySet().iterator(); iterator.hasNext();) {
		            String key = iterator.next();
		            CompareVO result = null;
		            // 기존 들어가 있던 아이템을 찾는다.
		            result = findItem(resultList, key, false);
		            // 만약 없으면 만들고 넣어준다.
		            if (result == null) {
		            	result = new CompareVO();
		            	resultList.add(result);
		            }
		            result.setAnotherKey(key);
		            result.setAnotherValue(jobjAnotherResult.get(key));
	        	}
        	}
                	
        	// 세부 내용 확인
        	for (int i = 0; i < resultList.size(); i++) {
        		CompareVO result = resultList.get(i);
        		// JSONObject일 경우
        		if (result.getValue() instanceof JSONObject) {
        			// 둘다 JSONObject일 경우
        			if (result.getAnotherValue() instanceof JSONObject) {
        				result.setChildList(getListToJSONObject((JSONObject) result.getValue(), (JSONObject) result.getAnotherValue()));
        			} 
        			// 기존만 JSONObject일 경우
        			else {
        				result.setChildList(getListToJSONObject((JSONObject) result.getValue(), null));
        			}
        		}
        		// 비교대상만 JSONObject일 경우
        		else if (result.getAnotherValue() instanceof JSONObject) {
    				result.setChildList(getListToJSONObject(null, (JSONObject) result.getAnotherValue()));
        		}
        		
        		// JSONArray일 경우
        		if (result.getValue() instanceof JSONArray) {
        			// 둘다 JSONArray일 경우
        			if (result.getAnotherValue() instanceof JSONArray) {
        				result.setChildList(getListToJSONObject(getJSONObjectToList((JSONArray) result.getValue()), 
        						getJSONObjectToList((JSONArray) result.getAnotherValue())));
        			}
        			// 기존만 JSONArray일 경우
        			else {
        				result.setChildList(getListToJSONObject(getJSONObjectToList((JSONArray) result.getValue()), null));
        			}
        		}
        		// 비교대상만 JSONArray일 경우
        		else if (result.getAnotherValue() instanceof JSONArray) {
    				result.setChildList(getListToJSONObject(null, getJSONObjectToList((JSONArray) result.getAnotherValue())));
        		}
          	}
		} catch (Exception e) {
			System.out.println("getListToJSONObject Error " + e.toString());
			return null;
		}
		return resultList;
	}
	
	// 리스트에서 가장 내용이 많은 JSONObject를 가지고 온다.
	public static JSONObject getJSONObjectToList(JSONArray list) {
	    	Object obj = null;
	    	int size = 0;
	    	// JSONObject에 값이 가장 많은 걸 찾는다.
	    	for (int i = 0; i < list.length(); i++) {
	    		Object item = list.get(i);
	    		if (item instanceof JSONObject) {
	    			// 이 JSONObject에 몇가지의 값이 있는지 확인한다.
	    			JSONObject jsonObj = (JSONObject) item;
	    			if (size < jsonObj.keySet().size()) {
	    				size = jsonObj.keySet().size();
	    				obj = item;
	    			}
	    		}
	    	}
	    	
	    	return (JSONObject) obj;
	}

}
