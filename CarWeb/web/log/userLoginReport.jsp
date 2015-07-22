<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link href="/log/report.css" rel="stylesheet" type="text/css"/>
    <title>用户点播日志查询</title>
    <script type="text/javascript" src="../js/jquery.js"></script>
</head>
<body>
<!--页面数据区 开始-->
<div id="main_list">
    <table cellpadding="0" cellspacing="0" class="t_list">
        <tr>
            <th>用户ID</th>
            <th>用户IP</th>
            <th>登陆方式</th>
            <th>所属区域</th>
            <th>登陆次数</th>
            <th>登陆天次</th>
        </tr>

        <%
            Map<String,Object> objMaps = (Map) request.getAttribute("objMaps");
            if(objMaps!=null){
                Map objMaps1 = (Map)objMaps.get("userLoginMap");
                List userLoginInfo = (List)objMaps1.get("userLoginInfo");
                List userLoginDayInfo = (List)objMaps1.get("userLoginDayInfo");
                for(int i=0;i<userLoginInfo.size();i++){
                    Object[] o = (Object[]) userLoginInfo.get(i);
        %>
        <tr>
            <td>
                <%=parseLong(o[1])%>&nbsp;
            </td>
            <td>
                <%=parseLong(o[2])%>&nbsp;
            </td>
            <td>
                <%=checkStatus(o[3])%>&nbsp;
            </td>
            <td>
                <%=checkArea(o[4])%>&nbsp;
            </td>
            <td>
                <%=parseLong(o[0])%>&nbsp;
            </td>
            <%
                if(i == 0){
            %>
            <td rowspan=<%=userLoginInfo.size()%>>
                <%=parseLong(userLoginDayInfo.get(0))%>&nbsp;
            </td>
             <%
                }
            %>
        </tr>
        <%
                }
            }

        %>

    </table>
</div>
</body>
</html>
<%!
    private String checkArea(Object o) {
        if(!"".equals(o)&&o!=null){
            int status = Integer.parseInt(o.toString());
            switch (status){
                case 311:return "石家庄";
                case 312:return "保定";
                case 313:return "张家口";
                case 314:return "承德";
                case 315:return "唐山";
                case 316:return "廊坊";
                case 317:return "沧州";
                case 318:return "衡水";
                case 319:return "邢台";
                case 310:return "邯郸";
                case 355:return "秦皇岛";
            }
        }
        return "其他";
    }

    private String parseLong(Object o) {
        if(!"".equals(o)&&o!=null){
            return o.toString();
        }
        return "无";
    }

    private String checkStatus(Object o){
        if(!"".equals(o)&&o!=null){
            int status = Integer.parseInt(o.toString());
            switch (status){
                case 1:return "总部取号登陆";
                case 2:return "Wap取号登陆";
                case 3:return "河北联通取号登陆";
                case 4:return "验证码登陆";
                case 5:return "客户端登陆";
            }
        }
        return "";
    }
%>