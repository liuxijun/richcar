package com.fortune.util;

import org.hibernate.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-7
 * Time: 11:07:05
 * 搜索有关的一些信息
 */
public class SearchCondition {
    private List<Object> paramValues;
    private List<Type> paramTypes;
    private String sqlStr;

    public SearchCondition(){
        paramValues = new ArrayList<Object>();
        paramTypes = new ArrayList<Type>();
    }
    public List getParamValues() {
        return paramValues;
    }

    public void setParamValues(List<Object> paramValues) {
        this.paramValues = paramValues;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<Type> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public SearchCondition appendAndSqlCondition(String condition,Object value, Type type){
        return appendAndSqlCondition(condition, value, type,false);
    }

    public SearchCondition appendAndSqlCondition(String condition,Object value, Type type,boolean checkNullValue){
        if(checkNullValue){
            if((value==null)||
                    (value instanceof Integer && ((Integer)value)<=0)||
                    (value instanceof Long && ((Long)value)<=0)
                    ){
                return this;
            }
        }
        if(sqlStr == null){
            sqlStr = condition;
            return this;
        }
        if(sqlStr.toLowerCase().indexOf("where")>0){
            sqlStr += " and ";
        }else{
            sqlStr += " where ";
        }
        sqlStr += condition;
        paramTypes.add(type);
        paramValues.add(value);
        return this;
    }

    public Object[] getObjectArrayParamValues(){
        return paramValues.toArray();
    }
    public Type[] getTypeArray(){
        Type[] result = new Type[paramTypes.size()];
        for(int i=0;i<paramTypes.size();i++){
            Type type = paramTypes.get(i);
            result[i] = type;
        }
        return result;
    }
}
