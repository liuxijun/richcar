<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="/log/report.css" rel="stylesheet" type="text/css"/>
    <title>区域贡献统计结果</title>
</head>
<body>
<div id="main_list">
    <table cellpadding="0" cellspacing="0" align="center" class="t_list">
        <tr>
            <td width="63" rowspan="2" align="center">区域</td>
            <td colspan="3">点播量</td>
            <td colspan="3" align="center">点播时长</td>
        </tr>
        <tr>
            <td width="170">次数</td>
            <td width="143" align="center">比重</td>
            <td width="116" align="center">排名</td>
            <td width="163" align="center">时长</td>
            <td width="154" align="center">比重</td>
            <td width="114" align="center">排名</td>
        </tr>
        <%
            Logger logger = LoggerFactory.getLogger("com.fortune.jsp.organizationContributionReport.jsp");
            Map objMaps = (HashMap) request.getAttribute("objMaps");
            List<Map> areaList = (List<Map>) objMaps.get("area_list");
            for(int i=0;i<areaList.size();i++){
                Map<String,Object> areaMap = areaList.get(i);
         %>
            <tr>
                <td align="center"><%=areaMap.get("area_name")%>&nbsp;</td>
                <td><%=areaMap.get("sp_num")%>&nbsp;</td>
                <td><%=areaMap.get("sp_num_percentage")%>&nbsp;</td>
                <td><%=areaMap.get("sp_num_rank")%>&nbsp;</td>
                <td><%=areaMap.get("sp_time")%>&nbsp;</td>
                <td><%=areaMap.get("sp_time_percentage")%>&nbsp;</td>
                <td><%=areaMap.get("sp_time_rank")%>&nbsp;</td>
            </tr>

        <%
            }
        %>

        <tr>
            <td align="center">总计</td>
            <td colspan="3"><%=objMaps.get("sp_num_count")%>&nbsp;</td>
            <td colspan="3"><%=objMaps.get("sp_time_count")%>&nbsp;</td>
        </tr>
    </table>
</div>

</body>
</html>