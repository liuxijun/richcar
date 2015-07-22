<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>频道点播统计结果页</title>
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
</head>
<body>
<div id="main11_list">
    <table cellpadding="0" cellspacing="0" class="t_list">
        <tr>

            <th>频道名称</th>
            <!--
           <th>用户IP</th>
            -->
            <th>点播人数</th>

        </tr>

        <%
            List<Map> channelList = (List<Map>) request.getAttribute("objLists");
            for(int i=0;i<channelList.size();i++){
                Map<String,Object> channelMap = channelList.get(i);
         %>

        <tr>
            <td>
                <%=channelMap.get("channel_name")%>&nbsp;
            </td>
            <!--
           <td>
               &nbsp;
           </td>
            -->
            <td>
                <%=channelMap.get("channel_num")%>&nbsp;
            </td>
        </tr>
        <%
            }
        %>


    </table>
</div>

</body>
</html>