<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�AccountDo.jsp
 * @��Ҫ���ܣ�
 * ��ʾ�����Ľ����Ϣ��
 * @���������
 * 1. useracount_id���������Ϣ
 * 2. command(add��delete)
 * @���������
 * �����ɹ����
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-09-02
 *    @����ʱ�䣺
 *
 *    @�޸���Ա��
 *    @�޸�������ԭ��
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
       session.invalidate();
       String msg = java.net.URLEncoder.encode("ע���ɹ�");
       response.sendRedirect("info.jsp?flag=true&msg="+msg);
       return;
    }catch(Throwable t){
        t.printStackTrace();
    }
    pageHelper.outPut(out,sbData.toString(),"","","","","zbxue");

%>