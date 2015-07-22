<%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    java.util.Enumeration params = request.getParameterNames();
    System.out.println("Method="+request.getMethod());
    System.out.println("QueryString="+request.getQueryString());

    while (params.hasMoreElements()) {
        String name = params.nextElement().toString();
        String[] values = request.getParameterValues(name);
        for (String value : values) {
            System.out.println("name:" + name + ",value:" + value);
        }
    }
    String callBack = request.getParameter("callback");
    if(callBack==null){
        callBack="";
    }else{
        callBack += "(";
    }
    //String result = "{totalCount:4,objs:[['1','男'],['2','女'],['3','妖'],['4','其他']]}}";
    String result2 = "{count:4,objs:[{value:'1',name:'正常'},{value:'2',name:'锁定'},{value:'3',name:'待审'},{value:'4',name:'其他'}]}";
    String result3 = "{\n" +
"    'objs': [{\n" +
"        name: '微软(中国)国际有限公司',\n" +
"        email: 'admin@microsoft.com.cn'\n" +
"    },\n" +
"    {\n" +
"        name: '中国石油化工集团公司',\n" +
"        email: 'master@sinopec.com.cn' \n" +
"    },\n" +
"    {\n" +
"        name: '国家电网公司',\n" +
"        email: 'sgcc-info@sgcc.com.cn'\n" +
"    } ],\n" +
"    count: 3\n" +
"}";
%><%=result2%>