<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @文件名称：UserLogin.jsp
 * @主要功能：
 * 1. 输入用户名密码，判断登陆
 * 2. 置session
 * @输入参数：
 * 1. user_id
 * 2. user_pas
 * @输出参数：
 * 1. 该user用户的详细信息
 * 2. 操作信息（add）
 * @作    者：zbxue
 * @日    期：
 *    @开始时间： 2004-09-03
 *    @结束时间：
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.user.UserLogic" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.mediastack.common.Db" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.*" %><%
   try{
       String userId = ParamTools.getParameter(request,"user_id","");
       String userPas = ParamTools.getParameter(request,"user_pas","");
       String returnUrl = ParamTools.getParameter(request,"return_url","");
       String randNew = ParamTools.getParameter(request,"rand","");
       String randOld = (String)session.getAttribute("rand");
       String flag = ParamTools.getParameter(request,"flag","");
       String returnFlag = ParamTools.getParameter(request,"return_flag","");
       String msg = "";

       MD5 myMd5=new MD5();
   if (randNew.equals(randOld)){
       SessionUser su = UserLogic.getSessionUser(userId);
       if (su != null ){
           if (su.getAudited() != Constants.USER_AUDITED) {
               msg = "用户没有激活或已经撤户，不能登陆！";
           }else{
               if (su.getBlackListStatus() == Constants.USER_BLACKLIST){
                   msg = "用户在黑名单中，不能登陆！";
               }else{
                   if (myMd5.getMD5ofStr(userPas).equals(su.getUserPwd())){
                       UserLogic.changeLastLoginTime(userId);
                       msg = "欢迎 " + userId + "，您可以点播影片了";
                       session.setAttribute(Constants.SESSION_USER,su);
//                       if (!returnUrl.equals("")){
//                           if (returnUrl.indexOf("?")==-1){
//                               returnUrl = returnUrl + "?user_id=" + su.getUserId();
//                           }else{
//                               returnUrl = returnUrl + "&user_id=" + su.getUserId();
//                           }
//                       }
                   }else{
                       msg = "登陆失败，可能是用户不存在或密码错误！";
                   }
               }
           }
       }else{
            msg = "此用户不存在！";
       }
   }else{
       msg = "验证码输入错误！";
   }
       if (!returnFlag.equals("")){
           response.sendRedirect(returnUrl);
           return;
       }

       msg = java.net.URLEncoder.encode(msg);
       returnUrl =  java.net.URLEncoder.encode( returnUrl);
       if (flag.equals("play")){
           response.sendRedirect("account/info.jsp?flag="+ flag +"&msg="+msg+"&return_url="+returnUrl);
           return;
       }
       response.sendRedirect("info.jsp?flag="+ flag +"&msg="+msg+"&return_url="+returnUrl);
       return;
    }catch(Throwable t){
        t.printStackTrace();
    }

%>