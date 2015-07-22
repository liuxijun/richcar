<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-4
  Time: 下午2:10
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String ranges = request.getParameter("ranges");
    String result = "";
    if(ranges==null){
        ranges = "";
    }else{
        String[] phoneRanges = ranges.split("\n");
        result ="delete from PHONE_RANGE;\n";
        int count = 0;
        String[] areaIds = new String[]{
                "石家庄","311"
                ,"保定","312"
                ,"张家口","313"
                ,"承德","314"
                ,"唐山","315"
                ,"廊坊","316"
                ,"沧州","317"
                ,"衡水","318"
                ,"邢台","319"
                ,"邯郸","310"
                ,"秦皇岛","355"
        };
        for(String phoneRange:phoneRanges){
            String[] data = phoneRange.split("\t");
            if(data.length==3){
                String phone = data[0].trim();
                String index = data[1].trim();
                String name = data[2].trim();
                if(phone.startsWith("86")){
                    phone = phone.substring(2);
/*                    while(phone.length()<7){
                        phone+="0";
                    }*/
                    String areaId = null;
                    for(int i=0,l=areaIds.length/2;i<l;i++){
                        String areaName = areaIds[i*2];
                        if(name.contains(areaName)){
                            areaId = areaIds[i*2+1];
                        }
                    }
                    String phoneFrom = phone;
                    while(phoneFrom.length()<11){
                        phoneFrom+="0";
                    }
                    String phoneTo=phone;
                    while(phoneTo.length()<11){
                        phoneTo+="9";
                    }
                    String desc = name+"("+phoneFrom+"-"+phoneTo+")["+index+"]";
                    if(areaId==null){
                        result+="/*"+name+"的归属地有误，无法获取归属地信息*/\n";
                        areaId = "-1";
                    }
                    result +="insert into PHONE_RANGE(NAME,DESP,PHONE_FROM,PHONE_TO,AREA_ID)" +
                            " values('"+name+"','"+desc+"',"+phoneFrom+","+phoneTo+","+areaId+");\n";
                    count++;
                }
            }
        }
        result+="/* 累计添加了"+count+"条记录*/";
    }
%>
<html>
<head>
    <title>整理电话号码段</title>
</head>
<body>
<form action="formatPhoneRange.jsp" method="post" id="rangesForm" name="rangesForm">
    <table>
        <tr>
            <td><label for="ranges">原始数据</label></td>
        </tr>
        <tr>
            <td>
                <textarea id="ranges" name="ranges" rows="80" cols="80" style="width:640px;height:320px;"><%=ranges%></textarea>
            </td>
        </tr>
        <tr>
            <td><label for="result">处理结果</label></td>
        </tr>
        <tr>
            <td>
                <textarea  id="result" name="result" rows="80" cols="80" style="width:640px;height:320px;overflow-x:auto"><%=result%></textarea>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="提交">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
