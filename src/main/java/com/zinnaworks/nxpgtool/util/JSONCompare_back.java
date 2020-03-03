package com.zinnaworks.nxpgtool.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zinnaworks.nxpgtool.entity.CompareVO;

public class JSONCompare_back {
	
	private static String strJSONObject = "JSONObject";
	private static String strJSONArray = "JSONArray";
	
	public static String ObjectToString(Object obj) {
		String result = null;
		if (obj == null) return result;
		
		if (obj instanceof String) return obj.toString();
		else if (obj instanceof Integer) return obj.toString();
		else if (obj instanceof JSONObject) return strJSONObject;
		else if (obj instanceof JSONArray) return strJSONArray;
		
		return result;
	}
	
	public static String getSpace(int level) {
		String result = "";
        for (int i = 0; i < level; i++) result = result + "&nbsp;&nbsp;&nbsp;&nbsp;";
        return result;
	}

	// 시놉시스 확인
	public static String CheckSynopsis(String id, String parentKey,
			String value, String anotherValue,
			String gridUrl, String gridAnotherUrl,
			String synopsisUrl, String synopsisAnotherUrl, 
			int level, boolean chkValue, boolean chkOpen) {
		if (value == null) return "";
		if (anotherValue == null) return "";
		if (synopsisUrl == null) return "";
		if (synopsisAnotherUrl == null) return "";
		
		if (value.equals("")) return "";
		if (anotherValue.equals("")) return "";
		if (synopsisUrl.equals("")) return "";
		if (synopsisAnotherUrl.equals("")) return "";
		
		JSONObject jobjResult = null;
		JSONObject jobjAnotherResult = null;
		
		// 데이터 불러오기.
		String url = synopsisUrl + "&con_id=" + value;
		String anotherUrl = synopsisAnotherUrl + "&con_id=" + anotherValue;

		String result = HttpUtils.getData(url);
		String anotherResult = HttpUtils.getData(anotherUrl);
		
		if (result == null || anotherResult == null) {
	        return "<li>" + id + " - Result Error</label></li>";
		}

		try {
			jobjResult = new JSONObject(result);
			jobjAnotherResult = new JSONObject(anotherResult);	
		} catch (Exception e) {
	        return "<li>" + id + " - JSON Error</label></li>";
		}
		
		return CompareJSONObject(id + "-synopsis", "synopsis", jobjResult, jobjAnotherResult, 
				gridUrl, gridAnotherUrl, 
				synopsisUrl, synopsisAnotherUrl, 
				level, chkValue, chkOpen);
	}
	
	// 그리도 확인
	public static String CheckGrid(String id, String parentKey,
			String value, String anotherValue,
			String gridUrl, String gridAnotherUrl,
			String synopsisUrl, String synopsisAnotherUrl, 
			int level, boolean chkValue, boolean chkOpen) {
		if (value == null) return "";
		if (anotherValue == null) return "";
		if (gridUrl == null) return "";
		if (gridAnotherUrl == null) return "";
		
		if (value.equals("")) return "";
		if (anotherValue.equals("")) return "";
		if (gridUrl.equals("")) return "";
		if (gridAnotherUrl.equals("")) return "";
		
		JSONObject jobjResult = null;
		JSONObject jobjAnotherResult = null;
		
		// 데이터 불러오기.
		String url = gridUrl + "&menu_id=" + value;
		String anotherUrl = gridAnotherUrl + "&menu_id=" + anotherValue;

		String result = HttpUtils.getData(url);
		String anotherResult = HttpUtils.getData(anotherUrl);
		
		if (result == null || anotherResult == null) {
	        return "<li>" + id + " - Result Error</label></li>";
		}

		try {
			jobjResult = new JSONObject(result);
			jobjAnotherResult = new JSONObject(anotherResult);	
		} catch (Exception e) {
	        return "<li>" + id + " - JSON Error</label></li>";
		}
		
		return CompareJSONObject(id + "-gird", "grid", jobjResult, jobjAnotherResult, 
				gridUrl, gridAnotherUrl, 
				synopsisUrl, synopsisAnotherUrl, 
				level, chkValue, chkOpen);
	}
	
	// JSONArray 2개를 비교한다.
	// 1번 기준으로 2번에 없으면 넘어간다.
	public static String CompareJSONArray(String id, String parentKey, JSONArray jarr, JSONArray jarrAnother, 
			String gridUrl, String gridAnotherUrl,
			String synopsisUrl, String synopsisAnotherUrl,
			int level, boolean chkValue, boolean chkOpen) {
//		String result = "";

//		result = result + "<ul><li><input type='checkbox' id='" + id + level + "'";
		FileUtils.fileWrite(FileUtils.strFileName, "<ul><li><input type='checkbox' id='" + id + level + "'");
		if (chkOpen) {
//			result = result + " checked='checked' "; 
			FileUtils.fileWrite(FileUtils.strFileName, " checked='checked' ");
		} 
//		result = result + "><label for='" + id + level + "'>" + parentKey + " - JSONArray</label><ul>";
		FileUtils.fileWrite(FileUtils.strFileName, "><label for='" + id + level + "'>" + parentKey + " - JSONArray</label><ul>");
		
		if (jarr == null) {
//	        result = result + "<li>" + parentKey + " - Array is NULL</label></li>";
			FileUtils.fileWrite(FileUtils.strFileName, "<li>" + parentKey + " - Array is NULL</label></li>");
		} else if (jarrAnother == null) {
//	        result = result + "<li>" + parentKey + " - Another Array is NULL</li>";
			FileUtils.fileWrite(FileUtils.strFileName, "<li>" + parentKey + " - Another Array is NULL</li>");
		} else {
			// 테스트 추후 삭제
			jarrAnother.remove(jarrAnother.length()-2);
			/////
			for (int idx = 0; idx < jarr.length(); idx++) {
				try {
					JSONObject jobj = jarr.getJSONObject(idx);
					if (jarrAnother.length() == idx) {
//						result = result + "<li><font color='red'>" + parentKey + " Another Array Index Out of Bounds(" + idx + ")</font></li>";
						FileUtils.fileWrite(FileUtils.strFileName, "<li><font color='red'>" + parentKey + " Another Array Index Out of Bounds(" + idx + ")</font></li>");
						break;
					}
					JSONObject jobjAnother = jarrAnother.getJSONObject(idx);
//					result = result + CompareJSONObject(id + idx, parentKey + idx, jobj, jobjAnother, 
//							gridUrl, gridAnotherUrl,
//		        				synopsisUrl, synopsisAnotherUrl,
//		        				level+1, chkValue, chkOpen);
					FileUtils.fileWrite(FileUtils.strFileName, CompareJSONObject(id + idx, parentKey + idx, jobj, jobjAnother, 
							gridUrl, gridAnotherUrl,
		        				synopsisUrl, synopsisAnotherUrl,
		        				level+1, chkValue, chkOpen));
				} catch (Exception e) {
//					result = result + "<li><font color='red'>CompareJSONArray parentKey - " + parentKey + " Error : " + e.toString() +"</font></li>";
					FileUtils.fileWrite(FileUtils.strFileName, "<li><font color='red'>CompareJSONArray parentKey - " + parentKey + " Error : " + e.toString() +"</font></li>");
				}
			}
			
		}
//		result = result + "</ul></li></ul>";
		FileUtils.fileWrite(FileUtils.strFileName, "</ul></li></ul>");
		return "";
	}
	
	// JSONObject 2개를 비교한다.
	// 1번 기준으로 2번에 없으면 넘어가버린다.
	public static String CompareJSONObject(String id, String parentKey, JSONObject jobj, JSONObject jobjAnother,
			String gridUrl, String gridAnotherUrl,
			String synopsisUrl, String synopsisAnotherUrl, 
			int level, boolean chkValue, boolean chkOpen) {
//		String result = "";

//		result = result + "<ul><li><input type='checkbox' id='" + id + level + "'";
		FileUtils.fileWrite(FileUtils.strFileName, "<ul><li><input type='checkbox' id='" + id + level + "'");
		
		if (chkOpen) {
//			result = result + " checked='checked' "; 
			FileUtils.fileWrite(FileUtils.strFileName, " checked='checked' ");
		}
//		result = result + "><label for='" + id + level + "'>" + parentKey + " - JSONObject</label><ul>";
		FileUtils.fileWrite(FileUtils.strFileName, "><label for='" + id + level + "'>" + parentKey + " - JSONObject</label><ul>");
				
		if (jobj == null) {
//	        result = result + "<li>" + parentKey + " - Object is NULL</label></li>";
			FileUtils.fileWrite(FileUtils.strFileName, "<li>" + parentKey + " - Object is NULL</label></li>");
		}
		else if (jobjAnother == null) {
//	        result = result + "<li>" + parentKey + " - Another Object is NULL</li>";
			FileUtils.fileWrite(FileUtils.strFileName, "<li>" + parentKey + " - Another Object is NULL</li>");
		} else {
			// 기존 리스트를 넣어준다.
	        	for (Iterator<String> iterator = jobj.keySet().iterator(); iterator.hasNext();) {
	        		try {
		        		String key = iterator.next();
			        Object obj = jobj.get(key);
			        Object anotherObj = jobjAnother.get(key);
		
			    		String value = ObjectToString(obj);
			    		String anotherValue = ObjectToString(anotherObj);
			    		
			        if (StringUtils.StringEqueals(value, anotherValue) == false) {
			        		// 다를때
//			        		result = result + "<li><font color='red'>" + key + " - " + value + "(" + anotherValue + ")</font></li>";
			    			FileUtils.fileWrite(FileUtils.strFileName, "<li><font color='red'>" + key + " - " + value + "(" + anotherValue + ")</font></li>");
			        } else {
			        		// 같을떄
			        		if (chkValue == false) {
//			        			result = result + "<li>" + key + " - " + value + "(" + anotherValue + ")</li>";
			        			FileUtils.fileWrite(FileUtils.strFileName, "<li>" + key + " - " + value + "(" + anotherValue + ")</li>");
			        		}
			        }
			        
			        // 그리드 확인
			        if (key.equals("menu_id")) {
			        		if (id.indexOf("home_all_menu-menus") > -1 
			        				|| id.indexOf("home_my_ppm-menus") > -1) {
//			        			result = result + CheckGrid(id + "-" + key, key, value, anotherValue,
//			        					gridUrl, gridAnotherUrl,
//			        					synopsisUrl, synopsisAnotherUrl,
//			        					level+1, chkValue, chkOpen);
			        			FileUtils.fileWrite(FileUtils.strFileName, CheckGrid(id + "-" + key, key, value, anotherValue,
			        					gridUrl, gridAnotherUrl,
			        					synopsisUrl, synopsisAnotherUrl,
			        					level+1, chkValue, chkOpen));
			        				
			        		}
			        }
			        // 시놉시스 확인
			        if (key.equals("con_id")) {
			        		if (id.indexOf("grids") > -1) {
//			        			result = result + CheckSynopsis(id + "-" + key, key, value, anotherValue,
//			        					gridUrl, gridAnotherUrl,
//			        					synopsisUrl, synopsisAnotherUrl,
//			        					level+1, chkValue, chkOpen);
			        			FileUtils.fileWrite(FileUtils.strFileName, CheckSynopsis(id + "-" + key, key, value, anotherValue,
			        					gridUrl, gridAnotherUrl,
			        					synopsisUrl, synopsisAnotherUrl,
			        					level+1, chkValue, chkOpen));
			        				
			        		}
			        }
			        
			        if (obj instanceof JSONObject) {
//			        		result = result + CompareJSONObject(id + "-" + key, key, (JSONObject) obj, (JSONObject) anotherObj, 
//			        				gridUrl, gridAnotherUrl,
//			        				synopsisUrl, synopsisAnotherUrl,
//			        				level+1, chkValue, chkOpen);
			    			CompareJSONObject(id + "-" + key, key, (JSONObject) obj, (JSONObject) anotherObj, 
			        				gridUrl, gridAnotherUrl,
			        				synopsisUrl, synopsisAnotherUrl,
			        				level+1, chkValue, chkOpen);
			        } else if (obj instanceof JSONArray) {
//		        			result = result + CompareJSONArray(id + "-" + key, key, (JSONArray) obj, (JSONArray) anotherObj, 
//		        					gridUrl, gridAnotherUrl,
//			        				synopsisUrl, synopsisAnotherUrl,
//			        				level+1, chkValue, chkOpen);
		        			CompareJSONArray(id + "-" + key, key, (JSONArray) obj, (JSONArray) anotherObj, 
		        					gridUrl, gridAnotherUrl,
			        				synopsisUrl, synopsisAnotherUrl,
			        				level+1, chkValue, chkOpen);
			        }
	        		} catch (Exception e) {
//	        			result = result + "<li><font color='red'>CompareJSONObject parentKey - " + parentKey + " Error : " + e.toString() +"</font></li>";
	        			FileUtils.fileWrite(FileUtils.strFileName, "<li><font color='red'>CompareJSONObject parentKey - " + parentKey + " Error : " + e.toString() +"</font></li>");
	        		}
	        	}
		}
//		result = result + "</ul></li></ul>";
		FileUtils.fileWrite(FileUtils.strFileName, "</ul></li></ul>");
		return "";
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
