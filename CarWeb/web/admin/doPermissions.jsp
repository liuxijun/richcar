<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2012-1-12
  Time: 20:03:07
  权限处理
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page
        import="java.util.List,java.util.ArrayList,
    com.fortune.common.business.security.logic.logicInterface.*,
    com.fortune.common.business.security.model.*,
    com.fortune.util.*" %><%@ page import="org.apache.log4j.Logger" %><%
{
        Admin _opLogined =
                (Admin)session.getAttribute(
                        com.fortune.common.Constants.SESSION_ADMIN);
        if(_opLogined == null){
            logger.error("超时！操作无法继续："+request.getRemoteAddr());
            out.println("你，你，你是谁？");
            return;
        }
    }
    String command = request.getParameter("command");
    PermissionLogicInterface permissionLogic = (PermissionLogicInterface) SpringUtils.getBean(
            "permissionLogicInterface",session.getServletContext());
    RoleLogicInterface roleLogic = (RoleLogicInterface) SpringUtils.getBean(
            "roleLogicInterface", session.getServletContext());
    boolean errorHappened=false;
    String errorMsg = "";
    if("clear".equals(command)){
        session.setAttribute("SessionAccessList",null);
    }else if("checkExists".equals(command)){
        String name = request.getParameter("permissionName");
        String target = request.getParameter("permissionTarget");
        String className = request.getParameter("permissionClass");
        String methodName = request.getParameter("permissionMethodName");
        String result = "{success:true,checkResult:{";
        result +="name:"+permissionExists("name",name,permissionLogic)+",";
        result +="classname:"+permissionExists("classname",className,permissionLogic)+",";
        result +="methodName:"+permissionExists("methodName",methodName,permissionLogic)+",";
        result +="target:"+permissionExists("target",target,permissionLogic)+"}}";
        out.println(result);
        return;
    }else if("save".equals(command)){
        String name = request.getParameter("permissionName");
        String target = request.getParameter("permissionTarget");
        String className = request.getParameter("permissionClass");
        String methodName = request.getParameter("permissionMethodName");
        String desp = request.getParameter("permissionDesc");
        if(name==null||className==null||target==null||methodName==null){
            errorHappened = true;
            errorMsg = "添加权限时，有些项目为空！";
        }else{
            if(permissionExists("name",name,permissionLogic)){
                errorHappened = true;
                errorMsg = "这个权限已经存在：“"+name+"”";
            }else if(permissionExists("target",target,permissionLogic)){
                errorHappened = true;
                errorMsg = "这个权限别名已经存在：“"+target+"”";
            }else{
                Permission p = new Permission();
                p.setName(name);p.setClassname(className);p.setMethodName(methodName);p.setTarget(target);
                p.setPermissionDesc(desp);
                permissionLogic.save(p);
            }
        }
    }else if("saveRolePermission".equals(command)){
        errorHappened = true;
        errorMsg = "绑定完毕！";
        int roleId = StringUtils.string2int(request.getParameter("roleId"),-1);
        if(roleId<0){
            errorMsg = "添加绑定时，角色ID为空！";
        }else{
            String[] permissionIdStrs = request.getParameterValues("permissionId");
            List<Integer> permissionIds = new ArrayList<Integer>();
            if(permissionIdStrs!=null){
                for(String idStr:permissionIdStrs){
                    int permissionId = StringUtils.string2int(idStr,-1);
                    if(permissionId>0){
                        boolean permissionIdFound = false;
                        for(Integer id:permissionIds){
                            if(id==permissionId){
                                permissionIdFound = true;
                                break;
                            }
                        }
                        if(!permissionIdFound){
                            permissionIds.add(permissionId);
                            System.out.println("permissionId="+permissionId);
                        }
                    }
                }
                if(permissionIds.size()>0){
                    roleLogic.savePermissionToRole(permissionIds,roleId);
                }else{
                    errorMsg = "绑定时发生异常，输入的权限ID为空";
                }
            }else{
                errorHappened = true;
                errorMsg = "绑定时发生异常，输入的权限permissionID为空";
            }
       }
    }
    if(!errorHappened){
        response.sendRedirect("permissions.jsp");
    }
%><html>
<head>
    <title>处理完毕</title>
</head>
<body>
    处理结果：<%=errorMsg%>！<a href="javascript:history.back()">返回</a>!
</body>
</html><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.doPermissions.jsp");
    public boolean permissionExists(String propertyName,String propertyValue,PermissionLogicInterface permissionLogic){
        Permission p  = new Permission();
        if(propertyValue==null||"".equals(propertyValue.trim())){
            logger.warn("property:"+propertyName+"，是空！");
            return false;
        }
        BeanUtils.setProperty(p,propertyName,propertyValue);
        List<Permission> results = permissionLogic.search(p);
        if(results!=null){
            logger.debug("results.size()="+results.size());
        }
        return results!=null&&results.size()>0;
    }
%>