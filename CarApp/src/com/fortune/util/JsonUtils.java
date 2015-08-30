package com.fortune.util;

import com.google.gson.Gson;

public class JsonUtils {

	public static <T> T fromJsonString(Class<T> cls,String jsonString){
        try {
        	Gson gson = new Gson();
        	return gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static String getJsonString(Object objs){
        try {
            Gson gson = new Gson();
            return gson.toJson(objs);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "{error:\""+e.getMessage()+"\"}";
        }
    }
}
