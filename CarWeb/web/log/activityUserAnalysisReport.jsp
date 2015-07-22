<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link href="/log/report.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
            /*表单和列表样式*/
        .t_list,
        .t_form{ width:95%; margin:0 auto 15px; border-top:1px #fff solid; border-left:1px #fff solid;}

        .t_form td,
        .t_form th,
        .t_list th,
        .t_list td{ border-bottom:1px #fff solid; border-right:1px #fff solid; text-align:center; padding:2px 1px;  }

        .t_list th{background:url(../images/anniu.jpg) repeat-x 0 0; height:20px; color:#647080}

        .t_form td,
        .t_list td{color:#666;}

        .t_list .tr_grey td{ color:#bbb;}
        .t_list .tr_zi td{ color:#cc2bef;}

        .t_form th,
        .t_form td{ text-align:left;}
        .ul_col6 li{ float:left; width:25%;}
        .ul_col7 li{ float:left; width:28%;}


        .t_list2,
        .t_form{ width:95%; margin:0 auto 15px; border-top:1px #fff solid; border-left:1px #fff solid;}/*表单和列表样式*/

        .t_form td,
        .t_form th,
        .t_list2 th,
        .t_list2 td{ border-bottom:1px #fff solid; border-right:1px #fff solid; text-align:left; padding:2px 1px;  }

        .t_list2 th{background:url(../images/anniu.jpg) repeat-x 0 0; height:20px; color:#647080}

        .t_form td,
        .t_list2 td{color:#666;}

        .t_list2 .tr_grey td{ color:#bbb;}


        .t_form th,
        .t_form td{ text-align:left;}
        .ul_col4 li{ float:left; width:25%;}

        .t_list3,
        .t_form3{ width:95%; margin:0 auto 15px; border-top:1px #fff solid; border-left:1px #fff solid;}/*表单和列表样式*/

        .t_form3 td,
        .t_form3 th,
        .t_list3 th,
        .t_list3 td{ border-bottom:1px #fff solid; border-right:1px #fff solid; text-align:center; padding:2px 1px;  }

        .t_list3 th{background:url(../images/anniu2.jpg) repeat-x 0 0; height:20px; color:#647080}

        .t_form3 td,
        .t_list3 td{color:#666;}

        .t_list3 .tr_grey td{ color:#bbb;}
        .t_list3 .tr_grey2 td{ color:#000000;background:#539DE8}

        .t_form3 th,
        .t_form3 td{ text-align:left;}
        .ul_col6 li{ float:left; width:15%;}

        .t_list5,
        .t_form{ width:95%; margin:0 auto 15px;  }/*表单和列表样式*/

        .t_form td,
        .t_form th,
        .t_list5 th,
        .t_list5 td{ border-bottom:1px #fff solid;  text-align:left; padding:3px 1px;  }

        .t_list5 th{background:url(../images/) repeat-x 0 0; height:20px; color:#647080}

        .t_form td,
        .t_list5 td{color:#666;}

        .t_list5 .tr_grey td{ color:#bbb;}
        .t_list5 .tr_grey2 td{ color:#000000;background:#539DE8}

        .t_bottom{ background:#81B4E4; width:100%;position:absolute; left:0; bottom:0;}
    </style>
    <title>页面访问记录统计结果</title>
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
</head>
<body>
<div id="main_list">
    <table cellpadding="0" cellspacing="0" class="t_list2">
        <tr>
            <th>频道</th>
            <th>资源</th>
            <th>浏览次数</th>
        </tr>
        <%
            List<Map> objLists = (List<Map>) request.getAttribute("objLists");
            for(int i=0;i<objLists.size();i++){
                Map<String,String> activityUserAnalysisMap = objLists.get(i);
        %>

        <tr>
            <td>
                <%=activityUserAnalysisMap.get("channel_name")%>&nbsp;
            </td>
            <td>
                <%=activityUserAnalysisMap.get("content_name")%>&nbsp;
            </td>
            <td>
                <%=activityUserAnalysisMap.get("num")%>&nbsp;
            </td>
        </tr>
        <%
            }
        %>
    </table>
    &nbsp;&nbsp;
</div>
<form id="form1" name="form1" action="/log/visitLog!getActivityUserAnalysisCount.action" method="post">
    <%
        PageBean pageBean = (PageBean) request.getAttribute("pageBean");
        String startTime="",endTime="";
        for(int j = 0;j< objLists.size();j++ ){
            if(j==0){
                Map<String,String> activityUserAnalysisMap = objLists.get(j);
                startTime = activityUserAnalysisMap.get("start_time");
                endTime = activityUserAnalysisMap.get("end_time");
            }
        }
    %>
    <input type="hidden" name="startTime" value="<%=startTime%>"/>
    <input type="hidden" name="endTime" value="<%=endTime%>"/>
    <s:hidden name="pageBean.pageNo" />
    <s:hidden name="pageBean.pageSize" />

    <div style="background-color:#3399FF;height: 50px; ">
        <table width='0' border='0' cellspacing='0' cellpadding='0'>
            <tr>
                <td width='61'>&nbsp;</td>
                <td width='47'><img src='../images/report/g1.jpg' width='6' height='21' /></td>
                <td width='350' class='copy_1'>每页
                    <input name='limit' type='text'value="<s:property value='pageBean.pageSize'/>" size='2' />
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