<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "cacheMonitor";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title>监控页面</title>
<%@include file="../inc/jsCssLib.jsp"%>
<%@include file="../inc/extBase.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
nextUrl = "cacheMonitorList.jsp";
actionHeader = "cacheMonitor";
var visitsStore = new Ext.data.JsonStore({
    fields:[{id:1,name:'time',type:'string'},{id:2,name:'count',type:'int'},{id:3,name:'dateTime',type:'date',
                   dateFormat:'Y-m-d H:i:s'}],
    root: 'visitLine',totalProperty: 'totalCount',
    proxy: new Ext.data.HttpProxy({method: 'POST',url: 'visitLineJson.jsp'})
});
var startId = 0;
checkAllFunctions();  // 检查用户权限
function reloadVisits(){
    var startTime = getCmpValue('startTime');
    if(startTime == null){
       startTime = "";
    }else{
        //startTime +=" 00:00:00";
    }
    if(typeof(startTime)=="object"){
        startTime = startTime.format("Y-m-d")+" 00:00:00";
    }
    var stopTime = getCmpValue('stopTime');
    if(stopTime == null){
        stopTime = "";
    }else{
        //stopTime +=" 23:59:59";
    }
    if(typeof(stopTime)=="object"){
        stopTime = stopTime.format("Y-m-d")+" 23:59:59";
    }
    visitsStore.baseParams["startTime"]=startTime;
    visitsStore.baseParams["stopTime"]=stopTime;
    startId = 0;
    visitsStore.load();
}
function initDisplay(){
    var panelWidth=650;
    var monitorPanel = new Ext.Panel({
        id:'BaseMonitor338547183092',
        title:'并发曲线',
        border:true,
        width:panelWidth,
        buttons:defaultViewFormButtons,
        tbar:[{xtype:'label',text:'起始日期'},{
            name:'startTime',
            id:'startTime',
            width:100,
            value:'2012-03-10',
            xtype:'datefield',
            format:'Y-m-d'
        },{xtype:'label',text:'截至日期'},{
            name:'stopTime',
            id:'stopTime',
            width:100,
            value:'2012-03-10',
            xtype:'datefield',
            format:'Y-m-d'
        },{text:'统计',handler:reloadVisits}],
        items:[{
                iconCls:'chart',
                //title: '并发数据',
                frame:true,
                id:'lineCharCmp',
                width:panelWidth-2,
                height:400,
                layout:'fit',

                items: {
                    xtype: 'linechart',
                    store: visitsStore,
                    url: '../resources/charts.swf',
                    xField: 'dateTime',
                    yField: 'count',
                    yAxis: new Ext.chart.NumericAxis({
                        displayName: '并发',
                        labelRenderer : Ext.util.Format.numberRenderer('0,0')
                    }),
///*
                    xAxis: new Ext.chart.TimeAxis({
                        //displayName: '时间',
                        labelRenderer :function (dateObj,v2,v3,v4) {
                            return dateObj.format("d日H点");
/*
                            try {
                                startId ++;
                                if(startId%12==0){
                                    return dateObj;
                                }
                                return "";
                            } catch (e) {
                                return dateObj;
                            }
*/
                        }
                    }),
//*/
                    tipRenderer : function(chart, record){
                            return  record.data.dateTime.format('Y-m-d H:i:s')+"有"+ record.data.count + '次并发';
                    },
                    chartStyle: {
                        padding: 10,
                        animationEnabled: true,
                        legend:{
                            display: "bottom"
                        },
                        background:{
                            color:0xF0F0F0
                        },
                        font: {
                            name: 'Tahoma',
                            color: 0x444444,
                            size: 11
                        },
                        dataTip: {
                            padding: 5,
                            border: {
                                color: 0x99bbe8,
                                size:1
                            },
                            background: {
                                color: 0xDAE7F6,
                                alpha: .9
                            },
                            font: {
                                name: 'Tahoma',
                                color: 0x15428B,
                                size: 10,
                                bold: true
                            }
                        },
                        xAxis: {
                            color: 0x69aBc8,
                            majorTicks: {color: 0x69aBc8, length: 50},
                            minorTicks: {color: 0x69aBc8, length: 40},
                            majorGridLines: {size: 1, color: 0xeeeeee}
                        },
                        yAxis: {
                            color: 0x69aBc8,
                            majorTicks: {color: 0x69aBc8, length: 1},
                            minorTicks: {color: 0x69aBc8, length: 1},
                            majorGridLines: {size: 1, color: 0xdfe8f6}
                        }
                    },
                    series:[
                        {
                            type: 'line',
                            displayName: '并发数据曲线',
                            yField: 'count',
                            width:1,
                            style: {
                                //image:'bar.gif',
                                mode: 'solid',
                                size:1,
                                //lineColor:0x79839B,
                                borderColor:0x79839B,
                                //fillColor:0x79839B,
                                color:0x00FF00
                            }
                        }

                    ]
                }
            }
        ]
    });
    //loadFormAjax();
    monitorPanel.render("displayDiv");
    reloadVisits();
}
Ext.onReady(function(){
    Ext.QuickTips.init();
    initDisplay();
});
</script>
</head>
<body>
<table class="mainListTable">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>