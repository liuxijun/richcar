<%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 13-2-20
  Time: 上午10:22
  根据xml添加影片信息到数据库
--%>
<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp"%>
<%@include file="../inc/extBase.jsp"%>
<script type="text/javascript" src="../js/FtpFileSelector.js" ></script>
<script type="text/javascript" src="../js/FortuneUtils.js"></script>

<script type="text/javascript">

function selectMediaFiles(fileStore){
    setCmpValue('path',fileStore);
}
//定义选中文件操作
var oldFiles =[];
function selectedCallBack(filenames){
    filenames = getCmpValue('path');
    alert(filenames);
    if(filenames!=null&&filenames!=""){
        var files = filenames.split(';');
        var i=0;
        for(;i<files.length;i++){
            oldFiles.push(files[i]);
        }
    }
}
function selectDir(){
    //showServerSelectWindow(8,selectedCallBack,{
    var xmlDevice=getCmpValue('deviceId');
    if(xmlDevice == ""){
        alert("请选择服务器后再进行操作！");
        return;
    }
    showFtpSelectWindow(selectMediaFiles,{
        singleSelect:true,
        oldData:oldFiles,
        showMediaPlayer:false,
        selectFileOnly:true,
        url:'../csp/cspModule!listFiles.action?deviceId='+xmlDevice,
        selectDirOnly:false,       //是否只选择目录
        multiSelectMethod:false,   //是否多选
        maxFileNameLength:13980
    });
}
var cspDeviceStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/csp/cspDevice!getDevicesOfCsp.action",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
cspDeviceStore.load({
    callback:function (records, options, success) {
        if (success) {
            this.insert(0, new Ext.data.Record({id:-1, name:'请选择...'}));
        }
    }
});
//向服务器提交
var isReplace=true;
var moduleId=5000;
function doImportXml(command){
    //保存前首先要判断前面的xml文件是否选择了
    var xmlValue=Ext.getDom('path').value;
    //var xmlDevice=Ext.getDom('deviceId').values;
    var xmlDevice=getCmpValue('deviceId');
    if(xmlValue==""||xmlDevice==""){
        alert("请先选择处理的xml文件,服务器！");
    } else{
        if(isReplace){
            isReplace=false;
        Ext.MessageBox.show({
            msg: '正在导入数据，请稍候...',
            progressText: '正在处理...',
            width:300,
            wait:true,
            waitConfig: {interval:500},
            icon:'ext-mb-info' //custom class in msg-box.html
            //,animEl: 'mb7'
        });
        Ext.Ajax.request({
            url:'../content/content!saveXmlMessage.action?moduleId=' +moduleId+
                    '&fileAddress='+encodeURI(encodeURI(xmlValue))+'&deviceId='+xmlDevice+"&importXmlCommand="+command,
            method:'GET',
            callback:function(opt, success, response) {
                Ext.MessageBox.hide();
                if(success){
                    try {
                        isReplace=true;
                        var continueResult = Ext.util.JSON.decode(response.responseText);
                        var resultMessage="";
                        if (continueResult.objs != null) {
                            for(var i=0;i<continueResult.totalCount;i++){
                                resultMessage += continueResult.objs[i]+"\r\n";
                            }
                            //alert(resultMessage);
                            var obj = document.getElementById("logMessage");
                            try {
                                obj.value=resultMessage;
                            } catch (e) {
                                obj.innerHTML = resultMessage;
                            }

                        }   else{
                            alert("xml处理失败，请重新处理");
                        }
                    } catch (e) {
                    }
                }else{
                    alert("可能因为xml处理时间过长，目前还没有正确的返回结果。\r\n请稍候检查媒体数量是否在增加，以确定导入是否正常！");
                }
            }
        });
    }  else{
            alert("数据还没处理完，请等待！");
        }
    }
}
//检查xml
function checkXml(){
    doImportXml("checkOnly");
}
//导入xml视频信息
function addAllMove(){
    doImportXml("doImport");
}
function initData(serverData) {
    var xmlSpaceInfoPanel = {
        xtype:'fieldset',
        collapsible: true,
        autoHeight:true,
        defaultType: 'textfield',
        layout:'table',
        width:1150,
        cls:'xmlStyle',
        title:'导入xml文件，解析保存',
        layoutConfig:{columns:7}    ,
        items:[
            {xtype:'label',text:'选择服务器: '},
            {
                id:'deviceId',
                hiddenName:'obj.deviceId',
                xtype:'combo',
                labelWidth:50,
                width:140,
                triggerAction:'all',
                emptyText:'请选择...',
                store:cspDeviceStore,
                valueField:'id',
                displayField:'name',
                loadingText:'加载中...'
            },{xtype:'label',text:"XML文件"+':'},
            {
                name:'obj.path',
                id:'path',
                allowBlank:false,
                disabled:true,
                readOnly :true,
                width:150
            } ,{
                xtype:'button',
                text:'选择Xml',
                handler:selectDir
            },
            {
                xtype:'button',
                text:'影片信息导入',
                handler:addAllMove
            },{
                xtype:'button',text:'检查数据',handler:checkXml
            }
        ]
    };
     var field4=new Ext.form.TextArea({
                 width:655,
                 height:400,
/*
                 grow:true,
                 preventScrollbars:true,
*/
                 fieldLabel:'remark',
                // allowBlank:false,
                 id:'logMessage',
                 style:{'margin-left':'10px'}
     });
    var cspViewForm = new Ext.FormPanel({
        title:'处理xml信息，录入数据',
        bodyStyle:'padding:15px 0px 0',
        align:'center',
        width:680,
        height:530,
        layout:'table',
        colspan:1,
        layoutConfig:{columns:1} ,
        items:[
            xmlSpaceInfoPanel
            ,
            field4
        ]
    });
<%if (request.getParameter("keyId")!=null){%>
    loadFormAjax();
<%      }            %>
    cspViewForm.render(displayDiv);
}
Ext.onReady(function() {
    Ext.QuickTips.init();
    checkAllFunctions(); //检查权限
    queueFunctions([
        {
            func:initDictStores,
            done:false,
            flag:'initDictStores'
        }
    ],
            initData)

});

</script>
</head>
<body>
<table class="leftTable">
    <tr>
        <td>
            <div class="text" id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>