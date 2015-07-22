/**
 *   广告组的一些处理
 *
 *
 */
var groupWin=null;
var groupPanel=null;
var showWin = null;
var showPanel = null;
var showInfoButtons = [{text:'保存字幕',handler:saveFormAjax,action:'save'},{text:'关闭窗口',handler:closeShowWin,action:'view'}];
showInfoButtons = checkFunctions(showInfoButtons);

function displayShowName(val,meta,rec){
    var onClickStr = "onclick='displayShowInfo("+rec.get("show.id")+")'";
   return "<a href='#' "+onClickStr+">"+val+"</a>";
}
function displayResourceFileName(val,meta,rec){
    if(meta&&rec){
        
    }
    return val;
}
function displayTime(val){
    return formatTime(val);
}
function displayShowInfo(showId){
    var pictureOfShowStore = new FortuneSearchStore({
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        fields:['name','size','modifyDate','length','directory'],
        proxy:new Ext.data.HttpProxy({method: 'GET',url: '../show/show!listResources.action?obj.id='+showId+'&resType=1'})
    });
    var videoOfShowStore = new FortuneSearchStore({
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        fields:['name','size','modifyDate','length','directory'],
        proxy:new Ext.data.HttpProxy({method: 'GET',url: '../show/show!listResources.action?obj.id='+showId+'&resType=2'})
    });
    var showViewWidth = 610;

    showPanel = new Ext.FormPanel({
        width:showViewWidth-2,
        height:320,
        defaultLabelAlign:'right',
        id:'BaseViewForm338547183092',
        layout:'table',
        saveUrl:'../show/text!saveText.action?lssTextDate='+checkGroupDate,
        url:'../show/text!saveText.action?lssTextDate='+checkGroupDate,
        layoutConfig:{columns:2},
        //autoHeight:true,
        items:[
            {
                title:'图片资源',
                xtype:'grid',
                width:305,
                store: pictureOfShowStore,
                height:200,
                cm:new Ext.grid.ColumnModel([
                    {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}   ,
                    {
                        header: "图片文件名",
                        hidden:false,
                        align:'left',
                        width: 180,
                        sortable: true,
                        renderer:displayResourceFileName,
                        dataIndex: 'name'
                    }, {
                        header: "时长",
                        hidden:false,
                        align:'left',
                        width: 60,
                        sortable: true,
                        dataIndex: 'length'
                    },
                    {
                        header: "文件大小",
                        hidden:false,
                        align:'left',
                        width: 60,
                        sortable: true,
                        renderer:displayFileSize,
                        dataIndex: 'size'
                    }
                ]),
                id:'showPictureListGridCom',
                name:'showPictureListGridCom'
            },{
                title:'视频资源',
                xtype:'grid',
                width:305,
                store: videoOfShowStore,
                height:200,
                cm:new Ext.grid.ColumnModel([
                    {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}   ,
                    {
                        header: "视频文件名",
                        hidden:false,
                        align:'left',
                        width: 180,
                        sortable: true,
                        renderer:displayResourceFileName,
                        dataIndex: 'name'
                    }, {
                        header: "时长",
                        hidden:false,
                        align:'left',
                        width: 60,
                        sortable: true,
                        renderer:displayTime,
                        dataIndex: 'length'
                    },
                    {
                        header: "文件大小",
                        hidden:false,
                        align:'left',
                        width: 60,
                        sortable: true,
                        renderer:displayFileSize,
                        dataIndex: 'size'
                    }
                ]),
                id:'showVideoListGridCom',
                name:'showVideoListGridCom'
            },
            {
                title:'广告基本信息',
                height:120,
                width:600,
                colspan:2,
                layout:'column',
                items:[
                    {
                        columnWidth:.35,
                        labelAlign:'right',
                        labelWidth:60,
                        border:false,
                        layout:'form',
                        items:[
                            {
                                fieldLabel:'广告名称',
                                xtype:'label',
                                id:'show.name'
                            },
                            {
                                fieldLabel:'上线时间',
                                xtype:'label',
                                id:'show.onlineTime'
                            },{
                                fieldLabel:'下线时间',
                                xtype:'label',
                                id:'show.offlineTime'
                            }
                        ]
                    },{
                        columnWidth:.65,
                        labelAlign:'right',
                        labelWidth:60,
                        border:false,
                        layout:'form',
                        xtype:'fieldset',
                        defaults: {width: 300},
                        defaultType: 'textfield',
                        items:[
                            {
                                fieldLabel:'字幕标题',
                                name:'obj.title',
                                readOnly:viewReadOnly,
                                id:'text.title'
                            },
                            {
                                fieldLabel:'字幕信息',
                                xtype:'textarea',
                                height:70,
                                readOnly:viewReadOnly,
                                name:'obj.texts',
                                id:'text.texts'
                            },
                            {
                                fieldLabel:'字幕ID',
                                inputType:'hidden',
                                name:'obj.id',
                                id:'text.id'
                            },
                            {
                                fieldLabel:'区域序号',
                                inputType:'hidden',
                                name:'obj.areaId',
                                id:'text.areaId'
                            },
                            {
                                fieldLabel:'起始时间',
                                inputType:'hidden',
                                name:'obj.startDate',
                                id:'text.startDate'
                            },
                            {
                                fieldLabel:'截至时间',
                                inputType:'hidden',
                                name:'obj.stopDate',
                                id:'text.stopDate'
                            },
                            {
                                fieldLabel:'相关广告',
                                inputType:'hidden',
                                name:'obj.showId',
                                id:'text.showId'
                            }
                        ]

                    }
                ]

            }
        ]
    }) ;
    showWin = new Ext.Window({
        width:showViewWidth,
        height:400,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'察看'+checkGroupDate+'针对此广告的字幕',
        autoHeight:true,
        items: showPanel,
        buttons:showInfoButtons
    });
    showWin.show();
    loadShowInfo(showId);
    pictureOfShowStore.load({limit:10000});
    videoOfShowStore.load({limit:10000});
}

function saveText(){
    
}
function closeShowWin(){
   showWin.close();
}
function closeGroupsWin(){
   groupWin.close();
}

function loadGroupsInfo(groupsId){
    Ext.Ajax.request({
        url:"../show/groups!view.action?keyId=" +groupsId,
        callback : function(opt, success, response) {
            if (success) {
                var serverData = Ext.util.JSON.decode(response.responseText);
                if (serverData) {
                    var data = serverData.data;
                    if(data){
                        setLabelText("groupsName",data["obj.name"]);
                        setLabelText("groupsStartTime",data["obj.onlineDate"]);
                        setLabelText("groupsStopTime",data["obj.offlineDate"]);
                    }
                } else {
                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                }
            }
        }
    });
}
function loadShowInfo(showId){
    Ext.Ajax.request({
        url:"../show/text!viewText.action?obj.showId=" +showId+"&lssTextDate="+checkGroupDate,
        callback : function(opt, success, response) {
            if (success) {
                try {
                    var serverData = Ext.util.JSON.decode(response.responseText);
                    if (serverData) {
                        if (serverData.success) {
                            var data = serverData.data;
                            if (data) {
                                showPanel.getForm().setValues(data);
                            }
                            setLabelText("show.name", serverData.show.name);
                            setLabelText("show.onlineTime", serverData.show.onlineTime);
                            setLabelText("show.offlineTime", serverData.show.offlineTime);
                        } else {
                            Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverData.error)
                        }
                    } else {
                        Ext.MessageBox.alert('发生异常', '无法对返回数据进行解析：' + response.responseText)
                    }
                } catch(e) {
                    Ext.MessageBox.alert('发生异常', '无法对返回数据进行解析：' + e.description)
                }
            }
        }
    });
}
var checkGroupDate ;
function displayGroupInfo(groupId,displayGroupDate){
    checkGroupDate = displayGroupDate;
    var showOfGroupStore = new FortuneSearchStore({
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        fields:['id','show.id','name','length','groupsId','onlineTime'],
        proxy:new Ext.data.HttpProxy({method: 'GET',url: '../show/showGroups!list.action?sort=orderId&dir=asc&obj.groupId=' + groupId})
    });
    var groupViewWidth = 610;

    groupPanel = new Ext.Panel({
        width:groupViewWidth-2,
        height:300,
        layout:'table',
        layoutConfig:{columns:2},
        //autoHeight:true,
        items:[
            {
                title:'时间计划信息',
                height:300,
                width:200,
                labelWidth:60,
                labelAlign:'right',
                layout:'form',
                items:[
                    {
                        fieldLabel:'计划名称',
                        xtype:'label',
                        id:'groupsName'
                    },
                    {
                        fieldLabel:'起始时间',
                        xtype:'label',
                        id:'groupsStartTime'
                    },
                    {
                        fieldLabel:'截至时间',
                        xtype:'label',
                        id:'groupsStopTime'
                    }
                ]

            },
            {
                title:'包含的广告',
                xtype:'grid',
                width:390,
                store: showOfGroupStore,
                height:300,
                cm:new Ext.grid.ColumnModel([
                    {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}   ,
                    {
                        header: "广告名称",
                        hidden:false,
                        align:'left',
                        width: 100,
                        sortable: true,
                        renderer:displayShowName,
                        dataIndex: 'name'
                    }, {
                        header: "时长",
                        hidden:false,
                        align:'left',
                        width: 80,
                        sortable: true,
                        dataIndex: 'length'
                    },
                    {
                        header: "上线日期",
                        hidden:false,
                        align:'left',
                        width: 120,
                        sortable: true,
                        dataIndex: 'onlineTime'
                    }
                ]),
                id:'showListGridCom',
                name:'showListGridCom'
            }
        ]
    }) ;
    groupWin = new Ext.Window({
        width:groupViewWidth,
        height:400,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'察看'+checkGroupDate+'推广计划信息',
        autoHeight:true,
        items: groupPanel,
        buttons:[{text:'关闭',handler:closeGroupsWin}]
    });
    groupWin.show();
    loadGroupsInfo(groupId);
    showOfGroupStore.load({limit:10000});
}
