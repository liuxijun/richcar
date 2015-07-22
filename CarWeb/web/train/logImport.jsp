<%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 13-2-20
  Time: 上午10:22
  根据xml添加影片信息到数据库
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript" src="../js/FtpFileSelector.js"></script>
<script type="text/javascript" src="../js/FortuneUtils.js"></script>

<script type="text/javascript">

    function selectMediaFiles(fileStore) {
        setCmpValue('fileName', fileStore);
    }
    //定义选中文件操作
    var oldFiles = [];
    function selectedCallBack(filenames) {
        filenames = getCmpValue('path');
        alert(filenames);
        if (filenames != null && filenames != "") {
            var files = filenames.split(';');
            var i = 0;
            for (; i < files.length; i++) {
                oldFiles.push(files[i]);
            }
        }
    }
    function selectDir() {
        //showServerSelectWindow(8,selectedCallBack,{
        var deviceId=-1;
        showFtpSelectWindow(selectMediaFiles, {
            singleSelect: true,
            oldData: oldFiles,
            showMediaPlayer: false,
            selectFileOnly: true,
            url: '../csp/cspModule!listFiles.action?deviceId=' + deviceId,
            selectDirOnly: false,       //是否只选择目录
            multiSelectMethod: false,   //是否多选
            maxFileNameLength: 13980
        });
    }
    function doImport(command) {
        var fileNameValue = getCmpValue("fileName");
        var uploadFileValue = getCmpValue("uploadFile");
        if(command=="uploadImport"){
            if(uploadFileValue==null||uploadFileValue==""){
                alert("请选择要上传的文件！");
                return;
            }
            var extPos = uploadFileValue.lastIndexOf(".");
            var extName = uploadFileValue;
            if(extPos>0){
                extName = uploadFileValue.substring(extPos);
            }
            if(extName!=".log"&&extName!=".txt"){
                alert("文件：" +uploadFileValue+"不符合要求！\r\n"+
                        "目前只支持后缀名是.log和.txt，请重新选择！");
                return;
            }
            setCmpValue("fileName",uploadFileValue);
        }else{
            if(command=="serverFileImport"){
                if(fileNameValue==null||fileNameValue==""){
                    alert("请选择要服务器上的的文件！");
                    return;
                }
            }
        }
        var dataForm = Ext.getCmp("baseForm338547183092");
        if(dataForm!=null){
            dataForm.getForm().submit({
                waitMsg:'提交请求中,请稍后...',
                waitTitle:'正在提交',
                clientValidation:false,
                url:'../train/train!importLog.action?importType='+command,
                success:function (re, v) {
                    var continueResult = Ext.util.JSON.decode(v.response.responseText);
                    var resultMessage="";
                    if (continueResult.msg != null) {
                        for(var i=0;i<continueResult.msg.length;i++){
                            resultMessage += continueResult.msg[i]+"\r\n";
                        }
                        //alert(resultMessage);
                        var obj = document.getElementById("logMessage");
                        try {
                            obj.value=resultMessage;
                        } catch (e) {
                            obj.innerHTML = resultMessage;
                        }

                    }
                    Ext.Msg.alert("成功", "提交完毕", function () {
                    });
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

            });
        }
        //var xmlDevice=Ext.getDom('deviceId').values;
    }
    //检查xml
    function uploadAndImport() {
        doImport("uploadImport");
    }
    //导入xml视频信息
    function serverFileImport() {
        doImport("serverFileImport");
    }
    function initData(serverData) {
        var viewWidth = 640;
        var buttonWidth = 100;
        var fieldWidth=viewWidth-160;
        var serverSidePanel = {
            xtype: 'fieldset',
            collapsible: true,
            autoHeight: true,
            defaultType: 'textfield',
            layout: 'table',
            width: viewWidth,
            cls: 'xmlStyle',
            updateFile: true,
            title: '服务器端文件',
            layoutConfig: {columns: 7},
            items: [
                {xtype: 'label', text: "选择文件:"},
                {
                    xtype:'textfield',
                    width:fieldWidth-buttonWidth,
                    cls:'fileStyle',
                    id:'fileName',
                    emptyText:'请选择',
                    name:'fileName'
                } ,{
                    xtype:'button',
                    text:'选择服务器文件',
                    width:buttonWidth,
                    handler:selectDir
                },
                {
                    xtype: 'button',
                    text: '日志导入',
                    handler: serverFileImport
                }
            ]
        };
        var uploadPanel = {
            xtype: 'fieldset',
            collapsible: true,
            autoHeight: true,
            defaultType: 'textfield',
            layout: 'table',
            width: viewWidth,
            cls: 'xmlStyle',
            updateFile: true,
            title: '上传本地文件',
            layoutConfig: {columns: 7},
            items: [
                {xtype: 'label', text: "上传文件:"},
                {
                    xtype:'fileuploadfield',
                    width:fieldWidth,
                    cls:'fileStyle',
                    id:'uploadFile',
                    emptyText:'请选择',
                    fieldLabel:'上传文件',
                    name:'uploadFile',
                    buttonText:'选择本地文件'
                },
                {
                    xtype: 'button',
                    text: '日志导入',
                    handler: uploadAndImport
                }
            ]
        };
        var field4 = new Ext.form.TextArea({
            width: viewWidth,
            height: 200,
            /*
             grow:true,
             preventScrollbars:true,
             */
            fieldLabel: 'remark',
            // allowBlank:false,
            id: 'logMessage'
        });
        var cspViewForm = new Ext.FormPanel({
            title: '导入日志文件',
            bodyStyle: 'padding:5px 5px 5px 5px',
            align: 'center',
            fileUpload:true,
            width: viewWidth+15,
            autoHeight:true,
            layout: 'table',
            colspan: 1,
            id:'baseForm338547183092',
            layoutConfig: {columns: 1},
            items: [
                serverSidePanel
                ,uploadPanel,
                field4
            ]
        });
        cspViewForm.render('displayDiv');
    }
    Ext.onReady(function () {
        Ext.QuickTips.init();
        checkAllFunctions(); //检查权限
        queueFunctions([
            {
                func: initDictStores,
                done: false,
                flag: 'initDictStores'
            }
        ],
                initData)

    });

</script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div class="text" id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>