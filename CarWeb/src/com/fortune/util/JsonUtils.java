package com.fortune.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-16
 * Time: 10:24:49
 * 处理Json字符串
 */
public class JsonUtils {

    public static JSONObject getJsonObj(String jsonString){
        if(jsonString==null){
            return null;
        }
        JsonUtils utils = new JsonUtils();
        return JSONObject.fromObject(jsonString,utils.configJson("yyyy-MM-dd HH:mm:ss"));
    }
    public static Object getObjectFromJsonString(Class cls,String jsonString){
        JSONObject obj = getJsonObj(jsonString);
        if(obj!=null){
            try {
                return JSONObject.toBean(obj,cls);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }

    }
    public static String getStringValue(Object obj) {
        if (obj != null) {
            String value = obj.toString();
            if (value.indexOf("\"jsonString\":true") >= 0) {
                return "\"" + obj.getClass().getSimpleName() + "\":" + value + "";
            } else {
                //把双引号换掉
                return value.replaceAll("\"", "\\\"");
            }
        } else {
            return "";
        }
    }

    public static String getJsonString(Object obj){
        return getJsonString(obj,null);
    }

    public static String getJsonString(Object obj,String prefix){
        JsonUtils jsonUtils = new JsonUtils();
        return jsonUtils.getJson(obj,prefix);
    }

    public String getJsonArray(Object objs){
        try {
            JSONArray json = JSONArray.fromObject(objs, configJson("yyyy-MM-dd HH:mm:ss"));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "[{error:\""+e.getMessage()+"\"}]";
        }
    }
    public static String getListJsonString(String listName, List objs, String totalCountName, int totalCount,String properties) {
        if(properties==null||"".equals(properties.trim())||objs==null){
            return getListJsonString(listName, objs, totalCountName, totalCount);
        }
/*
        if(!(properties.contains(",id,")||properties.startsWith("id,"))){
            properties = "id,"+properties;
        }
*/
        String[] propertyIds = properties.split(",");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(Object o:objs){
            Map<String,Object> m = new HashMap<String,Object>();
            for(String p:propertyIds){
                m.put(p,BeanUtils.getProperty(o,p));
            }
            list.add(m);
        }
        return getListJsonString(listName,list,totalCountName,totalCount);
    }
    public static String getListJsonString(String listName, List objs, String totalCountName, int totalCount) {
        JsonUtils jsonUtils = new JsonUtils();
        return jsonUtils.getListJson(listName,objs,totalCountName,totalCount);
    }
    public String getJson(Object obj) {
        return getJson(obj,null);
    }

    public String getJson(Object obj,String prefix) {
        try {
            if(obj!=null){
                if(obj instanceof List){
                    return getJsonArray(obj);
                }else{
                    JSONObject json = JSONObject.fromObject(obj, configJson("yyyy-MM-dd HH:mm:ss"));
                    if(prefix!=null){
                        JSONObject temp = new JSONObject();
                        Set keys = json.keySet();
                        for(Object key:keys){
                            temp.put(prefix+key.toString(),json.get(key));
                        }
                        json = temp;
                    }
                    return json.toString();
                }
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:\""+e.getMessage()+"\"}";
        }
    }

    public String getListJson(String listName, List objs, String totalCountName, int totalCount) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\"").append(totalCountName).append("\":").append(totalCount)
                    .append(",\"").append(listName).append("\":");
            JSONArray json = JSONArray.fromObject(objs, configJson("yyyy-MM-dd HH:mm:ss"));
//            JSONArray json = JSONArray.fromObject(objs);
            buffer.append(json.toString());
            buffer.append("}");
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[{error:\""+e.getMessage()+"\"}]";
        }
    }

    public String getListJson( List objs ) {
        try {
            JSONArray json = JSONArray.fromObject(objs, configJson("yyyy-MM-dd HH:mm:ss"));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[{error:\""+e.getMessage()+"\"}]";
        }
    }

    public JsonConfig configJson(String datePattern) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"objJson","simpleJson"});
        jsonConfig.setIgnoreDefaultExcludes(false);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(datePattern));
        jsonConfig.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor(datePattern));
        jsonConfig.registerJsonValueProcessor(Calendar.class, new DateJsonValueProcessor(datePattern));
        return jsonConfig;
    }

    public class DateJsonValueProcessor implements JsonValueProcessor {
        public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
        private DateFormat dateFormat;
        /**
         * 构造方法.
         *
         * @param datePattern 日期格式
         */

        public DateJsonValueProcessor(String datePattern) {
            if (null == datePattern)
                dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
            else
                dateFormat = new SimpleDateFormat(datePattern);
        }

        /* （非 Javadoc）

        * @see net.sf.json.processors.JsonValueProcessor#processArrayValue(java.lang.Object, net.sf.json.JsonConfig)

        */

        public Object processArrayValue(Object arg0, JsonConfig arg1) {
            // TODO 自动生成方法存根
            return process(arg0);
        }

        /* （非 Javadoc）

        * @see net.sf.json.processors.JsonValueProcessor#processObjectValue(java.lang.String, java.lang.Object, net.sf.json.JsonConfig)

        */

        public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
            // TODO 自动生成方法存根
           return process(arg1);
        }

        private Object process(Object value) {
            if(value==null){
                return "";
            }else{
                Date val = null;
                if(value instanceof Timestamp){
                    Timestamp stampValue = (Timestamp)value;
                    val = new Date(stampValue.getTime());
                }else if(value instanceof Date){
                    val = (Date) value;
                }else if(value instanceof Calendar){
                    Calendar calValue = (Calendar) value;
                    val = calValue.getTime();
                }
                return dateFormat.format(val);
            }
        }

    }
}
