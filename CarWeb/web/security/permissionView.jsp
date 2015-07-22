<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "view", "permissionManage,permissionView");
//        needPermissions(actionHeader, "list", "permissionManage,permissionList");
        needPermissions(actionHeader, "save", "permissionManage,permissionSave");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="permission"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "permission";
        nextUrl = "permissionList.jsp";

        function loadFormFromServerData() {
            Ext.Ajax.request({
                url:getActionUrl('view'),
                callback:function (opt, success, response) {
                    if (success) {
                        var serverData = Ext.util.JSON.decode(response.responseText);
                        if (serverData.success) {
                            initForm(serverData);
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                        }
                    }
                }
            });
        }
        function initForm(serverData) {
            var roles = serverData.data['obj.roles'];
            if (roles != null) {
                roleItems = [];
                for (var i = 0; i < roles.length; i++) {
                    var role = roles[i];
                    var item = {boxLabel:role.name, inputValue:role.roleid,
                        type:'checkbox',
                        name:'roleIds',
                        checked:role.selected};
                    roleItems.push(item);
                }
            }

            var permissionViewForm = new FortuneViewForm({
                title:'<fts:text name="permission"/>基本信息',
                url:'permission!save.action',
                saveUrl:'permission!save.action',
                viewUrl:'permission!view.action',
                width:380,
                layout:'fit',
                bodyStyle:'padding:10px 10px 0',
                defaults:{width:280},
                items:[
                    {
                        xtype:'fieldset',
                        collapsible:true,
                        title:'权限基本信息',
                        autoHeight:true,
                        defaults:{width:250},
                        defaultType:'textfield',
                        items:[
                            //{name:'keyId',value:keyId,inputType:'hidden'},
                            {
                                name:'obj.permissionId',
                                inputType:'hidden',
                                value:serverData.data['obj.permissionId']
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="permission.name"/>',
                                name:'obj.name',
                                value:serverData.data['obj.name'],
                                allowBlank:false,
                                readOnly:viewReadOnly
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="permission.target"/>',
                                name:'obj.target',
                                value:serverData.data['obj.target'],
                                allowBlank:false,
                                readOnly:viewReadOnly
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="permission.classname"/>',
                                name:'obj.classname',
                                value:serverData.data['obj.classname'],
                                allowBlank:false,
                                readOnly:viewReadOnly
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="permission.methodname"/>',
                                name:'obj.methodName',
                                value:serverData.data['obj.methodName'],
                                allowBlank:false,
                                readOnly:viewReadOnly
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="permission.permissiondesc"/>',
                                name:'obj.permissionDesc',
                                value:serverData.data['obj.permissionDesc'],
                                xtype:'textarea',
                                readOnly:viewReadOnly
                            }
                        ]
                    },
                    {
                        xtype:'fieldset',
                        collapsible:true,
                        title:'绑定到角色',
                        autoHeight:true,
                        width:360,
                        id:'operatorRoles',
                        name:'operatorRoles',
                        defaults:{width:190},
                        defaultType:'checkbox',
                        columns:2,
                        items:roleItems
                    }
                ]
            });
            displayDiv.innerHTML = '';
            permissionViewForm.render(displayDiv);
        }

        Ext.onReady(function () {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            loadFormAjax();
        });

    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>