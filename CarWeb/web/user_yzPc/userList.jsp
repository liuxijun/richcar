<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","deviceManage");
        needPermissions(actionHeader,"view","deviceManage,deviceView");
        needPermissions(actionHeader,"list","deviceManage,deviceList");
        needPermissions(actionHeader,"save","deviceManage,deviceSave");
        needPermissions(actionHeader,"add","deviceManage");
        needPermissions(actionHeader,"deleteSelected","deviceManage,deviceDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="usruser.usr"/>用户管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
        <script type="text/javascript">
        nextUrl = "userList.jsp";
        actionHeader = "user";
        checkAllFunctions();  // 检查用户权限
        defaultPageSize=17;
        function initDisplay(){
            storeConfig.fields = ['id','login','birthday','gernate','passWord','passWordHistory','userName','email','addr','tel','status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 20, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="usruser.login"/>",hidden:false, align:'center', width: 55, sortable: true,dataIndex: 'login'}                ,
                {header: "<fts:text name="usruser.status"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'status',
                        renderer:
                        function (val,p,row){
                         return getDictStoreText("userStatus",val);

                        }
                }  ,
                {header: "<fts:text name="usruser.birthday"/>",hidden:false, align:'center', width: 90, sortable: true,dataIndex: 'birthday'}                ,
                {header: "<fts:text name="usruser.gernate"/>",hidden:false, align:'center', width: 45, sortable: true,dataIndex: 'gernate'}                ,

                {header: "<fts:text name="usruser.username"/>",hidden:false, align:'center', width: 110, sortable: true,dataIndex: 'userName'}                ,
                {header: "<fts:text name="usruser.email"/>",hidden:true, align:'center', width: 100, sortable: true,dataIndex: 'email'}                ,
                {header: "<fts:text name="usruser.addr"/>",hidden:false, align:'center', width: 160, sortable: true,dataIndex: 'addr'},
                {header: "<fts:text name="usruser.tel"/>",hidden:false, align:'center', width: 160, sortable: true,dataIndex: 'tel'},
                {header: "管理", align:'center', width: 90, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var deviceGrid = new FortuneSearchListGrid({
                title:'<fts:text name="usruser.usr"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:870,
                height:505,

                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.userName',
                            width:360
                        })
                    ]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据",
                        items:defaultBottomButtons
                })
            });


            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            var fp = new Ext.FormPanel({
                id: 'status-form',
                renderTo: Ext.getBody(),
                labelWidth: 75,
                width: 350,
                buttonAlign: 'right',
                border: false,
                bodyStyle: 'padding:10px 10px 0;',
                defaults: {
                    anchor: '95%',
                    allowBlank: false,
                    selectOnFocus: true,
                    msgTarget: 'side'
                },
                items:[{
                    xtype: 'textfield',
                    fieldLabel: 'Name',
                    blankText: 'Name is required'
                },{
                    xtype: 'datefield',
                    fieldLabel: 'Birthdate',
                    blankText: 'Birthdate is required'
                }],
                buttons: [{
                    text: 'Save',
                    handler: function(){
                        if(fp.getForm().isValid()){
                            var sb = Ext.getCmp('form-statusbar');
                            sb.showBusy('Saving form...');
                            fp.getEl().mask('dasdasdasdsa');
                            fp.getForm().submit({
                                url: 'fake.php',
                                success: function(){
                                    sb.setStatus({
                                        text:'Form saved!',
                                        iconCls:'',
                                        clear: true
                                    });
                                    fp.getEl().unmask();
                                }
                            });
                        }
                    }
                }]
            });

            new Ext.Panel({
                title: 'StatusBar with Integrated Form Validation',
                renderTo: Ext.getBody(),
                width: 350,
                autoHeight: true,
                layout: 'fit',
                items: fp,
                bbar: new Ext.ux.StatusBar({
                    id: 'form-statusbar',
                    defaultText: 'Ready',
                    plugins: new Ext.ux.ValidationStatus({form:'status-form'})
                })
            });

        });

    </script>
</head>
<body>
<table align="center" width="800">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>