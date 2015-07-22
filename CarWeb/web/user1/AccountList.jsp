<%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.user.SessionUser,
        cn.sh.guanghua.mediastack.user.UserLogic" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.mediastack.common.*" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    PageHelper pageHelper = new PageHelper(request,session);
    StringBuffer sbData = new StringBuffer("");

    try{
       String icpId = String.valueOf(pageHelper.getCorporationId());
       SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
       if (su == null){
           response.sendRedirect("Login.jsp?msg=session_unvalid");
           return;
       }
        String userId = su.getUserId();

        List list = UserLogic.getUserAccounts(userId);
        long total = 0;
        if (list != null){
            total = list.size();
        }
        pageHelper.setAllRowCount(total);
        sbData.append(PageHelper.getList("account-inf",list));
    }catch(Exception e){
        e.printStackTrace();
    }
    pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");
%><%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�AccountList.jsp
 * @��Ҫ���ܣ�
 * ��ʾ�û������а󶨵��ʺ�
 * @���������
 * ��ҳ����pagesize��pageno
 * @���������
 * ��ҳ������group�Ļ�����Ϣ
 *
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-09-01
 *    @����ʱ�䣺
 *
 *    @�޸���Ա��
 *    @�޸�������ԭ��
 *
 */
%>
