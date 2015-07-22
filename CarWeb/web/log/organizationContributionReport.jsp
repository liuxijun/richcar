<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="/log/report.css" rel="stylesheet" type="text/css"/>
    <title>组织贡献统计结果</title>
</head>
<body>
<div id="main_list">
    <table cellpadding="0" cellspacing="0" align="center" class="t_list1">
        <tr>
            <td height="33%" colspan="5" rowspan="1" align="center">内容提供商贡献度</td>
            <td width="33%" colspan="5" align="center">服务提供商贡献度</td>
            <td width="34%" colspan="5" align="center">频道贡献度</td>
        </tr>
        <tr>
            <td width="6%" colspan="1" rowspan="3">内容提供商</td>
            <td colspan="2" rowspan="2">点播次数</td>
            <td colspan="2" rowspan="2">点播时长</td>
            <td rowspan="3">服务提供商</td>
            <td colspan="2">点播次数</td>
            <td colspan="2">点播时长</td>
            <td rowspan="3">频道</td>
            <td colspan="2">点播次数</td>
            <td colspan="2">点播时长</td>
        </tr>
        <tr>
            <td rowspan="2">次数</td>
            <td rowspan="2">比重</td>
            <td rowspan="2">时长</td>
            <td rowspan="2">比重</td>
            <td rowspan="2">次数</td>
            <td rowspan="2">比重</td>
            <td rowspan="2">时长</td>
            <td rowspan="2">比重</td>
        </tr>
        <tr>
            <td height="18">次数</td>
            <td>比重</td>
            <td>时长</td>
            <td>比重</td>
        </tr>
        <%
            Logger logger = LoggerFactory.getLogger("com.fortune.jsp.organizationContributionReport.jsp");
            HashMap objMaps = (HashMap) request.getAttribute("objMaps");
            logger.debug("spMap Size:" + objMaps.size());
            List cpLists = (List) objMaps.get("cp_list");
            for (int i=0; i< cpLists.size();i++) {
                HashMap cpMap = (HashMap) cpLists.get(i);
                List spLists = (List) cpMap.get("sp_list");
                long channelCountSize=0;
                for (Object spList : spLists) {
                    HashMap spMap = (HashMap) spList;
                    List channelList = (List) spMap.get("channel_list");
                    if(channelList!=null){
                        channelCountSize += channelList.size();
                    }

                }

        %>
        <tr>
            <td rowspan="<%=channelCountSize%>" align="center"><%=cpMap.get("cp_name")%>
            </td>
            <td rowspan="<%=channelCountSize%>"><%=cpMap.get("cp_num")%>
            </td>
            <td rowspan="<%=channelCountSize%>"><%=cpMap.get("cp_num_percentage")%>
            </td>
            <td rowspan="<%=channelCountSize%>"><%=cpMap.get("cp_time")%>
            </td>
            <td rowspan="<%=channelCountSize%>"><%=cpMap.get("cp_time_percentage")%>
            </td>
            <%

                for (Object spList : spLists) {
                    HashMap spMap = (HashMap) spList;
                    long channelSize=0;
                    List channelList = (List)spMap.get("channel_list");
                    if(channelList!=null){
                        channelSize = channelList.size();
                    }

            %>
            <td rowspan="<%=channelSize%>"><%=spMap.get("sp_name")%>
            </td>
            <td rowspan="<%=channelSize%>"><%=spMap.get("sp_num")%>
            </td>
            <td rowspan="<%=channelSize%>"><%=spMap.get("sp_num_percentage")%>
            </td>
            <td rowspan="<%=channelSize%>"><%=spMap.get("sp_time")%>
            </td>
            <td rowspan="<%=channelSize%>"><%=spMap.get("sp_time_percentage")%>
            </td>
            <% List channelLists = (List) spMap.get("channel_list");
                for (int k=0;k<channelLists.size();k++) {
                    HashMap channelMap = (HashMap) channelLists.get(k);
                    if(k>0){
                       out.print("<tr>");
                    }
            %>
            <td><%=channelMap.get("channel_name")%>
            </td>
            <td><%=channelMap.get("channel_num")%>
            </td>
            <td><%=channelMap.get("channel_num_percentage")%>
            </td>
            <td><%=channelMap.get("channel_time")%>
            </td>
            <td><%=channelMap.get("channel_time_percentage")%>
            </td>

            <%
                        if(k>0){
                            out.print("</tr>");
                        }
                    }
                }
            %>


        </tr>
        <%

            }

        %>


    </table>
    <table width="90%" cellpadding="0" cellspacing="0" align="center" class="t_list1">
        <tr>
            <td align="left">点播总计：<%=objMaps.get("cp_num_count")%>  点播总时长：<%=objMaps.get("cp_time_count")%>&nbsp;</td>
        </tr>
    </table>
</div>
<!--页面数据区 结束 -->

</body>
</html>