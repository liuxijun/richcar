var fileCount = 0;
var fileExts = "*.wmv; *.asf; *.avi; *.rm; *.rmvb; *.mkv; *.mp4; *.3gp; *.ts; *.flv; *.mpg; *.mov; *.mpeg; *.aac; *.mp3;" +
    " *.gif; *.jpg; *.png; *.pdf; *.doc; *.ppt; *.txt; *.docx; *.xlsx; *.xls";
$(function() {
    $("#upload").uploadify({
        'height'        : 32,
        'width'         :99,
        'auto'          : false,
        'multi'         : false,
        'successTimeout':84000,//24小时
        'uploadLimit' : 15,
        'buttonText'    : '选择文件',
        'fileObjName'  : 'videoFile',
        'swf'           : '/style/uploadify.swf',
        'fileTypeExts' : fileExts,
        'uploader'     : '../media/do.upload?adminId='+pubFileOptions.operator+"&cspId="+pubFileOptions.cspId,
        'queueID':'fileQueue',
        'formData'   : {},
        'overrideEvents' : ['onCancel','onSelectError'],
        //'uploadScript'       : '/content/uploader!upload.ql',
        'onSelect': function (file) {
           //alert("b.fileCount");
        },
        'onDialogClose' : function(d){
            fileCount = d.filesQueued;
        },
        'onCancel':function(f){
            //fileCount--;
            //alert(f.name + "取消上传！");
        },
        'onSelectError':function(){alert("最多只能选择15个文件上传！");},
        'onUploadSuccess' : function(file, data, response) {
            // 发布成功，转至转码设置页面
            //alert(data);
            var obj = JSON.parse(data);
            //alert(obj.path);
            if(Object.prototype.toString.call(obj) == '[object Array]'){
                if(obj.length>0){
                    obj = obj[0];
                }
            }
            if (obj.success) {
                //location.href = "../content/vod!trans.ql?vodId=" + obj.vodId;
               if(typeof(pubFileOptions)!='undefined'){
                   var item =obj.fileInfo;
                   if(typeof(item)=='undefined'||item==null){
                       item = {width:0,height:0,name:'',length:0,size:obj.size};
                   }
                   pubFileOptions.addFile({path:'/'+obj.url,name:obj.name,resolutionWidth:item.width,sn:pubFileOptions.sn++,
                       resolutionHeight:item.height,origFileName:obj.name,duration:parseInt(item.length),size:item.size}
                       ,true);
                   renderDownloadFiles(pubFileOptions);
               }
            } else {
                alert("文件上传失败，错误信息：" + data.errorMessage);
            }
            fileCount--;
        },
        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
            fileCount--;
            alert('文件' + file.name + '上传终止：' + errorString);
        }
    });

    $("#toList").button({icons: { primary: "ui-icon-arrowreturnthick-1-w"}}).click(function(){location.href = "../content/vod!vodList.ql";});
    $("#startUpload").button({icons: { primary: "ui-icon-circle-check"}}).click(function(){
        var uploadObj = $("#upload");
        if(parseInt(fileCount) > 0){
            var streamServerUrl = pubFileOptions.getServerUrl()+"?deviceId="+pubFileOptions.getSelectedDeviceId();
            if(streamServerUrl!=null){
                uploadObj.uploadify('settings','uploader',streamServerUrl+"&adminId="+pubFileOptions.operator+
                    "&cspId="+pubFileOptions.cspId);
            }
            uploadObj.uploadify('upload','*');
        }else{
            alert("请先选择要上传的媒体文件！");
        }
    });
    $("#cancelUpload").click(function(){
        if(confirm("取消所有上传吗？")){
            $("#upload").uploadify('cancel');
        }
    });
});
function renderDownloadFiles(o) {
    var result = '<tr class="template-download"><td class="name" colspan="2">已上传清单</td><td class="size">大小</td><td colspan="2"></td><td>删除</td></tr>';
    var serverUrl = pubFileOptions.getServerUrl();
    for (var i = 0, file; file = o.selectedFileStore[i]; i++) {
        var fileName = decodeURI(file.name);
        result += '<tr class="template-download">';
        if (file.error) {
            result += '<td></td>' +
                '<td class="name"><span>'+fileName+'</span></td>' +
                '<td class="size"><span>'+o.formatFileSize(file.size)+'</span></td>' +
                '<td class="error" colspan="2"><span class="label label-important">出错</span> '+file.error+'</td>';
        }else{
            result += '<td class="preview">';
            if (file.thumbnail_url) {
                result += '<a href="' + file.path + '" title="' + file.name + '" rel="gallery" download="' + file.name + '"><img src="' + file.thumbnail_url + '"></a>';
            }
            result += '</td>'+
                '<td class="name">' +
                '<a href="' +serverUrl+
                '?getfile=' + file.path + '" title="' + fileName + '" rel="gallery" download="' + fileName + '">' + fileName + '</a>' +
                '</td>' +
                '<td class="size"><span>' + o.formatFileSize(file.size) + '</span></td>' +
                '<td colspan="2"></td>';
        }
        result += '<td class="delete">' +
            '<button class="btn btn-danger" onclick="deleteUploadFile(\'' + file.path+
            '\')" data-type="GET" data-url="' +serverUrl+
            '?delfile=' + file.path + '">' +
            '<i class="icon-trash icon-white"></i>' +
            '<span>删除</span>' +
            '</button>' +
            //'<input type="checkbox" name="delete" value="1">' +
            '</td>' +
            '</tr>';
    }
    $(".files").html(result);
    return result;

}
function deleteUploadFile(url){
    if(!confirm("您确认要删除这个文件吗？\r\n"+url)){
        return;
    }

    $.ajax({
        url:'do.upload?delfile='+url,
        method:'GET',
        dataType:'json',
        success:function(jsonData){
            var obj = jsonData
            if(obj.success){
                alert("文件已经删除："+obj.url);
                pubFileOptions.deleteSelectedByUrl(obj.url);
                renderDownloadFiles(pubFileOptions);
            }else{
                alert("删除过程中发生异常："+obj.message);
            }
        }
    });
}