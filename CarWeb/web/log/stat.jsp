<%@ page import="java.util.List" %><%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/2/12
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%!
    @SuppressWarnings("unchecked")
    public String stat(String sql,List<String[]> headers){
        StringBuilder result = new StringBuilder();
        result.append("\t<table border='1' cellspacing='0' class='statMainBody'>\r\n\t\t<tr>\r\n\t\t\t");
        int i=0;
        for(String[] colInfo:headers){
            String style = "";
            String label;
            if(colInfo.length>1&&colInfo[1]!=null){
                label = colInfo[1];
            }else{
                label =  colInfo[0];
            }
            if(label==null||"".equals(label.trim())){
                label = "字段"+i;
            }
            i++;
            if(colInfo.length>2&&colInfo[2]!=null){
                style += " class='"+colInfo[2]+"'";
            }
            if(colInfo.length>3&&colInfo[3]!=null){
                style += " style='"+colInfo[3]+"'";
            }
            if("".equals(style)){
                style=" class='statCol'";
            }
            result.append("<th").append(style).append(">").append(label).append("</th>");
        }
        result.append("\r\n\t\t</tr>\r\n");
        List<Object> results = executeSql(sql,jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
        for(Object r:results){
            if(r instanceof Object[]){
                Object[] dataResult = (Object[])r;
                List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                int size = dataRows.size();
                if(size>0){
                    int index = 0;
                    for(Map<String,String> row:dataRows){
                        index++;
                        result.append("\r\n\t\t<tr>\r\n\t\t\t");
                        for(String[] colInfo:headers){
                            String colName = colInfo[0];
                            String value;
                            if("rownum".equals(colName.trim())){
                                value = ""+index;
                            }else{
                                value = row.get(colName);
                            }
                            if(value==null){
                                value = "";
                            }
                            String style = "";
                            if(colInfo.length>2&&colInfo[2]!=null){
                                style += " class='"+colInfo[2]+"'";
                            }
                            if(colInfo.length>3&&colInfo[3]!=null){
                                style += " style='"+colInfo[3]+"'";
                            }
                            result.append("<td").append(style).append(">").append(value).append("</td>");
                        }
                        result.append("\r\n\t\t</tr>");
                    }
                }
                result.append("\t\n\t</table>\r\n");
            }
        }
        return result.toString();
    }
%><%@include file="../admin/sqlBase.jsp"%>
