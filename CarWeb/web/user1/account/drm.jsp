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
import="cn.sh.guanghua.util.tools.*" %>
 <%

     String cid = ParamTools.getParameter(request,"cid","");
     String returnDrm = ParamTools.getParameter(request,"return","");

%>
<html>
<body>
      <form action="../Play.jsp" method="post" name="form1" target="drm">
      <input type="hidden" name="cid"  value="<%=cid%>"/>
      <input type="hidden" name="return"   value="<%=returnDrm%>"/>
      <input type="hidden" name="openflag"   value="true"/>
      </form>
���Ժ�...
<script language="javascript">
        //window.resizeTo(800,600);
        window.open("","drm","width=510,height=315,resizable=yes");
        form1.submit();
        //window
</script>

</body>
</html>

