<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
//         needPermissions(actionHeader,"list","synTaskManage");
        needPermissions(actionHeader,"upload","synFileManage,synFileUpload");
        needPermissions(actionHeader,"isExistFile","synFileManage,synFileIsExistFile");
        needPermissions(actionHeader,"listFiles","templateListFiles");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>                                                                     
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>同步管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>

    <script type="text/javascript" src="../js/FtpFileSelector.js" ></script>
    <script type="text/javascript" src="../js/ExtFortuneSelectField.js"></script>
    <script type="text/javascript">
        actionHeader = "synTask";
        nextUrl = "synTaskList.jsp";
        function isExistFile(fileName,uploadFileServer){

                      Ext.Ajax.request({

                      url:"/syn/synFile!isExistFile.action?uploadFileLocal="+fileName+"&uploadFileServer="+uploadFileServer,
                       callback : function(opt, success, response) {
                           if(success){
                        var serverData = Ext.util.JSON.decode(response.responseText);
                        if(serverData.data['obj.type']==1){
                            alert("该文件名已经存在！");
                        }else{
                            
                        }
                           }
                       }
                   });
               }
        

           function selectMediaFiles(fileStore){
            setCmpValue('fileNames',fileStore);
        }




        function initDisplay() {
            checkAllFunctions(); //检查权限
            var  fileUpload = new FortuneViewForm({
                fileUpload:true,
                title:'上传同步',
                url:'synFile!upload.action',
                saveUrl:'synFile!upload.action',
                bodyStyle:'padding:5px 5px 0',
                items:[
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'1'
                    }                     ,

                    new Ext.form.SelectField(
                            {
                                fieldLabel:'上传目录',
                                name:'uploadFileServer',
                                value:"/",
                                id:'fileNames',
                                listeners:{
                                   select:  function openSelectFilesWin(){
                                                 var oldFiles =[];
                                                 var oldFileNames = getCmpValue('fileNames');
                                                 if(oldFileNames!=null&&oldFileNames!=""){
                                                     var files = oldFileNames.split(';');
                                                     var i=0;
                                                     for(;i<files.length;i++){
                                                         oldFiles.push(files[i]);
                                                     }
                                                 }
                                                 showFtpSelectWindow(selectMediaFiles, {
                                                     oldData:oldFiles,
                                                     singleSelect:true,
                                                     showMediaPlayer:false,
                                                     selectFileOnly:false,
                                                     url:'../template/template!listFiles.action',
                                                     dirType:1,
                                                     maxFileNameLength:13980
                                                 });
                                   }
                                },
                                allowBlank:false,
                                readOnly:false
                            }),{
                                xtype:'textfield',
                                name:'uploadFileLocal',
                                hidden:true
                            },{
                                xtype: 'fileuploadfield',
                                id: 'uploadFile',
                                emptyText: '请选择',
                                fieldLabel: '上传文件',
                                name: 'uploadFile',
                                buttonText: '',
                                buttonCfg: {
                                    iconCls: 'upload-icon'
                                },
                                listeners:{
                                    "fileselected":function(btn,value)
                                    {
                                        fileUpload.getForm().findField('uploadFileLocal').setValue( value );
                                        var uploadFileServer = fileUpload.getForm().findField('uploadFileServer').value;
                                        if(typeof(uploadFileServer)!="undefined"){
                                             isExistFile(value,uploadFileServer);
                                        }else{
//                                            alert("请先选择上传目录");
//                                            fileUpload.getForm().findField('uploadFile').setValue("");
                                        }
                                    }
                                }
                            }

                ],
                buttons:[{
                    text:'保存数据',
                    handler:saveFormAjax,
                    action:'save'
                },{
                    text:'重新加载',
                    handler:loadFormAjax,
                    action:'view' 
                }]
            });
           if (keyId != "-1") {

                loadFormAjax();
            } else {
            }
            var displayDivObj = document.getElementById("displayDiv");
            fileUpload.render(displayDivObj);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],
                    initDisplay);
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>