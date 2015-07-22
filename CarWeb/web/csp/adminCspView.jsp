<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%
    //初始化本页权限需求
    String actionHeader = "adminCsp";
    {
        session.setAttribute("actionHeader", actionHeader);
    }
    request.setAttribute("adminId", request.getParameter("obj.adminId"));

%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title><fts:text name="adminCsp"/>管理</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript">
actionHeader = "adminCsp";
nextUrl = "adminCspList.jsp?obj.adminId=${adminId}";

var cspLoaded = false;
var adminCspViewForm = null;
var adminId=keyId;
function cspChanged(cspId) {
    if (cspId > 0) {

    }

    // loadFormFromServerData();
}

function loadFormFromServerData() {
    Ext.Ajax.request({
        url:getActionUrl('view', "keyId=" + keyId),
        callback:function (opt, success, response) {
            if (success) {
                var serverData = Ext.util.JSON.decode(response.responseText);
                if (serverData.success) {
                    initRoles(serverData.data);
                    adminCspViewForm.getForm().setValues(serverData.data);
                    nextUrl = "adminCspList.jsp?obj.adminId=" + serverData.data["obj.adminId"];
                    var realname = serverData.data['obj.admin'].realname;
                    if (realname == null || realname == "") {
                        realname = serverData.data['obj.admin'].login;
                    }
                    setLabelText('obj.admin.realname', realname);
                    if (serverData.data['obj.cspId'] == 0) {
                        //alert(0);
                        //setCmpValue('obj.cspId','请选择....');
                        setDefaultSelectCmp('cspId', true);
                    }
                    if (serverData.data['obj.isDefaultCsp'] == 0) {
                        setDefaultSelectCmp('isDefaultCsp', true);
                    }
                    adminId = serverData.data["obj.adminId"];
                    adminCspStore.load(
                            {params:{"obj.adminId":adminId}}
                    );
                } else {
                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                }
            }
        }
    });
}
var adminCspStore = new Ext.data.JsonStore({
    method:'POST',
    autoLoad:false,
    url:"/csp/adminCsp!list.action",
    root:'objs',
    totalProperty:'totalCount',
    fields:['id', 'cspId']
});
adminCspStore.load({params:{"obj.adminId":adminId}});

var roleItems = [];
function initRoles(serverData) {
    var adminRolesCmp = Ext.getCmp('adminRoles');
    var items = adminRolesCmp.items;
    var k;
    for (k = 0; k < items.length; k++) {
        adminRolesCmp.remove(items[k]);
    }
    adminRolesCmp.removeAll();
    adminRolesCmp.doLayout();
    var roles = serverData['obj.admin'].roles;
    if (roles != null) {
        roleItems = [];
        for (var i = 0; i < roles.length; i++) {
            var role = roles[i];
            var item = {boxLabel:role.name, inputValue:role.roleid,
                type:'checkbox',
                name:'roleIds',
                checked:role.selected};
            roleItems.push(item);
            adminRolesCmp.add(item);
        }
    }
    adminRolesCmp.doLayout();
    /*
     var rolePanel = Ext.getCmp('adminRoles');

     if(rolePanel!=null){
     rolePanel.setItems(roleItems);
     }
     */
}
function checkLoaded() {
    if (cspLoaded) {
        loadFormAjax();
    }
}

function initDisplay() {
    //除了基本操作（删除，查看），还要添加的链接
    //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
//           checkAllFunctions(); //检查权限
    adminCspViewForm = new FortuneViewForm({
        title:'绑定信息',
        layout:'fit',
        bodyStyle:'padding:5px 5px 0',
        url:'adminCsp!save.action',
        saveUrl:'adminCsp!save.action',
        viewUrl:'adminCsp!view.action',
        items:[
            //{name:'keyId',value:keyId,inputType:'hidden'},
            {
                title:'基本信息',
                collapsible:true,
                layout:'form',
                xtype:'fieldset',
                defaults:{width:185},
                autoHeight:true,
                defaultType:'textfield',
                items:[
                    {name:'obj.id', inputType:'hidden', value:'-1'},
                    {inputType:'hidden', name:'obj.adminId', readOnly:viewReadOnly},
                    {fieldLabel:'管理员', name:'obj.admin.realname',
                        id:'obj.admin.realname',
                        xtype:'label'},
                    new FortuneCombo({
                        fieldLabel:'CSP',
                        valueField:'id',
                        hiddenName:'obj.cspId',
                        readOnly:viewReadOnly,
                        hiddenId:'obj.cspId',
                        allowBlank:false,
                        id:'cspId',
                        listeners:{
                            select:function () {
                                cspChanged(this.value);
                                var id = Ext.getCmp('cspId').getValue();
                                for (var i = 0; i < adminCspStore.getCount(); i++) {
                                    if (id == adminCspStore.getAt(i).data.cspId) {
                                        Ext.MessageBox.alert("提示", " 该CSP已绑定，请重新选择 ");
                                        loadFormAjax();
                                        break;
                                    }
                                }
                            }
                        },
                        store:new Ext.data.JsonStore({
                            storeId:'cspStore',
                            // reader configs
                            root:'objs',
                            idProperty:'id',
                            autoLoad:true,
                            fields:['id', 'name'],
                            listeners:{
                                load:function () {
                                    cspLoaded = true;
                                    checkLoaded();
                                }
                            },
                            proxy:new Ext.data.HttpProxy({method:'GET',
                                url:'../csp/csp!list.action?limit=100000'
                            })
                        })}),
                    new FortuneCombo({fieldLabel:'默认CSP', hiddenName:'obj.isDefaultCsp',
                        hiddenId:'obj.isDefaultCsp',
                        id:'isDefaultCsp',
                        readOnly:viewReadOnly,
                        store:getDictStore('yesOrNo')})
        ]
            },
            {
                xtype:'fieldset',
                collapsible:true,
                title:'角色分配',
                autoHeight:true,
                width:330,
                id:'adminRoles',
                name:'adminRoles',
                defaults:{width:190},
                defaultType:'checkbox',
                columns:2,
                items:roleItems
            }

        ]
    });
    //loadFormAjax();
    adminCspViewForm.render(displayDiv);
}
Ext.onReady(function () {
    Ext.QuickTips.init();
    //除了基本操作（删除，查看），还要添加的链接
    //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
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
            initDisplay);
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