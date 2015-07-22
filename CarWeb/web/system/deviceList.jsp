<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "device";
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
    <title><fts:text name="device"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
        <script type="text/javascript">
        nextUrl = "deviceList.jsp";
        actionHeader = "device";
        checkAllFunctions();  // 检查用户权限
        defaultPageSize=17;
        function doSearch(){
            var baseParams = {
                "obj.name":getCmpValue("obj.name"),
                "obj.type":getCmpValue("obj.type"),
                "obj.ip":getCmpValue("obj.ip"),
                "obj.status":getCmpValue("obj.status"),
                limit:defaultPageSize
            };
            searchStore.baseParams = baseParams;
            searchStore.load();
        }
        function initDisplay(){
            storeConfig.fields = ['id','name','type','ip','url','status','ftpPort','ftpUser','ftpPwd','ftpPath'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="device.name"/>",hidden:false, align:'center', width: 135, sortable: true,dataIndex: 'name'}                ,
                {header: "<fts:text name="device.type"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'type',
                        renderer:
                        function (val,p,row){
                            return getDictStoreText('serverType',val);
                            /*switch(val){
                                case 1:return 'MMS';
                                case 2:return 'FLV';
                                case 3:return 'WEB'
                            }*/
                        }            
                }                ,
                {header: "<fts:text name="device.ip"/>",hidden:false, align:'center', width: 90, sortable: true,dataIndex: 'ip'}                ,
                {header: "<fts:text name="device.url"/>",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'url'}                ,
                {header: "<fts:text name="device.status"/>",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'status'
                    , renderer:
                        function (val,p,row){
                           return getDictStoreText('serverStatus',val);
                        }
                }                ,
                {header: "<fts:text name="device.ftpPort"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'ftpPort'}                ,
                {header: "<fts:text name="device.ftpUser"/>",hidden:false, align:'center', width: 110, sortable: true,dataIndex: 'ftpUser'}                ,
                {header: "<fts:text name="device.ftpPwd"/>",hidden:true, align:'center', width: 100, sortable: true,dataIndex: 'ftpPwd'}                ,
                {header: "<fts:text name="device.ftpPath"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'ftpPath'},
                {header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var deviceGrid = new FortuneSearchListGrid({
                title:'<fts:text name="device"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:870,
                height:505,
                tbar:new Ext.Toolbar({items:[
                    {xtype:'label',text:'服务器名：'},{xtype:'textfield',id:'obj.name',width:120}
                    ,{xtype:'label',text:'，IP：'},{xtype:'textfield',id:'obj.ip',width:120}
                    ,{xtype:'label',text:'，类型：'}
                    ,new Ext.form.ComboBox({
                        hiddenName:'obj.type',
                        mode:'local',
                        value:'',
                        width:100,
                        forceSelection:true,
                        triggerAction:"all",
                        editable:false,
                        store:getDictStore('serverType',true),
                        valueField:'value',displayField:'name',
                        emptyText:'请选择...'
                    })
                    ,{xtype:'label',text:'，状态：'},new Ext.form.ComboBox({
                        hiddenName:'obj.status',
                        mode:'local',
                        width:60,
                        value:'',
                        forceSelection:true,
                        triggerAction:"all",
                        editable:false,
                        store:getDictStore('serverStatus',true),
                        valueField:'value',displayField:'name',
                        emptyText:'请选择...'
                    })
                    ,{text:'搜索',handler:doSearch}
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
            deviceGrid.render(displayDiv);
            defaultGrid = deviceGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
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
<table align="center" width="660">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>