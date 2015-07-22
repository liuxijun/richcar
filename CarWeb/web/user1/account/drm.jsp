<%
    /**
     * Copyright ?2004 Shanghai Fudan Ghuanghua Co. Ltd.
     * All right reserved.
     *
     * @文件名称：LoginCheck.jsp
     * @主要功能：
     * mediastack认证登陆。
     * @输入参数：
     * 1. spid 用户当前所在的SP系统的指定spid
     * 2. verifyurl   Sp系统的验证签名的地址
     * 3. returnurl   用户当前操作sp系统的地址
     * @输出参数：
     * 操作成功与否
     * @作    者：zbxue
     * @日    期：
     *    @开始时间： 2004-10-27
     *    @结束时间：
     *
     *    @修改人员：
     *    @修改日期与原因：
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
请稍候...
<script language="javascript">
        //window.resizeTo(800,600);
        window.open("","drm","width=510,height=315,resizable=yes");
        form1.submit();
        //window
</script>

</body>
</html>

