<%@ page import="java.util.List" %><%@ page
        import="com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.util.PageBean" %><%@ page
        import="com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-6-1
  Time: 上午10:15
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    final ContentChannelLogicInterface contentChannelLogicInterface = (ContentChannelLogicInterface)
            SpringUtils.getBean("contentChannelLogicInterface", session.getServletContext());
    List<String> logs = contentChannelLogicInterface.getExportLogs();
    String message = "尚未启动";
    {
        Admin _opLogined =
                (Admin)session.getAttribute(
                        com.fortune.common.Constants.SESSION_ADMIN);
        if(_opLogined == null){
%><html><body onload="jumpForm.submit()">
<form name="jumpForm" target="_top" action="../index.jsp"></form></body></html>
<%
            return;
        }
    }
    String[] channelIds=  request.getParameterValues("channelId");
    if(channelIds==null){
        channelIds = new String[]{
                //"15884423",//	直播节目
                "15884424",//	电影
                "15884426",//	电视剧
                "15884428",//	新闻
                "15884430",//	体育
                "15884432",//	娱乐
                "15884434",//	名栏目
                "15884436",//	纪实
                "15884438",//	音乐
                "15884440"//	风尚
                ,"15884442"//	教育
                //"15884642",//	华数VIP
                //"15884644",//	优朋VIP
                //"15884646",//	百视通VIP
                //"115390100",//	专题
                //"474431585",//	本地

        };
    }
    final Long[] ids=new Long[channelIds.length];
    int i=0;
    int exportAD=StringUtils.string2int(request.getParameter("exportAD"),1);
    int exportSecurityMode=StringUtils.string2int(request.getParameter("exportSecurityMode"),0);
    for(String id:channelIds){
        if(id!=null&&!"".equals(id)){
            ids[i++]= StringUtils.string2long(id,-1);
        }
    }
    int size=StringUtils.string2int(request.getParameter("size"),10);
    String exportDir = request.getParameter("exportDir");
    if(exportDir==null){
        exportDir = "C:/exportSD";
    }
    final String willUseDir=exportDir;
    final String posterDir = application.getRealPath("/");
    final PageBean pageBean = new PageBean(1,size,"c.id","DESC");
    String command = request.getParameter("command");
    if(command==null){
        command = "";
    }
    if(logs.size()>0){
        message = logs.get(logs.size()-1);
    }
    String commandResult = null;
    if(channelIds.length>0){
        if("refresh".equals(command)){
            commandResult = "success";
        }else if("check".equals(command)){
            Thread task = new Thread(){
                public void run(){
                    contentChannelLogicInterface.scanChannelContents(ids,pageBean);
                }
            };
            task.start();
            commandResult = "success";
        }else if("start".equals(command)){
            Thread task = new Thread(){
                public void run(){
                    contentChannelLogicInterface.exportContents(ids,willUseDir, pageBean,posterDir);
                }
            };
            task.start();
            commandResult = "success";
        }
    }
    if(commandResult!=null){
        out.println("{\nwillCopySize:" +contentChannelLogicInterface.getWillCopyMediaFileSize()+
                ",\nwillCopyFileCount:" +contentChannelLogicInterface.getWillCopyMediaFileCount()+
                ",\nexportedSize:" +contentChannelLogicInterface.getExportedMediaFileSize()+
                ",\nexportedFileCount:" +contentChannelLogicInterface.getWillCopyMediaFileCount()+
                ",\nprocess:" +contentChannelLogicInterface.getProcess()+
                ",\nlastLog:'"+ message.replace('\'','"').replace('\n','_').replace('\r','_')+"',\nsuccess:true,\n" +
                "command:'"+command+"'}");
        return ;
    }
    int barWidth=600;
    int process = contentChannelLogicInterface.getProcess();
    ChannelLogicInterface channelLogicInterface = (ChannelLogicInterface) SpringUtils.getBean("channelLogicInterface",
            session.getServletContext());
    List<Channel> channels = channelLogicInterface.getChannelList("15884422");
    String channelItems = "";
    for(Channel channel:channels){
        String checked="false";
        for(Long id:ids){
            if(id.equals(channel.getId())){
                checked = "true";
            }
        }
        long id =channel.getId();
        channelItems +="{xtype:'checkbox',id:'channel" +id+
                "',name:'channelId',inputValue:"+id+",boxLabel:'"+channel.getName()+"',checked:"+checked+",width:100},";
    }
    if(!"".equals(channelItems)){
        channelItems=channelItems.substring(0,channelItems.length()-1);
    }
    channelItems="["+channelItems+"]";
    String agent = request.getHeader("user-agent");
    boolean isFirefox = false;
    if(agent!=null&&agent.contains("Firefox")){
        isFirefox = true;
        //System.out.println("is firefox!!!user-agent===="+agent);
    }
    if(!isFirefox){
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"><%
}else{
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"><%
    }
%><html>
<head>
    <title>导出媒体数据</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <style type="text/css">
        body{
            font-size:12px;
        }
        .mainBody{
            padding-left:10px;
        }
        .mainBodyTitle{

        }
        .mainBodyOptions{

        }
        .mainBodyLogs{

        }
        .processedBar{
            width:0px;
            text-align: center;
            float: left;
            background-color:green;
            border: solid #b2ffbb 1px;
            border-bottom-color:#808080;
            border-right-color:#808080;
            height: 18px;
        }
        .processBar{
            background-color:blue;
            border: solid black 1px;
            height: 20px;
        }
        .lastLog{
            font-size:12px;
        }
    </style>
    <script src="../js/jquery.js">
    </script>
    <script type="text/javascript">
        var barWidth=<%=barWidth%>;
        function refreshData(data){
            var progressBar = Ext.getCmp("progressBar");
            if(progressBar!=null){
                progressBar.updateProgress(data.process/100.0);
            }
            var htmlInfo =
            "导出执行的总量输出进度："+ data.process+"%<br>"+"将要导出的媒体文件总数：" +data.willCopyFileCount+
                    "<br>"+
            "将要导出的媒体文件大小：" +data.willCopySize+
                    " bytes<br>"+
            "已经导出的媒体文件总数：" +data.exportedFileCount+
                    "<br>"+
            "已经导出的媒体文件大小：" +data.exportedSize+
                    " bytes<br>"+
                    "最后一条输出日志："+data.lastLog;
            var infoBox = Ext.getCmp("messageBox");
            if(infoBox!=null){
                infoBox.body.update(htmlInfo);
            }
        }
        function doCommand(command) {
            try {
                var formPanel = Ext.getCmp('BaseViewForm338547183092');
                var dataForm = formPanel.getForm();
                if (dataForm == null) {
                    alert("form为空！不能继续！");
                } else {
                    setCmpValue("command",command);
                    if (dataForm.isValid()) {
                        dataForm.submit({
                            waitMsg:'保存中,请稍后...',
                            waitTitle:'正在保存',
                            clientValidation:false,
                            url:dataForm.url,
                            success:function (re, v) {
                                if(command == "refresh"){
                                    var jsonData=v.response.responseText;
                                    refreshData(Ext.decode(jsonData));
                                }else{
                                    Ext.Msg.alert("成功", "命令已经提交！", function () {
                                        try {

                                        } catch (e) {
                                            alert("发生异常：");
                                        }
                                    });
                                }
                            },
                            failure:function (form, action) {
                                switch (action.failureType) {
                                    case Ext.form.Action.CLIENT_INVALID:
                                        Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                                        break;
                                    case Ext.form.Action.CONNECT_FAILURE:
                                        Ext.Msg.alert('Failure', '连接通讯失败！可能服务器已经关闭或者网络出现问题！');
                                        break;
                                    case Ext.form.Action.SERVER_INVALID:
                                        var errorMsg = action.result.msg;
                                        if (action.result.fieldErrors) {
                                            errorMsg += "<br/>" + action.result.fieldErrors;
                                        }
                                        Ext.Msg.alert('Failure', errorMsg);
                                }
                            }

                        })
                    } else {
                        var unValidFields = "";
                        dataForm.items.each(function (item) {
                            if (!item.validate()) {
                                if (unValidFields != "") {
                                    unValidFields += "，";
                                }
                                for (il in item.fieldLabel) {
                                    //unValidFields+=","+il;
                                }
                                unValidFields += "'" + item.fieldLabel + "'";

                            }
                        });
                        //alert(unValidFields);
                        Ext.Msg.alert("校验错误", "有校验项目不对，请检查有红色标识的字段:<br><div style='color:#ff2634'>"
                                + unValidFields + "</div>");
                    }
                }
            } catch (exceptionE) {
                Ext.Msg.alert("有问题", "准备提交'" + formPanel.title + "'数据时发生异常：" + exceptionE.description);
            }

        }

        function refreshExport(){
            doCommand("refresh");
        }
        function startExport(){
            doCommand("start");
        }
        function checkExport(){
            doCommand("check");
        }
        var willStopAutoRefresh = false;
        function checkAutoRefresh(willAutoRefresh){
            if(willAutoRefresh){
                window.setTimeout("doRefresh()",1000);
            }else{
                willStopAutoRefresh=true;
            }
        }
        function doRefresh(){
            if(!willStopAutoRefresh){
                window.setTimeout("doRefresh()",1000);
            }
            $.ajax({ url: "exportSD.jsp?command=refresh&jsonFormat=true", dataType: "json",
                success: function(responseData){
                    var obj = document.getElementById('processedBar');
                    if(obj!=null){
                        obj.innerHTML = responseData.process+"%";
                        obj.style.width = (barWidth* responseData.process/100)+"px";
                    }

            }});
        }

        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var width=440;
            var channelItems = <%=channelItems%>;
            var adViewForm = new Ext.FormPanel({
                title: '导出媒体',
                width:width,
                url: 'exportSD.jsp',
                saveUrl: 'exportSD.jsp',
                id:'BaseViewForm338547183092',
                autoHeight:true,
                items: [
                    new Ext.form.FieldSet({
                        title: '选择频道',
                        //  disabled:true,
                        layout: 'table',
                        layoutConfig: {columns: 4},
                        xtype: 'panel',
                        frame:true,
                        border: true,
                        width: width-5,
                        id: 'channels',
                        items: channelItems,
                        listeners: {
                        }
                    }),
                    new Ext.form.FieldSet({
                        defaultLabelAlign:'right',
                        id:'BaseViewForm338547183092',
                        labelWidth: 155, // label settings here cascade unless overridden
                        width: width-5,
                        labelAlign:'right',
                        title: '导出配置',
                        //bodyStyle:'padding:5px 5px 0',
                        defaults: {width: width-200},
                        defaultType: 'textfield',
                        items:[
                            {fieldLabel: '每个频道要导出的影片数', name: 'size',value:<%=size%>}
                           ,{fieldLabel: '服务器端影片的存放目录', name: 'exportDir',value:'<%=exportDir%>'}
                           ,new FortuneCombo({
                                fieldLabel: '同步导出广告信息',
                                hiddenName: 'exportAD',
                                mode: 'local',
                                value: <%=exportAD%>,
                                forceSelection: true,
                                triggerAction: "all",
                                editable: false,
                                store: new Ext.data.SimpleStore({fields:['name','value'],data:[['是',1],['否',0]]}),
                                valueField: 'value', displayField: 'name',
                                emptyText: '请选择...'
                            })
                            ,new FortuneCombo({
                                fieldLabel: '导出数据进行指纹签名',
                                hiddenName: 'exportSecurityMode',
                                mode: 'local',
                                value: <%=exportSecurityMode%>,
                                forceSelection: true,
                                triggerAction: "all",
                                editable: false,
                                store: new Ext.data.SimpleStore({fields:['name','value'],data:[['MD5',1],['DSA',2],['不加密','0']]}),
                                valueField: 'value', displayField: 'name',
                                emptyText: '请选择...'
                            }),{name:'command',id:'command',inputType:'hidden'}
                        ]
                        })
                        ,new Ext.Panel({
                        title:'执行结果',
                        width:width,
                        layout:'form',
                        bodyStyle:'padding:5px',
                        items:[
                            {xtype:'label',text:'当前执行情况：',width:width-10},
                            new Ext.ProgressBar({
                                id:'progressBar',
                                width:width-10
                            })
                            ,{xtype:'panel',width:width-10,id:'messageBox',html:''+
'       将要导出的媒体文件总数：<%=(contentChannelLogicInterface.getWillCopyMediaFileCount())%><br>'+
'       将要导出的媒体文件总量：<%=StringUtils.formatBytes(contentChannelLogicInterface.getWillCopyMediaFileSize())%><br>'+
'       已经导出的媒体文件总数：<%=(contentChannelLogicInterface.getExportedPicFileCount())%><br>'+
'       已经导出的媒体文件总量：<%=StringUtils.formatBytes(contentChannelLogicInterface.getExportedMediaFileSize())%><br>'+
''}
                        ]
                    })
                ],buttons:[
                    {text:'导出测试',handler:checkExport}
                    ,{text:'开始导出',handler:startExport}
                    ,{text:'查看结果',handler:refreshExport}
                ]
            });
            //loadFormAjax();
            adViewForm.render('displayDiv');
        }
        Ext.onReady(function () {
            Ext.QuickTips.init();
            initDisplay();
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv">

            </div>
        </td>
    </tr>
</table>
</body>
</html>
