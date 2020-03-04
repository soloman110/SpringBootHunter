package com.zinnaworks.nxpgtool.entity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zinnaworks.nxpgtool.util.StringUtils;

public class CompareVO {
	
	private boolean error = false;
	private String id = "";
	private String key = "";
	private Object value = null;
	
	private String anotherKey = "";
	private Object anotherValue = null;
	
	private List<CompareVO> childList = null;
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getAnotherValue() {
		return anotherValue;
	}

	public void setAnotherValue(Object anotherValue) {
		this.anotherValue = anotherValue;
	}

	public List<CompareVO> getChildList() {
		return childList;
	}

	public void setChildList(List<CompareVO> childList) {
		this.childList = childList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAnotherKey() {
		return anotherKey;
	}

	public void setAnotherKey(String anotherKey) {
		this.anotherKey = anotherKey;
	}


	public String toValueString(Object obj) {
		String strValue = "";

		if (obj instanceof JSONObject) strValue = "JSONObject";
		else if (obj instanceof JSONArray) strValue = "JSONArray";
		else if (obj instanceof String) strValue = obj.toString();
		else if (obj instanceof Integer) strValue = obj.toString();
		else if (obj == null) strValue = null;
		else strValue = obj.getClass().toString();
		
		return strValue;
	}
	
	public String toClassString(Object obj) {
		String strClass = "";

		if (obj instanceof JSONObject) strClass = "JSONObject";
		else if (obj instanceof JSONArray) strClass = "JSONArray";
		else if (obj instanceof String) strClass = "String";
		else if (obj instanceof Integer) strClass = "Integer";
		else if (obj == null) strClass = "NULL";
		else strClass = value.getClass().toString();
		
		return strClass;
	}
	
	public String toDifferentValue() {
		String result = "";
		// Label안에 들어가는 
		String strResultValue = "";
	
		// 만약 메인 key가 없으면 
		if (key.equals("")) {
			strResultValue = "<font color='red'>(" + id + ")</font>";
		} else { 
			strResultValue = id;
		}
		
		String strValue = "";
		String strAnotherValue = "";

		if (value instanceof JSONObject) strValue = "JSONObject";
		else if (value instanceof JSONArray) strValue = "JSONArray";
		else if (value instanceof String) strValue = value.toString();
		else if (value instanceof Integer) strValue = value.toString();
		else if (value == null) strValue = null;
		else strValue = value.getClass().toString();
		
		if (anotherValue instanceof JSONObject) strAnotherValue = "JSONObject";
		else if (anotherValue instanceof JSONArray) strAnotherValue = "JSONArray";
		else if (anotherValue instanceof String) strAnotherValue = anotherValue.toString();
		else if (anotherValue instanceof Integer) strAnotherValue = anotherValue.toString();
		else if (anotherValue == null) strAnotherValue = null;
		else strAnotherValue = anotherValue.getClass().toString();

		if (StringUtils.StringEqueals(strValue, strAnotherValue)) {
			strResultValue = "";
		} else {
			strResultValue += " - " + strValue + "(<font color='blue'>" + strAnotherValue + "</font>)";
		}
		
		if (childList != null) {
			if (childList.size() > 0) {				
				for(int i = 0; i < childList.size(); i++) {
					result += childList.get(i).toDifferentValue();
				}
			} else 
				result += strResultValue;
		} else 
			result += strResultValue;
		
		if (!StringUtils.StringEqueals(result, ""))
			result = "<li>" + result + "</li>";

		return result;
	}
	
	public String toValueString(boolean isOpen) {
		String result = "";
		// Label안에 들어가는 
		String strResultValue = "";
		
		// tree 구조로 보여주기위한 값 
		result += "<li>";
		
		// 만약 메인 key가 없으면 
		if (key.equals("")) {
			strResultValue = "<font color='red'>(" + anotherKey + ")</font>";
		} else { 
			strResultValue = key;
		}
		
		String strValue = "";
		String strAnotherValue = "";

		if (value instanceof JSONObject) strValue = "JSONObject";
		else if (value instanceof JSONArray) strValue = "JSONArray";
		else if (value instanceof String) strValue = value.toString();
		else if (value instanceof Integer) strValue = value.toString();
		else if (value == null) strValue = null;
		else strValue = value.getClass().toString();
		
		if (anotherValue instanceof JSONObject) strAnotherValue = "JSONObject";
		else if (anotherValue instanceof JSONArray) strAnotherValue = "JSONArray";
		else if (anotherValue instanceof String) strAnotherValue = anotherValue.toString();
		else if (anotherValue instanceof Integer) strAnotherValue = anotherValue.toString();
		else if (anotherValue == null) strAnotherValue = null;
		else strAnotherValue = anotherValue.getClass().toString();

		if (StringUtils.StringEqueals(strValue, strAnotherValue)) {
			strResultValue += " - " + strValue;
		} else {
			strResultValue += " - " + strValue + "(<font color='blue'>" + strAnotherValue + "</font>)";
		}
		
		if (childList != null) {
			if (childList.size() > 0) {
				result += "<label for='" + id + "'>" + strResultValue + "</label>";
				result += "<input type='checkbox' id='" + id + "'";
				
				// 만약 오픈이면 오픈 상태로 보여줌
				if (isOpen) result += " checked='checked'";
				
				result += ">";
				result += "<ul>";
				for(int i = 0; i < childList.size(); i++) {
					result += childList.get(i).toValueString(isOpen);
				}
				result += "</ul>";
			} else 
				result += strResultValue;
		} else 
			result += strResultValue;

		// tree 구조로 보여주기위한 값 
		result += "</li>";
		return result;
	}

	public String toClassString(boolean isOpen) {
		String result = "";
		// Label안에 들어가는 
		String strResultValue = "";
		
		// tree 구조로 보여주기위한 값 
		result += "<li>";
		
		// 만약 메인 key가 없으면 
		if (key.equals("")) {
			strResultValue = "<font color='red'>(" + anotherKey + ")</font>";
		} else { 
			strResultValue = key;
		}
		
		String strClass = "";
		String strAnotherClass = "";

		if (value instanceof JSONObject) strClass = "JSONObject";
		else if (value instanceof JSONArray) strClass = "JSONArray";
		else if (value instanceof String) strClass = "String";
		else if (value instanceof Integer) strClass = "Integer";
		else if (value == null) strClass = "NULL";
		else strClass = value.getClass().toString();
		
		if (anotherValue instanceof JSONObject) strAnotherClass = "JSONObject";
		else if (anotherValue instanceof JSONArray) strAnotherClass = "JSONArray";
		else if (anotherValue instanceof String) strAnotherClass = "String";
		else if (anotherValue instanceof Integer) strAnotherClass = "Integer";
		else if (anotherValue == null) strAnotherClass = "NULL";
		else strAnotherClass = anotherValue.getClass().toString();

		if (StringUtils.StringEqueals(strClass, strAnotherClass)) {
			strResultValue += " - " + strClass;
		} else {
			strResultValue += " - " + strClass + "(<font color='blue'>" + strAnotherClass + "</font>)";
		}
		
		if (childList != null) {
			if (childList.size() > 0) {
				result += "<label for='" + id + "'>" + strResultValue + "</label>";
				result += "<input type='checkbox' id='" + id + "'";
				
				// 만약 오픈이면 오픈 상태로 보여줌
				if (isOpen) result += " checked='checked'";
				
				result += ">";
				result += "<ul>";
				for(int i = 0; i < childList.size(); i++) {
					result += childList.get(i).toClassString(isOpen);
				}
				result += "</ul>";
			} else 
				result += strResultValue;
		} else 
			result += strResultValue;

		// tree 구조로 보여주기위한 값 
		result += "</li>";
		return result;
	}
	/*
	public String toClassString(int index) {
		// 기존 값
		String result = key;
		if (key.equals("")) {
			result = "<font color='red'>(" + anotherKey + ")</font>";
		} 
		
		String strClass = "";
		String strAnotherClass = "";

		if (value instanceof JSONObject) strClass = "JSONObject";
		else if (value instanceof JSONArray) strClass = "JSONArray";
		else if (value instanceof String) strClass = "String";
		else if (value instanceof Integer) strClass = "Integer";
		else if (value == null) strClass = "NULL";
		else strClass = value.getClass().toString();
		
		if (anotherValue instanceof JSONObject) strAnotherClass = "JSONObject";
		else if (anotherValue instanceof JSONArray) strAnotherClass = "JSONArray";
		else if (anotherValue instanceof String) strAnotherClass = "String";
		else if (anotherValue instanceof Integer) strAnotherClass = "Integer";
		else if (anotherValue == null) strAnotherClass = "NULL";
		else strAnotherClass = anotherValue.getClass().toString();

		if (anotherValue != null && value != null) {
			if (value.getClass().equals(anotherValue.getClass()) == false) {
				result = result + "<font color='blue'> - " + strClass + "(" + strAnotherClass + ")</font>";
			} else {
				result = result + " - " + strClass;
			}
		} else if (anotherValue == null && value == null) {
			result = result + " - " + strClass;
		} else {
			result = result + "<font color='blue'> - " + strClass + "(" + strAnotherClass + ")</font>";
		}
		
		if (childList != null) {
			index++;
			for(int i = 0; i < childList.size(); i++) {
				// 한줄 띄기
				result = result + "<br/>";
				// 띄어쓰기
				for(int j = 0; j < index; j++) result = result + "&nbsp;&nbsp;&nbsp;&nbsp;";
				result = result + childList.get(i).toClassString(index);
			}
		}
		return result;
	}
	*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
