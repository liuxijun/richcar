<%@ page contentType="text/html;charset=gb2312"%>
<%@ page language="java" %>
<%@ page import = "com.runway.race.ipm.*,
                   cn.sh.guanghua.mediastack.common.ConfigManager" %><%@
 include file="./cookies.inc" %><%@ include file="./fj.inc" %>
<%
    /*
     * ��ҳ����Ҫ�������£�
     * ��ȡ�����еĲ���
     * ����в����ͽ��û��ĵ�¼��Ϣ֪ͨ������������û���¼����Ϣ      *
     */
     
    // ��ȡ�����еĲ�������SsoLogin�ӿڴ���
	String userInfo = request.getParameter("userInfo");
	String bindingId = request.getParameter("bindingId");
	String sessionId = request.getParameter("sessionId");
    //String resultChecker = ConfigManager.getConfig().node("account").get("resultChecker","");

	// �ж��Ƿ�����Ч���û���¼�Ĳ���
	if  (userInfo != null && !userInfo.equals("") && !userInfo.equals("null")
	     && bindingId != null && !bindingId.equals("") && !bindingId.equals("null")
	     && sessionId != null && !sessionId.equals("") && !sessionId.equals("null")){
	     
	    // ����в����ͽ��û��ĵ�¼��Ϣ֪ͨ������������û���¼����Ϣ
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	icpLoginInfoContainer.setLoginInfo(userInfo, sessionId, bindingId);

        //���û��ض�����resultChecker��������bindingId
        //response.sendRedirect(resultChecker +"bindingId="+bindingId);
	} else {
	    out.println("Failed to write Cookie for param losting!");
	}
%>