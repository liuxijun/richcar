<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-10-21
  Time: 13:00:53
  To change this template use File | Settings | File Templates.
--%><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@ taglib
        uri="/struts-tags" prefix="s" %><%@ page
        import="com.opensymphony.xwork2.ActionContext,
        com.opensymphony.xwork2.util.ValueStack,
        org.apache.log4j.Logger" %><%
    Logger logger = Logger.getLogger("com.fortune.common.jsonError.jsp");
    ActionContext context = ActionContext.getContext();
    if(context!=null){
        ValueStack vs = context.getValueStack();
        logger.debug("Error on "+context.getName()+"."+context.getActionInvocation().getProxy().getMethod());

        if(vs!=null){
/*
//        context.get
        for(String keyName:vs.getContext().keySet()){
            logger.debug("key="+keyName+",value="+vs.getContext().get(keyName));
        }
//  */
            try {
                Object ex = vs.findValue("exception");
                if(ex!=null){
                    logger.debug("异常："+ex.toString());
                }else{
                    logger.debug("没有找到exception!");
                    org.apache.struts2.dispatcher.RequestMap req = (org.apache.struts2.dispatcher.RequestMap)
                            vs.findValue("request");
                    vs.getContext().put("exception","可能是错误的输入参数,例如不该重复的参数或者数据格式不对："+req.get("struts.request_uri")+
                            "?"+req.get("javax.servlet.forward.query_string"));
                }
                Object exceptionStack = vs.findValue("exceptionStack");
                if(exceptionStack!=null){
                    logger.debug("exceptionStack="+exceptionStack.toString()) ;
                }else{
                    logger.debug("没有找到exceptionStack!");
                }
            } catch (Exception e) {
                logger.error("尝试获取exception时发生异常："+e.getMessage());
            }


        }
    }
%>{
    success:false,
    msg:"${exception}",error:"${exception}",stack:"",actionError:"${exception} ${jsonActionError}"<s:if test="hasFieldErrors()">,fieldErrors:'<s:iterator value="fieldErrors"><s:iterator value="value">#<s:property/></s:iterator>;</s:iterator>'
</s:if>}