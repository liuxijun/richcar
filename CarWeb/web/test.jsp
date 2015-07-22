<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.File" %>
<%@page contentType="text/html;charset=UTF-8" %><%
   String dateLongStr = request.getParameter("date");
   if(dateLongStr==null){
       dateLongStr = "1293886331000";
   }
   long now = System.currentTimeMillis();
   Date date = new Date(string2long(dateLongStr.trim(),0));
   String dateStr = request.getParameter("dateStr");
   if(dateStr ==null){
       dateStr = "2012-01-01 00:00:00";
   }
   long dateValue = string2date(dateStr.trim(),"yyyy-MM-dd HH:mm:ss").getTime();

%><html>
<head>
   <title>日期转换</title>
</head>
<body>
<form action="test.jsp" method="post">
   当前时间：<%=now%>=<%=date2string(now)%><br/>
   <label for="date">日期数值</label>：<input name="date" type="text" id="date" value="<%=dateLongStr%>">=<%=date2string(date)%>
   <br/>
   <label for="date">输入日期</label>：<input name="dateStr" type="text" id="dateStr" value="<%=dateStr%>">=<%=dateValue%>
   <br/>
   <input type="submit">
</form>
<%
    File[] roots = File.listRoots();
    for (File _file : roots) {
        System.out.println(_file.getPath());
        //System.out.println(_file.getName());
        System.out.println("Free space = " + _file.getFreeSpace());
        System.out.println("Usable space = " + _file.getUsableSpace());
        System.out.println("Total space = " + _file.getTotalSpace());
        System.out.println();
    }

%>
<div style="width:640px;height:200px;border:1px solid blue;background-color: #dcdcdc;overflow: hidden;white-space:nowrap;">
    <%
        for(int i=0;i<5;i++){
            %>
    <div style="text-align:center;width:140px;height:195px;border: 1px solid green;background-color: #808080;float:left;margin-left: 20px;">
       <img src="" style="margin-top:10px;margin-left:10px;width:120px;height:160px;"><br/>
       海报<%=i%>
    </div>
    <%
        }
    %>
</div>
</body>
</html><%!
    /**
     * 日期 与 long互相转换
     *
     * @param value      source
     * @param defaultVal defualt
     * @return result
     */
    public static long date2long(java.util.Date value, long defaultVal) {
        try {
            return value.getTime();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static long string2long(String value, long defaultVal) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Date string2date(String sDate, String sFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(sFormat);
        try {
            return sf.parse(sDate);
        } catch (Exception ex) {
            return new Date();
        }
    }
    public static String date2string(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2string(long date) {
        java.util.Date theTime = new java.util.Date(date);
        return date2string(theTime);
    }

    public static String date2string(Date date, String format) {
        if (date == null) {
            date = new java.util.Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

%>
