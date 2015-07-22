<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <%--<link href="/log/commen.css" rel="stylesheet" type="text/css"/>--%>
    <link href="/log/report.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript">
        function formSubmit(pageNo){
            var start = 0;
            if(pageNo<=0){
               return;
            }
            if(pageNo><s:property value="pageBean.pageCount"/>){
               return;
            }
            form1.elements["pageBean.pageNo"].value = pageNo;
            <%--//(pageNo-1)*<s:property value="pageBean.pageSize"/>;--%>
            //form1.method.value = method;
            form1.submit();
        }
    </script>
    <%--<link href="/log/styles.css" rel="stylesheet" type="text/css"/>--%>
    <title>资源贡献统计结果</title>
</head>
<body>
<div id="main11_list">
    <table cellpadding="0" align="center" cellspacing="0" class="t_list1">
        <tr>
            <td align="center">媒体名称</td>
            <td align="center">内容提供商</td>
            <td align="center">总点播量</td>
            <td align="center">总点播时长</td>
            <td align="center">服务提供商</td>
            <td align="center">频道</td>
            <td align="center">点播量</td>
            <td align="center">点播时长</td>
        </tr>
        <%
            Logger logger = LoggerFactory.getLogger("com.fortune.jsp.resourceContributionReport.jsp");
            HashMap objMaps = (HashMap) request.getAttribute("objMaps");
            List cpList = (List)objMaps.get("cp_list");
            for(int i=0;i<cpList.size();i++){
                HashMap cpMaps = (HashMap) cpList.get(i);
                List spList = (List) cpMaps.get("sp_list");
                int num = spList.size();
        %>
        <tr>
            <td rowspan="<%=num%>"><%=cpMaps.get("content_name")%></td>
            <td rowspan="<%=num%>"><%=cpMaps.get("cp_name")%></td>
            <td rowspan="<%=num%>"><%=cpMaps.get("cp_num")%></td>
            <td rowspan="<%=num%>"><%=cpMaps.get("cp_time")%></td>
            <%

                for(int j=0;j<spList.size();j++){
                    HashMap spMaps = (HashMap) spList.get(j);
            %>
            <td><%=spMaps.get("sp_name")%></td>
            <td><%=spMaps.get("channel_name")%></td>
            <td><%=spMaps.get("sp_num")%></td>
            <td><%=spMaps.get("sp_time")%></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>
<form id="form1" name="form1" action="/log/visitLog!getResourceContributionCount.action" method="post">
    <%
        PageBean pageBean = (PageBean) request.getAttribute("pageBean");
        long cpId=0;
        String startTime="",endTime="",channelsAndLeafs="",contentName="",channelSelect="",playTimeSelect="";
        try{
            cpId = (Long) objMaps.get("cpId");
            startTime = (String) objMaps.get("startTime");
            endTime = (String) objMaps.get("endTime");
            channelsAndLeafs = (String) objMaps.get("channelsAndLeafs");
            contentName = (String) objMaps.get("contentName");
            channelSelect = (String) objMaps.get("channelSelect");
            playTimeSelect = (String) objMaps.get("playTimeSelect");
        }catch (Exception e){
            logger.error("读取数据发生错误"+e.getMessage());
        }

    %>
    <input type="hidden" name="cpId" value="<%=cpId%>"/>
    <input type="hidden" name="startTime" value="<%=startTime%>"/>
    <input type="hidden" name="endTime" value="<%=endTime%>"/>
    <input type="hidden" name="channelsAndLeafs" value="<%=channelsAndLeafs%>"/>
    <input type="hidden" name="contentName" value="<%=contentName%>"/>
    <input type="hidden" name="channelSelect" value="<%=channelSelect%>"/>
    <input type="hidden" name="playTimeSelect" value="<%=playTimeSelect%>"/>
    <s:hidden name="pageBean.pageNo" />
    <s:hidden name="pageBean.pageSize" />

    <div style="background-color:#3399FF;height: 50px; ">
        <table width='0' border='0' cellspacing='0' cellpadding='0'>
            <tr>
                <td width='61'>&nbsp;</td>
                <td width='47'><img src='../images/report/g1.jpg' width='6' height='21' /></td>
                <td width='350' class='copy_1'>每页
                    <input name='limit' type='text'value="<s:property value="pageBean.pageSize"/>" size='2' />
                    条 &nbsp;&nbsp;
                    共 <%=pageBean.getRowCount()%> 条 第 <%=pageBean.getPageNo()%>/<%=pageBean.getPageCount()%> 页</td>
                <td width='10'><img src='../images/report/g1.jpg' width='6' height='21' /></td>

                <td width='25'>
                    <%
                        if(pageBean.getPageNo()!=1){
                    %>
                    <a href='javascript:formSubmit(1)'><img src='../images/report/g2.jpg' width='15' height='13' border='0' /></a>
                    <%
                        }
                    %>
                </td>
                <td width='25'><%
                    if(pageBean.getPageNo()!=1){
                %>
                    <a href='javascript:formSubmit(<%=pageBean.getPageNo()-1%>)'><img src='../images/report/g3.jpg' width='15' height='13' border='0' /></a>
                    <%
                        }
                    %></td>
                <td width='25'>
                    <%
                        if(pageBean.getPageNo()!=pageBean.getPageCount()){
                    %>
                    <a href='javascript:formSubmit(<%=pageBean.getPageNo()+1%>)'><img src='../images/report/g4.jpg' width='15' height='13' border='0' /></a>
                    <%
                        }
                    %>
                </td>
                <td width='25'>
                    <%
                        if(pageBean.getPageNo()!=pageBean.getPageCount()){
                    %>
                    <a href='javascript:formSubmit(<%=pageBean.getPageCount()%>)'><img src='../images/report/g5.jpg' width='15' height='13' border='0' /></a>
                    <%
                        }
                    %>
                </td>
                <td width='133' class='copy_1'>&nbsp;</td>
                <td width='150'><span class='copy_1'>转到第<input id="pageNo" name='page' type='text' value='' size='2' />
页</span></td>
                <td width='24'><a href='javascript:formSubmit(pageNo.value)'><img src='../images/report/g6.jpg' width='24' height='22' border='0' /></a></td>
                <td width='67'>&nbsp;</td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>