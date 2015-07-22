var current_id = -1;
var contentProperties=[];
var dataTypeHtml = 12,dataTypeClip=10,dataTypeText= 1,dataTypeTextArea= 2,dataTypeNumber=3;
var dataTypeDate= 4,dataTypeCombo= 5,dataTypeCheckBox= 7,dataTypeRadio= 6,dataTypeWmv=8;
var dataTypeMp4= 10,dataTypeFlv= 9,dataTypePic=11,dataTypeZip=13;
var currentDeviceId=-1,currentModuleId=-1;
var mustSelectUserTypes = false;
var pictureProperties=[];
function appendContentPropertyFields(idx,params,skipFields,id){
    var result = '';
    var willSkipFields = null;
    if(skipFields!=null&&skipFields!=''){
        willSkipFields=    ','+skipFields+',';
    }
    for(var p in params){
        if(params.hasOwnProperty(p)&&p!='property'){
            //stringValue不隐藏
            if(willSkipFields!=null){
                if(willSkipFields.indexOf(','+p+',')>=0){
                    continue;
                }
            }
            var extraId='';
            if(id!=''&&id!=null){
                extraId=' id="'+id+'_'+p+'"';
            }else{
                extraId='';
            }
            result +='<input type="hidden" name="contentProperties['+idx+'].'+p+'"'+extraId+' value="'+params[p]+'">\n';
        }
    }
//    alert(result);
    return result;
}
function getContentPropertyData(contentProperty,fieldName,defaultValue,idx,id,fieldType){
    if(id==null||typeof(id)=='undefined'){
        id='';
    }else{
        id=' id="cp_'+id+'_'+fieldName+'"';
    }
    if(fieldType==null||typeof(fieldType)=='undefined'){
        fieldType='text';
    }
    return '<input type="'+fieldType+'" name="contentProperties['+idx+'].'+fieldName+'"'+id+' value="'+getParameters(contentProperty,fieldName,defaultValue)+'">\n';
}
var refreshByModuleChanged=false;
function getHtmlOf(contentProperties,mergeBaseInfoAndPicInfo,parameters){
    if(typeof(parameters)=='undefined'||parameters == null||parameters==''){
        parameters = {};
    }
    var result = {baseInfo:'',picInfo:'',clipInfo:'',hiddens:'',channels:''};
    var baseInfo = '<div class="form-group" id="movieTypeBox">'+$('#movieTypeBox').html()+'</div>';
    var picInfo = '';
    var clipInfo = '';
    var channels = '';
    var hiddens='';
    pictureProperties=[];
    var labelWidthCls = getParameters(parameters,"labelWidthCls","col-sm-3");
    var fieldWidthCls = getParameters(parameters,"fieldWidthCls","col-sm-11");
    var idx = 0,fileIdx=0;
    var frm =  document.getElementById("movieForm");
    if(frm==null){
        frm =   document.getElementById("modify-form");
    }
    for(var i= 0,l=contentProperties.length;i<l;i++){
        var contentProperty = contentProperties[i];
        var contentId = contentProperty['contentId'];
        var property = contentProperty['property'];
        var dataType = property['dataType'];
        var isMain = property['isMain']==1;
        var code = property['code'];
        var columnName =property['columnName'];
        var fieldName = '';
        var format='';
        var label = property['name'];
        var comment = '';
        var p = label.indexOf('[');
        if(p>0){
            comment = label ;
            format = label.substring(p+1);
            label = label.substring(0,p);
            var firstP=p+1;
            p = comment.indexOf(']');
            if(p>0){
                format = comment.substring(firstP,p);
                comment = comment.substring(p+1);
            }else{
                comment = '';
            }
        }
        var isMultiLine = property['isMultiLine'];
        var id = 'field_'+i;
        var propertyId = contentProperty['propertyId'];
        var value = contentProperty['stringValue'];
        if(code=='CHANNEL_ID'){
            if(channels!=''){
                channels+=',';
            }
            channels+=contentProperty['stringValue'];
            continue;
        }else if(code=='DEVICE'){
            continue;
        }else if(code=='USER_TYPES'){
            select_user_type(value);
            continue;
        }
        if(code=='contentStatus'){
            continue;
        }
        if(isMain==1){
            fieldName = 'obj.'+columnName;
        }else{
            fieldName = 'contentProperties['+idx+'].stringValue';
        }
        if(frm!=null&&refreshByModuleChanged){
            var fieldObject = frm.elements[fieldName];
            if(fieldObject!=null){
                var v = fieldObject.value;
                if(typeof(v)!='undefined'&&v!=null&&v!=''){
                    value = v;
                }
            }
        }else if(!refreshByModuleChanged){
            if(value==''||value==null){
                var valFromParam = parameters[fieldName];
                if(typeof(valFromParam)!='undefined'&&valFromParam!=null&&valFromParam!=''){
                    value = valFromParam;
                }else{
                    valFromParam = parameters[code];
                    if(typeof(valFromParam)!='undefined'&&valFromParam!=null&&valFromParam!=''){
                        value = valFromParam;
                    }
                }
            }
        }

        var fieldNeed = ' filed-need';
        if(property['isNull']==1){
            fieldNeed = '';
        }else{
            fieldNeed = ' filed-need';
        }
        if(dataType>=dataTypePic){//如果是文件
            var tempHtml = '';
            if(mergeBaseInfoAndPicInfo){
                tempHtml+='<div class="form-group posterBlock">\n'+
                    '<label class="'+labelWidthCls+' control-label no-padding-right' + fieldNeed+
                    '">' +label+
                    '</label><div class="col-sm-9">';
                if(comment!=''){
                    tempHtml +='<h6 class="tips">'+comment+'</h6>';
                }
            }else{
                tempHtml+='\t\t<div class="posterBlock"><h5>' +label+'</h5>';
/*
                    '<div class="file-thumb"><img src="'+value+'" class="previewImage" id="previewImage_'+id+'" style="max-height:200px">'+
                    '</div>';
*/
            }
            var imageDisplay = ' showing ';
            if(value==''){
                imageDisplay = ' hidden';
            }

            tempHtml+='<input type="file"  id="'+id+'" class="inputFileClasses" name="files['+fileIdx+']"/>';
            tempHtml+='<div class="posterDiv'+imageDisplay+'" id="imageInfoDiv_'+id+'">' +
                '<img src="'+value+'"'+
                ' class="previewImage" id="previewImage_' +id+'">';
            if(!isMain){//如果不是在主表里，就要做些设置
                tempHtml+='<ul>';
                tempHtml+='<li><label>标题：</label>'+getContentPropertyData(contentProperty,'name','',idx,id)+'</li>';
                tempHtml+='<li><label>序号：</label>'+getContentPropertyData(contentProperty,'intValue','',idx,id)+'</li>';
                tempHtml+='<li><label>扩展：</label>'+getContentPropertyData(contentProperty,'extraData','',idx,id)+'</li>';
                tempHtml+='<li><label>介绍：</label>'+getContentPropertyData(contentProperty,'desp','',idx,id)+'</li>';
                tempHtml+='</ul>';
                hiddens+=appendContentPropertyFields(idx,contentProperty,'name,intValue,extraData,stringValue,desp',id);
            }else{

            }
            tempHtml+='</div>';
            tempHtml +='<input type="hidden" id="inputFileName_'+id+'" name="filesFileName['+fileIdx+']"/>';
            var propertiesData = propertyId;
            if(isMain!=1){
                propertiesData+=','+idx;
                //hiddens+=appendContentPropertyFields(idx,contentProperty,'stringValue',id);
                idx++;
            }else{
                hiddens +='<input type="hidden" id="org_'+id+'" name="'+fieldName+'" value="'+value+'">';
            }
            tempHtml +='<input type="hidden" name="fileProperties['+fileIdx+']" value="'+propertiesData+'">\n';
            fileIdx++;
            if(mergeBaseInfoAndPicInfo){
                baseInfo+=tempHtml+'</div></div>';
            }else{
                picInfo+=tempHtml+'</div>';
            }
            pictureProperties.push({fieldId:'inputFileName_'+id,imageId:'previewImage_'+id,name:label,propertyId:propertyId,id:id,idx:i});
        }else if((dataType==dataTypeWmv||dataType==dataTypeMp4||dataType==dataTypeFlv)) {
            //如果要编辑链接，这里的null要改成'stringValue'，并添加可编辑的field来代替'stringValue'
            hiddens+=appendContentPropertyFields(idx,contentProperty,null,id);
            idx++;
            if(propertyId!=1){
//只显示源链接的话，这里就要跳出
//                continue;
            }
            clipInfo+='<div class="row">' +
                '<div class="col-md-11">'+property['name']+':' + contentProperty['name'] + ':'+contentProperty['stringValue']+'</div>' +
                '<div class="col-md-1 text-right cursor">' +
                '<i class="fa fa-play-circle-o" onclick="play_url(\'' +
                contentProperty['stringValue'] + '\',' + currentDeviceId + ',' + contentId + ','+contentProperty['propertyId']+')">' +
                '</i></div></div>';
        }else{
            baseInfo+='<div class="form-group">\n'+
                '<label class="'+labelWidthCls+' control-label no-padding-right' + fieldNeed+
                '">' +label+
                '</label><div class="col-sm-9">';
            if(dataType==dataTypeText&&isMultiLine!=1){
                baseInfo+='<input type="text" class="'+fieldWidthCls+'" id="field_'+i+'" name="'+fieldName+
                    '" value="' +value+'">';
            }else if(dataType==dataTypeTextArea||isMultiLine){
                baseInfo+='<textArea style="height:100px;" class="'+fieldWidthCls+'" id="field_'+i+
                    '" name="'+fieldName+'">' +value+'</textarea>';
            }else if(dataType==dataTypeNumber){
                baseInfo+='<input type="text" class="numberType '+fieldWidthCls+'" id="field_'+i+'" name="'+
                    fieldName+'" value="' +value+'">';
            }else if(dataType == dataTypeDate){
                ///*
                if(format==''){
                    format = 'yyyymmdd';
                }

                var displayValue= value;
                if(value==null||value==''){
                    displayValue = '';
                }else if(value.length>=8&&value.indexOf('-')<0){
                    displayValue = value.substring(0,4)+'-'+value.substring(4,6)+'-'+value.substring(6,8);
                }
                baseInfo+='<div class="input-group date form_date datepickerForInput '+fieldWidthCls+'"' +
                    ' data-date="'+displayValue+'" data-date-format="'+format+'" data-link-field="' +id+
                    '" data-link-format="'+format+'">'+
                    '<input class="form-control" size="16" type="text" id="displayField_'+fieldName+'" value="'+displayValue+'" readonly>'+
                    '<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>'+
                    '<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>'+
                    '</div>'+
                    '<input type="hidden" name="'+fieldName+'" id="' +id+'" value="'+value+'" /><br/>';
                // */
                //baseInfo += '<input type="text" class="col-sm-12 datepickerForInput" id="' + id + '" name="' + fieldName + '" value="' + value + '">\n';
            }
            baseInfo+='            </div></div>';

            if(isMain!=1){
                hiddens += appendContentPropertyFields(idx,contentProperty,'stringValue',id);
            }
        }
    }
    hiddens+='<input type="hidden" name="obj.deviceId" value="'+currentDeviceId+"'>";
    baseInfo+=hiddens;
    result.baseInfo = baseInfo;
    result.picInfo = picInfo;
    result.hiddens = hiddens;
    result.channels = channels;
    result.clipInfo = clipInfo;
    return result;
}
function getParameters(parameters,name,defaultVal){
    if(parameters==null||typeof(parameters)=='undefined'||parameters==''){
        return defaultVal;
    }
    var val = parameters[name];
    if(val==null||typeof(val)=='undefined'){
        return defaultVal;
    }
    return val;
}

function renderContentDetail(contentProperties,parameters){
    var mergeBaseAndPic = getParameters(parameters,"mergeBaseAndPic",false);
    var html = getHtmlOf(contentProperties,mergeBaseAndPic,parameters);
    var baseInfoBoxId = getParameters(parameters,'baseInfoBoxId','baseInfoBox');
    if(baseInfoBoxId!='hidden'){
        $('#'+baseInfoBoxId).html(html.baseInfo);
    }
    var picInfoBoxId =  getParameters(parameters,'picInfoBoxId','picInfoBox');
    if(picInfoBoxId!='hidden'){
        $('#'+picInfoBoxId).html(html.picInfo);
    }
    var clipInfoBoxId=  getParameters(parameters,'clipInfoBoxId','content-file-container');
    if(clipInfoBoxId!='hidden'){
        $('#'+clipInfoBoxId).html(html.clipInfo);
    }
    $('#movie-type').val(currentModuleId);
    var dateTimePickers = $(".datepickerForInput");
    dateTimePickers.datetimepicker({
        language: 'zh-CN',
        //format:'yyyymmdd',
        weekStart: 1,
        todayBtn: true,
        autoclose: true,
        pickTime: false,
        todayHighlight: 1,
//        startView: 2,
        minView: 2,
        forceParse: false,
        outOfRange:function(ev){

        }
    });
    //dateTimePickers.datetimepicker('update','2015-03-03');
    $(".inputFileClasses").ace_file_input({
        no_file:'未选择文件 ...',
        btn_choose:'选择',
        styleClass:'ace_file_upload_style',
        btn_change:'选择',
        droppable:false,
        whitelist:'png|jpg|jpeg',
        before_change:function(files, dropped) {
            if (files.length > 0) {
                $(this).closest("div.form-group").removeClass("has-error");
                var file = files[0];
                if (typeof file === 'string') { // 不预览海报
                } else if ('File' in window && file instanceof window.File) {
                    var imageObj = $(this).closest("img.previewImage");
                    var posterDiv = $(this).closest("div.posterDiv");
                    var id=this.id;
                    if(id!=null&&!(typeof(id)=='undefined')){
                        posterDiv = $("#imageInfoDiv_"+id);
                        id = "previewImage_"+id;
                        imageObj = $("#"+id);
                    }
                    if(posterDiv!=null){
                        posterDiv.removeClass('hidden');
                        posterDiv.addClass('showing');
                    }
                    $("#inputFileName_"+this.id).val(this.value);
                    readImageFromUploadFile(this, imageObj);
                }
            }
            return true;
        }
    });
    channelReset = false;
    showSelectedChannels(html.channels);
    $("#"+getParameters(parameters,"contentChannelBoxId","contentChannel")).val(html.channels);
}
function readImageFromUploadFile(input,image){
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        //alert($(image).attr("id"));
        reader.onload = function (e) {
            image.attr('src', e.target.result).show();
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function getContentAjax(id,moduleId,deviceId,parameters){
    current_id = id;
    currentModuleId = moduleId;
    currentDeviceId = deviceId;
    channelReset = false;
    $.ajax({
        url:'/content/contentProperty!list.action?obj.contentId='+id+"&moduleId="+moduleId+"&dataType=-1",
        dataType:'json',
        success:function(data){
            contentProperties = data['objs'];
            renderContentDetail(contentProperties,extraContentDisplayParameters);
        }
    });
}
function showSelectedChannels(channels){
    var ele = document.getElementById('tree-channel');
    if(ele==null){
        return;
    }
    delete($('#tree-channel').data().tree);
    $('#tree-channel').remove();
    $("#channel-container").append('<div id="tree-channel" class="tree" style="height: 220px;overflow:scroll"></div>');
    if (!treeUtils) {
        treeUtils = new ChannelUtils();
        treeUtils.initByJson(__channels);
    }
    treeUtils.setSelectedChannel(channels);
    var treeData = treeUtils.generateTreeData();
    show_tree(treeData);
}
var userTypes = [];
function loadUserType(){
    $.ajax({
        type: "POST",
        url: "/user/userType!list.action",
        //dataType: "json",
        dataType: "text",
        //data: {name: ver},
        success: function(msg){
            var response = eval("(function(){return " + msg + ";})()");
            userTypes = response['objs'];
            for(var i=0; i<userTypes.length; i++){
                var type = userTypes[i];
                if(type){
                    $("#user-type-container").append('<div class="checkbox"><label><input type="checkbox" class="ace" name="cb-user-type" ' +
                    'value="' + type.id + '">' +
                    '<span class="lbl">' + type.name + '</span></label></div>');
                }
            }
        }
    });
}
var extraContentDisplayParameters = {};
function getContent(id,moduleId,deviceId,parameters,extra){
    refreshByModuleChanged = false;
    extraContentDisplayParameters = parameters;
    getContentAjax(id,moduleId,deviceId,parameters,extra);
}
function movieTypeChanged(newModuleId){
    if(currentModuleId != newModuleId){
        if(confirm("现在修改模板会导致输入的部分数据由可能丢失，您确认修改类型吗？")){
            refreshByModuleChanged = true;
            getContentAjax(current_id,newModuleId,currentDeviceId,extraContentDisplayParameters);
        }else{
            $("#movie-type").val(currentModuleId);
        }
    }
}

function checkForm() {
    var r = true;
    var logs = '';
    for(var i= 0,l=contentProperties.length;i<l;i++){
        var property = contentProperties[i]['property'];
        var allowEmpty = property['isNull']==1;
        var id = 'field_'+i;
        var field = $("#"+id);
        var val = field.val();
        if(val == null||val==''){
            var inMainTable = property['isMain']==1;
            var orgObj = null;
            orgObj = document.getElementById('org_'+id);
            if(orgObj!=null){
                val = orgObj.value;
            }
            if(val==null||val==''){
                if(!allowEmpty){//如果允许为空，就不检查
                    logs+=(property['name']+"不能为空！");
                    field.closest("div.form-group").addClass("has-error");
                }
            }else{
                try {
                    if(!property['dataType']==dataTypePic){
                        field.val(val);
                    }
                } catch (e) {
                }
            }
        }
    }
    if(logs!=''){
        alert(logs);
        return false;
    }
    return r;
}

function loadMovieType(){
    $.ajax({
        type: "POST",
        url: "/module/module!list.action",
        dataType: "text",
        //data: {recommendId:$("#channel").val(), keyIds:items},
        success: function(msg){
            var response = eval("(function(){return " + msg + ";})()");
            if (response.objs) {
                for (var n = 0; n < response.objs.length; n++) {
                    var cn = response.objs[n];
                    $("#movie-type").append($("<option/>", {
                        value: cn.id,
                        text: cn.name
                    }));
                }
            }
        }
    });
}


function loadChannel() {
    $.ajax({
        type: "POST",
        url: "/publish/channel!channelTree.action",
        dataType: "text",
        success: function(msg){
            __channels = msg;
            if( treeUtils == null){
                treeUtils = new ChannelUtils();
                treeUtils.initByJson(__channels);
            }

            showFilterTree(treeUtils.generateFilterTree());
        }
    });
}

function showFilterTree(data){
    $('#filter-channel').ace_tree({
        dataSource: data ,
        multiSelect:false,
        loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
        'open-icon' : 'ace-icon tree-minus',
        'close-icon' : 'ace-icon tree-plus',
        'selected-icon' : null,
        'unselected-icon' : null,
        'selectable' : true
    });
    $('#filter-channel').on('updated', function(e, result) {
        if(result && result.info && result.info.length > 0){
            $("#selectedChannel").html(result.info[0].name);
            filter_channel_id = result.info[0].id;
            // trigger refresh
            get_all_item();
        }
    });
}

function show_tree(data){
    var channelTree = $('#tree-channel');
    channelTree.ace_tree({
        dataSource: data ,
        multiSelect:true,
        loadingHTML:'<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
        'open-icon' : 'ace-icon tree-minus',
        'close-icon' : 'ace-icon tree-plus',
        'selected-icon' : 'ace-icon fa fa-check',
        'unselected-icon' : 'ace-icon',
        'selectable' : true
    });
    channelTree.on('updated', function(e, result) {
        channelReset = true;   // 没有在树上操作时，selectedItem取不到，如果没有操作过，使用原来的值
    });
}
function save_modify(){
    // 检查输入
    var r = checkForm();
    if(!r){
        $("#tab_baseinfo").tab('show');
        return;
    }

    var channelItems = $('#tree-channel').tree('selectedItems');
    var channel_array = "";
    if(!channelReset){
        channel_array = $("#contentChannel").val();
    }else{
        if(channelItems && channelItems.length>0 ){
            for(var i=0; i<channelItems.length; i++){
                channel_array += (i == 0)? + channelItems[i].id : "," + channelItems[i].id;
            }
            //alert(channel_array);
            $("#contentChannel").val(channel_array);
        }
    }
    if( channel_array == ""){
        alert("请设置视频所属栏目！");
        $("#tab_channel").tab('show');
        return;
    }
    var userTypeSelected =$( "input[name='cb-user-type']:checked" );
    if(userTypes!=null&&userTypes.length>0){
        if(userTypeSelected.length == 0){
            if( mustSelectUserTypes){
                alert("请选择可以观看该视频的用户类型！");
                $("#tab_channel").tab('show');
                return;
            }else{
                $("#contentUserType").val("");
            }
        }else{
            var userTypeValues = [];
            userTypeSelected.each(function(i){
                userTypeValues.push( $(this).val());
            });
            $("#contentUserType").val("," + userTypeValues.join(",") + ",");
        }
    }else{
        $("#contentUserType").val("");
    }

    //alert($("#content_id").val());
    $("#content_id").val(current_id);

    // submit
    $("#modify-form").submit();
}

function select_user_type(types){
    if(types){
        while(types[0]==','&&types.length>0){
            types = types.substring(1);
        }
        while(types[types.length-1]==','&&types.length>0){
            types = types.substring(0,types.length-1);
        }
        var type_array = types.split(",");
        for(var i=0; i<type_array[i]; i++){
            jQuery("input[name='cb-user-type']").each(function() {
                if( $.inArray(this.value, type_array) >= 0 ){
                    $(this).attr("checked", "checked");
                    $(this).prop('checked', true);
                }else{
                    $(this).removeAttr("checked");
                    $(this).prop('checked', false);
                }
            });
        }
    }
}
/*

function select_user_type(types){
    if(types){
        var type_array = types.split(",");
        jQuery("input[name='cb-user-type']").each(function() {
            if( $.inArray(this.value, type_array) >= 0 ){
                $(this).attr("checked", "checked");
                $(this).prop('checked', true);
                //console.info("select value:" + this.value);
            }else{
                $(this).removeAttr("checked");
                $(this).prop('checked', false);
                //console.info("unselect value:" + this.value);
            }
        });
    }
}

//*/
function play_url(url, device_id, content_id,property_id){
    $.ajax({
        type: "POST",
        url: "../V5/media/getFlashPlayer.jsp",
        //dataType: "json",
        dataType: "text",
        data: {urlPlay:url,propertyId:property_id,deviceId:device_id,contentId:content_id},
        success: function(msg){
            var response = eval("(function(){return " + msg + ";})()");
            var playUrl = "../V5/media/getPlayUrl.jsp?deviceId=" + device_id;
            //$("#player_container").html('<div id="ck_player" style="width:400px;height:200px;background-color:green"></div>');
            //$("#test_container").show();
            //playMovie(playUrl,0,'/ckplayer/swf/','ckplayer_preview','ck_player', 400, 200);
            $("#float_player").show();
            playMovie(playUrl,0,'/ckplayer/swf/','ckplayer_preview','fl_player', 320, 240);
        }
    });
}
