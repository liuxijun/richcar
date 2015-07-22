var fileSelectorOptions={
    searchFileStore:null,
    dirNameCmpId:"ftpListPath",
    filePathServerSideName:"filePath",
    filterNameId:"filter",
    singleFileSelect:false,
    selectFileOnly:true,
    selectedFunction:function(){},
    clickOnPictureFileName:function(fullFileName){

    },
    beforeFileListLoad:function(){},
    loadFtpDirLocked:false
};
var ftpFileWin = null;
var selectedGrid = null;
//ftp搜索的store
defaultPageSize = 10;
/**
 * 进入指定的目录
 * @param dirName
 */

function loadFtpDir(dirName) {
    if(fileSelectorOptions.loadFtpDirLocked){
        return;
    }
    fileSelectorOptions.loadFtpDirLocked = true;
    if(dirName==null||dirName==""){
        dirName = "/";
    }
    if(dirName.indexOf("//")==0){
        dirName = dirName.substring(1);
    }
    if(fileSelectorOptions.beforeFileListLoad!=null){
        fileSelectorOptions.beforeFileListLoad();
    }
    if (dirName == null) {
        dirName = "";
    }
    fileSelectorOptions.searchFileStore.baseParams[fileSelectorOptions.filePathServerSideName] = encodeURI(encodeURI(dirName));
    fileSelectorOptions.searchFileStore.load({start:0,limit:defaultPageSize,callback:function(){
        fileSelectorOptions.loadFtpDirLocked = false;
    }});
    var dirNameCmp = Ext.getCmp(fileSelectorOptions.dirNameCmpId);
    if (dirNameCmp) {
        dirNameCmp.setValue(dirName);
    }
}
/**
 * 直接进入指定的目录
 *
 */
function gotoAbsDir() {
    var dirNameCmp = Ext.getCmp(dirNameCmpId);
    if (dirNameCmp) {
        loadFtpDir(dirNameCmp.getValue());
    }
}
/**
 * 进入父目录
 */
function gotoParentDir() {
    var oldPath = fileSelectorOptions.searchFileStore.baseParams[fileSelectorOptions.filePathServerSideName];
    var dirName = "/";
    if (oldPath) {
        var i = oldPath.lastIndexOf("/");
        if (i > 0) {
            dirName = oldPath.substring(0, i);
        }
    }
    loadFtpDir(dirName);
}
/**
 * 进入当前目录下的某个目录
 * @param dirName
 */
function gotoDirFromCurrentDir(dirName) {
    var oldPath = fileSelectorOptions.searchFileStore.baseParams[fileSelectorOptions.filePathServerSideName];
    if (oldPath) {
        dirName = oldPath + "/" + dirName;
    }
    loadFtpDir(dirName);
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
    if(metaData!=null){

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
    var iconImgStr = '<img src="/images/icon/folder.png" border="0">';
    var cls = rowRecord.get("type");
    var result = val;
    if(metaData){

    }
    if(isDir){
        cls = 'folder';
    }
    var clickScript;
    if (isDir) {
        clickScript = "onclick='gotoDirFromCurrentDir(\"" + val + "\")'";
        result = '<div ' + clickScript + ' style="color:blue">' + iconImgStr + val + '</div>';
        if (result) {

        }
        //alert(result);
    } else {
        clickScript = "onclick='fileSelectorOptions.clickOnPictureFileName(\""+
            fileSelectorOptions.searchFileStore.baseParams[fileSelectorOptions.filePathServerSideName]+
            "/" + val + "\")'";
        iconImgStr = '<img src="/images/icon/' +cls+
                     '.png" border="0" ' +clickScript+
            '>';
        result = '<div style="cursor: pointer" '+clickScript+'>'+ iconImgStr + val+'</div>';

    }
    return "<div  style='vertical-align: middle;' class='fileType_"+cls+"'>"+result+"</div>";
}
//如果是目录，则不显示被选中的checkbox
function checkboxRenderer(value, meta, record) {
    if (meta&&value) {
    }
    var isSelect = record.get("directory");
    //var isSelect = false;
    //选中逻辑
    if (!isSelect) {
        return '<div  class="x-grid3-row-checker">&nbsp;</div>';
    } else {
        return '';
    }

}
var ftpListSm = new Ext.grid.CheckboxSelectionModel({
    singleSelect:false,
    renderer:checkboxRenderer.createDelegate(this) //注意添加这个renderer
});

function displayFileControl(val,meta,rec){
    if(rec.get("directory")&&fileSelectorOptions.selectFileOnly){
       return "";
    }
    if(meta){

    }
    return "<a href='javascript:selectSingleFile(\""+val+"\"," +rec.get("size")+","
        +rec.get("length")+","+rec.get("width")+","+rec.get("height")+
        ")'>选定</a>";
    //return result;
}

function getFileListGrid(parameters){
    var url = parameters.url;
    if(url==null){
        url = '../template/template!listFiles.action';
    }
    fileSelectorOptions.clickOnPictureFileName = parameters.clickOnPictureFileName;
    fileSelectorOptions.searchFileStore = new Ext.data.JsonStore({
        root: 'objs',
        totalProperty: 'totalCount',
        remoteSort: true,
        fields:['name','type','size','modifyDate','directory','length','width','height','videoCodec','audioCodec'],
//        baseParams:{limit:defaultPageSize},
        sm:sm,
        proxy: new Ext.data.HttpProxy({
            method: 'POST',
            url: url
        })
    });
    var ftpFileGridWidth;
    var nameFieldWidth = 230;
    if(parameters.fileGridWidth){
        ftpFileGridWidth = parameters.fileGridWidth;
    }else{
        ftpFileGridWidth = 350;
        if(parameters.singleSelect) {
            ftpFileGridWidth += 100;
        }
    }
    nameFieldWidth =ftpFileGridWidth-200;
    if(parameters.singleSelect) {
        nameFieldWidth -= 100;
    }
    var fileColumns= new Ext.grid.ColumnModel([
        ftpListSm,
        {
            header: "文件名",
            hidden:false,
            align:'left',
            width: nameFieldWidth,
            sortable: true,
            renderer:displayFileName,
            dataIndex: 'name'
        },
        {
            header: "大小",
            hidden:false,
            align:'left',
            width: 60,
            sortable: true,
            renderer:displayFileSize,
            dataIndex: 'size'
        },
        {
            header: "修改日期",
            hidden:false,
            align:'center',
            width: 120,
            sortable: true,
            dataIndex: 'modifyDate'
        }
    ]);
    if(parameters.singleSelect){
        fileColumns.config.push({header:"控制",hidden:false,align:'center',width:100,sortable:false,dataIndex:'name',renderer:displayFileControl});
    }
    return new Ext.grid.GridPanel({
        title:'文件列表',
        sm : sm,
        cm:fileColumns,
        id:'ftpFileGrid338547183092',
        name:'ftpFileGrid338547183092',
        width:ftpFileGridWidth,
        height:402,
        store:fileSelectorOptions.searchFileStore,
        tbar:new Ext.Toolbar({items:[
            {
                text:'目录',
                xtype:'textfield',
                value:'/',
                id:fileSelectorOptions.dirNameCmpId
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
                store: fileSelectorOptions.searchFileStore,
                paramName:fileSelectorOptions.filterNameId,
                width:80
            })
        ]}),
        bbar:new Ext.PagingToolbar({
            pageSize: defaultPageSize,
            store: fileSelectorOptions.searchFileStore,
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
}
/**
 * 显示主窗口
 * @param selectedFun 回调函数
 * @param parameters
 */
function showFtpSelectWindow(selectedFun, parameters) {
    var oldData = null;
    var showMediaPlayer = false;
    var maxFileNameLength = 4000;
    fileSelectorOptions.selectedFunction = selectedFun;
    fileSelectorOptions.singleFileSelect = parameters.singleSelect;
    if (parameters != null) {
        oldData = parameters.oldData;
        showMediaPlayer = parameters.showMediaPlayer;
        if(parameters.maxFileNameLength){
            maxFileNameLength = parameters.maxFileNameLength;
        }
        fileSelectorOptions.selectFileOnly = parameters.selectFileOnly;
    }


    var ftpFileGrid = getFileListGrid(parameters);
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
        title:'播放器',
        html:'<div style="background-color: black;width:230px;height:210px;"></div>'
    });
    var selectGridRowSpan = 1;
    if (!showMediaPlayer) {
        selectGridRowSpan = 2;
    }
    selectedGrid = new Ext.grid.GridPanel({
        width:230,
        height:showMediaPlayer?190:400,
        rowspan:selectGridRowSpan,
        id:'ftpSelectedFileList338547183092',
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
                    ftpFileWin.close();
                }
            }
        ]
    });
    var winItems = [
        {
            rowspan:2,
            layout:'fit',
            items:ftpFileGrid
        }
    ];
    var colCount = 1;
    var winWidth = ftpFileGrid.width+15;
    if(!parameters.singleSelect){
        colCount = 3;
        winWidth = ftpFileGrid.width+toolBarPanel.width+selectedGrid.width+15;
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
    ftpFileWin = new Ext.Window({
        layout:'table',
        width:winWidth,
        height:400,
        closable: true,
        resizable: true,
        plain: true,
        border: false,
        title:'选择服务器上的文件',
        layoutConfig: {columns:colCount},
        autoHeight:true,
        items: winItems
    });


    ftpFileWin.show();
    if ((!parameters.singleSelect)&&oldData && "" != oldData) {
        addRecords(oldData);
    }
    //searchFileStore.load({params:{start:0, "pageBean.pageSize":defaultPageSize,filePath:'/'}});
    loadFtpDir("/");
}


function removeSelected() {
    var i;
    var row = selectedGrid.getSelectionModel().getSelections();
    var dataSource = selectedGrid.getStore();
    for (i = row.length-1; i >= 0; i--) {
        var record = row[i];
        dataSource.remove(record);
    }
}

function moveSelected(step){
    if(step){}
}
function upSelected() {
    var row = selectedGrid.getSelectionModel().getSelections();
    var i;
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
    var i;
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

    var fileListGrid = Ext.getCmp("ftpFileGrid338547183092");
    if (fileListGrid != null) {
        var row = fileListGrid.getSelectionModel().getSelections();
        var i;
        var len;
        var currentPath = fileSelectorOptions.searchFileStore.baseParams[fileSelectorOptions.filePathServerSideName];
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
        var fileListGrid = Ext.getCmp("ftpSelectedFileList338547183092");
        if (fileListGrid != null) {
            var row = fileListGrid.getStore();
            var i;
            var len;
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

        ftpFileWin.close();
    } catch(e) {
        Ext.MessageBox.alert("异常", "回调选择数据时发生意外：" + e.description);
    }

}

function selectSingleFile(fileName,fileSize,fileDuration,width,height){
    var result = fileSelectorOptions.searchFileStore.baseParams["filePath"];
    if(result==null){
       result = "";
    }
    var repeatTimes= 0;
    result += "/"+fileName;
    while(result.indexOf("%")>=0&&repeatTimes<5){
        result = decodeURI(result);
        repeatTimes ++;
    }
    while(result.indexOf("//")==0&&result.length>1){
        result = result.substring(1);
    }
    if(fileSelectorOptions.selectedFunction!=null&&typeof(fileSelectorOptions.selectedFunction)=='function'){
        fileSelectorOptions.selectedFunction(result,{name:result,width:width,height:height,duration:fileDuration,size:fileSize});
        ftpFileWin.close();
    }else{
        alert(result);
    }
}
function avoidWarning(){
    var step = 0;
    if(moveSelected(step)||gotoDirFromCurrentDir('')||selectSingleFile('')){

    }
}
if(window.location.href=='GoGoGo'){
    avoidWarning();
}
