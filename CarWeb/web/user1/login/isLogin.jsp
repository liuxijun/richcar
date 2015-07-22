<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.util.tools.Base64,
        cn.sh.guanghua.mediastack.business.normal.PlayLogic,
        cn.sh.guanghua.mediastack.dataunit.MediaIcp,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.dataunit.Channel,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.interfaceicp.MediaInfoBuilder,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.PreparedStatement,
        java.sql.Connection,
        java.sql.ResultSet,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.dataunit.IcpMediaInfo,
        cn.sh.guanghua.cache.CacheManager,
        cn.sh.guanghua.mediastack.dataunit.Tvchannel,
        cn.sh.guanghua.mediastack.business.normal.ChannelLogic,
        cn.sh.guanghua.mediastack.common.Db,
        cn.sh.guanghua.util.tools.StringTools"%><%@ include file="../cookies.jsp" %>
<%

     /**
     * ��ҳ��������ж���
     * һ�Ƕ�Cookie�е���Ϣ��
     * ���Ǹ��ݶ����Ľ����ʾ��ͬ������
     */
    String userInfo = "";
    String sessionId = "";
    String bingindId = "";

    /*  ��ҳ�������֮һ����Cookie�е���Ϣ��userInfo��sessionId��bindingId */
    userInfo  = getCookieValue(request, "userInfo");
    sessionId = getCookieValue(request, "sessionID");
    bingindId = getCookieValue(request, "bindingID");

    String returnUrl = ParamTools.getParameter(request, "return_url", "");

    //�û���¼�ɹ�
    if  (userInfo != null
                 && !userInfo.equals("")
                 && !userInfo.equals("null")
                 && sessionId != null
                 && !sessionId.equals("")
                 && !sessionId.equals("null"))
    {
        SessionUser su = new SessionUser();
        su.setUserInfo(userInfo);
        su.setSessionId(sessionId);
         //�õ��û�id
        su.setUserId(StringTools.getSubString(userInfo,"|",5));
        session.setAttribute(Constants.SESSION_USER,su);
        response.sendRedirect(returnUrl);
        return;
    }else{
        response.sendRedirect("Login.jsp?return_url="+java.net.URLEncoder.encode(returnUrl));
        return;
    }

%>