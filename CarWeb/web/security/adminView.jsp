<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2009-4-5
  Time: 22:28:04
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"view","adminManage");
        needPermissions(actionHeader,"list","adminManage");
        needPermissions(actionHeader,"save","adminManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title>管理员管理</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript" src="../js/md5.js"></script>
<script type="text/javascript">
    nextUrl = "adminList.jsp";
    actionHeader = "admin";
    var roleItems = [

    ];//[{boxLabel:'超级管理员',name:'roleIds',inputValue:1,checked:true}];

    formOptions.beforePost = function() {
        var newPasswordObj = Ext.getCmp("newPassword");
        var passwordObj = Ext.getCmp("obj.password");
        if (passwordObj != null && newPasswordObj != null && newPasswordObj.getValue() != '') {
            var newPwd = hex_md5(newPasswordObj.getValue());
            //alert(newPwd);
            passwordObj.setValue(newPwd);
            //alert(passwordObj.getValue());
            newPasswordObj.setValue("");
            var confirmPasswordObj = Ext.getCmp("confirmPassword");
            if (confirmPasswordObj != null) {
                confirmPasswordObj.setValue("");
            }
        } else {
            //alert("口令组件错误！");
            //return false;
        }
        return true;
    };
    function loadFormFromServerData() {
        Ext.Ajax.request({
            url:getActionUrl('view', "keyId=" + keyId),
            callback : function(opt, success, response) {
                if (success) {
                    var serverData = Ext.util.JSON.decode(response.responseText);
                    if (serverData.success) {
                        initRoles(serverData);

                    } else {
                        Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                    }
                }
            }
        });
    }
    function isSystemChanged(val){
        removeAllRoles();
        if(val==1){
            addAllRoles();
        }else{
        }
    }
    function removeAllRoles(){
        var adminRolesCmp = Ext.getCmp('adminRoles');
        var items=adminRolesCmp.items;
        var k;
        for(k=0;k<items.length;k++){
          adminRolesCmp.remove(items[k]);
        }
        adminRolesCmp.removeAll();
        adminRolesCmp.doLayout();
    }
    function addAllRoles(){
        var adminRolesCmp = Ext.getCmp('adminRoles');
        var k;
        for(k=0;k<roleItems.length;k++){
            adminRolesCmp.add(roleItems[k]);
        }
        adminRolesCmp.doLayout();
    }

    function initRoles(serverData) {
        var roles = serverData.data['obj.roles'];
        if (roles != null) {
            roleItems = [];
            for (var i = 0; i < roles.length; i++) {
                var role = roles[i];
                var item = {boxLabel:role.name,inputValue:role.roleid,
                    type:'checkbox',
                    name:'roleIds',
                    checked:role.selected};
                roleItems.push(item);
            }
        }
        var adminViewForm = new FortuneViewForm({
            title:'管理员基本信息',
            url:'adminAction!save.action',
            saveUrl:'admin!save.action',
            viewUrl:'admin!view.action',
            layout:'fit',
            bodyStyle:'padding:5px 5px 0',                   
            items:[
                {
                 
                    xtype:'fieldset',
                    collapsible: true,
                    title: '基本信息',
                   // width:250,
                    autoHeight:true,
                    defaults: {width: 190},
                    defaultType: 'textfield',
                    items:[

                        //{name:'keyId',value:keyId,inputType:'hidden'},
                        {
                            name:'obj.id',
                            inputType:'hidden',
                            value:-1
                        },
                        {
                            name:'obj.modifydate',
                            inputType:'hidden'
                        },
                        {
                            name:'obj.lastlogintime',
                            inputType:'hidden'
                        },
                        {
                            name:'obj.password',
                            id:'obj.password',
                            inputType:'hidden'
                        },
                        {
                            name:'obj.nodeId',
                            inputType:'hidden'
                        },
                        {
                            name:'obj.oldpasswordlog',
                            inputType:'hidden'
                        },
                        {

                            fieldLabel:'登录',
                            name:'obj.login',
                            readOnly:modifyOldData,
                            allowBlank:false,
                            blankText:'登录名不能为空',
                            id:'obj.login',
                            vtype:modifyOldData ? '' : 'login',
                            invalidText: '已经存在的登录名'
                        },
                        {
                            fieldLabel:'口令',
                            name:'newPassword',
                            inputType:'password',
                            id:'newPassword',
                            allowBlank:modifyOldData,
                            blankMessage:'口令不能为空'
                        },
                        {
                            fieldLabel:'确认',
                            name:'confirmPassword',
                            id:'confirmPassword',
                            inputType:'password',
                            allowBlank:modifyOldData,
                            blankMessage:'口令不能为空',
                            vtype:'confirmPassword',
                            confirmTo:'newPassword',
                            vtypeText:"两次密码不一致！"
                        },
                        {
                            fieldLabel:'姓名',
                            id:'objName',
                            name:'obj.realname'
                        },
                        {
                            fieldLabel:'电话',
                            name:'obj.telephone'
                        },
                        {
                            name:'obj.areaId',
                            id:'obj.areaId',
                            inputType:'hidden'
                        },
/*
                        new Ext.form.SelectField({
                            fieldLabel:'区域',
                            name:'obj.areaName',
                            id:'areaName',
                            listeners:{
                                select:selectArea
                            },
                            readOnly:true
                        }),
*/
                        new FortuneCombo({fieldLabel:'超级管理',
                            hiddenName:'obj.isRoot',
                            store: getDictStore('yesOrNo')}),
                        new FortuneCombo({fieldLabel:'维护级管理',
                            hiddenName:'obj.isSystem',
                            hiddenId:'obj.isSystem',
                            listeners:{
                                select:function() {
                                    isSystemChanged(this.value);
                                }
                            },
                            store: getDictStore('yesOrNo')}),
                        new FortuneCombo({fieldLabel:'状态',
                            hiddenName:'obj.status',
                            store: getDictStore('adminStatus')})
                    ]
                },
                {
                    xtype:'fieldset',
                    collapsible: true,
                    title: '绑定系统权限',
                    autoHeight:true,
                    id:'adminRoles',
                    name:'adminRoles',
                    width:338,
                    defaults: {width: 190},
                    defaultType: 'checkbox',
                    columns: 2,
                    items:roleItems
                }
            ]
        });
        displayDiv.innerHTML = '';
        adminViewForm.render(displayDiv);
        adminViewForm.getForm().setValues(serverData.data);
    }

    Ext.onReady(function() {
        Ext.QuickTips.init();
        if (keyId == "1") {//如果当前要编辑用户是超级用户，则禁止删除等操作
            defaultViewFormButtons = removeFunction(defaultViewFormButtons, 'delete');
            defaultViewFormButtons = removeFunction(defaultViewFormButtons, 'lock');
        }
        checkAllFunctions();
        queueFunctions([
            {
                func:initDictStores,
                done:false,
                flag:'initDictStores'
            }/*,{
             func:initAreaStore,
             done:false,
             flag:'initAreaStore'
             }*/
        ],
                loadFormFromServerData);
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
