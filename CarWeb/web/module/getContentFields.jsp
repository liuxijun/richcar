<%@ page import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.lang.reflect.Field" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-9-16
  Time: 上午9:47
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Field[] fields = Content.class.getDeclaredFields();
    int count = fields.length;//跳过id
    out.println("{\r\n\tsuccess:true,\r\n\tobjs:[");
    String allowTypes = ",Integer,Long,int,long,Date,String,TimeStamp,";
    int fieldCount=0;
    for(int i=0;i<count;i++){
        Field field = fields[i];
        String name = field.getName();
        String type = field.getType().getSimpleName();
        if("id".equals(name)){
            continue;
        }
        if(!allowTypes.contains(type)){
            continue;
        }
        fieldCount++;
        out.print("\t\t{name:'"+name+"',value:'" +name+
                "',type:'"+type+"'}");
        if(i<count-1){
            out.print(",");
        }
        out.println();
    }
    out.println("\t],\r\n\ttotalCount:"+(fieldCount)+"\r\n}");

%>