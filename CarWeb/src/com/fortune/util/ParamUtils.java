package com.fortune.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-2-27
 * Time: 12:12:41
 * 字符串参数处理
 */
public class ParamUtils {
    /**
     * 处理命令行参数，将 port=1024 addr=127.0.0.1 dir=1234转化为一个map类型的object返回
     * @param args  参数
     * @return      结果
     */
    public static Map<String,String> getCmdParameters(String[] args){
        Map<String, String> parameters = new HashMap<String, String>();
        if (args != null) {
            for (String parameter : args) {
                int p = parameter.indexOf("=");
                if (p > 0) {
                    String param = parameter.substring(0, p);
                    String value = parameter.substring(p + 1);
                    parameters.put(param, value);
                } else {
                    parameters.put(parameter, parameter);
                }
            }
        }
        return parameters;
    }

    public static long getLongParameter(HttpServletRequest request,String parameterName,long defaultValue){
        return StringUtils.string2long(request.getParameter(parameterName),defaultValue);
    }

    public static String getParameter(HttpServletRequest request,String parameterName,String defaultVal){
        String result = request.getParameter(parameterName);
        return result==null?result:defaultVal;
    }
    
    public static long getIntParameter(HttpServletRequest request,String parameterName,int defaultValue){
        return StringUtils.string2int(request.getParameter(parameterName),defaultValue);
    }

}
