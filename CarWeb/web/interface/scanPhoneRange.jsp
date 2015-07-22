<%@ page import="com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.log.model.ClientLog" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.rms.business.system.model.PhoneRange" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.PhoneRangeLogicInterface" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface" %>
<%@ page import="com.fortune.rms.business.user.model.UserLogin" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-4
  Time: 上午10:26
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    PhoneRangeLogicInterface phoneRangeLogicInterface = (PhoneRangeLogicInterface) SpringUtils.getBean("phoneRangeLogicInterface",session.getServletContext());
    PhoneRange bean =new PhoneRange();
    List<PhoneRange> allRange = phoneRangeLogicInterface.search(bean,new PageBean(0,Integer.MAX_VALUE,"o1.phoneFrom","asc"));
    int i=0;
    String result = "";
    long phoneTo=Long.MAX_VALUE;
    PhoneRange lastPhoneRange = null;
    String willDeletePhoneRangePhoneTo = "";
    int deleteCount = 0,updateCount=0,reserveCount=0,errorCount=0;
    for(PhoneRange pr:allRange){
        long prFrom = pr.getPhoneFrom();
        long prTo = pr.getPhoneTo();
        if(lastPhoneRange==null){
            phoneTo = prTo;
            lastPhoneRange = pr;
            continue;
        }
        //如果两个号段相连
        if(phoneTo==prFrom-1&&pr.getAreaId().equals(lastPhoneRange.getAreaId())){
            logger.debug("相连号段，这个pr可以删除："+pr);
            phoneTo = prTo;
            willDeletePhoneRangePhoneTo +=pr.getPhoneFrom()+"-"+pr.getPhoneTo()+",";
            result += "delete from PHONE_RANGE where ID="+pr.getId()+";/*"+pr.getName()+"," +pr.getPhoneFrom()+"->"+pr.getPhoneTo()+
                    "*/\n";
            deleteCount++;
        }else if(phoneTo>=prFrom){
            //重复号段
            result = ("-- !!!!!!号段堆叠重复：id="+pr.getId()+","+phoneTo+">"+prFrom+","+pr.getName()+","+pr.getDesp()+"\n");
            errorCount++;
        }else{
            if(phoneTo>lastPhoneRange.getPhoneTo()){
                result +="--  这些号段和本号段合并："+lastPhoneRange.getPhoneFrom()+"-"+lastPhoneRange.getPhoneTo()+
                        ","+ willDeletePhoneRangePhoneTo +"==>"+lastPhoneRange.getPhoneFrom()+"->"+phoneTo+"\n";
                lastPhoneRange.setPhoneTo(phoneTo);
                result+="update PHONE_RANGE set PHONE_TO="+lastPhoneRange.getPhoneTo()+" where ID="+lastPhoneRange.getId()+";\n\n";
                updateCount++;
            }else{
                result +="--  这个不用动："+pr.getName()+","+pr.getPhoneFrom()+","+pr.getPhoneTo()+"\n";
                reserveCount++;
            }
            willDeletePhoneRangePhoneTo = "";
            phoneTo = prTo;
            lastPhoneRange = pr;
        }
    }
    if(lastPhoneRange!=null){
        if(phoneTo>lastPhoneRange.getPhoneTo()){
            result +="--  这些号段和本号段合并："+lastPhoneRange.getPhoneFrom()+"-"+lastPhoneRange.getPhoneTo()+
                    ","+ willDeletePhoneRangePhoneTo +"==>"+lastPhoneRange.getPhoneFrom()+"->"+lastPhoneRange.getPhoneTo()+"\n";
            lastPhoneRange.setPhoneTo(phoneTo);
            result+="update PHONE_RANGE set PHONE_TO="+lastPhoneRange.getPhoneTo()+" where ID="+lastPhoneRange.getId()+";\n\n";
        }else{
            result +="--  这个不用动："+lastPhoneRange.getName()+","+lastPhoneRange.getPhoneFrom()+","+lastPhoneRange.getPhoneTo()+"\n";
        }
    }
    result+="-- 累计，删除" +deleteCount+
            "条记录，保留" +reserveCount+
            "条记录，更新" +updateCount+
            "条记录，错误" +errorCount+
            "条记录。现在系统依然有" +(reserveCount+updateCount+errorCount)+
            "条记录。\n";

%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairAreaId.jsp");
%>
<html>
<head>
    <title>检查号段的情况</title>
</head>
<body>
<pre><%=result%></pre>
</body>
</html>
