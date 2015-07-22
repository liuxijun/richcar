<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-20
  Time: 上午10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>TouchEvent测试</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=640px,user-scalable=0"/>
    <style>
        .scroll_div {
            width: 614px;
            height: 200px;
            margin: 0 auto;
            overflow: hidden;
            white-space: nowrap;
            background: #ffffff;
            border: 1px solid red;
        }

        .poster {
            width: 90px;
            height: 140px;
            margin-top: 10px;
            border: 1px #efefef solid;
        }

        .scroll_header {
            display: inline-block;
        }

        .item {
            width: 150px;
            height: 180px;
            text-align: center;
            border: 1px solid blue;
            margin-left: 10px;
            margin-top: 10px;
            display: inline-block;
        }

            /*
                    #scroll_header, #scroll_tail, #scroll_header ul, #scroll_tail ul, #scroll_header ul li, #scroll_tail ul li {
                        display:inline;
                    }
            */
    </style>
</head>
<body>
<h2>TouchEvent测试</h2>

<%
    for(int l=0;l<11;l++){
%><div id="scroll_div_<%=l%>" class="scroll_div">
    <div id="scroll_header" class="scroll_header">
        <ul>
            <%
                for (int i = 0; i < 10; i++) {
            %>
            <li class="item"><a href="drag.jsp?id=<%=i%>"><img class="poster" src="images/1-1-0-_02.jpg"
                                                                alt="海报"><br/>电影<%=i%>
            </a></li>
            <%
                }
            %>
        </ul>
    </div>
</div>

<%
    }
%>
<div id="result" style="border:2px solid red; color:red;">未触发事件！</div>

<script type="text/javascript" src="page/hbMobile/js/dragUtils.js">
</script>
</body>
</html>