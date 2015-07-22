<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @文件名称：AccountDo.jsp
 * @主要功能：
 * 完成user的绑定帐户增、删；
 * @输入参数：
 * 1. useracount_id或其基本信息
 * 2. command(add，delete)
 * @输出参数：
 * 操作成功与否
 * @作    者：zbxue
 * @日    期：
 *    @开始时间： 2004-09-02
 *    @结束时间：
 *
 *    @修改人员：
 *    @修改日期与原因：
 *
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.user.UserLogic,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.cache.CacheManager" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.MD5" %><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%><%@page
import="cn.sh.guanghua.mediastack.common.Db"%><%@page
import="cn.sh.guanghua.mediastack.common.Logger"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");
    String msg = "";
    String returnUrl = "AccountList.jsp";
    try{
        SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
        if (su == null){
            response.sendRedirect("Login.jsp?msg=session_unvalid");
            return;
        }
         String userId = su.getUserId();

        String sCommand = ParamTools.getParameter(request,"command","invalid");
        sbData.append(PageHelper.addElement("command",sCommand));

        if ("add".equals(sCommand) || "modify".equals(sCommand)){
        }

        if ("delete".equals(sCommand)){
            String userAccountId[] = request.getParameterValues("useraccount_id");
            for (int i=0; i<userAccountId.length; i++){
                if (!UserLogic.removeUserAccount(userAccountId[i])){
                    msg ="删除用户帐户失败";
                    throw new Exception();
                }
            }


            CacheManager.getInstance().notifyServerUpdate("User",userId);
        }

        msg = "删除用户帐户成功";
    }catch(Throwable t){
        t.printStackTrace();
    }

    msg = java.net.URLEncoder.encode(msg);
    returnUrl =  java.net.URLEncoder.encode(returnUrl);
    response.sendRedirect("info.jsp?msg="+msg+"&return_url="+returnUrl);
    return;

%>