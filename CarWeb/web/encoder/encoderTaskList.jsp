<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "encoderTask";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","encoderTaskManage");
        needPermissions(actionHeader,"view","encoderTaskManage,encoderTaskView");
        needPermissions(actionHeader,"list","encoderTaskManage,encoderTaskList");
        needPermissions(actionHeader,"save","encoderTaskManage,encoderTaskSave");
        needPermissions(actionHeader,"add","encoderTaskManage");
        needPermissions(actionHeader,"deleteSelected","encoderTaskManage,encoderTaskDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>编码队列管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
        <script type="text/javascript">
        nextUrl = "encoderTaskList.jsp";
        actionHeader = "encoderTask";
        checkAllFunctions();  // 检查用户权限
        defaultPageSize=17;
        function initDisplay(){
            storeConfig.fields = ['id','clipId','encoderId','process','sourceFileName',
                'startTime','stopTime','status','templateId','desertFileName','deviceName',
                'contentName','templateName'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('searchTask')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            searchStore.setDefaultSort("o1.createTime","desc");
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "任务名",hidden:false, align:'center', width: 135, sortable: true,dataIndex: 'contentName',renderer:function (val,p,row){
                    return val+"-"+row.get("templateName");
                }},
                {header: "转码服务器",hidden:false, align:'center', width: 90, sortable: true,dataIndex: 'deviceName'} ,
                {header: "启动时间",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'startTime'}                ,
                {header: "结束时间",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'stopTime'}                ,
                {header: "源文件",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'sourceFileName'}                ,
                {header: "目标源文件",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'desertFileName'}                ,
                {header: "状态",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'status'
                    , renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '运行中';
                                case 2:return '等待中';
                                case 3:return '已完成';
                                case 4:return '错误';
                                case 0:return '失效';
                            }
                        }
                }                ,
                {header: "进度",hidden:false, align:'center', width: 101, sortable: true,dataIndex: 'process',renderer:function(val,p,row){
                    return "<div style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:"+val+"px'>"+ val+"%</div></div>";
                }}
                //,{header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var encoderTaskGrid = new FortuneSearchListGrid({
                title:'转码任务列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:870,
                height:505,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'源文件名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'et.sourceFileName',
                            width:360
                        })
                    ]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据"
                    //,items:defaultBottomButtons
                })
            });
            encoderTaskGrid.render("displayDiv");
            defaultGrid = encoderTaskGrid;
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