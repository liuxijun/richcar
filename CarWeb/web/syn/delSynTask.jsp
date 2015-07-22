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
//        needPermissions(actionHeader,"list","synTaskList");
        needPermissions(actionHeader,"del","synFileDel");
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


           function selectMediaFiles(fileStore){
            setCmpValue('fileNames',fileStore);
        }

         function openSelectFilesWin(){
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
                singleSelect:false,
                showMediaPlayer:false,
                selectFileOnly:false,
                url:'../template/template!listFiles.action',
                dirType:1,
                maxFileNameLength:13980
            });
        }

        function initDisplay() {
            checkAllFunctions(); //检查权限
            var fileViewForm = new FortuneViewForm({
                title:'删除同步',

                saveUrl:'synFile!del.action',

                bodyStyle:'padding:5px 5px 0',
                items:[
                    {
                        name:'obj.id',
                        inputType:'hidden',
                        value:'1'
                    }                     ,
                     
                    new Ext.form.SelectField({
                                fieldLabel:'文件名',
                                name:'fileNameString',
                                id:'fileNames',
                                listeners:{
                                   select:  openSelectFilesWin
                                },
                                allowBlank:false,
                                readOnly:true
                            })

                ]
            });
           if (keyId != "-1") {

                loadFormAjax();
            } else {
            }
            fileViewForm.render(document.getElementById("displayDiv"));
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