<%@ page contentType="text/html;charset=gb2312"%><%@ page language="java" %><%@ page
        import = "com.runway.race.ipm.*" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@include file="../sxCookies.jsp"%><%
    /* 
     * ���ҳ���������£�
     *                 ֪ͨICP����������������ϵ��û���¼��Ϣ
     *                 �����ǰIE�����е�ICP���ڵ�cookie
     *                 ���û��˳���¼�Ĳ���֪ͨCNUS������
     */
    
    /* ��cookie�ж�bingdingId��Ϣ,������ھ������µ������� */
//    SessionUser su = new SessionUser();
    String bindingID =getCookieValue(request, "bindingID");
	if (null != bindingID && !bindingID.equals("") && !bindingID.equalsIgnoreCase("null") ) {
	    //֪ͨICP����������������ϵ��û���¼��Ϣ
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	icpLoginInfoContainer.removeLoginInfo(bindingID);
		
		//�����ǰIE�����е�ICP���ڵ�cookie
/*
		su.setBindingId("");
        su.setSessionId("");
        su.setSessionId("");
				
*/
	    //���û��˳���¼�Ĳ���֪ͨCNUS������
        String ispLogoutURL = AppConfigurator.getInstance().getConfig("account.ispLogoutURL","");
		response.sendRedirect(ispLogoutURL);
	}
	
%>

