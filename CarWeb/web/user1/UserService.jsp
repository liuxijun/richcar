<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�UserService.jsp
 * @��Ҫ���ܣ�
 * 1. �����������user_id��ʾ��user����ϸ��Ϣ��
 * 2. �û����һ����룬
 * @���������
 * 1. user_id
 * 2. command(="findpwd")
 * @���������
 * 1. ��user�û�����ϸ��Ϣ
 * 2. ������Ϣ��add��
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-09-01
 *    @����ʱ�䣺
 *
 *
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.user.UserLogic,
        cn.sh.guanghua.cache.CacheManager" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.mediastack.common.Db" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");
    String msg = "";
    String returnUrl = "UserService.jsp?command=findpwd";
   try{
       String command = ParamTools.getParameter(request,"command","");
       MD5 myMd5=new MD5();
       if ("findpwd".equals(command)){

        }
       if ("answer".equals(command)){
           String userId = ParamTools.getParameter(request,"user_id","-1");
            User user =  (User)CacheManager.getInstance().getFromDB("User",userId);
           if (user == null){
              msg = "���û�������";
              throw new Exception();
           }else{
              sbData.append(PageHelper.addElement("user-inf",user));
           }
        }
      if ("pwd".equals(command)){
          String userId = ParamTools.getParameter(request,"user_id","-1");
          String answer = ParamTools.getParameter(request,"user_answer","");
            User user =  (User)CacheManager.getInstance().getFromDB("User",userId);
          if (!answer.equals(user.getUserAnswer())){
              msg = "�𰸴���";
              throw new Exception();
          }
          java.util.Random r = new Random();
          String pas = String.valueOf(r.nextInt(10000000));
          user.setUserPassword(pas);
          sbData.append(PageHelper.addElement("user-inf",user));
          if (!UserLogic.changePwd(userId,myMd5.getMD5ofStr(pas))){
              msg = "�һ�����ʧ��";
              throw new Exception();
          }

            CacheManager.getInstance().notifyServerUpdate("User",userId);
        }
        sbData.append(PageHelper.addElement("command",command));

    }catch(Throwable t){
       //t.printStackTrace();

       msg = java.net.URLEncoder.encode(msg);
       returnUrl =  java.net.URLEncoder.encode(returnUrl);
       response.sendRedirect("info.jsp?msg="+msg+"&return_url="+returnUrl);
       return;

    }
    pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");
%>