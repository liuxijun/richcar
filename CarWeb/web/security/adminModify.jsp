<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-4-10
  Time: 20:00
  管理员修改自身信息
--%><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib 
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"save","operatorSelfManage");
        needPermissions(actionHeader,"view","operatorSelfManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
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
            url:getActionUrl('viewSelf', "keyId=" + keyId),
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
    function initRoles(serverData) {
        var operatorViewForm = new FortuneViewForm({
            title:'管理员基本信息',
            url:'operatorAction!modifySelf.action',
            saveUrl:'operator!modifySelf.action',
            viewUrl:'operator!viewSelf.action',
            layout:'fit',
            bodyStyle:'padding:5px 5px 0',
            items:[
                {
                    xtype:'fieldset',
                    collapsible: true,
                    title: '基本信息',
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
                            readOnly:true,
                            allowBlank:false,
                            blankText:'登录名不能为空',
                            id:'obj.login',
                            invalidText: '已经存在的登录名'
                        }/*,
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
                        }*/,
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
                        {
                            fieldLabel:'区域',
                            name:'obj.areaName',
                            id:'areaName',
                            readOnly:true
                        },
                        {fieldLabel:'状态',
                            name:'obj.status',
                            inputType:'hidden'}
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
            }
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