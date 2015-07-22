var showMediaPlayer = false;
var maxFileNameLength = 4000;
var fileType = "*.*";
var selectDirOnly=false;
var serverFileWin = null;
var selectedGrid = null;
var defaultPageSize=10;
var multiSelectMethod=true;
//server搜索的store
var searchFileStore;
var fileSelectFunctionInput = null;
/**
 * 进入指定的目录
 * @param dirName
 */
function loadServerDir(dirName) {
    if (dirName == null) {
        dirName = "";
    }
    searchFileStore.baseParams["filePath"] = dirName;
    searchFileStore.baseParams["fileRegex"]= Ext.getCmp("fileRegex").getValue();
    searchFileStore.load({start:0,limit:defaultPageSize});
    var dirNameCmp = Ext.getCmp("serverListPath");
    if (dirNameCmp) {
        dirNameCmp.setValue(dirName);
    }
}
/**
 * 直接进入指定的目录
 *
 */
function gotoAbsDir() {
    var dirNameCmp = Ext.getCmp("serverListPath");
    if (dirNameCmp) {
        loadServerDir(dirNameCmp.getValue());
    }
}
/**
 * 进入父目录
 */
function gotoParentDir() {
    var oldPath = searchFileStore.baseParams["filePath"];
    var dirName = "/";
    if (oldPath) {
        var i = oldPath.lastIndexOf("/");
        if (i > 0) {
            dirName = oldPath.substring(0, i);
        }
    }
    loadServerDir(dirName);
}
/**
 * 进入当前目录下的某个目录
 * @param dirName
 */
function gotoDirFromCurrentDir(dirName) {
    var oldPath = searchFileStore.baseParams["filePath"];
    if (oldPath) {
        dirName = oldPath + "/" + dirName;
    }
    loadServerDir(dirName);
}

/**
 * 显示文件大小，进行M,G等单位的换算
 * @param val
 * @param metaData
 * @param record
 */
function displayFileSize(val, metaData, record) {
    var isDir = record.get("directory");
    if (isDir) {
        return "";
    }
    if (val > 1024) {//超过1KB就用KB方式显示
        val = Math.round(val / 1024);
        if (val > 1024 * 10) {  //超过10M就用GB方式显示
            val = Math.round(val / 1024);
            if (val > 1024) {//超过1
                return Math.round(val / 1024) + " GB";
            } else {
                return val + " MB";
            }
        } else {
            return val + " KB";
        }
    } else {
        return val + " Byte";
    }
}
/**
 * 显示目录链接以及图标
 * @param val
 * @param metaData
 * @param rowRecord
 */
function displayFileName(val, metaData, rowRecord) {
    //alert(Ext.isArray(rec));
    var isDir = rowRecord.get("directory");
    var iconImgStr = '<img src="../images/fileType_1.gif" border="0">';
    //alert("rowIndex="+rowIndex+",colIndex="+colIndex+",isDir="+isDir);
    var clickScript;
    var result;
    if (isDir) {
        if(selectDirOnly){
            clickScript = "onclick='selectSingleFile(\"" + val + "\","+rowRecord.data["size"]+")'";
        }else{
            clickScript = "onclick='gotoDirFromCurrentDir(\"" + val + "\")'";
        }
        result = '<a href="#" ' + clickScript + '>' + iconImgStr + val + '</a>';
        if (result) {
        }
        //alert(result);
        return result;
    } else {
        iconImgStr = '<img src="../images/fileType_0.gif" border="0">';
        if(multiSelectMethod){
            //clickScript = "onclick='selectSingleFile(\"" + val + "\")'";
            return iconImgStr + val;
        }else{
            clickScript = "onclick='selectSingleFile(\"" + val + "\","+rowRecord.data["size"]+")'";
        }
        result = '<a href="#" ' + clickScript + '>' + iconImgStr + val + '</a>';
        return result;
    }
}

function selectSingleFile(fileName,fileSize){
    if(!multiSelectMethod){
        if(fileSelectFunctionInput && typeof(fileSelectFunctionInput)=='function'){
            var dirNameCmp = Ext.getCmp("serverListPath");
            if (dirNameCmp) {
                fileName = dirNameCmp.getValue()+"/"+fileName;
            }
            fileSelectFunctionInput.call(this,fileName,fileSize);
            serverFileWin.close();
        }else{
            alert('没有定义回调函数，选择的文件是：'+fileName);
        }
    }
}
//如果是目录，则不显示被选中的checkbox
function checkboxRenderer(value, meta, record) {
//    alert(value);
    if (meta) {
    }
    var isDirectory = record.get("directory");
    //var isSelect = false;
    //选中逻辑
    var isSelect = isDirectory&&selectDirOnly;
    if(isDirectory){
       isSelect = selectDirOnly;
    }else{
       isSelect = !selectDirOnly;
    }
    if (!isSelect) {
        return '<div  class="x-grid3-row-checker">&nbsp;</div>';
    } else {
        return '';
    }

}
/**
 * 显示主窗口
 * @param deviceId 服务器ID
 * @param selectedFun 回调函数
 * @param parameters
 */
function showServerSelectWindow(deviceId, selectedFun, parameters) {
    var oldData = null;
    fileSelectFunctionInput = selectedFun;
    if (parameters != null) {
        oldData = parameters.oldData;
        showMediaPlayer = parameters.showMediaPlayer;
        if(parameters.maxFileNameLength){
            maxFileNameLength = parameters.maxFileNameLength;
        }
        if(parameters.selectDirOnly){
            selectDirOnly = parameters.selectDirOnly;
        }
       // alert(selectDirOnly);
        multiSelectMethod = parameters.multiSelectMethod;
        if(parameters.fileType!=null){
            fileType = parameters.fileType;
        }
    }else{
        alert("输入参数为空，采用缺省参数");
    }

    var serverListSm = new Ext.grid.CheckboxSelectionModel({
        singleSelect:false,
        renderer:checkboxRenderer.createDelegate(this) //注意添加这个renderer
    });
    if(multiSelectMethod){
        serverListSm.singleSelect = true;
    }
    searchFileStore = new Ext.data.JsonStore({
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        fields:['name','size','modifyDate','directory'],
        baseParams:{limit:defaultPageSize},
        sm:serverListSm,
        proxy: new Ext.data.HttpProxy({
            method: 'POST',
            url: '../system/server!listFiles.action?obj.serverId=' + deviceId
        })
    });
    searchFileStore.setDefaultSort("name");
    var columns = [];
    if(multiSelectMethod){
        columns.push(serverListSm);
    }
    columns.push(
        {
            header: "文件名",
            hidden:false,
            align:'left',
            width: 130,
            sortable: true,
            renderer:displayFileName,
            dataIndex: 'name'
        });
   columns.push(
        {
            header: "大小",
            hidden:false,
            align:'left',
            width: 60,
            sortable: true,
            renderer:displayFileSize,
            dataIndex: 'size'
        });
    columns.push(
        {
            header: "修改日期",
            hidden:false,
            align:'center',
            width: 120,
            sortable: true,
            dataIndex: 'modifyDate'
        });
    //var fileColumns = new Ext.grid.ColumnModel(columns);
    var fileListTBar;
    if(!selectDirOnly){
        fileListTBar = new Ext.Toolbar({items:[
            {
                text:'目录',
                xtype:'textfield',
                id:'serverListPath'
            },
            {
                text:'进入',
                handler:gotoAbsDir
            },
            {
                text:'上级目录',
                handler:gotoParentDir
            },
            {
                text:'过滤'
            },
            new Ext.ux.form.SearchField({
                store: searchFileStore,
                id:'fileRegex',
                paramName:'fileRegex',
                value:fileType,
                width:80
            })
        ]});
    }else{
        fileListTBar = new Ext.Toolbar({items:[
            {
                text:'目录',
                xtype:'textfield',
                id:'serverListPath'
            }]});
    }
    var serverFileGrid = new Ext.grid.GridPanel({
        title:'文件列表',
        sm : multiSelectMethod?serverListSm:null,
        columns:columns,
        id:'serverFileGrid338547183092',
        name:'serverFileGrid338547183092',
        width:350,
        height:multiSelectMethod?402:375,
        store:searchFileStore,
        tbar:fileListTBar,
        bbar:new Ext.PagingToolbar({
            pageSize: defaultPageSize,
            store: searchFileStore,
            displayInfo: true,
            displayMsg: '共{2}个,{0}-{1}',
            emptyMsg: "没有文件",
            items:[
                /*
                 {
                 text:'选择文件',
                 handler:function() {
                 selectFiles(selectedFun);
                 }
                 }
                 */
            ]
        }),
            listeners:{
                rowdblclick : function(){addSelected(maxFileNameLength)}
            }
    });
    var toolBarPanel = new Ext.Panel({
        width:50,
        height:402,
        layout:'form',
        defaults:{width:48},
        items:[
            {
                xtype:'box',
                width:38,
                height:showMediaPlayer?250:100
            },
            new Ext.Button({
                text:'选中>>',
                //handler:addSelected,
                onClick:function(){addSelected(maxFileNameLength)},
                height:20
            }),
            new Ext.Button({
                text:'<' + '<' + '清除', //为了去掉idea的一个警告，才这么写的
                handler:removeSelected,
                height:20
            }),
            new Ext.Button({
                text:'上移',
                handler:upSelected,
                height:20
            }),
            new Ext.Button({
                text:'下移',
                handler:downSelected,
                height:20
            })/*,{
             text:'↑'
             },{
             text:'↓'
             }*/
        ]
    });
    var playerPanel = new Ext.Panel({
        width:230,
        height:210,
        title:'播放器'
    });
    var selectGridRowSpan = 1;
    if (!showMediaPlayer) {
        selectGridRowSpan = 2;
    }
    selectedGrid = new Ext.grid.GridPanel({
        width:230,
        height:showMediaPlayer?190:400,
        rowspan:selectGridRowSpan,
        id:'serverSelectedFileList338547183092',
        columns:[
            new Ext.grid.CheckboxSelectionModel({
                singleSelect:false}),
            {
                id:'value',
                header: "完整路径",
                hidden:true,
                align:'left',
                width: 220,
                sortable: true,
                //renderer:displayFileName,
                dataIndex: 'value'
            },{
                header: "文件名",
                hidden:false,
                align:'left',
                width: 180,
                sortable: true,
                //renderer:displayFileName,
                dataIndex: 'name'
            }],
        sm:new Ext.grid.CheckboxSelectionModel({
            singleSelect:false}),
        store:new Ext.data.SimpleStore({fields:['value','name'],data:[]}),
        title:'选中文件列表',
        buttons:[
            {
                text:'确定',
                handler:function() {
                    selectFiles(selectedFun);
                }
            },
            {
                text:'取消',
                handler:function() {
                    serverFileWin.close();
                }
            }
        ]
    });
    var winItems = [
        {
            rowspan:2,
            layout:'fit',
            items:serverFileGrid
        }

    ];
    if(multiSelectMethod){
        winItems.push({
            rowspan:2,
            items:toolBarPanel
        });
        if (showMediaPlayer) {
            winItems.push({items:playerPanel});
        }
        winItems.push({
            items:selectedGrid
        });
    }
    serverFileWin = new Ext.Window({
        layout:'table',
        width:multiSelectMethod?648:360,
        height:multiSelectMethod?400:375,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'选择服务器上的文件',
        layoutConfig: {columns:3},
        autoHeight:true,
        items: winItems
    });

    serverFileWin.show();
    if (oldData && "" != oldData) {
        addRecords(oldData);
    }
    searchFileStore.load({params:{start:0, limit:defaultPageSize,fileRegex:fileType}});
}
function removeSelected() {
    var row = selectedGrid.getSelectionModel().getSelections();
    var i = 0;
    var dataSource = selectedGrid.getStore();
    for (i = row.length-1; i >= 0; i--) {
        var record = row[i];
        dataSource.remove(record);
    }
}

function upSelected() {
    var row = selectedGrid.getSelectionModel().getSelections();
    var i = 0;
    var l = row.length;
    var dataStore = selectedGrid.getStore();
    for (i = 0; i <l; i++) {
        var record = row[i];
        var p = dataStore.indexOf(record);
        if(p==0){
            break;
        }
        dataStore.remove(record);
        dataStore.insert(p-1,record);
    }
}

function downSelected() {
    var row = selectedGrid.getSelectionModel().getSelections();
    var i = 0;
    var dataStore = selectedGrid.getStore();
    for (i = row.length-1; i >= 0; i--) {
        var record = row[i];
        var p = dataStore.indexOf(record);
        if(p==(dataStore.getCount()-1)){
            break;
        }
        dataStore.remove(record);
        dataStore.insert(p+1,record);
    }
}

function addRecords(fileNames) {
    var names = fileNames.split(";");
    if (names != null) {
        var hasBeenAddedMsg = "";
        var dataSource = selectedGrid.getStore();
        for (var i = 0,l = names.length; i < l; i++) {
            var fullName = names[i];
            var fileName = fullName;
            var p = fullName.lastIndexOf("/");
            if (p > 0) {
                fileName = fullName.substring(p + 1);
            }
            var record = new Ext.data.Record({value:fullName,name:fileName}, fullName);
            if (dataSource.indexOfId(fullName) >= 0) {
                hasBeenAddedMsg += fileName + ",\n";
                //alert('该数据已经存在:'+value);
            } else {
                dataSource.insert(dataSource.getCount(), record);
            }
        }
        if (hasBeenAddedMsg != "") {
            Ext.MessageBox.alert("已经存在", "这些文件已经存在，不能重复添加：" + hasBeenAddedMsg);
        }
    }
}
function addSelected(maxFileNameLength) {
    //    /*
    //    alert("in addSelected");

    var fileListGrid = Ext.getCmp("serverFileGrid338547183092");
    if (fileListGrid != null) {
        var row = fileListGrid.getSelectionModel().getSelections();
        var i = 0;
        var len = 0;
        var currentPath = searchFileStore.baseParams["obj.serverPath"];
        if (currentPath == null) {
            currentPath = "";
        }
        var hasBeenOldedMsg = "";
        var hasBeenAdded = "";
        var dataSource = selectedGrid.getStore();
        if (dataSource == null) {
            alert("DataSource为空！");
            return;
        }
        //var data=[];
        for (i = 0,len = row.length; i < len; i++) {
            if (row[i].get("directory")) {
                //alert("是目录，不能加入！");
                continue;
            }
            var ss = row[i].get("name");
            var tempValue = getSelectedFileNames();
            var value = currentPath + "/" + ss;
            if(tempValue!=null&&tempValue.length>maxFileNameLength-value.length){
                var alertMsg = "长度超出了预期："+maxFileNameLength;
                if(hasBeenAdded!=""){
                    alertMsg += ",已经添加了："+hasBeenAdded;
                }
                Ext.MessageBox.alert("警告：有些被选择的文件没有添加！",alertMsg);
                break;
            }
            var record = new Ext.data.Record({value:value,name:ss}, value);
            if (dataSource.indexOfId(value) >= 0) {
                hasBeenOldedMsg += ss + ",\n";
                //alert('该数据已经存在:'+value);
            } else {
                hasBeenAdded += value+",";
                dataSource.insert(dataSource.getCount(), record);
            }
        }
        if (hasBeenOldedMsg != "") {
            Ext.MessageBox.alert("已经存在", "这些文件已经存在，不能重复添加：" + hasBeenOldedMsg);
        }
        //dataSource.reload();
        //dataSource.insert(data);

    }
    //*/
}

function getSelectedFileNames(){
    var result = "";
    try {
        var fileListGrid = Ext.getCmp("serverSelectedFileList338547183092");
        if (fileListGrid != null) {
            var row = fileListGrid.getStore();
            var i = 0;
            var len = 0;
            for (i = 0,len = row.getCount(); i < len; i++) {
                var record = row.getAt(i);
                if (record != null) {
                    var ss = record.get("value");
                    if (result == "") {
                        result = result + ss;
                    } else {
                        result = result + ";" + ss;
                    }
                }
            }
            return result;
        } else {

        }
    } catch(e) {
        Ext.MessageBox.alert("异常", "尝试选择数据时，发生意外：" + e.description);
    }
    return result;
}
function selectFiles(selectedFun) {
    try {
        if (selectedFun && typeof(selectedFun) == "function") {
            selectedFun.call(this, getSelectedFileNames());
        } else {
            Ext.MessageBox.alert("没有回调函数，选择的文件为：" + jsonData);
        }
        serverFileWin.close();
    } catch(e) {
        Ext.MessageBox.alert("异常", "尝试选择的数据时，发生意外：" + e.description);
    }

}
