<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-10-17
  Time: 16:18:10
  关于Ext的一些基础工具
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page
        import="com.fortune.common.business.security.model.Menu,
        com.fortune.util.AppConfigurator" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.common.business.security.model.Permission,
        java.util.Map,java.util.List,com.fortune.common.web.ext.*,
        com.fortune.common.Constants,org.apache.log4j.Logger,
        com.fortune.common.business.security.model.Admin" %><%@ page import="com.fortune.util.TreeUtils" %><%
    boolean needCheckPermission = AppConfigurator.getInstance().getBoolConfig("needCheckPermission",false);
    Logger logger = Logger.getLogger("com.fortune.redex.extBase.jsp");
    int functionCanUseCount = 0;
    String _canUseFunctions = "";
    {
        String _actionHeader = (String) session.getAttribute("actionHeader");
        if(_actionHeader!=null){
            Map<String,List<ActionPermission>> limits = (Map<String,List<ActionPermission>>)request.getAttribute("permissionLimits");
            if(limits!=null){
                List<ActionPermission> needPermissions = limits.get(_actionHeader);
                Map<String, Permission> map = (Map<String, Permission>) session.getAttribute(Constants.SESSION_ADMIN_PERMISSION);
                if(needCheckPermission&&map == null){
                }else{
                    if(needPermissions!=null){
                        for(ActionPermission permissions:needPermissions){
                            boolean canUseThisFun = false;
                            if(needCheckPermission){
                                for(String thisPermissionStr:permissions.getNeedPermissions()){
                                    String[] thisPermissions = thisPermissionStr.split(",");
                                    for(String thisPermission:thisPermissions){
                                        if(map.containsKey(thisPermission)){
                                            //可以使用这个button或链接
                                            canUseThisFun = true;
                                            break;
                                        }
                                    }
                                    if(canUseThisFun){
                                        break;
                                    }
                                }
                            }else{  //不需要检查，直接允许使用
                                canUseThisFun = true;
                            }
                            if(canUseThisFun){
                                String actionId = "'"+permissions.getAction()+"'";
                                if(_canUseFunctions.indexOf(actionId)<0){
                                    if(!"".equals(_canUseFunctions)){
                                        _canUseFunctions += ",";
                                    }
                                    _canUseFunctions+="'"+permissions.getAction()+"'";
                                    functionCanUseCount++;
                                }
                            }else{
                                logger.warn(_actionHeader+"的这个权限不能用："+permissions.getAction());
                            }
                        }
                    }else{
                        logger.warn(_actionHeader+"的输出权限列表为空");
                    }
                }
            }else{
                logger.warn(_actionHeader+"的输出权限列表为空");
            }
        }else{
            logger.warn("actionHeader为空！");
        }
    }
    Admin admin = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
    Integer operatorAreaId = -999999;
    String operatorAreaText = "没有任何区域";
    if(functionCanUseCount==0){//}&&(!needCheckPermission)){
         _canUseFunctions ="'view'";//"'delete','view','list','add','save','deleteSelected','lock'";
    }
    TreeUtils tu = TreeUtils.getInstance();
%><script type="text/javascript">
    canUseFunctions=[<%=_canUseFunctions%>];
</script>
<%!
%>