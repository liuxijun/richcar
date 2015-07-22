<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "synTask";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"reSync","synTaskManage,synTaskReSync");
        needPermissions(actionHeader,"list","synTaskManage,synTaskList");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>同步管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript" src="../js/FtpFileSelector.js"></script>
    <script type="text/javascript">

        defaultPageSize=12;
        nextUrl = "synTaskList.jsp";
        actionHeader = "synTask";
        var win = null;

        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','deviceId','fileId','synStatus','startTime','startPos','endPos','device.name','synFile.name'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "设备名",hidden:false, align:'center', width: 150, sortable: true,dataIndex: 'device.name'}                ,
                {header: "文件名",hidden:false, align:'center', width: 100, sortable: true,dataIndex: 'synFile.name' }                ,
                {header: "同步状态",hidden:false, align:'center', width: 100, sortable: true,dataIndex: 'synStatus',renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '等待同步';
                                case 2:return '开始同步';
                                case 3:return '下载中';
                                case 4:return '下载成功';
                                case 5:return '下载失败';
                                case 6:return '删除中';
                                case 7:return '删除成功';
                                case 8:return '删除失败';                                                    
                            }
                        }

                }                ,
                {header: "同步时间",hidden:false, align:'center', width: 120, sortable: false,
                   dataIndex:"startTime"},
                {header: "同步进度",hidden:false, align:'center', width: 120, sortable: false,
                   renderer:

                   function(val,p,row){
                        if(row.json.synStatus==3||row.json.synStatus==4){
                             var val = parseInt(row.json.startPos / row.json.endPos)*100;
                             if(val<0){
                                val=0;
                            }
                            if(val>100){
                                val=100;
                            }
                             return"<div align='left' style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:"+val+"px'>"+ val+"%</div></div>";

                        }else if(row.json.synStatus == 7){
                            var val =100;
                            return"<div align='left' style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:"+val+"px'>"+ val+"%</div></div>";

                        }else{
                            var val =0;
                            return"<div align='left' style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:"+val+"px'>"+ val+"%</div></div>";

                        }
                   }

                },
//                {header: "重新启动",hidden:false, align:'center', width: 70, sortable: false,
//                    renderer:function(val,p,row){ //d
//
//                        if(row.json.synStatus == 5 || row.json.synStatus == 8){
//                            return '<a href="/syn/synTask!reSync.action?keyId='+val+'">开始同步</a>';
//                        }
//
//
//                    },dataIndex:"id"},
                {header: "管理", align:'center', width: 70, sortable: false,renderer:displayManage,dataIndex: 'id'}

            ]);
            //除了基本操作（删除，查看），还要添加的链接
            function displayManage(val, p, rec) {
                if(rec.json.synStatus == 1 || rec.json.synStatus == 8){
                    return displayControl(val, p, rec, defaultManageLinks);
                }
            }
            defaultManageLinks.push({text:'同步',action:'reSync',type:'onclick',messageInfo:''});
            canUseFunctions = ['reSync'];
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var synGrid = new FortuneSearchListGrid({
                title:'同步列表',
                sm : sm,
                cm:columns,
                width:630,
                height:425,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'文件名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.synFile.name',
                            width:320
                        })
                    ]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据"

                })
            });
            var displayDivObj = document.getElementById("displayDiv");
            synGrid.render(displayDivObj);

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