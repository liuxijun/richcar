<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
  //   needPermissions(actionHeader,"searchAll","channelSearchAll");
        needPermissions(actionHeader,"bingfaLogs","systemBingFa");
   ///     needPermissions(actionHeader,"list","cspcspList");
     //   needPermissions(actionHeader,"list","cspSearch");
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date nowDate = StringUtils.string2date(sdf.format(new Date()));
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript" src="/ext3.4/examples/chart/charts.js"></script>
<script language="javascript">
Ext.onReady(function() {
    var pageSize = 12000000;
    var tableWidth = 850;
    var tableHeight = 600;
    var nowTime = <%=nowDate.getTime()%>;
    var curTime = '<%=StringUtils.date2string(new Date(),"yyyy-MM-dd")%>';

    var actionUrl = "/log/visitLog";

    var searchForm = new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 100,
        //labelWidth: 80,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
        baseCls: 'x-plain',


        items: [
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:815,
                height:90,
                items:[
                    {
                        //          width:600,
                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:4},
                        items: [
                            /*   {
                             //    width:50,
                             baseCls: 'x-plain',
                             layout: 'form',
                             items: [
                             {
                             xtype: 'label',
                             labelSeparator : '',
                             //                      labelWidth: 60,
                             fieldLabel: '搜索栏:'

                             }
                             ]
                             }
                             ,*/
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 45,
                                items: [
                                    {
                                        hiddenName:'vl_spId',
                                        xtype: 'combo',
                                        labelWidth:60,
                                        fieldLabel: '　　SP',
                                        width:135,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store: new Ext.data.ArrayStore({
                                            fields: ["id",'name'],
                                            data:[]
                                        }),
                                        valueField: 'id',
                                        displayField: 'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable: false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection: true,
                                        typeAhead: false,
                                        //allowBlank:false,
                                        listeners:{
                                            select: function(combo, record, index) {
                                                var spId = this.getValue();
                                                var datas = [];
                                                for (var i = 0; i < cspStore.getCount(); i++) {
                                                    var storeRecord = cspStore.getAt(i);
                                                    if (storeRecord.data.isCp == 1) {
                                                        var cpId = storeRecord.data.id;
                                                        if (cpId == spId) {
                                                            datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
                                                            continue;
                                                        }
                                                        for (var j = 0; j < cspCspStore.getCount(); j++) {
                                                            var cspCspStoreRecord = cspCspStore.getAt(j);
                                                            if (cspCspStoreRecord.data.masterCspId == spId && cspCspStoreRecord.data.cspId == cpId) {
                                                                datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
                                                            }
                                                        }
                                                    }
                                                }
                                                var obj = searchForm.getForm().findField('vl_cpId');
                                                obj.clearValue();
                                                obj.store.loadData(datas);

                                                datas = [];
                                                for (var i = 0; i < channelStore.getCount(); i++) {
                                                    var storeRecord = channelStore.getAt(i);
                                                    if (storeRecord.data.cspId == spId) {
                                                        datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
                                                    }
                                                }
                                                obj = searchForm.getForm().findField('vl_channelId');
                                                obj.clearValue();
                                                obj.store.loadData(datas);

                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 78,
                                items: [
                                    {
                                        hiddenName:'vl_channelId',
                                        xtype: 'combo',
                                        labelWidth: 70,
                                        fieldLabel: '　　　SP频道',
                                        width:140,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store: new Ext.data.ArrayStore({
                                            fields: ["id",'name'],
                                            data:[]
                                        }),
                                        valueField: 'id',
                                        displayField: 'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable: false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection: true,
                                        typeAhead: false,
                                        //allowBlank:false,
                                        listeners:{
                                            select: function(combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 95,
                                items: [
                                    {
                                        id:'startDate',
                                        name:'startDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 00:00:00',
                                        labelWidth: 60,
                                        fieldLabel: '　　　开始时间',
                                        width:150

                                    }
                                ]
                            },
                            {
                                baseCls: 'x-btn-over',
                                layout:'column',
                                items:[
                                    {
                                        text: '查询',
                                        xtype:'button',
                                        minWidth:60,
                                        listeners:{
                                            "click":function()
                                            {
                                                loadData();
                                            }
                                        }
                                    }
                                ]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 45,
                                items: [
                                    {
                                        hiddenName:'vl_cpId',
                                        xtype: 'combo',
                                        labelWidth: 60,
                                        fieldLabel: '　　CP',
                                        width:135,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store: new Ext.data.ArrayStore({
                                            fields: ["id",'name'],
                                            data:[]
                                        }),
                                        valueField: 'id',
                                        displayField: 'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable: false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection: true,
                                        typeAhead: false,
                                        //allowBlank:false,
                                        listeners:{
                                            select: function(combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth:78,
                                items: [
                                    {
                                        name:'contentName',
                                        xtype: 'textfield',
                                        labelWidth: 70,
                                        fieldLabel: '　　资源名称',
                                        width:140
                                    }
                                ]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 95,
                                items: [
                                    {
                                        name:'endDate',
                                        id:'endDate' ,
                                        xtype: 'datefield',
                                        format:'Y-m-d 23:59:59',
                                        labelWidth: 70,
                                        fieldLabel: '　　　结束时间',
                                        width:150
                                    }
                                ]
                            },
                            {
                                baseCls: 'x-btn-over',
                                layout:"form",
                                items:[{
                                    xtype:'button',
                                    text: '导出数据',
                                    minWidth:60,
                                    handler:function(){
                                        var startTime = Ext.getCmp("startDate").value;
                                        var endTime = Ext.getCmp("endDate").value;
                                        if(startTime==undefined||endTime==undefined){
                                            Ext.MessageBox.show({
                                                title:"标题",
                                                msg:"请选择需要查询的时间段",
                                                buttons:{"ok":"OK"},
                                                width:500,
                                                icon:Ext.MessageBox.INFO,
                                                closable:false
                                            });
                                            return ;
                                        }
                                        var form = searchForm.getForm().getEl().dom;
                                        form.action = '/log/visitLog!bingfaLogs.action?excel=isTrue';
                                        form.method = 'POST';
                                        form.target = "_blank" ;
                                        form.submit();
                                    }
                                }]
                            }
                        ]
                    }
                ]})
        ]
    });

    //刷新列表
    this.loadData = function() {
        var endDate =  getCmpValue("endDate");
        var startDate = getCmpValue("startDate");
        if(""!=endDate&&""!=startDate){
            if(endDate.getTime()>nowTime) {
                Ext.Msg.alert("提示","结束日期大于当前日期，请检查！");
            }else{
                var form1 = searchForm.getForm();
                dataListStore.removeAll();
                dataListStore.baseParams = form1.getValues();
                dataListStore.reload({params: {start:0,limit:pageSize}});
            }
        }
//        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!bingfaLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'time'
            },
            {
                name:'value'
            }
        ]
        ,
        listeners:{
            load: function(store) {

                Ext.getCmp("linechart").setYAxis(new Ext.chart.NumericAxis({
                    title : ' ',
                    //length:6,
                    maximum : this.reader.jsonData.totalCount
                    //maximum:1000
                }));

            }
        }
    });

    var cspStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/csp/csp!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            },
            {
                name:'isCp'
            },
            {
                name:'isSp'
            }
        ]
    });
    cspStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });
    var cspCspStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/csp/cspCsp!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'masterCspId'
            },
            {
                name:'cspId'
            }
        ]
    });
    cspCspStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });

    var channelStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/publish/channel!searchAll.action?obj.type=1",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            },
            {
                name:'cspId'
            }
        ]
    });
    channelStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });
    var sequence = 0;

    function sequenceDo() {
        sequence++;
        if (sequence == 3) {
            //dataListStore.load({params: {start:0,limit:pageSize}});
            initSpCombo();
            initCpCombo();
            loadData();
        }
    }

    function initSpCombo() {
        var datas = [];
        for (var i = 0; i < cspStore.getCount(); i++) {
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isSp == 1) {
                datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('vl_spId');
        obj.clearValue();
        obj.store.loadData(datas);
    }

    function initCpCombo() {
        var datas = [];
        for (var i = 0; i < cspStore.getCount(); i++) {
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isCp == 1) {
                datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('vl_cpId');
        obj.clearValue();
        obj.store.loadData(datas);
    }
    var listGrid =
            new Ext.Panel({
                style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                id:'searchForm',
                //        width: '100%',
                title:"点播日志",
                width:tableWidth,
                height:tableHeight + 40,
                labelWidth: 80,
                frame:true,
                layout:'table',
                layoutConfig: {columns:1},
                //baseCls: 'x-plain',

                items: [
                    searchForm,

                    new Ext.Panel({
                        width:tableWidth - 12,
                        height:tableHeight - 102,
                        layout:'fit',
                        frame:true,
                        style: {'margin-left':'0px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                        items: [
                            {
                                id:'linechart',
                                xtype: 'linechart',
                                store: dataListStore,
                                xField: 'time',
                                yField: 'value',
                                xAxis : new Ext.chart.CategoryAxis({
                                    //title : 'Month'
                                }),
                                yAxis : new Ext.chart.NumericAxis({
                                    //title : 'Hits',
                                    //minimum : 1,
                                    maximum : 1000
                                }),
                                chartStyle : {
                                    animationEnabled : true,// 是否支持动态数据变化
                                    dataTip : { // 鼠标经过,数据提示样式
                                        padding : 4,// 提示内容与边框的距离
                                        border : {
                                            color : 0x69aBc8,
                                            size : 1
                                        },
                                        background : {
                                            color : 0xDAE7F6,// 背景颜色透明度
                                            alpha : .9
                                            // 背景颜色透明度
                                        },
                                        font : {
                                            name : '黑体',
                                            color : '#FF3300',
                                            size : 12
                                            //bold : true
                                        }
                                    }
                                    //                                    majorTicks : {// 大刻度值
                                    //                                        color : 0x69aBc8,
                                    //                                        length : 4
                                    //                                    },
                                    //                                    minorTicks : {// 小刻度值
                                    //                                        color : 0x69aBc8,
                                    //                                        length : 4
                                    //                                    }

                                },
                                listeners: {
                                    itemclick: function(o) {
                                        var rec = store.getAt(o.index);
                                        Ext.example.msg('Item Selected', 'You chose {0}.', rec.get('value'));
                                    }
                                },

                                series : [
                                    {
                                        type : 'line',
                                        // line
                                        displayName : '点击数',
                                        yField : 'value',
                                        style : {
                                            color : '#6666CC',
                                            size : 6,
                                            lineSize : 2
                                            //color : 0x69aBc8,
                                            //                                               majorTicks : {
                                            //                                                   color : 0x69aBc8,
                                            //                                                   length : 4
                                            //                                               },
                                            //                                               minorTicks : {
                                            //                                                   color : 0x69aBc8,
                                            //                                                   length : 4
                                            //                                               }
                                            //                                               majorGridLines : {
                                            //                                                   size : 1,
                                            //                                                   color : 0xdfe8f6
                                            //                                               },

                                        }
                                    }
                                ]

                            }
                        ]
                    })
                ]
            });

    listGrid.render('display');
    //listGrid.render(Ext.getBody());

})
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