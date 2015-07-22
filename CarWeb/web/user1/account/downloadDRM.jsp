<%
    /**
     * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
     * All right reserved.
     * 文件名称: .jsp
     * 主要功能:
     *
     * 输入参数： 
     * 输出数据:
     *
     * 作    者:    xjliu
     * 日    期：
     *    开始时间： 2003-12-28 18:22:35
     *    结束时间：
     *
     *    修改人员：
     *    修改日期与原因：
     */
%><%@ page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.util.tools.PageHelper,
        cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.StringTools,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.mediastack.common.Constants"%><%
            String userId = (String)session.getAttribute(Constants.SESSION_USER_ID);
            if(userId==null||"".equals(userId) || Constants.GUEST_SESSION_NAME.equals(session.getAttribute(Constants.SESSION_USER_ID))){
                response.sendRedirect("error.jsp");
                return;
            }
            String mediaUrl = ParamTools.getParameter(request,"mu","");
            mediaUrl = Base64.decode(mediaUrl) ;
            String ftpUrl = PlayLogic.getFtpUrl(mediaUrl);
            response.sendRedirect(ftpUrl);
            return;
%>