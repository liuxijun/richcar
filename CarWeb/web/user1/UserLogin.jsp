<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�UserLogin.jsp
 * @��Ҫ���ܣ�
 * 1. �����û������룬�жϵ�½
 * 2. ��session
 * @���������
 * 1. user_id
 * 2. user_pas
 * @���������
 * 1. ��user�û�����ϸ��Ϣ
 * 2. ������Ϣ��add��
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-09-03
 *    @����ʱ�䣺
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
               msg = "�û�û�м�����Ѿ����������ܵ�½��";
           }else{
               if (su.getBlackListStatus() == Constants.USER_BLACKLIST){
                   msg = "�û��ں������У����ܵ�½��";
               }else{
                   if (myMd5.getMD5ofStr(userPas).equals(su.getUserPwd())){
                       UserLogic.changeLastLoginTime(userId);
                       msg = "��ӭ " + userId + "�������Ե㲥ӰƬ��";
                       session.setAttribute(Constants.SESSION_USER,su);
//                       if (!returnUrl.equals("")){
//                           if (returnUrl.indexOf("?")==-1){
//                               returnUrl = returnUrl + "?user_id=" + su.getUserId();
//                           }else{
//                               returnUrl = returnUrl + "&user_id=" + su.getUserId();
//                           }
//                       }
                   }else{
                       msg = "��½ʧ�ܣ��������û������ڻ��������";
                   }
               }
           }
       }else{
            msg = "���û������ڣ�";
       }
   }else{
       msg = "��֤���������";
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