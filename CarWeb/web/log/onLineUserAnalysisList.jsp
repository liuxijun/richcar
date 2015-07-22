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
    <script language="javascript">
        function dateDiff(interval, date1, date2)
        {
            var objInterval = {'D' : 1000 * 60 * 60 * 24, 'H' : 1000 * 60 * 60,
                'M' : 1000 * 60, 'S' : 1000, 'T' : 1};
            interval = interval.toUpperCase();
            var dt1 = Date.parse(date1.replace(/-/g, '/'));
            var dt2 = Date.parse(date2.replace(/-/g, '/'));
            try
            {
                return Math.round((dt2 - dt1) / eval('(objInterval.' + interval + ')'));
            }
            catch (e)
            {
                return e.message;
            }
        }
        var cpStore = new Ext.data.JsonStore({
            method:'POST',
            autoLoad:true,
            url:"/csp/csp!searchAllCp.action",
            root:'objs',
            totalProperty:'totalCount',
            fields:['id', 'name']
        });
        var currentNode = null;
        var formButtons = [
            {
                columnWidth:.4,
                //第三列
                text:'查询',
                xtype:'button',
                minWidth:60,
                action:'getAreaContributionCount',
                listeners:{
                    "click":function () {
                        var startTime = Ext.getCmp("startTime").value;
                        var endTime = Ext.getCmp("endTime").value;
                        if (startTime == undefined || endTime == undefined) {
                            Ext.MessageBox.show({
                                title:"标题",
                                msg:"请选择需要查询的时间段",
                                buttons:{"ok":"我不再显示OK了"},
                                width:500,
                                icon:Ext.MessageBox.INFO,
                                closable:false



                                // prompt:true
                                // multiline:true
                            });

                            return;
                        }
                        if(startTime>endTime){
                            alert("结束时间不能小于开始时间");
                            return ;
                        } else{
                            var ts = dateDiff('D', startTime, endTime) ;
                            if(ts>=2)
                            {
                                alert('时间段不能超过2天');
                                return false;
                            }
                        }


//                        var store = new Ext.data.JsonStore({
//                            url:'/log/visitLog!getOnLineUserAnalysisCount.action',
//                            params:{startTime:startTime,endTime:endTime},
//                            root:'objMaps',
//                            field:['','']
//                        });
//                        store.load();
//                        alert("hello");
//                        Ext.Ajax.request({
//                            method:'POST',
//                            url:'/log/visitLog!getOnLineUserAnalysisCount.action',
//                            params:{startTime:startTime,endTime:endTime},
//                            success:function(request){
//                                 alert(Ext.decode(request.responseText));
//                            }
//                        });
                        var form = channelViewForm.getForm().getEl().dom;
                        form.action = '/log/visitLog!getOnLineUserAnalysisCount.action';
                        form.method = 'POST';
//                        form.target = "_blank";
                        form.submit();
                    }
                }
            }
        ];

        function initDisplay() {

            // render the tree
            //tree.render('displayDiv');

            channelViewForm = new Ext.form.FormPanel({
                title:'在线用户分析(并发统计)',
                border:true,
                url:'channel!view.action',
                saveUrl:'channel!save.action',

                width:550,
                frame:false,

                items:[
                    new Ext.Toolbar({

                        items:[ new Ext.form.FieldSet({
                                    title:'搜索栏',
                                    width:535,

                                    items:[
                                        {
                                            baseCls:'x-plain',
                                            layout:'column',
                                            items:[
                                                {
                                                    columnWidth:.5, //第一列
                                                    layout:"form",
                                                    baseCls:'x-plain',
                                                    labelWidth:80,
                                                    items:[
                                                        {
                                                            xtype:"datefield",
                                                            fieldLabel:'　　开始时间',
                                                            name:'startTime',
                                                            id:'startTime',
                                                            format:'Y-m-d',
                                                            labelWidth:60,
                                                            width:100
                                                        },
                                                        {

                                                            xtype:"combo",
                                                            fieldLabel:'来源组织',
                                                            name:'cpId',
                                                            hiddenName:'cpId',
                                                            labelWidth:60,

                                                            emptyText:'请选择...',
                                                            store:cpStore,
                                                            valueField:'id',
                                                            displayField:'name',
                                                            forceSelection:true,
                                                            triggerAction:'all',
                                                            selectOnFocus:true
                                                        }
                                                    ]
                                                },
                                                {
                                                    columnWidth:.5, //第二列
                                                    baseCls:'x-plain',
                                                    layout:"form",
                                                    labelWidth:90,
                                                    items:[
                                                        {
                                                            xtype:"datefield",
                                                            fieldLabel:'结束时间',
                                                            name:'endTime',
                                                            id:'endTime',
                                                            format:'Y-m-d',
                                                            labelWidth:60,
                                                            width:100
                                                        },
                                                        {
                                                            baseCls:'x-btn-over',

                                                            layout:"form",
                                                            items:formButtons
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
                                }
                        )]
                    })
                ]
            });
            //loadFormAjax();
            channelViewForm.render(displayDiv);
//            root.expand(false,false);
        }
        Ext.onReady(function () {
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
<table align="center" width="660">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>