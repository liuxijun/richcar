<%
    /**
     * Copyright ?2004 Shanghai Fudan Ghuanghua Co. Ltd.
     * All right reserved.
     *
     * @�ļ����ƣ�LoginCheck.jsp
     * @��Ҫ���ܣ�
     * mediastack��֤��½��
     * @���������
     * 1. spid �û���ǰ���ڵ�SPϵͳ��ָ��spid
     * 2. verifyurl   Spϵͳ����֤ǩ���ĵ�ַ
     * 3. returnurl   �û���ǰ����spϵͳ�ĵ�ַ
     * @���������
     * �����ɹ����
     * @��    �ߣ�zbxue
     * @��    �ڣ�
     *    @��ʼʱ�䣺 2004-10-27
     *    @����ʱ�䣺
     *
     *    @�޸���Ա��
     *    @�޸�������ԭ��
     *
     */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%>
 <%

     String cid = (String)session.getAttribute("DrmCid");
     String returnDrm = (String)session.getAttribute("LICENSE_RETURN");

%>
<html>
<body>
      <script language="javascript">
             self.opener.document.location.href='<%=returnDrm+"?login=true&time="+(Constants.PLAY_VALID_TIME)%>'
             window.close();
      </script>

</body>
</html>

