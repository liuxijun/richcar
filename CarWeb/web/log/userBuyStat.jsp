<%@ page import="java.util.ArrayList" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/2/12
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    List<String[]> userBuyHeaders = new ArrayList<String[]>();
    String productId = request.getParameter("productId");
    userBuyHeaders.add(new String[]{"rownum","序号","userBuyIndex"});
    userBuyHeaders.add(new String[]{"NAME","区域","userBuyArea"});
    userBuyHeaders.add(new String[]{"UC","购买量","userBuyCount"});
    String dateStart = request.getParameter("dateStart");
    String dateStop = request.getParameter("dateStop");
    if (dateStart == null) {
        dateStart = StringUtils.date2string(new Date(System.currentTimeMillis() - 28 * 24 * 3600 * 1000L), "yyyy-MM") + "-01";
    }
    if (dateStop == null) {
        dateStop = StringUtils.date2string(new Date(System.currentTimeMillis()), "yyyy-MM") + "-01";
    }
    String userBuyStatSql = "select a.name,b.uc from area a,(" +
            "select count(*) uc,area_id from user_buy where" +
            " buy_time>=to_date('" + dateStart + "','YYYY-MM-DD') and buy_time<=to_date('" + dateStop + "','YYYY-MM-DD')";
    if (productId != null && !"".equals(productId.trim())&&!"null".equals(productId)) {
        userBuyStatSql += " and product_id like '" + productId + "'";
    }else{
        productId = "";
    }
    userBuyStatSql += " group by area_id ) b " +
            "where a.id=b.area_id order by uc desc";
    String productStatSql = "select p.name,p.pay_product_no,b.uc from product p,(" +
            "select count(*) uc,product_id from user_buy where" +
            " buy_time>=to_date('" + dateStart + "','YYYY-MM-DD') and buy_time<=to_date('" + dateStop +
            "','YYYY-MM-DD') group by product_id) b" +
            " where p.pay_product_no = b.product_id order by b.uc desc";
    List<String[]> productStatHeaders = new ArrayList<String[]>();
    productStatHeaders.add(new String[]{"rownum","序号","productIndex"});
    productStatHeaders.add(new String[]{"NAME","产品","productName"});
    productStatHeaders.add(new String[]{"PAY_PRODUCT_NO","产品编号","productId"});
    productStatHeaders.add(new String[]{"UC","购买量","productBuyCount"});
%><html>
<head>
    <title>统计</title>
    <style type="text/css">
        .statMainBody{
            width:382px;
        }
        .statMainBody tr td{
            text-align: center;
        }
        .statMainBody tr th{
            background-color: #f8f8f8;
        }
        .statRow{
            width:382px;
        }
        .statRowHeader{
            background-color:#ffffff;
        }
        .userBuyIndex{
            width:50px;
        }
        .userBuyArea{
            width:150px;
        }
        .userBuyCount{
            width:160px;
        }
        .productIndex{
            width:50px;
        }
        .productId{
            width:80px;
        }
        .productName{
            width:100px;
        }
        .productBuyCount{
            width:100px;
        }
        input{
            width:80px;
        }
        #productList{
            width:170px;
        }
    </style>
</head>
<body>
<form action="userBuyStat.jsp" method="post">
    <table>
        <tr>
            <td>
                <label for="dateStart">起始日期：</label><input type="text" name="dateStart" value="<%=dateStart%>" id="dateStart">
                <label for="dateStop">截止日期：</label><input type="text" name="dateStop" value="<%=dateStop%>" id="dateStop"><br/>
                <label for="productList">产品编号：</label><input type="text" name="productId" id="productList" value="<%=productId%>"><input type="submit" value="搜索">
            </td>
        </tr>
        <tr>
            <td>
                <%=stat(userBuyStatSql, userBuyHeaders)%>
            </td>
        </tr>
        <tr>
            <td><%=dateStart%>到<%=dateStop%>之间，产品购买情况统计：<br/>
                <%=stat(productStatSql,productStatHeaders)%>
            </td>
        </tr>
    </table>
</form>
</body>
</html><%@include file="stat.jsp"%>
