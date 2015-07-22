<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"view","roleManage,roleView");
        needPermissions(actionHeader,"list","roleManage,roleList");
        needPermissions(actionHeader,"save","roleManage,roleSave");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="role"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "role";
        nextUrl = "roleList.jsp";


        function loadFormFromServerData() {
            Ext.Ajax.request({
                url:getActionUrl('view', "keyId=" + keyId),
                callback : function(opt, success, response) {
                    if (success) {
                        var serverData = Ext.util.JSON.decode(response.responseText);
                        if (serverData.success) {
                            initData(serverData);
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                        }
                    }
                }
            });
        }


        var permissionStoreData = [];
        var permissionStore;
        function doSearchPermissionName() {
            var searchPermissionName = Ext.getCmp("permissionName").getValue();
            var storeData = [];
            for(var i=0;i<permissionStoreData.length;i++){
                var permissionName = permissionStoreData[i][1];
                if(permissionName.indexOf(searchPermissionName)>=0){
                    storeData.push(permissionStoreData[i]);
                }
            }
         //   permissionStore.data = storeData;
            permissionStore.loadData(storeData);
        //    permissionStore.reload();
        }

        function initData(serverData) {
            var selectedStoreData = [];
            var permissions = serverData.data['obj.permissions'];
            if (permissions != null) {
                for (var i = 0; i < permissions.length; i++) {
                    var permission = permissions[i];
                    var item = [permission.permissionId,permission.name];
                    if (permission.selected) {
                        selectedStoreData.push(item);
                    } else {
                        permissionStoreData.push(item);
                    }
                }
            }

            permissionStore = new Ext.data.ArrayStore({
                data:permissionStoreData,
                fields: ['value','text'],
                sortInfo: {
                    field: 'text',
                    direction: 'ASC'
                }
            });

            var selectedStore = new Ext.data.ArrayStore({
                data:selectedStoreData,
                fields: ['value','text'],
                sortInfo: {
                    field: 'text',
                    direction: 'ASC'
                }
            });
            var roleViewForm = new FortuneViewForm({
                title:'<fts:text name="role"/>基本信息',
                url:'role!save.action',
                saveUrl:'role!save.action',  //'../ext-test/saveData.jsp',//
                viewUrl:'role!view.action',
                bodyStyle:'padding:10px 10px 0',
                align:'center',
                width:390,
                height:475,
                layout:'fit',
                items:[
                    {
                        xtype:'fieldset',
                        collapsible: true,
                        title: '基本信息',
                        autoHeight:true,
                        labelWidth:120,
                        defaults: {width: 150},
                        defaultType: 'textfield',
                        items:[
                            {
                                name:'obj.roleid',
                                inputType:'hidden',
                                value:serverData.data["obj.roleid"]
                            }                     ,
                            {
                                fieldLabel:'角色名称',
                                name:'obj.name',
                                allowBlank:false,
                                value:serverData.data["obj.name"],
                                readOnly:viewReadOnly
                            }                     ,
                            new FortuneCombo({
                                fieldLabel:'角色类型',
                                hiddenName:'obj.type',
                                allowBlank:false,
                                value:serverData.data["obj.type"],
                                store:getDictStore('roleType'),
                                readOnly:viewReadOnly
                            }),
                            {
                                fieldLabel:'角色描述',
                                name:'obj.memo',
                                //   allowBlank:true,
                                value:serverData.data["obj.memo"],
                                xtype:'textarea',
                                readOnly:viewReadOnly
                            }
                        ]
                    },
                    {
                        collapsible: true,
                        title: '基本信息',
                        xtype: 'itemselector',
                        name: 'permissionIdsString',
                        fieldLabel: '权限设置',
                        imagePath: '../ext/ux/images/',
                        width:610,
                        emptyText: '请选择...',
                        autoHeight:true,
                        multiselects: [
                            {
                                legend:'可选权限',
                                width: 170,
                                height: 215,
                                store: permissionStore,
                                displayField: 'text',
                                valueField: 'value',
                                tbar: [
                                    {
                                        xtype:'textfield',
                                        name:'permissionName',
                                        id:'permissionName',
                                        width:123
                                    } ,
                                    {
                                        text:'查询',
                                        handler:doSearchPermissionName
                                    }
                                ]
                            },
                            {
                                legend:'已选权限',
                                width: 170,
                                height: 215,
                                displayField: 'text',
                                valueField: 'value',
                                store: selectedStore,
                                name:'permissionIds',
                                tbar:[
                                    {
                                        text: '清空',
                                        handler:function() {
                                            roleViewForm.getForm().findField('permissionIdsString').reset();
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                ]
            });
            displayDiv.innerHTML = '';
            roleViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
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
                    loadFormFromServerData());
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