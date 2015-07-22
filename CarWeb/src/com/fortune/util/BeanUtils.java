package com.fortune.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-1
 * Time: 13:41:56
 * 对一些bean的处理工具
 */
public class BeanUtils {
    private static Logger logger = Logger.getLogger("com.fortune.util.BeanUtils");
    static public void copyPropertiesIfDesertIsNull(Object src,Object dst){
        if(src==null||dst==null){
            return;
        }
        Field[] fields = src.getClass().getDeclaredFields();
        for(Field field:fields){
            String propertyName = field.getName();
            if(!"jsonString".equals(propertyName)){
                Object value = getProperty(src, propertyName);
                if(value!=null &&value instanceof Timestamp){
                    //这是个BUG，类型会返回错误！
                    value = new Date(((Timestamp) value).getTime());
                }
                setDefaultValue(dst,propertyName,value);
            }
        }
    }

    static public void setDefaultValue(Object obj,String propertyName,Object defaultValue){
        Object oldValue = getProperty(obj,propertyName);
        //设置缺省值
        if(oldValue==null ||"".equals(oldValue.toString())||(oldValue instanceof Integer && (Integer)oldValue<=0)||
                (oldValue instanceof Long &&(Long)oldValue<=0)){
            setProperty(obj,propertyName,defaultValue);
        }
    }
    static public Object getObject(HttpServletRequest request, Class theClass) {
        Object obj = null;
        try {
            obj = theClass.newInstance();
//            Configuration cfg = new Configuration().configure();
            Field[] fields = theClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object fieldValue = request.getParameter(fieldName);
                if (fieldValue != null) {
                    Class fieldType = field.getType();
                    if (fieldType.equals(Integer.class)) {
                        fieldValue = new Integer(fieldValue.toString());
                    } else if (fieldType.equals(Long.class)) {
                        fieldValue = new Long(fieldValue.toString());
                    } else if (fieldType.equals(Date.class)) {
                        fieldValue = StringUtils.string2date(fieldValue.toString());
                    } else if (fieldType.equals(BigDecimal.class)) {
                        fieldValue = new BigDecimal(fieldValue.toString());
                    } else if (fieldType.equals(Timestamp.class)) {
                        fieldValue = new Timestamp(StringUtils.string2date(fieldValue.toString()).getTime());
                    }
                    try {
                        org.apache.commons.beanutils.BeanUtils.setProperty(obj, fieldName, fieldValue);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static boolean setProperty(Object obj, String propertyName, Object value, Class type) {
        if (value == null || obj == null) return false;
        try {
            Method setPropertyMethod = obj.getClass().getMethod("set" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1), new Class[]{type});
            if (setPropertyMethod != null) {
                setPropertyMethod.invoke(obj, value);
                return true;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(obj, propertyName, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setProperty(Object obj, String propertyName, Object value) {
        if (value == null) {
            return false;
        }
        return setProperty(obj, propertyName, value, value.getClass());
    }

    public static boolean setProperty(Object obj,String colName,String value){
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            boolean fieldFound = false;
            Field field = null;
            for (Field f : fields) {
                if (f.getName().equals(colName)) {
                    field = f;
                    fieldFound = true;
                    break;
                }
            }
            if (!fieldFound) {
                try {
                    field = obj.getClass().getDeclaredField(colName);
                } catch (NoSuchFieldException e) {
                    logger.error("没有找到这个Field：" + colName + "," + e.getMessage());
                } catch (SecurityException e) {
                    logger.error("没有找到这个Field：" + colName + ",发生了权限异常：" + e.getMessage());
                }
            }

            Object valObj = value;
            Class type;
            if (field != null) {
                type = field.getType();
            } else {
                type = String.class;
            }
            if (type != null) {
                String typeName = type.getName();
                if (type.isInstance(Date.class)||"java.util.Date".equals(typeName)) {
                    if(value!=null){
                        value = value.replace('T',' ');
                    }
                    valObj = StringUtils.string2date(value);
                } else if (type.isInstance(Long.class)||"java.lang.Long".equals(typeName)) {
                    valObj = StringUtils.string2long(value, 0);
                } else if (type.isInstance(Integer.class)||"java.lang.Integer".equals(typeName)) {
                    valObj = StringUtils.string2int(value, 0);
                }
                try {
                    org.apache.commons.beanutils.BeanUtils.setProperty(obj, colName, valObj);
                    return true;
                } catch (IllegalAccessException e) {
                    logger.error("无法设置：" + colName + ",value=" + value + "," + e.getMessage());
                } catch (InvocationTargetException e) {
                    logger.error("无法设置：" + colName + ",value=" + value + "," + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("无法设置：" + colName + ",value=" + value + "," + e.getMessage());
        }
        return false;
    }
    public static Object getProperty(Object obj, String propertyName) {
        try {
            Method getPropertyMethod = obj.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1), new Class[0]);
            if (getPropertyMethod != null) {
                return getPropertyMethod.invoke(obj);
            }
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        } catch (SecurityException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
        return null;
    }
    public static void copyProperties(Object dst,Object src){
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dst,src);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public static boolean hasProperty(Object obj,String propertyName){
        try {
            Method getPropertyMethod = obj.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1), new Class[0]);
            if (getPropertyMethod != null) {
                return true;
            }
        } catch (NoSuchMethodException e) {
           // e.printStackTrace();
        } catch (SecurityException e) {
           //  e.printStackTrace();
        } catch (IllegalArgumentException e) {
           // e.printStackTrace();
        }
        return false;
    }
    public static Object clone(Object src){
        try {
            return org.apache.commons.beanutils.BeanUtils.cloneBean(src);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
