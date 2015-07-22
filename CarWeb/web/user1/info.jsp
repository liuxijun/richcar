<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @文件名称：AccountDo.jsp
 * @主要功能：
 * 显示操作的结果信息。
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
        cn.sh.guanghua.mediastack.business.normal.PlayLogic" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.MD5" %><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%><%@page
import="cn.sh.guanghua.mediastack.common.Db"%><%@page
import="cn.sh.guanghua.mediastack.common.Logger"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");
    try{
        String msg = ParamTools.getParameter(request,"msg","");
        String flag =  ParamTools.getParameter(request,"flag","");
        String returnUrl =  ParamTools.getParameter(request,"return_url","");

        sbData.append(PageHelper.addElement("flag",flag));
        sbData.append("<msg>" + msg + "</msg>");
        sbData.append(PageHelper.addElement("return_url",returnUrl));

    }catch(Throwable t){
        t.printStackTrace();
    }
    pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");

%>