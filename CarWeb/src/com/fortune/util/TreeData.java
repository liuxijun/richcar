package com.fortune.util;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * 用于存放树状数据,比如 频道
 * User: zbxue
 * Date: 2010-10-22
 * Time: 23:56:01
 */
public class TreeData<V> {
    public List keys = new ArrayList();
    public HashMap<Object,V> values = new HashMap();
    public boolean pkFlag = false;
    public String pkName;

    public TreeData(){
    }

    public TreeData(boolean pkFlag,String pkName){
        this.pkFlag = pkFlag;
        this.pkName = pkName;        
    }


    public void set(Object key,V value){
        keys.add(key);
        if (pkFlag){
            Object keyPk = BeanUtils.getProperty(key,pkName);
            values.put(keyPk , value);
        } else {
            values.put(key, value);
        }
    }

    public void insert(int no,Object key,V value){
        List list1 = new ArrayList();
        list1.addAll(keys.subList(0,no));
        list1.add(key);
        list1.addAll(keys.subList(no,keys.size()));
        keys = list1;
        
        if (pkFlag){
            Object keyPk = BeanUtils.getProperty(key,pkName);
            values.put(keyPk , value);
        } else {
            values.put(key, value);
        }

    }

    public Object getKey(int no){
        return keys.get(no);
    }

    public V getValue(Object key){
        if (pkFlag){
            Object keyPk = BeanUtils.getProperty(key,pkName);
            return values.get(keyPk);
        } else {
            return values.get(key);
        }
    }

    public int size(){
        return keys.size();
    }
    
}
