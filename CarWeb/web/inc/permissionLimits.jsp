<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-10-17
  Time: 16:40:14
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@page
        import="java.util.*" %><%@ page
        import="com.fortune.common.web.ext.*" %><%
    ___httpSession = request;
%><%!
    private HttpServletRequest ___httpSession ;
    @SuppressWarnings("unchecked")
    public void needPermissions(String actionName,String actionId,String permissionId){
        Map<String,List<ActionPermission>> permissionLimits =
                (Map<String,List<ActionPermission>>)___httpSession.getAttribute("permissionLimits");
        if(permissionLimits==null){
            permissionLimits = new HashMap<String,List<ActionPermission>>();
            ___httpSession.setAttribute("permissionLimits",permissionLimits);
        }
        List<ActionPermission> limits = permissionLimits.get(actionName);
        if(limits==null){
            limits = new ArrayList<ActionPermission>();
            permissionLimits.put(actionName,limits);
        }
        limits.add(new ActionPermission(actionId,permissionId));
    }
%>