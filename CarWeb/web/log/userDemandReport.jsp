<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String excel = request.getParameter("excel");
    if ("true".equals(excel)){
        response.setContentType( "applicationnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Dist.xls");
    }
%>
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
    <script type="text/javascript">
        function formSubmit(pageNo){
            var startRow = 0;
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
        function formSubmit1(pageNo,pageSize){
            if(pageNo==""&&pageSize==""){
                return;
            }else{
              if(pageNo==""){
                  pageNo = 1;
              }
              if(pageSize==""){
                  pageSize = form1.elements["pageBean.pageSize"].value;
              }
                form1.elements["pageBean.pageNo"].value = pageNo;
                form1.elements["pageBean.pageSize"].value = pageSize;
                form1.elements["pageBean.startRow"].value = 1;
           /*     form1.elements["pageBean.limit"].value = pageSize;*/
                form1.submit();
            }
        }
    </script>
</head>
<body>
<!--页面数据区 开始-->
<div id="main_list">
    <table cellpadding="0" cellspacing="0" class="t_list">
        <tr>
            <th>服务提供商</th>
            <th>频道名称</th>
            <th>资源名称</th>
            <th>播放时长</th>
            <th>起始时间</th>
            <th>结束时间</th>
            <th>用户IP</th>
            <th>用户Id</th>
        </tr>

        <%
            Map<String,Object> objMaps = (Map) request.getAttribute("objMaps");
            if(objMaps!=null){
                List objLists = (List) objMaps.get("userDemandList");
                for(int i=0;i<objLists.size();i++){
                    Map<String,Object> userDemandMap = (Map<String, Object>) objLists.get(i);
        %>
        <tr>
            <td>
                <%=userDemandMap.get("cp_name")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("channel_name")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("content_name")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("play_time")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("start_time")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("end_time")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("user_ip")%>&nbsp;
            </td>
            <td>
                <%=userDemandMap.get("user_id")%>&nbsp;
            </td>
        </tr>
        <%
                }
            }
        %>

    </table>
</div>
<%
    if ("true".equals(excel)) {
%>
<table>
    <tr>
        <td>&nbsp;</td>
        <td>管理员：<%
            Admin admin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
            out.print(admin.getRealname());
        %></td>
        <td>导出时间：<%
            SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
            String   date   =   sDateFormat.format(new   java.util.Date());
            out.print(date);
        %></td>
    </tr>
</table>
<%
}else{
%>
<form id="form1" name="form1" action="/log/visitLog!getUserDemandCount.action" method="post">
    <%
        PageBean pageBean = (PageBean) request.getAttribute("pageBean");
        String startTime="",endTime="",contentName="",channelName="",userIp="",userId="",playTime="";
        long spId,cpId;
        Map<String,Object> searchParams = (Map<String, Object>) objMaps.get("searchParams");

        startTime = (String) searchParams.get("start_time");
        endTime = (String)searchParams.get("end_time");
        contentName = (String)searchParams.get("content_name");
        channelName = (String)searchParams.get("channel_name");
        userIp = (String)searchParams.get("user_ip");
        userId = (String)searchParams.get("user_id");
        playTime = (String)searchParams.get("play_time");
        spId = (Long)searchParams.get("sp_id");
        cpId = (Long)searchParams.get("cp_id");
    %>
    <input type="hidden" name="startTime" value="<%=startTime%>"/>
    <input type="hidden" name="endTime" value="<%=endTime%>"/>
    <input type="hidden" name="contentName" value="<%=contentName%>"/>
    <input type="hidden" name="channelName" value="<%=channelName%>"/>
    <input type="hidden" name="userIp" value="<%=userIp%>"/>
    <input type="hidden" name="userId" value="<%=userId%>"/>
    <input type="hidden" name="playTime" value="<%=playTime%>"/>
    <input type="hidden" name="spId" value="<%=spId%>"/>
    <input type="hidden" name="cpId" value="<%=cpId%>"/>

    <s:hidden name="pageBean.pageNo" />
    <s:hidden name="pageBean.pageSize" />
    <s:hidden name="pageBean.startRow" />

    <div style="background-color:#3399FF;height: 50px; ">
        <table width='0' border='0' cellspacing='0' cellpadding='0'>
            <tr>
                <td width='61'>&nbsp;</td>
                <td width='47'><img src='../images/report/g1.jpg' width='6' height='21' /></td>
                <td width='350' class='copy_1'>每页
                    <input name='limit' id= "pageSize" type='text'value="<s:property value='pageBean.pageSize'/>" size='2' />
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
                <td width='24'><a href='javascript:formSubmit1(pageNo.value,pageSize.value)'><img src='../images/report/g6.jpg' width='24' height='22' border='0' /></a></td>
                <td width='67'>&nbsp;</td>
            </tr>
        </table>
    </div>
</form>
<%
    }
%>
</body>
</html>