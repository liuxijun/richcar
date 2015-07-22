package com.fortune.util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-5-23
 * Time: 8:55:19
 */
public class SortUtils implements Comparator {
    String orderBy;
    String orderDir;
    int less = -1;
    int grows = 1;

    public void setOrderBy(String orderBy) {
        if (orderBy == null || "".equals(orderBy.trim())) {
            orderBy = "name";
            setOrderDir("asc");
        }
        this.orderBy = orderBy;
    }

    public void setOrderDir(String orderDir) {
        if (orderDir == null || "".equals(orderDir)) {
            orderDir = "asc";
        }
        this.orderDir = orderDir;
        if ("asc".equals(orderDir.toLowerCase())) {
            less = 1;
            grows = -1;
        } else {
            less = -1;
            grows = 1;
        }
    }
    public boolean isBaseType(Object o){
        return o==null||o instanceof String
                || o instanceof Integer || o instanceof Long || o instanceof Date|| o instanceof Calendar ;
    }
    public long guessLong(String str,long defaultValue){
        if(str==null)return defaultValue;
        String numbers = "";
        boolean numberFound=false;
        for(int i=str.length()-1;i>=0;i--){
            char ch = str.charAt(i);
            if(ch>='0'&&ch<='9'){
                if(numberFound){
                   numbers =ch+numbers;
                }else{
                    numberFound = true;
                    numbers = ch+"";
                }
            }else{
                if(numberFound){
                    break;
                }
            }
        }
        return StringUtils.string2long(numbers,defaultValue);
    }
    public int compare(Object o1, Object o2) {
        if (o1 == null) return less;
        if (o2 == null) return grows;
        Object orderObj1;
        if(isBaseType(o1)){
            orderObj1=o1;
        }else{
            if(o1 instanceof Map){
                orderObj1=((Map)o1).get(orderBy);
            }else{
                orderObj1 = BeanUtils.getProperty(o1, orderBy);
            }
        }
        if (orderObj1 == null) return less;

        Object orderObj2;
        if(isBaseType(o2)){
           orderObj2 = o2;
        }else{
            if(o2 instanceof Map){
                orderObj2=((Map)o2).get(orderBy);
            }else{
                orderObj2 = BeanUtils.getProperty(o2, orderBy);
            }
        }
        if (orderObj2 == null) return grows;
//        Method compareToMethod = orderObj1.getClass().getMethod("compareTo",new Class[0]{});
//        System.out.println(orderObj1+" vs "+orderObj2);
        long result;
        if (orderObj1 instanceof String) {
            result = ((String) orderObj1).compareTo((String) orderObj2);
/*
            long v1 = guessLong(orderObj1.toString(),-1);
            long v2 = guessLong(orderObj2.toString(),-1);
            if(v1==-1||v2==-1){
                return (((String) orderObj1).compareTo((String) orderObj2) == 1) ? less : grows;
            }else{
                return v1>v2 ? less : grows;
            }
*/
        } else if (orderObj1 instanceof Integer) {
            result = ((Integer) orderObj1).compareTo((Integer) orderObj2);
        } else if (orderObj1 instanceof Long) {
            result = ((Long) orderObj1).compareTo((Long) orderObj2);
        } else if (orderObj1 instanceof Calendar) {
            result = ((Calendar) orderObj1).getTime().compareTo(((Calendar) orderObj2).getTime());
        } else if (orderObj1 instanceof Date) {
            result = ((Date) orderObj1).getTime() - ((Date) orderObj2).getTime();
        } else {
            result = (orderObj1.toString()).compareTo(orderObj2.toString());
        }
        if(result>0){
            return less;
        }else if(result<0){
            return grows;
        }else{
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public static List sortArray(List objects, String orderBy, String orderDir) {
        SortUtils comp = new SortUtils();
        comp.setOrderBy(orderBy);
        comp.setOrderDir(orderDir);
        Collections.sort(objects, comp);
        return objects;
    }

    @SuppressWarnings("unchecked")
    public static Object[] sortArray(Object[] objects, String orderBy, String orderDir) {
///*
        List objs = new ArrayList(objects.length);
        objs.addAll(Arrays.asList(objects));
        objs = sortArray(objs, orderBy, orderDir);
        return objs.toArray();
//*/
/*
        SortUtils comp = new SortUtils();
        comp.setOrderBy(orderBy);
        comp.setOrderDir(orderDir);
        for(int i=0;i<objects.length;i++){
            Object o1 = objects[i];
            for(int j=i+1;j<objects.length;j++){
                Object o2 = objects[j];
                if(comp.compare(o1,o2)==1){
                    objects[j]=o1;
                    objects[i]=o2;
                    o1 = o2;
                    o2 = objects[j];
                }
            }
        }
        return objects;
         */
    }
}
