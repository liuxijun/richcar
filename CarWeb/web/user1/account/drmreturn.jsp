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

