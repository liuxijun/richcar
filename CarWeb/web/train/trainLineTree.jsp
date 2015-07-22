<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "trainLine";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="trainLine"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "trainLineTree.jsp";
        actionHeader="trainLine";
        var currentNode=null;
        var AREA_TYPE_CITY="";
        defaultPageSize = 80;
        storeConfig.fields = ['id','name','parentId','description','status','type','picture'];
        storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: 'trainLine!getNodes.action'});
        storeConfig.autoLoad = true;
        storeConfig.baseParams = {};
        storeConfig.baseParams.limit=80;
        var provinceStore = new FortuneSearchStore(storeConfig);
        function areaTypeChanged(){
            var areaType = getCmpValue("objType");
            if(areaType==AREA_TYPE_CITY){
//                showCmp("objProvince");
//                alert("省份必须输入");
            }else{
//                hiddenCmp("objProvince");
            }
        }
        function deleteArea(){
            if(currentNode!=null){
                if(currentNode.leaf){
                    confirmAction(keyId,"删除","delete",
                            Ext.getCmp("obj.name").getValue(),"确认要删除该数据吗？");
                    return;
                }
                alert(currentNode.text+" 有子数据，不能删除！");
            }else{
                alert("当前节点数据不正确，不能删除！");
            }
        }
        function areaProvinceChanged(){

        }

        function resetForm(parentId,currentLevel){
            keyId=null;
            setCmpValue("obj.id","");
            setCmpValue("obj.name","");
            setCmpValue("obj.picture","");
            setCmpValue("obj.description","");
            setCmpValue("obj.status","1");
            setCmpValue("obj.parentId",parentId);
        }
        function addBrother(){
            resetForm(Ext.getCmp("obj.parentId").getValue());
        }
        function getLevel(currentNode){
            var result = 0;
            while(currentNode!=null&&currentNode.parentNode!=null){
                result++;
                if(result>10){
                    break;
                }
                currentNode = currentNode.parentNode;
            }
            return result;
        }
        function addChild(){
            var currentLevel = getLevel(currentNode);
            if(currentLevel>3){
                alert("已经为最后一层，不能再添加了！");
                return;
            }
            resetForm(Ext.getCmp("obj.id").getValue(),currentLevel);
            try {
                setCmpValue("objType",currentLevel);
                setCmpValue("parentText", getParentTreeText(currentNode));
            } catch(e) {
            }
        }
        function getParentTreeText(parentNode){
            var parentText= "";
            try {
                var repeatTimes = 0;
                while(parentNode!=null){
                    if(parentText!=""){
                        parentText=">>"+parentText;
                    }
                    parentText =parentNode.text+parentText;
                    parentNode = parentNode.parentNode;
                    if(parentNode==null||parentNode.id==-1){
                        break;
                    }
                    //为了防止死循环,做一个计数器
                    repeatTimes++;
                    if(repeatTimes>10){
                        break;
                    }
                }
                //alert(parentText);
            } catch(e) {

            }
            return parentText;
        }
        formOptions.beforePost=function(){
            var province = getCmpValue("objProvince");
            var areaType = getCmpValue("objType");
            if(areaType == AREA_TYPE_CITY&&(province==null||province==""||province=="null")){
                alert("当类型为城市时，请务必输入省份信息！");
                return "error";
            }
            return "ok";
        };
        formOptions.afterLoad=function(){
            try {
                var currentLevel = getLevel(currentNode);
                if(currentLevel>=1&&currentLevel<=4){
                    setCmpValue('objType',currentLevel);
                }else{
                    setCmpValue('objType',4);
                }
                setCmpValue("parentText", getParentTreeText(currentNode.parentNode));
                var status=parseInt(getCmpValue('objStatus'));
                if(status<=0||status>99){
                    setCmpValue('objStatus',1);
                }
            } catch(e) {
                alert("发生异常："+ e.description);
            }
        };
        function loadAreaFromAjax(nodeId){
           keyId = nodeId;
           loadForm(Ext.getCmp("BaseViewForm338547183092").getForm(),
                   "trainLine!view.action?keyId="+nodeId);
        }
        function initDisplay(){
            var tree =
            new Ext.tree.TreePanel(
            {
                animate:true,
                useArrows:true,
                //frame: true,
                xtype:'treepanel',
                autoScroll:true,
                loader: new Ext.tree.TreeLoader({dataUrl:'trainLine!getNodes.action',nodeParameter:'parentId'}),
                enableDD:true,
                containerScroll: true,
                border: false,
                width: 305,
                height: 330,
                rootVisible: false,
                dropConfig: {appendOnly:true},
/*
                root:{
                    text: '上海通用别克',
                    draggable:false, // disable root node dragging
                    id:'-1'
                },
*/                
                listeners: {
                    click:function(node,e){
                        if(e){

                        }
                        currentNode = node;
                       loadAreaFromAjax(node.id)
                    },
                    'checkchange': function(node, checked){
                        if(checked){
                            node.getUI().addClass('complete');
                        }else{
                            node.getUI().removeClass('complete');
                        }
                    }
                }
            }
                    )       //  */
                    ;

            // add a tree sorter in folder mode
           // new Ext.tree.TreeSorter(tree, {folderSort:true});

            // set the root node
            var root = new Ext.tree.AsyncTreeNode({
                text: '中国铁路',
                draggable:false, // disable root node dragging
                id:'-1'
            });
            tree.setRootNode(root);

            // render the tree
            //tree.render('displayDiv');
            defaultViewFormButtons.push({text:'添加子节点',handler:addChild});
            defaultViewFormButtons.push({text:'添加同级',handler:addBrother});
            defaultViewFormButtons.push({text:'删  除',handler:deleteArea});
            var areaViewForm = new Ext.form.FormPanel({
                id:'BaseViewForm338547183092',
                title:'<fts:text name="area"/>基本信息',
                border:true,
                url:'trainLine!view.action',
                saveUrl:'trainLine!save.action',
                layout:'column',
                width:600,
                buttons:defaultViewFormButtons,
                items:[
                    {
                       columnWidth:.45,
                        items:[
                           tree
                        ]
                    },{
                        columnWidth:.55,
                        defaultLabelAlign:'right',
                        labelWidth: 75, // label settings here cascade unless overridden
                        labelAlign:'right',
                        layout:'form',
                        xtype:'fieldset',
                        saveUrl:'trainLine!save.action',
                        viewUrl:'trainLine!view.action',
                        defaults: {width: 230},
                        defaultType: 'textfield',
                        items:[
                            //{name:'keyId',value:keyId,inputType:'hidden'},
                             {name:'obj.id',id:'obj.id',inputType:'hidden',value:'-1'},
                            new FortuneCombo(
                            {
                                fieldLabel:'类型',
                                hiddenName:'obj.type',
                                hiddenId:'obj.type',
                                id:'objType',
                                store:getDictStore('trainLineType'),
                                readOnly:viewReadOnly,
                                listeners:{
                                    select:areaTypeChanged
                                }
                            }),
/*
 new FortuneCombo(
 {
 fieldLabel:'省份',
 hiddenName:'obj.province',
 hiddenId:'obj.province',
 id:'objProvince',
 store:provinceStore,
 valueField:'name',
 readOnly:viewReadOnly,
 hidden:false,
 listeners:{
 select:areaProvinceChanged
 }
 }),
*/
                             {fieldLabel:'<fts:text name="trainLine.parentId"/>',
                                 id:'obj.parentId',inputType:'hidden',name:'obj.parentId',readOnly:viewReadOnly},
                            {fieldLabel:'上级节点',
                                id:'parentText',
                                name:'parentText'/*,xtype:'label'*/,
                                readOnly:true},
                            {fieldLabel:'<fts:text name="trainLine.name"/>',
                                name:'obj.name',id:'obj.name',readOnly:viewReadOnly},
/*
                             {fieldLabel:'<fts:text name="trainLine.posX"/>',inputType:'hidden',
                                 id:'obj.posX',name:'obj.posX',readOnly:viewReadOnly},
                             {fieldLabel:'<fts:text name="trainLine.posY"/>',inputType:'hidden',
                                 id:'obj.posY',name:'obj.posY',readOnly:viewReadOnly},
                             {fieldLabel:'<fts:text name="trainLine.code"/>',
                                 id:'obj.code',name:'obj.code',readOnly:viewReadOnly},
*/
                            {fieldLabel:'<fts:text name="trainLine.description"/>',
                                xtype:'textarea',height:221,
                                id:'obj.description',name:'obj.description',readOnly:viewReadOnly},
                             new FortuneCombo(
                             {
                                 fieldLabel:'<fts:text name="trainLine.status"/>',
                                 hiddenName:'obj.status',
                                 hiddenId:'obj.status',
                                 id:'objStatus',
                                 store:getDictStore('status'),
                                 readOnly:viewReadOnly
                             })
                        ]
                    }
                ]
            });
            //loadFormAjax();
            areaViewForm.render('displayDiv');
            root.expand(false, /*no anim*/ false);
        }
        Ext.onReady(function(){
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