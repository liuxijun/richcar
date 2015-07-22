<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.util.Date" %>
<%@ taglib
        prefix="s" uri="/WEB-INF/tlds/struts-tags.tld" %><%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    SimpleDateFormat dateFormator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStr = request.getParameter("dateStr");
    if(dateStr == null){
        dateStr = "2009-05-04 00:00:00";
    }
    String timeStr = request.getParameter("timeStr");
    if(timeStr == null){
        try {
            timeStr = ""+dateFormator.parse(dateStr).getTime();
        } catch (ParseException e) {
            timeStr = "0";
        }
    }
    String dateToTimeStr ;
    String timeToDateStr ;
    try {
        dateToTimeStr = ""+dateFormator.parse(dateStr).getTime();
    } catch (ParseException e) {
        dateToTimeStr = e.getMessage();
    }
    try {
        timeToDateStr = dateFormator.format(new Date(Long.parseLong(timeStr)));
    } catch (NumberFormatException e) {
        timeToDateStr = e.getMessage();
    }
%><html>
<head>
    <title>时间测试</title>
</head>
<body>
  <form method="post" action="timeTesor.jsp">
      <table>
          <tr>
              <td>日期：</td><td>
              <input style="width:220" type="text" name="dateStr" value="<%=dateStr%>"/>
              ==>
              <input style="width:220" type="text" name="dateToTimeStr" value="<%=dateToTimeStr%>"/>
              </td>
          </tr>
          <tr>
              <td>时间：</td><td>
              <input style="width:220" type="text" name="timeStr" value="<%=timeStr%>"/> ==>
              <input style="width:220" type="text" name="timeToDateStr" value="<%=timeToDateStr%>"/>
              </td>
          </tr>
          <tr>
              <td colspan="2" align="center">
                  <input style="width:120"  type="submit" value="提交"/>
                  &nbsp;&nbsp;
                  <input style="width:120"  type="reset" value="重置"/>
              </td>
          </tr>
      </table>

  </form>
</body>
</html>