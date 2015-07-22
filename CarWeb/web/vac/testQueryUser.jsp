<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-29
  Time: 上午10:24
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userIp = request.getRemoteAddr();%><!DOCTYPE html><html>
<head>
    <meta name="viewport" content="width=640" />
    <title>测试用户IP</title>
    <script type="text/javascript">
        var phoneQueryResult = null;
        var systemIsReady =false;
        function setInfo(id, info) {
            try {
                $(id).innerHTML = info;
            } catch (e) {
                var ele = document.getElementById(id);
                if (ele != null) {
                    if(info==""){
                        info = "&nbsp;&nbsp;";
                    }
                    ele.innerHTML = info;
                } else {
                    alert(id + "=" + info);
                }
            }
        }
        function setResult(result) {
            phoneQueryResult = result;
            tryDisplay();
        }
        function displayResult(){
            setInfo("phoneDiv", phoneQueryResult.phone);
            setInfo("msgDiv", phoneQueryResult.result);
        }
        function tryDisplay(){
            if(phoneQueryResult!=null&&systemIsReady==true){
                 displayResult();
            }
        }
    </script>
    <script type="text/javascript" src="http://61.55.144.86/vac/queryUser.jsp?command=ip2phone&t=<%=System.currentTimeMillis()%>&format=json&callBack=setResult"></script>
    <style type="text/css">
        .header {
            font-size: 20px;
            text-align: center;
        }

        .main {
            border: black solid 1px;
            width: 640px;
        }

        .infoLine {
            border-bottom: black solid 1px;
        }

        .info {
            float: left;
            width: 500px;
            color:blue;
            border-left: black solid 1px;
        }

        .label {
            text-align: right;
            float: left;
            width: 100px;
        }

        .onlineRow {
            width: 640px;
            text-align: center;
            border-top: black solid 1px;
        }
    </style>
</head>
<body onload="systemIsReady=true;tryDisplay();">
<div class="header" id="pageHeader">测试用户手机号码：</div>
<div class="main">
    <div class="infoLine">
        <div class="label">当前IP：</div>
        <div class="info"><%=userIp%>
        </div>
    </div>
    <br/>
    <div class="infoLine">
        <div class="label">请求时间：</div>
        <div class="info" id="queryTimeDiv"><%=StringUtils.date2string(new Date())%></div>
    </div>
    <br/>
    <div class="infoLine">
        <div class="label">用户号码：</div>
        <div class="info" id="phoneDiv">正在查询</div>
    </div>
    <br/>

    <div class="infoLine">
        <div class="label">调用信息：</div>
        <div class="info" id="msgDiv">...</div>
    </div>
    <br/>
    <br/>
    <div class="infoLine">
        <div class="onlineRow"><a href="?time=<%=System.currentTimeMillis()%>">刷新</a></div>
    </div>
</div>
</body>
</html>