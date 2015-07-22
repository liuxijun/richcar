<%@ page import="java.util.HashMap" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>在线用户统计（并发统计）</title>
    <%--<%@include file="../inc/jsCssLib.jsp" %>--%>
    <link rel="stylesheet" href="../ext3.4/resources/css/ext-all.css"/>
    <script type="text/javascript" src="../ext3.4/adapter/ext/ext-base-debug.js"></script>
    <script type="text/javascript" src="../ext3.4/ext-all-debug.js"></script>

    <%
        Logger logger = LoggerFactory.getLogger("com.fortune.jsp.onLineUserAnalysisReport.jsp");
        String objJson = (String) request.getAttribute("objJson");
        logger.debug(objJson.toString());
    %>
    <script type="text/javascript">
        Ext.chart.Chart.CHART_URL = '../ext3.4/resources/charts.swf';
        Ext.onReady(function(){
            var store = new Ext.data.JsonStore({
                fields:[
                    {name:'culTime',type:'date',dateFormat:'Y-m-d H:i:s'},{name:'analysisCount',type:'int'}
                ],
                data:<%=objJson%>
            });
            new Ext.Panel({
            iconCls:'chart',
            title: '在线用户分析统计',
            frame:true,
            renderTo: 'displayDiv',
            width:500,
            height:300,
            layout:'fit',

            items: {
                xtype: 'columnchart',
                store: store,
                url:'../ext3.4/resources/charts.swf',
                xField: 'culTime',
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
                yAxis: new Ext.chart.NumericAxis({
                    displayName: 'analysisCount',
                    labelRenderer : Ext.util.Format.numberRenderer('0,0')
                }),
                tipRenderer : function(chart, record, index, series){
                    if(series.yField == 'analysisCount'){
                        return record.data.culTime.format('Y-m-d H:i:s')+"有"+Ext.util.Format.number(record.data.analysisCount, '0,0') + '次并发';
                    }else{
                        return Ext.util.Format.number(record.data.analysisCount, '0,0') + ' page views in ' + record.data.analysisCount;
                    }
                },

//*/
//                tipRenderer : function(chart, record){
//                    return  record.data.dateTime.format('Y-m-d H:i:s')+"有"+Ext.util.Format.number(record.data.analysisCount, '0,0') + '次并发';
//                },
                chartStyle: {
                    padding: 10,
                    animationEnabled: false,
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
                        majorTicks: {color: 0x69aBc8, length: 4},
                        minorTicks: {color: 0x69aBc8, length: 2},
                        majorGridLines: {size: 1, color: 0xeeeeee}
                    },
                    yAxis: {
                        color: 0x69aBc8,
                        majorTicks: {color: 0x69aBc8, length: 4},
                        minorTicks: {color: 0x69aBc8, length: 2},
                        majorGridLines: {size: 1, color: 0xdfe8f6}
                    }
                }
,
                series: [{
                    type: 'column',
                    displayName: '并发数量',
                    yField: 'analysisCount',
                    style: {
                        image:'bar.gif',
                        mode: 'stretch',
                        color:0x99BBE8
                    }
                }]
            }
        });
        });

    </script>
</head>
<body>

<table align="center" width="660">

    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>