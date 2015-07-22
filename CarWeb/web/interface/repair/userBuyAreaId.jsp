<%@ page import="com.fortune.rms.business.product.logic.logicInterface.UserBuyLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.product.model.UserBuy" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/2/12
  Time: 9:47
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    UserBuyLogicInterface userBuyLogicInterface = (UserBuyLogicInterface) SpringUtils.getBean("userBuyLogicInterface",session.getServletContext());
    PageBean pageBean = new PageBean(1,Integer.MAX_VALUE,null,null);
    List<UserBuy> userBuys = userBuyLogicInterface.getUnSetAreaIdData(new UserBuy(),pageBean);
    int allData =0;
    int repairedCount = 0;
    if(userBuys!=null){
        allData = userBuys.size();
        PhoneRangeLogicInterface phoneRangeLogicInterface = (PhoneRangeLogicInterface)SpringUtils.getBean("phoneRangeLogicInterface",session.getServletContext());
        for(UserBuy buy:userBuys){
            Long areaId = buy.getAreaId();
            if(areaId==null||areaId<=0){
                Long phone = StringUtils.string2long(buy.getUserId(),-1);
                if(phone>0){
                    areaId = phoneRangeLogicInterface.getAreaIdOfPhone(phone);
                    if(areaId>0){
                        buy.setAreaId(areaId);
                        userBuyLogicInterface.save(buy);
                        repairedCount ++;
                    }
                }
            }
        }
    }
    Date stopTime = new Date();
%><html>
<head>
    <title>修复未保存区域信息的用户购买数据</title>
</head>
<body>
启动时间：<%=StringUtils.date2string(startTime)%><br/>
结束时间：<%=StringUtils.date2string(stopTime)%><br/>
执行时长：<%=StringUtils.formatTime((startTime.getTime()-stopTime.getTime())/1000)%>
待修正数：<%=allData%>条<br/>
修正数量：<%=repairedCount%>条<br/>
</body>
</html>
