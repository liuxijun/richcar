<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @文件名称：UserView.jsp
 * @主要功能：
 * 1. 根据输入参数user_id显示该user的详细信息；
 * 2. 新建用户
 * @输入参数：
 * 1. user_id
 * 2. command(="add")
 * @输出参数：
 * 1. 该user用户的详细信息
 * 2. 操作信息（add）
 * @作    者：zbxue
 * @日    期：
 *    @开始时间： 2004-08-30
 *    @结束时间：
 *
 *    @修改人员：qmwu
 *    @修改日期与原因：增加一个command，“chgpwd”用于更改密码
 *
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.user.UserLogic,
        cn.sh.guanghua.cache.CacheManager" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.mediastack.common.Db" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.*" %><%

   try{
       PageHelper pageHelper = new PageHelper(request,session);
       StringBuffer sbData = new StringBuffer("");
       String command = ParamTools.getParameter(request,"command","");
       if (!"add".equals(command)){
            SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
            if (su == null){
                 response.sendRedirect("Login.jsp?msg=session_unvalid");
                 return;
            }

            String userId = su.getUserId();
            if ("add".equals(command)){

            }else if ("modify".equals(command)){
                User user =  (User)CacheManager.getInstance().getFromDB("User",userId);
                sbData.append(PageHelper.addElement("user-inf",user));
            }

            if ("chgpwd".equals(command)){
                sbData.append(PageHelper.addElement("user-id",userId));
            }
       }
       HashMap userOption = Constants.USER_NEW_OPTION;
       Object set[] = userOption.keySet().toArray();
       for (int i=0; i<set.length; i++){
           String name = (String)set[i];
           HashMap hm = (HashMap)userOption.get(name);

           Object subkeys[] = hm.keySet().toArray();
           Arrays.sort(subkeys);
           StringBuffer sb = new StringBuffer();
           for (int j=0;j<subkeys.length;j++){
               String value = (String)subkeys[j];
               String text = (String)hm.get(value);
               value =PageHelper.addElement("value",value);
               text = PageHelper.addElement("text",text);
               sb.append("<item>").append(value).append(text).append("</item>");
           }
           sbData.append("<").append(name).append(">").append(sb.toString()).append("</").append(name).append(">");
       }

        sbData.append(PageHelper.addElement("command",command));
        pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");
    }catch(Throwable t){
        t.printStackTrace();
    }
%>