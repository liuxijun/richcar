<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="../resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../ext/ux/css/ux-all.css" rel="stylesheet" />
<script type="text/javascript" src="../ext/ext-base.js"></script>
<script type="text/javascript" src="../ext/ext-all.js"></script>
<!--添加这个js，是为了显示中文-->
<script type="text/javascript" src="../ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="../ext/ux/ux-all.js"></script>

<script type="text/javascript" src="../js/ExtValidator.js"></script>

<script type="text/javascript" src="contentView.js"></script>


<script language="javascript">
<%
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    String nowTime = com.fortune.util.StringUtils.date2string(new java.util.Date());
%>

var viewForm ;

Ext.onReady(function(){

    var moduleId = <%=request.getParameter("moduleId")%>;

    var action = '<%=action%>';
    var id = '<%=id%>';
    var tableWidth = 750;

    var actionUrl = "/content/content";

    var propertyStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/module/property!list.action?obj.moduleId="+moduleId,
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'code'},
            {name:'dataType'},
            {name:'isMultiLine'},
            {name:'isMerge'},
            {name:'isNull'},
            {name:'isMain'},
            {name:'columnName'},
            {name:'relatedTable'},                
            {name:'status'},
            {name:'displayOrder'}
        ]
    });
    propertyStore.setDefaultSort('displayOrder','asc');
    propertyStore.load({
        callback :
                function(records,options,success){
                    if(success){
                        for (i=0; i<propertyStore.getCount(); i++){
                            var storeRecord =propertyStore.getAt(i);
                            addElement(properties,new Property(
                                    storeRecord.data.id,storeRecord.data.code,
                                    storeRecord.data.name,storeRecord.data.dataType,
                                    storeRecord.data.isMultiLine,storeRecord.data.isNull,
                                    storeRecord.data.maxSize,storeRecord.data.relatedTable,storeRecord.data.displayOrder,1));
                            if (storeRecord.data.relatedTable=='1'){
                                getDeviceList(storeRecord.data.id);
                            }
                        }
                        sequenceDo();
                    }
                }
    });


    var propertySelectStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/module/propertySelect!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'code'},
            {name:'status'},
            {name:'propertyId'},
            {name:'displayOrder'}
        ]
    });
    propertySelectStore.setDefaultSort('displayOrder','asc');
    propertySelectStore.load({
        callback :
                function(records,options,success){
                    if(success){
                        for (i=0; i<propertySelectStore.getCount(); i++){
                            var storeRecord =propertySelectStore.getAt(i);
                            addElement(propertySelects,new PropertySelect(
                                    storeRecord.data.id,storeRecord.data.code,
                                    storeRecord.data.name,storeRecord.data.propertyId,
                                    storeRecord.data.displayOrder));
                        }
                        sequenceDo();
                    }
                }
    });


    function getDeviceList(propertyId){
        sequenceInit++;
        var deviceStore = new Ext.data.JsonStore({
            method:'POST',
            url:"/system/device!list.action",
            baseParams:{limit:1000000},
            totalProperty:'totalCount',
            root:'objs',
            fields:[
                {name:'id'},
                {name:'name'}
            ]
        });
        deviceStore.load({
            callback :
                    function(records,options,success){
                        if(success){
                            for (i=0; i<deviceStore.getCount(); i++){
                                var storeRecord =deviceStore.getAt(i);
                                addElement(propertySelects,new PropertySelect(
                                        storeRecord.data.id,'',
                                        storeRecord.data.name,propertyId,
                                        storeRecord.data.id));
                            }
                            sequenceDo();
                        }
                    }
        });
    }

    var sequenceInit = 2;
    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == sequenceInit){
            for (i=0; i<properties.length; i++){
                addFormItem(properties[i],viewForm,i);
            }

            if (action=='view'){
                var contentPropertyStore = new Ext.data.JsonStore({
                    method:'POST',
                    url:"/content/content!view.action",
                    //url:"/content/contentProperty!list.action",
                    baseParams:{limit:1000000,keyId:id},
                    totalProperty:'totalCount',
                    root:'objs',
                    fields:[
                        {name:'propertyId'},
                        {name:'stringValue'}
                    ]
                });
                contentPropertyStore.load({
                    callback :
                            function(records,options,success){
                                if(success){
                                    var doId = "";
                                    //for (i=0; i<contentPropertyStore.getCount(); i++){
                                    for (var i=0; i<records.length; i++){
                                        var storeRecord =records[i];
                                        var propertyId = storeRecord.data.propertyId;
                                        var propertyValue = storeRecord.data.stringValue;
                                        var pp = getElement(properties,propertyId);
                                        if (doId.indexOf("."+propertyId+".")==-1 || pp.dataType==7){
                                            doId += "."+propertyId+".";
                                            if (pp.dataType==7){
                                                var str = ""+propertyId + "_"+ propertyValue;
                                                viewForm.getForm().findField( ''+propertyId ).eachItem(function(item){
                                                    if (item.getId()==str){
                                                        item.setValue(true);
                                                    }
                                                });
                                            }else{
                                                viewForm.getForm().findField( ''+propertyId ).setValue( propertyValue );
                                            }
                                        }else{
                                            pp.count++;
                                            fno = addFormItem(pp,viewForm,getPropertyNo(pp.id,pp.count));
                                            viewForm.doLayout();
                                            viewForm.getForm().findField( fno ).setValue( propertyValue );
                                        }

                                    }
                                    //viewForm.getForm().findField('17').setValue({'17_34':true});
                                    //viewForm.getForm().findField( '17' ).setValue({'17_31':true});
                                }
                            }
                });

            }



            viewForm.add(
                    new Ext.Panel({
                        width:'100%',
                        layout:'table',
                        layoutConfig: {columns:1},
                        baseCls: 'x-plain',
                        items:[
                            {
                                //layout:'form',
                                baseCls: 'x-plain',
                                items:[{
                                    xtype: 'button',
                                    text: '提交',
                                    width: 30,
                                    listeners:{
                                        "click":function()
                                        {
                                            //alert(viewForm.getForm().findField('MEDIA_NAME').getValue())

                                            if(viewForm.getForm().isValid()){
                                                viewForm.getForm().submit({
                                                    params:{action:action,moduleId:moduleId,keyId:id },
                                                    url: actionUrl + '!save.action', //处理修改后台地址
                                                    method: 'post',
                                                    waitMsg: '正在处理数据，请稍后……',
                                                    success: function (form,returnMsg){
                                                        var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                                        Ext.Msg.alert("提示","操作成功！");
                                                        action = 'view';
                                                        //id = serverData.data[0].id ;
                                                        //dataFresh();
                                                        //dataForm.reset();           //表单中所有数据置空
                                                    },
                                                    failure: function (form,returnMsg){
                                                        Ext.MessageBox.show({
                                                            title: '提示',
                                                            msg: '操作失败，原因是:' + returnMsg.result.error,
                                                            buttons: Ext.MessageBox.OK,
                                                            icon: Ext.MessageBox.ERROR
                                                        });
                                                    },
                                                    scope: this
                                                });
                                            }
                                        }
                                    }
                                }]

                            }

                        ]

                    })

                    );

            viewForm.doLayout();

//            if (action=='view'){
//                dataFresh();
//            }

        }
    }


    var dataViewStore = new Ext.data.JsonStore({
        method:'POST',
        url: actionUrl + "!view.action"
    });

    function dataFresh(){
        dataViewStore.load({
            params:{keyId:id},
            callback :
                    function(records,options,success){
                        viewForm.getForm().setValues( this.reader.jsonData.data );
                    }
        });
    }



    var columnWidth = 180;

    viewForm = new Ext.FormPanel({
        style: {'margin-left':'15px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'viewForm',
        width: '90%',

        labelWidth: 200,
        frame:true,
        //layout:'column',
        layout:'form',
        layoutConfig: {columns:1},
        baseCls: 'x-plain',
        waitMsgTarget: true,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        fileUpload:true,
        items: []
    })

    var  viewPanel = new Ext.Panel({
        title:"资源管理",
        width:tableWidth,
        //height:tableHeight,
        items:[
            new Ext.form.FieldSet({
                title: '',
                border:false,
                autoHeight: true,
                //autoWidth: true,
                width:tableWidth-30,
                style: {'margin-left':'12px','margin-top': '10px','margin-right':'12px','margin-bottom':'12px'},

                items: [
                    viewForm
                ]
            })
        ]
    })




    viewPanel.render('display');

    if (action=='view'){
        dataFresh();
    }






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