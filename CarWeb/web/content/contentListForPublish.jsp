<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "content";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","courseManage");
        needPermissions(actionHeader,"viewCourse","courseManage,courseView");
        needPermissions(actionHeader,"list","courseManage,courseList");
        needPermissions(actionHeader,"publishCourse","courseManage,coursePublish");
        needPermissions(actionHeader,"unpublishCourse","courseManage,coursePublish");
        needPermissions(actionHeader,"deleteSelected","courseManage,courseDeleteSelected");
    }
    long cspId=11054540;
    {
        Admin admin222 = (Admin) session.getAttribute(Constants.SESSION_ADMIN);
        if(admin222!=null){
            cspId = admin222.getCspId();
        }
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>课件管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
        <script type="text/javascript">
        nextUrl = "contentListForPublish.jsp";
        addViewPageUrl = "contentAdd.jsp";
        actionHeader = "content";
        defaultPageSize=17;
        function doSearch(){
            var baseParams = searchStore.baseParams;
            baseParams["obj.name"]=getCmpValue("obj.name");
            baseParams["obj.status"]=getCmpValue("obj.status");
            searchStore.baseParams = baseParams;
            searchStore.load();
        }
        function publishSelectedRows(){
            doSelectedActionForIdAndText('publishCourse', '发布所选');
        }
        function unpublishSelectedRows(){
            doSelectedActionForIdAndText('unpublishCourse', '停止发布');
        }
        defaultBottomButtons = [
            {
                text:'添加记录',
                handler:addRecord,
                action:'viewCourse'
            },
            {
                text:'发布所选',
                handler:publishSelectedRows,
                action:'publishCourse'
            },
            {
                text:'停止发布',
                handler:unpublishSelectedRows,
                action:'unpublishCourse'
            }
        ];

        defaultManageLinks = [
            {
                text:'删除',
                action:'delete',
                type:'onclick',
                messageInfo:''
            },
            {
                text:'查看',
                action:'add',
                type:'href',
                messageInfo:''
            }
        ];
        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','cspId','actors','directors','name','property8','createTime','status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('listCourse')+"?test=1&obj.cspId=<%=cspId%>&"});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "课件",hidden:false, align:'left', width: 135, sortable: true,dataIndex: 'name',renderer:function (val,p,row){
                    return "<a href='contentAdd.jsp?keyId="+row.get("id")+"'>"+val+"</a>";
                }},
                {header: "讲师",hidden:false, align:'center', width: 90, sortable: true,dataIndex: 'actors'} ,
                {header: "分类",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'directors'}                ,
                {header: "关键字",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'property8'}                ,
                {header: "创建时间",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'createTime'}                ,
                {header: "发布状态",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'status'
                    , renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '<font color="red">下线</font>';
                                case 2:return '<font color="blue">上线</font>';
                                case 100:return "等待转码";
                                default:return "-" + val+
                                        "-";
                            }
                        }
                },{header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var courseGrid = new FortuneSearchListGrid({
                title:'课件列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:870,
                height:505,
                tbar:new Ext.Toolbar({items:[
                    {xtype:'label',text:'课件名称'},
                    {id:'obj.name',xtype:'textfield',name:'obj.name',width:160},
                    {xtype:'label',text:'状态'},
                    new FortuneCombo({hiddenName:'obj.status',hiddenId:'obj.status',store:getDictStore("contentStatus")}),
                    {text:'搜索',handler:doSearch}
                    ]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据"
                    ,items:defaultBottomButtons
                })
            });
            courseGrid.render("displayDiv");
            defaultGrid = courseGrid;
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