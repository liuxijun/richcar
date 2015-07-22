<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"getCspDevice","deviceGetDeviceRegUrl");
//        needPermissions(actionHeader,"list","propertyList");
//        needPermissions(actionHeader,"list","propertySelectList");
  //      needPermissions(actionHeader,"uploadFile","contentManage,contentUploadFile");
        needPermissions(actionHeader,"save","contentManage,contentSave");
        needPermissions(actionHeader,"getFtpList","contentManage,contentGetFtpList");
        needPermissions(actionHeader,"getDeviceRegUrl","deviceGetDeviceRegUrl");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script type="text/javascript" src="/js/common.js"></script>      
<script type="text/javascript" src="contentView.js"></script>    
<script language="javascript">
<%
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    if (id==null || "".equals(id)){
        id = "0";
    }

    long cpId = 0;
    if (admin != null && admin.getCspId()!=null){
        cpId = admin.getCspId();
    }
%>

var viewForm ;

Ext.onReady(function(){
    var cpId = '<%=cpId%>';
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
            {name:'maxSize'},                
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
            url:"/system/device!getCspDevice.action?cpId="+cpId,
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
                        {name:'stringValue'},
                        {name:'extraData'}                            
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
                                        var propertyExtra = storeRecord.data.extraData;
                                        var pp = getElement(properties,propertyId);
                                        var str ;

                                        var fno;
                                        if (doId.indexOf("."+propertyId+".")==-1 || pp.dataType==7){
                                            doId += "."+propertyId+".";
                                            fno = ""+propertyId;
                                        }else{
                                            pp.count++;
                                            fno = addFormItem(pp,viewForm,getPropertyNo(pp.id,pp.count));
                                            viewForm.doLayout();                                            
                                        }

                                        switch (pp.dataType){
                                            case 5:
                                                if (propertyValue!='' && propertyValue!='0'){
                                                     viewForm.getForm().findField( fno ).setValue( propertyValue );
                                                }
                                                break;
                                            case 7:
                                                str = ""+propertyId + "_"+ propertyValue;
                                                viewForm.getForm().findField( fno ).eachItem(function(item){
                                                    if (item.getId()==str){
                                                        item.setValue(true);
                                                    }
                                                });
                                                break;
                                            case 8:case 9:case 10:
                                                viewForm.getForm().findField( fno ).setValue( propertyValue );
                                                str = propertyExtra.split('###');
                                                viewForm.getForm().findField( fno + '_desp' ).setValue(str[0]);
                                                viewForm.getForm().findField( fno + '_bandwidth' ).setValue(str[1]);
                                                viewForm.getForm().findField( fno + '_length' ).setValue(str[2]);
                                                break;
                                            default:
                                                viewForm.getForm().findField( fno ).setValue( propertyValue );
                                        }

                                    }
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
                                    text: '提　交',
                                    width: 100,
                                    listeners:{
                                        "click":function()
                                        {
                                            //alert(viewForm.getForm().findField('MEDIA_NAME').getValue())
                                            var msg = '';
                                            var form1 = viewForm.getForm();
                                            for (var i=0;i<formObjects.length;i++){
                                                var p = formObjects[i].property;
                                                var fno = formObjects[i].id;
                                                var fvalue = form1.findField( fno ).getValue();

                                                if (p.isNull==0 && fvalue==''){
                                                    msg+= "{"+p.name+"} 必填<br/>";
                                                }

                                                if (fvalue!=''){
                                                    //alert("{"+ p.name +"} 字数超过" +p.maxSize +"限制<br/>"+getStringLength(fvalue));
                                                }

                                                //if (fvalue!='' && typeof(fvalue)=='string' && getStringLength(fvalue)>p.maxSize){
                                                //    msg+= "{"+ p.name +"} 字数超过" +p.maxSize +"限制<br/>";
                                                //}

                                                switch (p.dataType){
                                                    case 3:
                                                        if (fvalue!='' && !checkNumber(fvalue)){
                                                            msg+= "{"+ p.name +"} 应填写数字<br/>";
                                                        }
                                                        break;
                                                    case 8:
                                                    case 9:
                                                    case 10:

                                                        if (fvalue!=''){
                                                            var fvalue1 = form1.findField( fno + "_desp").getValue();
                                                            var fvalue2 = form1.findField( fno + "_bandwidth").getValue();
                                                            var fvalue3 = form1.findField( fno + "_length").getValue();
                                                            form1.findField( fno + "_extra").setValue(fvalue+"###"+fvalue1+"###"+fvalue2+"###"+fvalue3);
                                                        }
                                                        break;
                                                }
                                            }

                                            if (msg !=''){
                                                msg = msg.substring(0,msg.length-1);
                                                Ext.Msg.alert("提示",msg);
                                                return;
                                            }

                                            if(viewForm.getForm().isValid()){
                                                viewForm.getForm().submit({
                                                    params:{action:action,moduleId:moduleId,keyId:id },
                                                    url: actionUrl + '!save.action', //处理修改后台地址
                                                    method: 'post',
                                                    waitMsg: '正在处理数据，请稍后……',
                                                    success: function (form,returnMsg){
                                                        var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                                        //Ext.Msg.alert("提示","操作成功！");
                                                        id = serverData.data['obj.id'] ;
                                                        if (action=='add'){
                                                            Ext.MessageBox.confirm("操作成功", " 继续添加 ",function(btn){
                                                                if(btn=="yes"){
                                                                    document.location='cpContentDefaultAdd.jsp';
                                                                }
                                                            });
                                                        }else{
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        }
                                                        action = 'view';                                                        
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
        style: {'margin-left':'5px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'viewForm',
        width: '100%',
       height: '600',

        labelWidth: 150,
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
        width:tableWidth+30,
    
        items:[
            new Ext.form.FieldSet({
                title: '',
                border:false,
                autoHeight: true,
               //autoWidth: true,
                width:tableWidth+30,
                style: {'margin-left':'0px','margin-top': '10px','margin-right':'0px','margin-bottom':'20px'},

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
<table align="center" width="780">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>