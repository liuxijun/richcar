var pubFileOptions={
    innerIp:'10.0.66.11',
    innerPort:8080,
    loading:false,
    operator:1,
    cspId:2,
    listMovieUrl:'../system/device!listLocalFiles.action',
    nextPagePrepareUrl:'../media/rp-m!mediaPropPrepare.action',
    nextPageUrl:'../media/rp-m!mediaProp.action',
    currentPath:'/',
    currentDeviceId:-1,
    sn:0,
    currentFileStore:[],
    selectedFileStore:[],
    devices:{},
    limit:10,
    sort:'name',
    dir:'asc',
    filter:'*',
    streamServerCount:2,
    serverSideFileGridId:'serverSideFiles',
    selectedFilesId:'selectedFiles',
    init:function(){
        $.ajax({
            url:'/system/device!list.action?obj.type=2',
            dataType:'json',
            success:function(jsonData){
                if(jsonData!=null){
                    var i= 0,l=jsonData.objs.length;
                    var selectObj = $("#streamServerUrl");
                    selectObj.html('');
                    var selected =  l==1;
                    pubFileOptions.devices = [];

                    for(;i<l;i++){
                        var d = jsonData.objs[i];
                        var port = d['monitorPort'];
                        if(port==null||typeof(port)=='undefined'){
                            port=80;
                        }
                        var serverIp = d['ip'];
                        if(serverIp==null||serverIp=='serverIP'||serverIp=='serverIp'){
                            serverIp='';
                        }else{
                            //如果是潍柴内部IP，做IP的翻译 todo 这部分代码需要进行优化 by xjliu 20141122
                            if(d['id']==9){
                                serverIp = pubFileOptions.innerIp;
                                port = pubFileOptions.innerPort;
                            }else{

                            }
                            serverIp = 'http://'+serverIp+':'+port;
                        }
                        serverIp+='/do.upload';
                        var deviceInfo={id: d.id,name: d.name,url:serverIp};
                        pubFileOptions.devices['device_'+ d.id]=serverIp;
                        var option = selectObj.append($('<option/>',{
                            value: deviceInfo.id,
                            selected:selected,
                            text: deviceInfo.name
                        }));
                    }
                    if(l>=1){
                        $("#streamServerContainer").show();
                    }else{
                        $("#streamServerContainer").hide();
                    }
                }
            }
        });
    },
    serverChanged:function(serverId){
        pubFileOptions.currentDeviceId = serverId;
        pubFileOptions.loadMovieFiles('/',0,pubFileOptions.limit);

    },

    loadMovieFiles:function(filePath,start,limit){
        if(pubFileOptions.loading){
            alert("正在加载，请勿多次点击！请稍候！");
            return;
        }
        pubFileOptions.loading = true;
        pubFileOptions.currentPath=filePath;
        pubFileOptions.limit = limit;
        pubFileOptions.start = start;
        pubFileOptions.filter = $("#search_word").val();
        $.ajax({
            url:pubFileOptions.listMovieUrl,
            dataType:'json',
            data:{
                "obj.id":pubFileOptions.currentDeviceId,start:start,limit:limit,
                filePath:encodeURI(encodeURI(filePath)),
                sort: pubFileOptions.sort,dir:pubFileOptions.dir,filter:pubFileOptions.filter
            },
            success:function(jsonData){
                pubFileOptions.loading = false;
                if(jsonData){
                    var i= 0,l=jsonData.objs.length;
                    var o=pubFileOptions;
                    o.currentFileStore = jsonData.objs;
                    $("#serverSideFileCaption").html("服务器端媒体文件:"+o.currentPath);
                    var html = '';
                    var onclickEvent;
                    var title=null;
                    if(o.currentPath!='/'){
                        onclickEvent = 'pubFileOptions.goToParentDir()';
                        title = '..(上一级目录)';
                    }else if(o.currentDeviceId>=0){
                        onclickEvent = 'pubFileOptions.selectServer()';
                        title = '选择服务器';
                    }
                    if(title!=null&&title!=''){
                        html+='<tr><td colspan="3" onclick="' +onclickEvent+
                            '" style="cursor:pointer;color:blue;">' +title+
                            '</td></tr>';
                    }
                    for(;i<l;i++){
                        var item = jsonData.objs[i];
                        onclickEvent="pubFileOptions.selectFile";
                        title = "选择";
                        var nameClick="";
                        if(item.directory){
                            if(item.type == 'streamServer'){
                                onclickEvent = "pubFileOptions.loadServer";
                            }else{
                                onclickEvent = "pubFileOptions.loadDir";
                            }
                            nameClick = ' style="cursor:pointer;color:blue" onclick="'+onclickEvent+'('+i+')"';
                            title="进入目录";
                        }
                        html +='<tr>\r\n'+
                            '<td '+nameClick+'>'+ o.getMediaName(item)+'</td>\r\n'+
                            '<td class="center">'+o.getMediaInfo(item)+'</td>\r\n'+
                        '<td class="center">'+ o.getMediaDate(item)+'</td>\r\n'+
                        '<td class="center"><a class="btn btn-btn" onclick="'+onclickEvent+'('+i+')">' +title+
                            '</a> </td>\r\n'+
                        '</tr>\r\n';
                    }
                    pubFileOptions.renderTo(pubFileOptions.serverSideFileGridId,html);
                    var currentPage = Math.ceil((o.start + o.limit -1)/o.limit);
                    rebuild_page_nav($("#page-nav"), Math.ceil(jsonData.totalCount /o.limit),currentPage, "pubFileOptions.goToPage", jsonData.totalCount);
                }
            }
            });
    },

    getMediaProperties:function(item,propertyNames,defaultValue){
        for(var i= 0,l=propertyNames.length;i<l;i++){
            var propertyName = propertyNames[i];
            var val = item[propertyName];
            if(val!=null&&typeof(val)!='undefined'){
                return val;
            }
        }
        return defaultValue;
    },
    getMediaDate:function(item){
        return pubFileOptions.getMediaProperties(item,["modifyDate","StartTime"],'');
    },
    getMediaName:function(item){
        return pubFileOptions.getMediaProperties(item,["name","UploadId"],'');
    },
    getMediaInfo:function(media){
        if(media.directory){
            return "目录";
        }
        var result = "";
        var width = media['width'];
        if(width==null){
            var uploadBytes = media['UploadBytes'];
            if(uploadBytes!=null){
                result += "上传："+pubFileOptions.formatFileSize(parseInt(uploadBytes));
            }
            var remoteIp = media['RemoteIP'];
            if(remoteIp!=null){
                if(result!=""){
                    result+=",";
                }
                result +="远端："+remoteIp;
            }
            return result;
        }else{
            if(width==-1){
                result = "非视频文件,";
            }else{
                if(media.videoCodec!=""){
                    result += media.videoCodec+",";
                }
                if(media.audioCodec!=""){
                    result+=media.audioCodec+",";
                }
                result +=width+'x'+media.height+','+this.formatTime(media.length)+',';
            }
            return result+this.formatFileSize(media.size);
        }
    },
    formatFileSize: function (bytes) {
        if (typeof bytes !== 'number') {
            return '';
        }
        if (bytes >= 1000000000) {
            return (bytes / 1000000000).toFixed(2) + ' GB';
        }
        if (bytes >= 1000000) {
            return (bytes / 1000000).toFixed(2) + ' MB';
        }
        return (bytes / 1000).toFixed(2) + ' KB';
    },

    formatBitrate: function (bits) {
        if (typeof bits !== 'number') {
            return '';
        }
        if (bits >= 1000000000) {
            return (bits / 1000000000).toFixed(2) + ' Gbit/s';
        }
        if (bits >= 1000000) {
            return (bits / 1000000).toFixed(2) + ' Mbit/s';
        }
        if (bits >= 1000) {
            return (bits / 1000).toFixed(2) + ' kbit/s';
        }
        return bits + ' bit/s';
    },

    formatTime: function (seconds) {
        var date = new Date(seconds * 1000),
            days = parseInt(seconds / 86400, 10);
        days = days ? days + 'd ' : '';
        return days +
            ('0' + date.getUTCHours()).slice(-2) + ':' +
            ('0' + date.getUTCMinutes()).slice(-2) + ':' +
            ('0' + date.getUTCSeconds()).slice(-2);
    },
    addFileManual:function(){
        var url = $("#mediaFileUrl").val();
        if(url!=''&&url!=null&&!(typeof(url) == 'undefined')){
            var path = this.currentPath;
            if(path==null||path==''||typeof(path=='undefined')){
                path = '/';
            }
            path = path + url;
            var o=pubFileOptions;
            var name = url;
            var p = name.lastIndexOf("/");
            if(p>0){
                name = name.substring(p);
            }
            var willAddItem = {path:path,name:name,resolutionWidth:0,sn:o.sn,
                resolutionHeight:0,origFileName:name,duration:0};
            o.sn++;
            o.addFile(willAddItem);
        }
        $("#fileDetailModal").modal('hide');
    },
    showAddFileDialog:function(){
        if(pubFileOptions.currentDeviceId<=0){
            alert("请先选择一个服务器，然后再选择手工添加！");
            return;
        }
            showDialog({
                title:'手工输入链接',
                id:'fileDetailModal',renderTo:'modalDialog',

                items:[
                    {fieldLabel:'连接：',id:'mediaFileUrl'}
                ],
                buttons:[
                    {text:'确定',cls:'btn-blue',handler:pubFileOptions.addFileManual},
                    {text:'关闭',style:'margin-left:100px;',handler:function(){
                            $("#fileDetailModal").modal('hide');
                        }
                    }
                ]
            });
    },
    addFile:function(willAddItem,keepQuite){
        var selectedStore = this.selectedFileStore;
        l=selectedStore.length;
        for(var i=0;i<l;i++){
            var selectedItem = selectedStore[i];
            if(selectedItem.path == willAddItem.path){
                if(typeof(keepQuite)!='undefined'&&!keepQuite){
                    alert("文件："+willAddItem.name+"已经添加，无法重复添加");
                }
                return false;
            }
        }
        pubFileOptions.fillNameToStore();
        selectedStore.push(willAddItem);
        this.renderSelected(selectedStore);
        return true;
    },
    selectFile:function(idx){
        var store = this.currentFileStore;
        var l=store.length;
        if(isNaN(idx)||idx<0||idx>=l){
            return false;
        }
        var item = store[idx];
        var o = pubFileOptions;
        var name = o.getMediaProperties(item,['name','UploadId'],null);
        if(name!=null){
            var path = this.currentPath+name;
            var height = o.getMediaProperties(item,['height'],0);
            var width = o.getMediaProperties(item,['width'],0);
            var length = o.getMediaProperties(item,['length'],0);
            var willAddItem = {path:path,name:name,resolutionWidth:width,sn:o.sn,
                resolutionHeight:height,origFileName:name,duration:parseInt(length)};
            o.sn++;
            o.addFile(willAddItem);
            return true;
        }else{
            alert('无法添加当前的媒体！');
            return false;
        }
    },
    deleteSelectedByUrl:function(url){
        var store = this.selectedFileStore;
        var l=store.length;
        var idx = -1;
        var ii=0;
        for(;ii<l;ii++){
            var item = store[ii];
            if(item.path == url){
                idx = ii;
                break;
            }
        }
        if(isNaN(idx)||idx<0||idx>=l){
            return false;
        }
        pubFileOptions.fillNameToStore();
        store.splice( idx, 1 );
        this.renderSelected(store);
        return true;
    },
    deleteSelected:function(idx){
        var store = this.selectedFileStore;
        var l=store.length;
        if(isNaN(idx)||idx<0||idx>=l){
            return false;
        }
        /*
        var i=idx;
        l--;
        for(;i<l;i++){
            store[i]=store[i+1];
        }
        store.length = store.length-1;
        //  */
        store.splice( idx, 1 );
        this.renderSelected(store);
        return true;
    },
    renderSelected:function(store){
        if(typeof store == 'undefined'){
            store = this.selectedFileStore;
        }
        var i= 0,l=store.length;
        var html ="";
        if(l>=1){
            $("#streamServerUrl").attr("disabled",true);
            for(;i<l;i++){
                var item = store[i];
                //var formEleName = 'contentFiles['+i+'].name';
                var name = null;//this.getFormValue(formEleName);
                if(name==null){
                    name = item.name;
                }
                if(name==null){
                    name = '';
                }
                var repeatTimes = 0;
                while(repeatTimes<5&&name.indexOf("%")>=0){
                    repeatTimes++;
                    name = decodeURI(name);
                }
                var id= "contentFiles["+item.sn+"].name";
                html +=
                    "<tr>\n" +
                    "    <td>\n"+
                    "        <fieldset><label for='clipName_"+i+"'>第" +(i+1)+"部（集），名称:</label><input style='width:200px;' " +
                    "' name='"+id+"' type='text' value='" +name+
                    "' id='clipName_"+i+"'><label>，文件链接：" +item.path+
                    "</label></fieldset>\n" +
                    "    </td>\n" +
                    "    <td class='center'>\n" +
                    "        <button class='btn btn-grey btn-xs' onclick='pubFileOptions.upSelected("+i+")'>\n" +
                    "            <i class='ace-icon fa fa-arrow-circle-up bigger-110 icon-only'></i>\n" +
                    "        </button>\n" +
                    "        <button class='btn btn-grey btn-xs' onclick='pubFileOptions.downSelected("+i+")'>\n" +
                    "            <i class='ace-icon fa fa-arrow-circle-down bigger-110 icon-only'></i>\n" +
                    "        </button>\n" +
                    "        <button class='btn btn-grey btn-xs' onclick='pubFileOptions.deleteSelected("+i+")'>\n" +
                    "            <i class='ace-icon fa fa-trash-o bigger-110 icon-only'></i>\n" +
                    "        </button>\n" +
                    "    </td>\n" +
                    "</tr>\n";

            }
        }else{
            $("#streamServerUrl").attr("disabled",false);
        }
        html+="";
        pubFileOptions.renderTo(this.selectedFilesId,html);
    },
    swapSelected:function(from ,to){
        var store = pubFileOptions.selectedFileStore;
        var l=store.length;
        if(from<0||to<0||from>=l||to>=l){
            return false;
        }
        pubFileOptions.fillNameToStore();
        var fromRec = store[from];
        var toRec = store[to];
        store[from]=toRec;
        store[to]=fromRec;
        pubFileOptions.renderSelected(store);
        return true;
    },
    fillNameToStore:function(){//把用户输入的片段名称存储到store中
        var store = pubFileOptions.selectedFileStore;
        var l=store.length;
        var i=0;
        for(;i<l;i++){
            var item = store[i];
            item['name']=$("#clipName_"+i).val();
        }
    },
    upSelected:function(idx){
        return pubFileOptions.swapSelected(idx,idx-1);
    },
    downSelected:function(idx){
        return pubFileOptions.swapSelected(idx,idx+1);
    },
    goToParentDir:function(){
        var path = this.currentPath;
        var l = path.length-1;
        while(l>=0){
            l--;
            if(path.charAt(l)=='/'){
                path = path.substring(0,l);
                this.loadMovieFiles(path+'/',0,this.limit);
                return;
            }

        }
    },
    selectServer:function(){
        if(pubFileOptions.selectedFileStore.length>0){
            alert("已经选择了文件，不能再更改服务器！");
            return;
        }
        pubFileOptions.currentDeviceId = -1;
        pubFileOptions.loadMovieFiles('/',0,pubFileOptions.limit);
    },
    loadServer:function(idx){
        var deviceId=pubFileOptions.currentFileStore[idx].size;
        pubFileOptions.currentDeviceId = deviceId;
        $("#streamServerUrl").val(deviceId);
        pubFileOptions.loadMovieFiles('/',0,pubFileOptions.limit);
    },
    loadDir:function(idx){
        var dirName=pubFileOptions.currentFileStore[idx].name;
        pubFileOptions.loadMovieFiles(pubFileOptions.currentPath+dirName+'/',0,pubFileOptions.limit);
    },
    goToPage:function(pageNo){
        pubFileOptions.loadMovieFiles(pubFileOptions.currentPath,(pageNo-1)*pubFileOptions.limit,pubFileOptions.limit);
    },
    renderTo:function(id,html){
        $("#"+id).html(html);
    },
    getFormValue:function(id){
        var form = document.forms[0];
        if(form==null){
            var ele = document.getElementById(id);
            if(ele!=null){
                return ele.value;
            }
            return null;
        }
        if(form[id]!=null){
            return form[id].value;
        }
        return null;
    },
    collectData:function(nullConfirmMessage,mustSelectFile){
        var data = {};
        var store = pubFileOptions.selectedFileStore;
        var i= 0,l=store.length;
        if(typeof(fileCount)!='undefined'){
            if(fileCount>0){
                if(!confirm("还有上传的文件在处理或者等待处理！您确认继续操作码？\r\n如果继续操作，会放弃上传过程！")){
                    return null;
                }
            }
        }
        if(typeof(nullConfirmMessage)=='undefined'||nullConfirmMessage==null||nullConfirmMessage==''){
            nullConfirmMessage = "没有选择任何数据，您确认要继续操作吗？";
        }

        if(l<=0){
            if(mustSelectFile){
                alert("没有选择任何数据文件，无法继续操作！");
                return null;
            }else if(!confirm(nullConfirmMessage)){
                return null;
            }
        }
        for(;i<l;i++){
            var item = store[i];
            for(var d in item){
                if(item.hasOwnProperty(d)){
                    var name = "contentFiles["+i+"]."+d;
                    var value =null;
                    if(d=="name"){
                        value = pubFileOptions.getFormValue("contentFiles["+item.sn+"].name");
                    }
                    if(value==null){
                        value = item[d];
                    }
                    if(d=="name"||d=="path"||d=="origFileName"){
                        value = encodeURI(value);
                    }
                    data[name]=value;
                }
            }
        }
        var deviceId=pubFileOptions.currentDeviceId;
        if(deviceId==''||deviceId=='-1'||deviceId<0){
            deviceId = $("#streamServerUrl").val();
            pubFileOptions.currentDeviceId =deviceId;
        }
        data['obj.moduleId']=$("movie-type").val();
        data["obj.deviceId"]=deviceId;
        return data;
    },
    postToNextPage:function(){
        var data=pubFileOptions.collectData();
        if(data==null){
            return;
        }
        $.ajax({
            url:pubFileOptions.nextPagePrepareUrl,
            dataType:'json',
            data:data,
            method:'post',
            success:function(){
                //alert("处理完毕，准备下一步");
                window.location.href=pubFileOptions.nextPageUrl+"?obj.deviceId="+pubFileOptions.currentDeviceId
                +"&obj.moduleId="+$("#movie-type").val();
            }
        });

  },
  getServerUrl:function(){
      var streamServerId = $("#streamServerUrl").val();
      if(streamServerId!=null){
          var streamServerUrl = pubFileOptions.devices['device_'+streamServerId];
          if(streamServerUrl!=null){
              return streamServerUrl;
          }
      }
      return "";
  },
    getSelectedDeviceId:function(){
       return $("#streamServerUrl").val();
    }
};