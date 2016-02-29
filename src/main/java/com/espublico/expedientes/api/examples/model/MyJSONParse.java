package com.espublico.expedientes.api.examples.model;

public class MyJSONParse {
	
	public StringBuilder initJSONObject(){
		StringBuilder json = new StringBuilder();
		json.append("{");
		return json;
	}
	
	public StringBuilder endJSONObject(StringBuilder json){
		json.append("}");
		return json;
	}
	
	public StringBuilder initJSONArray(){
		StringBuilder json = new StringBuilder();
		json.append("[");
		return json;
	}
	
	public StringBuilder endJSONArray(StringBuilder json){
		json.append("]");
		return json;
	}
	
	public StringBuilder addItemArray(StringBuilder json, String item, boolean isString){
		if(item == null)
			return json;
		
		String newItem = null;
		if(isString)
			newItem = String.format("\"%s\"", item);
		else
			newItem = item;
		if(!isEmptyObject(json))
			json.append(", ");
		
		return json.append(newItem);
	}
	
	public StringBuilder addKeyValueString(StringBuilder json, String key, String value){
		if(value == null)
			return json;
		
		String item = String.format("\"%s\":\"%s\"", key, value);
		if(!isEmptyObject(json))
			json.append(", ");
		
		return json.append(item);
	}
	
	public StringBuilder addKeyValueNumber(StringBuilder json, String key, String value){
		if(value == null)
			return json;
		
		String item = String.format("\"%s\":%s", key, value);
		if(!isEmptyObject(json))
			json.append(", ");
		
		return json.append(item);
	}
	
	public StringBuilder addKeyValueBoolean(StringBuilder json, String key, boolean value){
		String item = String.format("\"%s\":%s", key, value);
		if(!isEmptyObject(json))
			json.append(", ");
		
		return json.append(item);
	}
	
	private boolean isEmptyObject(StringBuilder ob){
		String json = ob.toString();
		return (json.endsWith("{") || json.endsWith("["));
	}

}
