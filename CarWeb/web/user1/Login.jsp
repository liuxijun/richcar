<%@page
contentType="text/html;charset=gb2312" %><%@ page import="cn.sh.guanghua.util.tools.ParamTools,
                 cn.sh.guanghua.util.tools.PageHelper"%><%

    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");
    session.invalidate();
    try{
        String msg = ParamTools.getParameter(request,"msg","");
        String returnUrl = ParamTools.getParameter(request,"return_url","");
        sbData.append(PageHelper.addElement("msg",msg));
        sbData.append(PageHelper.addElement("return_url",returnUrl));
    }catch(Throwable t){
        t.printStackTrace();
    }
    pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");


%>