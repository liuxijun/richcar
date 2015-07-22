<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>转码管理</title>
<%@include file="../inc/jsCssLib.jsp"%>
<%@include file="../inc/extBase.jsp"%>
<script type="text/javascript">

Ext.onReady(function() {
    Ext.QuickTips.init();

    var pageSize = 20;
    var columnWidth = 160;

    function loadData() {
        //dataListStore.baseParams["obj.templateName"] = Ext.getCmp("obj.templateName").getValue();
        dataListStore.setDefaultSort('id','desc');
        dataListStore.load({params:{start:0,limit:pageSize}});
    }

    var propertyStore = new Ext.data.JsonStore({
        method:'post',
        url:'/module/property!getPropertyIdsByDataType.action',
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
    propertyStore.load({
        callback :
                function(records, options, success) {
                }
    });

   var dataListStore = new Ext.data.JsonStore({
      method:'post',
      url:'/encoder/encoderTemplate!list.action',
      totalProperty:'totalCount',
      root:'objs',
      fields:[
          {name:'id'},
          {name:'templateName'}
      ]
   });
  dataListStore.setDefaultSort('id','desc');
  dataListStore.load({params:{start:0,limit:pageSize}});


   this.viewObj = function(val) {
       Ext.Ajax.request({
           url:"/encoder/encoderTemplate!view.action?keyId="+val,
           callback : function(opt, success, response) {
               var mediaIndexInfo = Ext.util.JSON.decode(response.responseText);
               if (mediaIndexInfo.success) {
                   viewForm.getForm().setValues(mediaIndexInfo.data);
               }
           }
       });
   };

   this.deleteTemplate = function(val) {
       Ext.MessageBox.confirm("提示", "确认删除记录吗？", function(btn) {
           if (btn == "yes") {
               var remoteRequestStore = new Ext.data.JsonStore({
                   method:'POST',
                   url:"/encoder/encoderTemplate!delete.action"
               });
               remoteRequestStore.reload({
                   params:{keyId:val},
                   callback :
                           function(records, options, success) {
                               var returnData = this.reader.jsonData;
                               if (returnData.success) {
                                   //刷新列表
                                   loadData();
                                   Ext.Msg.alert("提示", "删除成功！");
                               } else {
                                   Ext.Msg.alert('发生异常,服务器返回错误信息：' + returnData.error,Ext.MessageBox.ERROR);
                               }
                           }
               });
           }
       });
   };

    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });

    var  listGrid = new Ext.grid.GridPanel({
        width:265,
        height:515,
        store: dataListStore,
        style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        loadMask:{msg:'正在加载数据，请稍侯……'},
//        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },
        sm:checkSelect,
        columns: [
            checkSelect,
            {
                header: "模版名称",
                dataIndex: 'templateName',
                width: 150,
                sortable: true,
                align:'center'
            },
            {
                header: "操作",
                width: 90,
                align:'center',
                sortable: false,
                dataIndex: 'id',
                renderer:   function (val, p, row) {
                    //分成多段赋值，就是为了去掉iDea的警告信息
                    var viewScript = "javascript:viewObj("+ val + ")";
                    var view = '<a href="'+viewScript+'">查看</a>        ';
                    var delScript = 'javascript:deleteTemplate('+ val +')';
                    var del = '<a href="'+delScript+'">删除</a> ';
                    return view+del;
                }
            }
        ],
        tbar:new Ext.Toolbar({
            items : [
                {xtype : "tbfill"},
               /* {text:'模版名称'},
                {xtype:'textfield',
                    id:'obj.templateName',
                    name:'obj.templateName',
                    width:180
                },
                {
                    text:'搜索',
                    handler:loadData
                }*/
                {
                    text:'添加模版',
                    handler:function() {
                        viewForm.getForm().reset();
                    }
                },
                {
                    text:'删除所选',
                    handler:function() {
                        var row = listGrid.getSelectionModel().getSelections();
                        if (row.length == 0) {
                            Ext.Msg.alert("提示", "未选择记录！");
                            return;
                        }

                        var keyIds = "";
                        for (var i = 0; i < row.length; i++) {
                            keyIds += row[i].get('id') + ",";
                        }
                        keyIds = keyIds.substr(0, keyIds.length - 1);

                        Ext.MessageBox.confirm("提示", "确认删除所选的记录吗？ ", function(btn) {
                            if (btn == "yes") {
                                var remoteRequestStore = new Ext.data.JsonStore({
                                    method:'POST',
                                    url:"/encoder/encoderTemplate!deleteSelected.action"
                                });
                                remoteRequestStore.reload({
                                    params:{keyIds:keyIds},
                                    callback :
                                            function(records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    loadData();
                                                    //showMessages('删除成功！',Ext.MessageBox.INFO);
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                                }
                                            }
                                });
                            }
                        })
                    }
                }
            ]
        }),
        bbar:new Ext.PagingToolbar({
            pageSize: pageSize,
            store: dataListStore,
            displayInfo: true,
            displayMsg: '结果数据 {0} - {1} of {2}',
            emptyMsg: "没有数据"
        })
    });


    var viewForm = new Ext.FormPanel({
//        style: {'margin-left':'15px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'viewForm',
        title:"转码管理",
        width:530,
        height:515,
        layout:'form',
        labelWidth:70,
        //baseCls: 'x-plain',
        waitMsgTarget: true,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        //fileUpload:true,
        items:[
            {
                name:'obj.id',
                xtype:'hidden'
            },
            {
                name:'obj.distribute',
                xtype:'hidden'
            },
            {
                id:'indexFrom',
                //title:"视频编码器",
                width:646,
                height:100,
                //frame:true,
                layout:'table',
                layoutConfig:{columns:2},
                baseCls: 'x-plain',
                waitMsgTarget: true,
                loadMask:{msg:'正在加载数据，请稍侯……'},
                fileUpload:true,
                items:[
                    {
                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                        baseCls: 'x-plain',
                        items: {
                            baseCls: 'x-plain',
                            layout: 'form',
                            items: [
                                {
                                    style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                    baseCls: 'x-plain',
                                    height:30,
                                    items: {
                                        baseCls: 'x-plain',
                                        layout: 'form',
                                        items: [
                                            {
                                                name:'obj.templateName',
                                                id:'templateName',
                                                xtype: 'textfield',
                                                fieldLabel: '模版名称',
                                                width:columnWidth,
                                                allowBlank:false
                                            }
                                        ]
                                    }
                                },
                                {
                                    style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                    baseCls: 'x-plain',
                                    items: {
                                        baseCls: 'x-plain',
                                        layout: 'form',
                                        items: [
                                            {
                                                name:'obj.templateCode',
                                                xtype: 'textfield',
                                                fieldLabel: '模版代码',
                                                width:columnWidth,
                                                allowBlank:false
                                            }
                                        ]
                                    }
                                }
                            ]
                        }
                    },
                    {
                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                        baseCls: 'x-plain',
                        items: {
                            baseCls: 'x-plain',
                            layout: 'form',
                            items: [
                                {
                                    style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                    baseCls: 'x-plain',
                                    height:30,
                                    items: {
                                        baseCls: 'x-plain',
                                        layout: 'form',
                                        items: [
                                            {
                                                hiddenName:'obj.fileFormat',
                                                xtype:'combo',
                                                fieldLabel:'文件格式',
                                                width:columnWidth,
                                                store: new Ext.data.ArrayStore({
                                                    fields: ['value', 'display'],
                                                    data: [
                                                        ['mp4', 'MP4'],
                                                        ['m3u8', 'M3U8']/*,
                                                        ['wmv', 'WMV']*/
                                                    ]
                                                }),
                                                valueField:'value',
                                                displayField:'display',
                                                mode:'local',
                                                triggerAction:'all',
                                                loadingText:'加载中...',
                                                emptyText:'请选择...',
                                                selectOnFocus:true,
                                                editable: false,
                                                typeAheadDelay:1000,
                                                //pageSize:5,
                                                forceSection: true,
                                                typeAhead: false,
                                                listeners:{

                                                }
                                            }
                                        ]
                                    }

                                },
                                {
                                    style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                    baseCls: 'x-plain',
                                    items: {
                                        baseCls: 'x-plain',
                                        layout: 'form',
                                        items: [
                                            {
                                                hiddenName:'obj.propertyId',
                                                xtype:'combo',
                                                fieldLabel:'对应模版',
                                                width:columnWidth,
                                                store:propertyStore,
                                                valueField:'id',
                                                displayField:'name',
                                                mode:'local',
                                                triggerAction:'all',
                                                loadingText:'加载中...',
                                                emptyText:'请选择...',
                                                selectOnFocus:true,
                                                editable: false,
                                                typeAheadDelay:1000,
                                                //pageSize:5,
                                                forceSection: true,
                                                typeAhead: false,
                                                listeners:{

                                                }
                                            }
                                        ]
                                    }

                                }
                            ]
                        }
                    }
                ]
            },
            {
                title:"视频编码器",
                colspan:2,
                items:{
                    id:'videoFrom',
                    //title:"视频编码器",
                    width:646,
                    height:160,
                    //frame:true,
                    layout:'table',
                    layoutConfig:{columns:2},
                    baseCls: 'x-plain',
                    waitMsgTarget: true,
                    loadMask:{msg:'正在加载数据，请稍侯……'},
                    fileUpload:true,
                    items:[
                        {
                            style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                            baseCls: 'x-plain',
                            items: {
                                baseCls: 'x-plain',
                                layout: 'form',
                                items: [
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VEncoderType',
                                                    xtype:'combo',
                                                    fieldLabel:'编码类型',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['x264', 'h.264'],['mpeg4', 'mpeg4'],['wmv','wmv']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VBitrate',
                                                    xtype:'combo',
                                                    fieldLabel:'比特率',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['128', '128kbps'],['256', '256kbps'],['374', '374kbps'],['758', '758kbps'],['1024', '1024kbps'],
                                                            ['1500', '1500kbps'],['2048', '2048kbps']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VFrameRate',
                                                    xtype:'combo',
                                                    fieldLabel:'帧率',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['10', '10'],['15', '15'],['20', '20'],['25', '25'],['30', '30']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VWidth',
                                                    xtype:'combo',
                                                    fieldLabel:'宽',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['172', '172'],['320', '320'],['640', '640'],['720', '720'],['1280', '1280'],['1920', '1920']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    value:'640',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                     }

                                ]
                            }
                        },
                        {
                            style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                            baseCls: 'x-plain',
                            items: {
                                baseCls: 'x-plain',
                                layout: 'form',
                                items: [
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VKeyframeInterval',
                                                    xtype:'combo',
                                                    fieldLabel:'关键帧间隔',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['1', '1'],['2', '2'],['5', '5']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VFixedQp',
                                                    xtype:'combo',
                                                    fieldLabel:'固定质量',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['1', '1'],['2', '2'],['3', '3'],['4', '4'],['5', '5'],['6', '6'],['7', '7']
                                                            ,['8', '8'],['9', '9'],['10', '10'],['11', '11'],['12', '12'],['13', '13'],['14', '14']
                                                            ,['15', '15'],['16', '16'],['17', '17'],['18', '18'],['19', '19'],['20', '20'],['21', '21']
                                                            ,['22', '22'],['23', '23'],['24', '24'],['25', '25'],['26', '26'],['27', '27'],['28', '28']
                                                            ,['29', '29'],['30', '30'],['31', '31'],['32', '32']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VMaxQp',
                                                    xtype:'combo',
                                                    fieldLabel:'最大质量',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['1', '1'],['2', '2'],['3', '3'],['4', '4'],['5', '5'],['6', '6'],['7', '7']
                                                            ,['8', '8'],['9', '9'],['10', '10'],['11', '11'],['12', '12'],['13', '13'],['14', '14']
                                                            ,['15', '15'],['16', '16'],['17', '17'],['18', '18'],['19', '19'],['20', '20'],['21', '21']
                                                            ,['22', '22'],['23', '23'],['24', '24'],['25', '25'],['26', '26'],['27', '27'],['28', '28']
                                                            ,['29', '29'],['30', '30'],['31', '31'],['32', '32']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.VHeight',
                                                    xtype:'combo',
                                                    fieldLabel:'高',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['144', '144'],['240', '240'],['360', '360'],['480', '480'],['540', '540'],['720', '720'],['1080', '1080']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    value:'480',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                    }
                                ]
                            }
                        }
                    ]
                }
            },
            {
                title:"音频编码器",
                colspan:2,
                items:{
                    //        style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                    id:'videoFrom',
                    //title:"视频编码器",
                    width:646,
                    height:130,
                    //frame:true,
                    layout:'table',
                    layoutConfig:{columns:2},
                    baseCls: 'x-plain',
                    waitMsgTarget: true,
                    loadMask:{msg:'正在加载数据，请稍侯……'},
                    fileUpload:true,
                    items:[
                        {
                            style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                            baseCls: 'x-plain',
                            items: {
                                baseCls: 'x-plain',
                                layout: 'form',
                                items: [
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.ACodec',
                                                    xtype:'combo',
                                                    fieldLabel:'编码器',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['faac', 'AAC'],['mp3', 'MP3']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    value:'AAC',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.ASampleRate',
                                                    xtype:'combo',
                                                    fieldLabel:'音频采样率',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['24000', '24000'],['44100', '44100'],['48000', '48000']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    value:'48000',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    /*{
                                        style: {'margin-left':'15px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [*/
                                                {
                                                    name:'obj.AType',
                                                    xtype:'hidden'
                                                  /*  name:'obj.AType',
                                                    xtype: 'textfield',
                                                    fieldLabel: '音频类型',
                                                    width:columnWidth,
                                                    vtype:'number'*/
                                                }
                                  /*          ]
                                        }

                                    }*/
                                ]
                            }
                        },
                        {
                            style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                            baseCls: 'x-plain',
                            items: {
                                baseCls: 'x-plain',
                                layout: 'form',
                                items: [
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.AChannel',
                                                    xtype:'combo',
                                                    fieldLabel:'频道',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['1', '单声道'],['2', '立体声']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    value:'2',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }

                                    },
                                    {
                                        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                                        baseCls: 'x-plain',
                                        height:30,
                                        items: {
                                            baseCls: 'x-plain',
                                            layout: 'form',
                                            items: [
                                                {
                                                    hiddenName:'obj.ABitrate',
                                                    xtype:'combo',
                                                    fieldLabel:'比特率',
                                                    width:columnWidth,
                                                    store: new Ext.data.ArrayStore({
                                                        fields: ['value', 'display'],
                                                        data: [['8', '8kbps'],['32', '32kbps'],['64', '64kbps'],['96', '96kbps'],['128', '128kbps']]
                                                    }),
                                                    valueField:'value',
                                                    displayField:'display',
                                                    mode:'local',
                                                    triggerAction:'all',
                                                    loadingText:'加载中...',
                                                    emptyText:'请选择...',
                                                    selectOnFocus:true,
                                                    editable: false,
                                                    typeAheadDelay:1000,
                                                    //pageSize:5,
                                                    forceSection: true,
                                                    typeAhead: false,
                                                    listeners:{

                                                    }
                                                }
                                            ]
                                        }
                                    }
                                ]
                            }
                        }

                    ]
                }
            },
            {
                buttons:[
                    {
                        text:'保存',
                        handler:function(){
                            if(viewForm.getForm().isValid()) {
                                viewForm.getForm().submit({
                                    url:'/encoder/encoderTemplate!save.action',
                                    method:'post',
                                    waitMsg:'正在处理数据，请稍后...',
                                    success:function(from,returnMsg) {
                                        var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                        var message = Ext.MessageBox.show({
                                            title:'提示',
                                            msg:"保存成功！",
                                            modal:false,
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.INFO
                                        });
                                        loadData()
                                    },
                                    failure:function(from,returnMsg){
                                        var message = Ext.MessageBox.show({
                                            title: '提示',
                                            msg: '操作失败，原因是:' + returnMsg.result.error,
                                            modal:false,
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.ERROR
                                        });
                                    }
                                });
                            }
                        }
                    }
                ]
            }

        ]

    });

    var mainPanel = new Ext.Panel({
        id:'mainPanel',
        //title:"视频编码器",
        width:820,
        height:550,
        //frame:true,
        layout:'table',
        layoutConfig:{columns:2},
        baseCls: 'x-plain',
        waitMsgTarget: true,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        fileUpload:true,
        items:[
            {
               items:[
                   listGrid
               ]
            },
            {
                items:[
                    viewForm
                ]
            }
        ]
    });

    mainPanel.render("display");
});

</script>
</head>
<body>
<table style="margin:0 auto" width="660" >
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>