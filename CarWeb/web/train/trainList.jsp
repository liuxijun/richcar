<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "train";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader, "view", "trainManage");
        needPermissions(actionHeader,"delete","trainManage");
        needPermissions(actionHeader,"deleteSelected","trainManage");
        needPermissions(actionHeader,"add","trainManage");
        needPermissions(actionHeader,"save","trainManage");
        needPermissions(actionHeader,"list","trainManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="train"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "adList.jsp";
        actionHeader = "train";
        checkAllFunctions();  // 检查用户权限
        var trainLineStore = new Ext.data.JsonStore({
            method: 'POST',
            url: "/train/trainLine!list.action",
            root: 'objs',
            fields: [
                {name: 'id'},
                {name: 'name'}
            ]
        });
        function initTrainLineStore() {
            trainLineStore.load({callback: function (records, options, success) {
                if (success) {
                }
                functionDone('initTrainLineStore');
            }});
        }

        function initDisplay(){
            storeConfig.fields = ['id','name','description','sn','trainCode','trainLineId','type','status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'},
                {header: "<fts:text name="train.name"/>",hidden:false, align:'center', width: 120, sortable: true,dataIndex: 'name'},
                {header: "<fts:text name="train.description"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'description'},
                {header: "<fts:text name="train.sn"/>",hidden:false, align:'center', width: 120, sortable: true,dataIndex: 'sn'},
                {header: "<fts:text name="train.trainCode"/>",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'trainCode'},
                {header: "<fts:text name="train.status"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'status',renderer:function(val){
                    return getDictStoreText('status',val);
                }},
                {header: "<fts:text name="train.type"/>",hidden:false, align:'center', width:50, sortable: true,dataIndex: 'type',renderer:function(val){
                    return getDictStoreText('trainType',val);
                }},
                {header: "<fts:text name="train.trainLineId"/>",hidden:false, align:'center', width:100, sortable: true,dataIndex: 'trainLineId',
                renderer:function(val){
                   return getStoreText(trainLineStore,val,'id','name');
                }},
                {header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var trainGrid = new FortuneSearchListGrid({
                title:'<fts:text name="train"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.name',
                            width:320
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
            trainGrid.render('displayDiv');
            defaultGrid = trainGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                },{
                    func:initTrainLineStore,
                    done:false,
                    flag:'initTrainLineStore'
                }
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