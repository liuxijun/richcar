<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-4-10
  Time: 22:28:04
  管理员修改自己的口令
--%><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"save","operatorSelfManage");
    }
%>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title>管理员管理</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript" src="../js/md5.js"></script>
<script type="text/javascript">
    nextUrl = "operatorList.jsp";
    actionHeader = "operator";
    var roleItems = [

    ];//[{boxLabel:'超级管理员',name:'roleIds',inputValue:1,checked:true}];

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
    formOptions.beforePost = function() {
        var newPasswordObj = Ext.getCmp("newPassword");
        var newPasswordMD5 = Ext.getCmp("newPasswordMD5");
        var oldPasswordObj = Ext.getCmp("oldPassword");
        var passwordObj = Ext.getCmp("obj.password");
        if (passwordObj != null && newPasswordObj != null && newPasswordObj.getValue() != '') {
            var newPwd = hex_md5(newPasswordObj.getValue());
            passwordObj.setValue(hex_md5(oldPasswordObj.getValue()));
            newPasswordMD5.setValue(newPwd);
        } else {
            //alert("passwordObj is null or newPasswordObj is null:"+passwordObj+","+newPasswordObj);
        }
    };
    function initRoles(serverData) {
        var operatorViewForm = new FortuneViewForm({
            title:'我的口令',
            url:'operatorAction!savePassword.action',
            saveUrl:'operator!savePassword.action',
            viewUrl:'operator!view.action',
            layout:'fit',
            bodyStyle:'padding:5px 5px 0',
            buttons:[{text:'保存数据',handler:saveFormAjax,action:'save'}],
            items:[
                {
                    xtype:'fieldset',
                    collapsible: true,
                    title: '口令信息',
                    autoHeight:true,
                    defaults: {width: 190},
                    defaultType: 'textfield',
                    items:[
                        //{name:'keyId',value:keyId,inputType:'hidden'},
                        {
                            name:'obj.operatorid',
                            inputType:'hidden',
                            value:-1
                        },
                        {
                            name:'obj.password',
                            id:'obj.password',
                            inputType:'hidden'
                        },
                        {
                            id:'newPasswordMD5',
                            name:'newPasswordMD5',
                            inputType:'hidden'
                        },
/*                        {
                            fieldLabel:'登录',
                            name:'obj.login',
                            readOnly:modifyOldData,
                            allowBlank:false,
                            blankText:'登录名不能为空',
                            id:'obj.login',
                            vtype:modifyOldData ? '' : 'login',
                            invalidText: '已经存在的登录名'
                        },
                        */
                        {
                            fieldLabel:'原始口令',
                            name:'oldPassword',
                            id:'oldPassword',
                            inputType:'password',
                            allowBlank:false,
                            blankMessage:'原始口令不能为空'
                        },
                        {
                            fieldLabel:'新的口令',
                            name:'newPassword',
                            inputType:'password',
                            id:'newPassword',
                            allowBlank:false,
                            blankMessage:'口令不能为空'
                        },
                        {
                            fieldLabel:'确认口令',
                            name:'confirmPassword',
                            id:'confirmPassword',
                            inputType:'password',
                            allowBlank:false,
                            blankMessage:'口令不能为空',
                            vtype:'confirmPassword',
                            confirmTo:'newPassword',
                            vtypeText:"两次密码不一致！"
                        }
/*,                        {
                            fieldLabel:'姓名',
                            id:'objName',
                            name:'obj.realname',
                            readonly:true
                        },
                        {
                            fieldLabel:'电话',
                            name:'obj.telephone',
                            readonly:true
                        },
                        {
                            fieldLabel:'区域',
                            name:'obj.areaName',
                            id:'areaName',
                            readOnly:true
                        }
                        */
                    ]
                }
            ]
        });
        displayDiv.innerHTML = '';
        operatorViewForm.render(displayDiv);
        operatorViewForm.getForm().setValues(serverData.data);
    }

    Ext.onReady(function() {
        Ext.QuickTips.init();
        defaultViewFormButtons = [{text:'保存数据',handler:saveFormAjax,action:'save'}];
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