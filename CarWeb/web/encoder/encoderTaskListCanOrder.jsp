<%@ page import="com.fortune.util.StringUtils" %><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib
        prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
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
    int status = StringUtils.string2int(request.getParameter("status"),-1);
    String beginDay = request.getParameter("begin");
    String endDay = request.getParameter("end");
    if("null".equals(beginDay)||beginDay==null){
        beginDay = "";
    }
    if(endDay==null||"null".equals(endDay)){
        endDay = "";
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
        var isRoot = getParameter(document.location.search,"isSystem")=="true";
        function cancelTask(taskId){
            dealTask(taskId,"cancel","请确认取消编码任务排队！");
        }
        function restartTask(taskId){
            dealTask(taskId,"restart","请确认重启编码任务！");
        }
        function dealTask(taskId,command,confirmMsg){
            if(confirm(confirmMsg)){
                Ext.Ajax.request({
                    url:'/encoder/encoderTask!' +command+
                            '.action?keyId='+taskId,
                    callback:function(opt, success, response) {
                        if (success) {
                            var serverResult = Ext.util.JSON.decode(response.responseText);
                            if (serverResult.success) {
                                Ext.MessageBox.alert('处理完毕', '服务器返回信息：' + serverResult.msg)
                            } else {
                                Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverResult.error)
                            }
                        }
                    }
                });
            }
        }
        function formatDate(date,timeStr){
            if(date!=null&&date!=""){
                var result = date.getFullYear();
                var month =""+(date.getMonth()+1);
                if(month.length<2){
                    month = "0"+month;
                }
                var day = ""+date.getDate();
                if(day.length<2){
                    day = "0"+day;
                }
                result = result+"-" + month+"-"+day+" "+timeStr;
                return result;
            }
            return "";
        }
        function doSearchTask(){
            var beginDay = formatDate(getCmpValue("beginDay"),"00:00:00");
            var endDay = formatDate(getCmpValue("endDay"),"23:59:59");
            var baseParams = {
                "obj.name":getCmpValue("taskName"),
                "obj.sourceFileName":getCmpValue("taskSourceFileName"),
                "obj.status":getCmpValue("taskStatus"),
                "contentName":getCmpValue("contentName"),
                "beginDay":beginDay,
                "endDay":endDay,
                limit:defaultPageSize
            };
            searchStore.baseParams = baseParams;
            searchStore.load();
        }
        function initDisplay(){
            storeConfig.fields = ['et_DOT_id','et_DOT_name','et_DOT_clipId','et_DOT_encoderId','et_DOT_process','et_DOT_sourceFileName',
                'et_DOT_startTime','et_DOT_stopTime','et_DOT_status','et_DOT_templateId','et_DOT_desertFileName','d_DOT_name',
                'c_DOT_name','t_DOT_templateName'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('searchTask')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            searchStore.setDefaultSort("et_DOT_startTime","desc");
            keyFieldId = "et_DOT_id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'et_DOT_id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'et_DOT_id'}                ,
                {header: "任务名",hidden:false, align:'center', width: 135, sortable: true,dataIndex: 'et_DOT_name'},
                {header: "转码服务器",hidden:true, align:'center', width: 90, sortable: true,dataIndex: 'd_DOT_name'} ,
                {header: "启动时间",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'et_DOT_startTime'}                ,
                {header: "结束时间",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'et_DOT_stopTime',renderer:function(val,meta,rec){
                    var status=parseInt(rec.get("et_DOT_status"));
                    if(status==2||status==5||status==6||status==1){
                        return "";
                    }else{
                        return val;
                    }
                }}                ,
                {header: "源文件",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'et_DOT_sourceFileName'}                ,
                {header: "目标源文件",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'et_DOT_desertFileName'}                ,
                {header: "状态",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'et_DOT_status'
                    , renderer:
                        function (val,p,row){
                            return getDictStoreText('encoderTaskStatus',val);
                        }
                }                ,
                {header: "进度",hidden:false, align:'center', width: 101, sortable: true,dataIndex: 'et_DOT_process',
                    renderer:function(val,p,row){
                    return "<div style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:"+val+"px'>"+ val+"%</div></div>";
                }}
                ,{header: "管理", align:'center', width: 120, sortable: false,renderer:function(val,meta,rec){
                    var result  = isRoot?"<a target='_blank' href='encoderTaskView.jsp?keyId="+val+"'>查看</a>":"";
                    var status=parseInt(rec.get("et_DOT_status"));
                    if(status!=1&&status!=2){
                        result += "&nbsp;&nbsp;<a href='#' onclick='restartTask("+val+")'>重新启动</a>";
                    }else if(status==2){
                        result += "&nbsp;&nbsp;<a href='#' onclick='cancelTask("+val+")'>取消等待</a>";
                    }
                    return result;
                },dataIndex: 'et_DOT_id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var statusStore = getDictStore("encoderTaskStatus");
            statusStore.insert(0,new Ext.data.Record({id:-1,value:"-1",name:'全部'}));
            var encoderTaskGrid = new FortuneSearchListGrid({
                title:'转码任务列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:870,
                height:505,
                tbar:new Ext.Toolbar({items:[
                    {text:'电影名'},
                    {xtype:'textfield',id:'contentName',name:'contentName',width:80},
                    {text:'任务名'},
                    {xtype:'textfield',id:'taskName',name:'taskName',width:80},
                    {text:'源名称'},
                    {xtype:'textfield',id:'taskSourceFileName',name:'taskSourceFileName',width:80},
                    {text:'状态'},
                    new FortuneCombo({hiddenName:'taskStatus',hiddenId:'taskStatus',store:statusStore,width:80,value:'<%=status%>',hiddenValue:'<%=status%>'}),
                    {text:'起始'},
                    {xtype:'datefield',id:'beginDay',format:'Y-m-d',name:'beginDay',width:80,value:'<%=beginDay%>'},
                    {text:'截至'},
                    {xtype:'datefield',id:'endDay',format:'Y-m-d',name:'endDay',width:80,value:'<%=endDay%>'},
                    {text:'搜索',handler:function(){
                        doSearchTask();
                    }}
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
            //searchStore.load({params:{start:0, limit:defaultPageSize,"obj.status":<%=status%>}});
            doSearchTask();
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