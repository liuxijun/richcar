<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String id = request.getParameter("id");
    if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
        response.sendRedirect("../V5/media/mediaView.jsp?readOnly=true&keyId="+id);
        return;
    }
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "viewOnly", "contentViewOnly");
        needPermissions(actionHeader, "getContentRegUrl", "contentGetContentRegUrl");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript" src="contentAddV2.js"></script>
<script language="javascript">
Ext.onReady(function () {
    <%
    %>
    var id = <%=id%>;

    var pageSize = 12;
    var tableWidth = 765;
    var tableHeight = 560;
    var isSystem = getParameter(document.location.search, "isSystem") == "true";
    var actionUrl = "/content/content";
    var listGrid;


    function getParameter(urlStr, parameterName) {
        var parameterValue = null;
        if (urlStr && parameterName) {
            var p = urlStr.lastIndexOf("&" + parameterName + "=");
            if (p < 0) {
                p = urlStr.lastIndexOf("?" + parameterName + "=");
            }
            if (p >= 0) {
                p += parameterName.length + 2;
                parameterValue = "";
                //alert(parameterValue);
                while (p < urlStr.length) {
                    var ch = urlStr.charAt(p);
                    p++;
                    if (ch != '&' && ch != '?' && ch != '#') {
                        parameterValue += ch;
                    } else {
                        break;
                    }
                }
            } else {
                parameterValue = "-1";
            }
        }
        return parameterValue;
    }


    var dataViewStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:actionUrl + "!viewOnly.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'contentId'} ,
            {name:'propertyId'},
            {name:'intValue'},
            {name:'stringValue'},
            {name:'desp'},
            {name:'extraData'} ,
            {name:'extraInt'} ,
            {name:'length'},
            {name:'thumbPic'}
        ]
    });
    //dataListStore.setDefaultSort('m_mediaId','desc');
    dataViewStore.load({params:{keyId:id, start:0, limit:10000000}});

    var columnWidth = 180;

    var tip;
    var buttons = [
        {text:'关闭', handler:function () {
             window.parent.close();
        }}
    ];
    if (true) {
        buttons.push({text:'获取拉动连接', handler:function () {
            Ext.Ajax.request({
                url:'getPullUrls.jsp?contentId=' + id,
                callback:function (opt, success, response) {
                    if (success) {
                        var serverResult = Ext.util.JSON.decode(response.responseText);
                        if (serverResult.success) {
                            Ext.MessageBox.alert('处理完毕', '请复制连接：<br/>' + serverResult.msg)
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverResult.error)
                        }
                    }
                }           });
        }});
    }
    if (true) {
        buttons.push({text:'获取专题连接', handler:function () {
            Ext.Ajax.request({
                url:'getContentZtUrls.jsp?contentId=' + id,
                callback:function (opt, success, response) {
                    if (success) {
                        var serverResult = Ext.util.JSON.decode(response.responseText);
                        if (serverResult.success) {
                            Ext.MessageBox.alert('处理完毕', '请复制连接：<br/>' + serverResult.msg)
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverResult.error)
                        }
                    }
                }           });
        }});
    }
     listGrid = new Ext.grid.GridPanel({
        title:"资源详情",
        width:tableWidth,
        height:tableHeight + 15,
        store:dataViewStore,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },
        listeners:{
            "cellclick":function (grid, rowIndex, columnIndex, e) {

                if (tip) {
                    tip.hide();
                }
                if (columnIndex == 1) {
                    tip = new Ext.Tip({
                        maxWidth:500,
                        html:grid.getStore().getAt(rowIndex).get(grid.colModel.config[ columnIndex ].dataIndex)
                    });
                    tip.showAt(e.getXY());
                }
            }
        },
        columns:[
            {
                header:"属性名", dataIndex:'name', width:200, sortable:true, align:'left', renderer:function (val, p, row) {
                var thumbPic = row.data.thumbPic;
                if (thumbPic != null) {
                    var propertyType = parseInt(getParameter(thumbPic, "propertyType"));
                    var name = getParameter(thumbPic, "propertyName");
                    if (propertyType == 9 || propertyType == 10 || propertyType == 8) {
                        return name + ",第" + row.data.intValue + "集";
                    } else {
                        return name;
                    }
                }
            }
            },
            {header:"值", dataIndex:'stringValue', width:390, sortable:true, align:'left',
                renderer:function (val, p, row) {
                    return  val;
                }
            },
            {header:"其他", dataIndex:'extraValue', width:150, sortable:true, align:'left',
                renderer:function (val, p, row) {
                    var dataType = 0;
                    var thumbPic = row.data.thumbPic;
                    if (thumbPic != null) {
                        dataType = parseInt(getParameter(thumbPic, "propertyType"));
                    }
                    if (dataType == 8) {//wmv
                        var str = row.data.extraData.split('###');
                        var output = '';
                        if (str[0]) {
                            output += '说明:' + str[0];
                        }
                        if (str[1]) {
                            output += "　码流:" + str[1];
                        }
                        if (str[2]) {
                            output += "　时长:" + str[2];
                        }
                        return '<input type="button" value="浏览视频" onclick="viewWmv(\'' + row.data.stringValue + '\')"/>' + output;
                        //return '说明:'+str[0]+"　码流:"+str[1]+"　时长:"+str[2];

                    } else if (dataType == 11) {//photo
                        return '<input type="button" value="浏览图片" onclick="viewPhoto(\'' + row.data.stringValue + '\')"/>';
                        //return '说明:'+str[0]+"　码流:"+str[1]+"　时长:"+str[2];

                    } else if (dataType == 9 || dataType == 10) {//Flash
                        var result = '<input type="button" value="观看" onclick="viewFlash(\'' + row.data.stringValue + '\')"/>';
                        if (isSystem) {
                            var propertyCode = getParameter(thumbPic, "propertyCode");
                            if ("Media_Url_Source" == propertyCode) {
                                result += '<input type="button" value="转码" onclick="startEncode(' + row.data.id + ')"/>';
                            }
                        }
                        return result;
                    }

                }
            }

        ],
        tbar:buttons
    });

    listGrid.render('display');
    this.startEncode = function (clipId) {
        var taskStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort:true,
            url:"/encoder/encoderTask!checkTask.action?obj.clipId=" + clipId,
            totalProperty:"totalCount",
            root:'objs',
            fields:['id', 'clipId', 'encoderId', 'process', 'sourceFileName',
                'startTime', 'stopTime', 'status', 'templateId', 'desertFileName', 'deviceName',
                'contentName', 'templateName', 'name']
        });
        var columns = new Ext.grid.ColumnModel([
            sm,
            {id:'id', hidden:true, align:'center', header:"序号", width:60, sortable:true, dataIndex:'id'}                ,
            {header:"任务名", hidden:false, align:'center', width:235, sortable:true, dataIndex:'name', renderer:function (val, p, row) {
                if (val != "") {
                    return val;
                }
                return row.get("templateName");
            }},
            {header:"启动时间", hidden:false, align:'center', width:120, sortable:true, dataIndex:'startTime'}                ,
            {header:"结束时间", hidden:false, align:'center', width:120, sortable:true, dataIndex:'stopTime'}                ,
            {header:"源文件", hidden:true, align:'center', width:175, sortable:true, dataIndex:'sourceFileName'}                ,
            {header:"目标源文件", hidden:true, align:'center', width:175, sortable:true, dataIndex:'desertFileName'}                ,
            {header:"状态", hidden:false, align:'center', width:70, sortable:true, dataIndex:'status', renderer:function (val, p, row) {
                var resultVal = getDictStoreText('encoderTaskStatus',val);
                if(resultVal!=null){
                    return resultVal;
                }
                switch (val) {
                    case 1:
                        return '运行中';
                    case 2:
                        return '等待中';
                    case 3:
                        return '已完成';
                    case 4:
                        return '错误';
                    case 5:
                        return '待创建';
                    case 6:
                        return '取消';
                }
            }
            }                ,
            {header:"进度", hidden:true, align:'center', width:101, sortable:true, dataIndex:'process', renderer:function (val, p, row) {
                return "<div style='border:1px solid;height:15px;background:red;width:100px'><div align='center' style='height:15px;background:green;width:" + val + "px'>" + val + "%</div></div>";
            }}
            //,{header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
        ]);
        var taskGrid = new Ext.grid.EditorGridPanel({
            width:590,
            height:380,
            store:taskStore,
            sm:new Ext.grid.CheckboxSelectionModel({singleSelect:false}),
            cm:columns,
            tbar:[
                {
                    text:'启动选中的转码', handler:function () {
                    var row = taskGrid.getSelectionModel().getSelections();
                    if (row.length == 0) {
                        Ext.Msg.alert("提示", "请至少选择一个转码任务！");
                        return;
                    }
                    var objs = "";
                    var warningInfo = "";
                    for (var i = 0; i < row.length; i++) {
                        var data = row[i].data;
                        if (data.status == 3) {
                            warningInfo += data.name + "已经存在并已经执行完毕！<br/>"
                        } else if (data.status == 1) {
                            warningInfo += data.name + "已经存在于队列中！<br/>"
                        }
                        objs += "&encodeTasks[" + i + "].id=" + data.id;
                        objs += "&encodeTasks[" + i + "].clipId=" + data.clipId;
                        objs += "&encodeTasks[" + i + "].templateId=" + data.templateId;
                    }
                    Ext.MessageBox.confirm("请您确认操作", "转码任务启动<br/>" + warningInfo + "您确认要执行这些转码操作吗？如果确认，将会覆盖原有的已存在的转码结果文件！", function (btn) {
                        if (btn == "yes") {
                            Ext.Ajax.request({
                                url:'/encoder/encoderTask!startTask.action?' + objs,
                                method:'POST',
                                callback:function (opt, success, response) {
                                    if (success) {
                                        var serverResult = Ext.util.JSON.decode(response.responseText);
                                        if (serverResult.success) {
                                            Ext.MessageBox.alert('处理完毕', '服务器返回信息：<br/>' + serverResult.msg);
                                        } else {
                                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：<br/>' + serverResult.error);
                                        }
                                    } else {
                                        Ext.MessageBox.alert('发生异常', '可能是服务器无法连接，或者获取数据超时！');
                                    }
                                    winEncoder.close();
                                }
                            });
                        }
                    });

                }
                },
                {
                    text:'关闭', handler:function () {
                    winEncoder.close();
                }
                }
            ]
        });
        var winEncoder = new Ext.Window({
            title:'选择转码配置' + clipId,
            width:600,
            height:400,
            items:[
                taskGrid
            ]
        });
        taskStore.load({limit:200});
        winEncoder.show();
    };
    this.viewPhoto = function (url) {
        var winPhoto = new Ext.Window({
            title:"显示图片",
            width:300,
            height:300,
            closeAction:"close",
            closable:true
        });
        winPhoto.html = '<img src=' + url + ' border="0"/>';
        winPhoto.show();
    };
    this.openPlayer = function (htmlContent) {
        var winMedia = new Ext.Window({
            title:"视频播放",
            width:500,
            height:420,
            closeAction:"close",
            closable:true,
            listeners:{
                "close":function () {
                }
            },
            html:htmlContent
        });
        winMedia.show();
    };
    this.viewFlash = function (url) {
        var deviceId = 1;
        clipWidth = 485;
        clipHeight = 390;
        openPlayer(getFlvStr(deviceId, url, id));
    };
    this.viewWmv = function (url) {

        var remoteRequestStore = new Ext.data.JsonStore({
            method:'POST',
            url:"/system/device!getContentRegUrl.action"
        });
        remoteRequestStore.reload({
            params:{contentId:id, url:url },
            callback:function (records, options, success) {
                var returnData = this.reader.jsonData;
                if (returnData.success) {
                    url = returnData.msg;
                    var htmlContent =
                            '<object id="program" codeBase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2"' +
                                    '          type="application/x-oleobject"                                                      ' +
                                    '          width="486" height="388"                                                            ' +
                                    '          standby="Loading Microsoft Windows Media Player components..."                      ' +
                                    '          classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">                               ' +
                                    '          <PARAM NAME="uiMode" VALUE="full"/>                                                           ' +
                                    '          <PARAM NAME="AUTOSIZE" VALUE="true"/>                                                         ' +
                                    '          <PARAM NAME="stretchToFit" VALUE="true"/>                                                    ' +
                                    '          <PARAM NAME="url" VALUE="' + url + '"/>     ';
                    openPlayer(htmlContent);
                } else {
                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                }

            }
        });

    }

});
initDictStores();
</script>
</head>
<body>
<table align="center" width="760">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>