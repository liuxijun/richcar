<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2012-1-12
  Time: 19:27:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map,
                 java.util.HashMap,
                 java.util.List,
                 java.util.ArrayList,
                 com.fortune.common.business.security.logic.logicInterface.*,
                 com.fortune.common.business.security.model.*,
                 com.fortune.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    boolean displayUnknownActionPackage = "true".equals(request.getParameter("displayUnknownActionPackage"));
    Map<String, Long> accessData = (Map<String, Long>) session.getAttribute("SessionAccessList");
    if (accessData == null) {
        accessData = new HashMap<String, Long>();
    }
    PermissionLogicInterface permissionLogic = (PermissionLogicInterface) SpringUtils.getBean(
            "permissionLogicInterface", session.getServletContext());
    RoleLogicInterface roleLogic = (RoleLogicInterface) SpringUtils.getBean(
            "roleLogicInterface", session.getServletContext());
    List<Role> roles = roleLogic.getAll();
    Map<String, List<String>> actionMethods = new HashMap<String, List<String>>();
    for (String key : accessData.keySet()) {
        String[] nameAndMethod = key.split(":");
        if (nameAndMethod.length >= 2) {
            String actionName = nameAndMethod[0];
            String methodName = nameAndMethod[1];
            List<String> methods = actionMethods.get(actionName);
            if (methods == null) {
                methods = new ArrayList<String>();
            }
            methods.add(methodName);
            actionMethods.put(actionName, methods);
        }
    }
%>
<html>
<head><title>Action使用情况列表</title>
    <style type="text/css">
        .textClass {
            width: 400px
        }

        .textAreaClass {
            width: 400px;
            height: 80px
        }

        .actionNameClass {
            width:300px
        }

        .methodMainTable {
            width:400px
        }
        .methodNameTableLeftPart{
            width:200px
        }
        .methodNameTableRightPart{
            width:200px
        }
        .infoLabel {
        }

        .infoInput {
        }

        .mainTableLeftPart {
            width: 700px
        }

        .mainTableRightPart {
            width: 600px
        }
    </style>
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript">
        function displayResult(eleId,propertyName,jsonData){
            var ele = document.getElementById(eleId);
            if(ele!=null){
                var info = "";
                if(jsonData.checkResult[propertyName]){
                    info = "<font color='red'>**</font>";
                }else{

                }
                ele.innerHTML = info;
            }
        }
        function checkExists(){
            alert("去检查");
            jQuery.getJSON('doPermissions.jsp?command=checkExists&date='+new Date(), {
                permissionName:permission.permissionName,
                permissionTarget:permission.permissionTarget,
                permissionClass:permission.permissionClass,
                permissionMethodName:permission.permissionMethodName
            }, function(jsonData) {
                displayResult("permissionNameInfo","name",jsonData); 
                displayResult("permissionTargetInfo","target",jsonData); 
                displayResult("permissionMethodNameInfo","methodName",jsonData); 
            });
        }
        function clearSession() {
            permission.command.value = "clear";
            permission.submit();
        }
        function saveRolePermission() {
            var allRoles = permission.elements["roleId"];
            var roleSelected = false;
            if(allRoles!=null){
                for(var i=0,l=allRoles.length;i<l;i++){
                    var ele = allRoles[i];
                    if(ele.checked){
                        roleSelected =true;
                        break;
                    }
                }
            }
            if(!roleSelected){
                alert("没有选择任何的角色！请选择角色后再试！");
                return;
            }
            var allPermissions = permission.elements["permissionId"];
            var permissionSelected = false;
            if(allPermissions!=null){
                for(var ii=0,ll=allPermissions.length;ii<ll;ii++){
                    var p = allPermissions[ii];
                    if(p.checked){
                        permissionSelected =true;
                        break;
                    }
                }
            }
            if(!permissionSelected){
                alert("没有选择任何的权限！请选择至少一个权限后再试！");
                return;
            }

            permission.action = "doPermissions.jsp";
            permission.command.value = "saveRolePermission";
            permission.submit();
        }
        function selectPermission(permissionId,selected){
            var allPermissions = permission.elements["permissionId"];
            if(allPermissions!=null){
                var firstSelected = false;
                for(var i=0,l=allPermissions.length;i<l;i++){
                    var ele = allPermissions[i];
                    if(ele!=null&&ele.value == permissionId){
                        if(selected){
                            if(!firstSelected){
                                firstSelected = true;
                                ele.disabled = false;
                            }else{
                                ele.disabled = selected;
                            }
                        }else{
                            ele.disabled = false;
                        }
                        ele.checked = selected;
                    }else{
                    }
                }
            }else{
                alert("居然没有："+permissionId);
            }
        }
        function refreshSession() {
            permission.action = "permissions.jsp";
            permission.command.value = "refresh";
            permission.submit();
        }
        function isEmpty(eleName) {
            var ele = permission.elements[eleName];
            return (ele == null || ele.value == "" || ele.value == null);
        }
        function savePermission() {
            if (isEmpty("permissionName")) {
                alert("名称不能为空");
                return;
            }
            if (isEmpty("permissionTarget")) {
                alert("别名不能为空");
                return;
            }
            if (isEmpty("permissionClass")) {
                alert("类名不能为空");
                return;
            }
            if (isEmpty("permissionMethodName")) {
                alert("方法名不能为空");
                return;
            }
            permission.action = "doPermissions.jsp";
            permission.command.value = "save";
            permission.submit();
        }
        function setValues(actionIndex) {
            var actionObj = null;
            var methodNames = "";
            var targetName= "";
            for (var i = 0,l = permission.elements.length; i < l; i++) {
                var ele = permission.elements[i];
                var name = ele.name;
                if (name.indexOf("method_" + actionIndex + "_") == 0 && ele.checked) {
                    if (methodNames != "") {
                        methodNames += "|";
                    }
                    var methodName = ele.value;
                    targetName += methodName.substring(0,1).toUpperCase()+methodName.substring(1);
                    methodNames += methodName;
                } else if (name == "actionName" && ele.checked) {
                    actionObj = ele;
                }
            }

            if (actionObj) {
                var actionName = actionObj.value;
                var p = actionName.lastIndexOf(".");
                if(p>0){
                    actionName = actionName.substring(p+1);
                }
                p = actionName.indexOf("Action");
                if(p>0){
                    actionName = actionName.substring(0,p);
                }
                actionName = actionName.substring(0,1).toLowerCase()+actionName.substring(1);
                permission.permissionClass.value = actionObj.value;
                permission.permissionMethodName.value = methodNames;
                permission.permissionTarget.value =actionName+targetName;
                if(permission.permissionTarget.value==""&&targetName!=""){
                }
            }
        }
        function selectAction(actionName, actionIndex) {
            setValues(actionIndex);
        }
        function selectMethod(methodName, methodIndex, actionIndex) {
            var actionObj = document.getElementById("actionName_"+actionIndex);
            if(actionObj!=null){
                if(actionObj.checked){
                    setValues(actionIndex);
                }else{
                    alert("未选中“"+actionObj.value+"”");
                }
            }else{

            }
        }
    </script>
</head>
<body>
<form method="post" name="permission" action="doPermissions.jsp">
    <table class="mainTable">
        <tr>
            <td class="mainTableLeftPart">
                <table class="permissionTable" border="1" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="actionNameClass">类名</td>
                        <td>方法名</td>
                    </tr>
                    <%
                        int i = 0;
                        for (String key : actionMethods.keySet()) {
                            if (key.indexOf("com.fortune.rms.web") < 0) {
                                if (!displayUnknownActionPackage) {
                                    continue;
                                }
                            }
                            List<String> methods = actionMethods.get(key);
                            Permission searchBean = new Permission();
                            searchBean.setClassname(key);
                            List<Permission> permissions = permissionLogic.search(searchBean);
                            i++;
                    %>
                    <tr>
                        <td class="actionNameClass"><input type="radio" id="actionName_<%=i%>" name="actionName" value="<%=key%>"
                                                           onclick="selectAction('<%=key%>',<%=i%>)"><label for="actionName_<%=i%>"><%=key%></label> 
                        </td>
                        <td >
                            <table border="1" cellspacing="0" class="methodMainTable">
                                <%
                                    for (int m = 0; m < methods.size(); m++) {
                                        String method = methods.get(m);

                                %>
                                <tr>
                                    <td class="methodNameTableLeftPart"><input type="checkbox" name="method_<%=i%>_<%=m%>" value="<%=method%>"
                                               id="method_<%=i%>_<%=m%>"
                                               onclick="selectMethod('<%=method%>',<%=m%>,<%=i%>)"><label
                                            for="method_<%=i%>_<%=m%>"><%=method%></label>
                                    </td>
                                    <td class="methodNameTableRightPart">
                                        <%
                                            boolean permissionOutputed = false;
                                            for (Permission p : permissions) {
                                                String[] oldMethodNames = p.getMethodName().split("\\|");
                                                for (String oldMethodName : oldMethodNames) {
                                                    if (method.equals(oldMethodName)) {
                                                        permissionOutputed = true;
                                                        out.print("<input type='checkbox' name='permissionId' id='permission_" +p.getPermissionId()+
                                                                "' onClick='selectPermission(this.value,this.checked)'" +
                                                                " value='"+p.getPermissionId()+"'>" +
                                                                "<a href='../security/permissionView.jsp?keyId=" +
                                                                p.getPermissionId() + "'>" + p.getName() + "</a><br/>");
                                                    }
                                                }

                                            }
                                            if(!permissionOutputed){
                                                out.print("&nbsp;");
                                            }
                                        %>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td width="50" class="infoLabel"><label for="permissionName">名称</label></td>
                                    <td class="infoInput"><input class="textClass" type="text"
                                                                 id="permissionName" onchange="checkExists()"
                                                                 name="permissionName"></td><td><div id="permissionNameInfo"></div></td>
                                </tr>
                                <tr>
                                    <td class="infoLabel"><label for="permissionTarget">别名</label></td>
                                    <td class="infoInput"><input class="textClass" type="text" name="permissionTarget"
                                                                  onchange="checkExists()"
                                            id="permissionTarget"></td><td><div id="permissionTargetInfo"></div></td>
                                </tr>
                                <tr>
                                    <td class="infoLabel"><label for="permissionClass">类名</label></td>
                                    <td class="infoInput"><input class="textClass" type="text" name="permissionClass"
                                             id="permissionClass"></td><td><div
                                        id="permissionClassInfo"></div></td>
                                </tr>
                                <tr>
                                    <td class="infoLabel"><label for="permissionMethodName">方法</label></td>
                                    <td class="infoInput"><input class="textClass" type="text"
                                                                 id="permissionMethodName"
                                                                  onchange="checkExists()"
                                                                 name="permissionMethodName"></td><td><div
                                        id="permissionMethodNameInfo"></div></td>
                                </tr>
                                <tr>
                                    <td class="infoLabel"><label for="permissionDesc">描述</label></td>
                                    <td class="infoInput"><textarea class="textAreaClass" name="permissionDesc"
                                             id="permissionDesc"></textarea></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="checkbox" name="displayUnknownActionPackage"
                                   value="true" <%=displayUnknownActionPackage?"checked":""%>>显示非RMS包内Action<br/>
                            <input type="button" value="刷新" onclick="refreshSession()">
                            <input type="button" value="清空" onclick="clearSession()">
                            <input type="button" value="保存" onclick="savePermission()">
                        </td>
                    </tr>
                </table>
            </td>
            <td class="mainTableRightPart">
                <table class="roleTable">
                    <tr>
                        <td class="roleListTd">
                            <table border="1" cellspacing="0">
                                <tr>
                                <%
                                    int rIndex=0;
                                    for(;rIndex<roles.size();rIndex++){
                                        Role r = roles.get(rIndex);
                                        %><td width="200"><input type="radio" id="role<%=r.getRoleid()%>" name="roleId"
                      value="<%=r.getRoleid()%>"><label for="role<%=r.getRoleid()%>"><a
                                        href="../security/roleView.jsp?keyId=<%=r.getRoleid()%>"><%=r.getName()%></a></label></td><%
                                        if(rIndex%3==2){
                                            out.println("</tr><tr>");
                                        }
                                    }
                                    while(rIndex%3!=0){
                                        rIndex++;
                                        out.print("<td>&nbsp;</td>");
                                    }
                                %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="button" value="保存角色的权限绑定" onclick="saveRolePermission()">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <input type="hidden" name="command">
</form>

</body>
</html>