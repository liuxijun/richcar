var mediaClips = new Array;

function MediaClip(clipId,clipName,clipUrl,mediaId,orderNumber){
    this.clipId = clipId;
    this.clipName = clipName;
    this.clipUrl = clipUrl;
    this.mediaId = mediaId;
    this.orderNumber = orderNumber;
}

function retriveAllClips(){
    var i=0;
    for(i=0;i<mediaClips.length;i++){
        mediaClips[i]=getMediaClip(i);
    }
}
function outPutAllClips(){
    var i=0;
    var allStr = "<table><tr><td align='center'>片段名</td><td>顺序</td><td align='center'>连接</td>" +
                 "<td align='center'>操作</td></tr>\n";
    for(i=0;i<mediaClips.length;i++){
         allStr += mediaClipToStr(i,mediaClips[i]);
    }
    allStr += "</table>\n";
    return allStr;
}

function displayAllClips(displayDivName){
    var displayDiv = document.getElementById(displayDivName);
    displayDiv.innerHTML = outPutAllClips();
}

function mediaClipToStr(displayId,mediaClip){
    if(!mediaClip){
        return "";
    }
    var resultStr = "<tr>\n";
    resultStr += '<td><input class="inputCheckBox" type="checkbox" name="mediaClips[' +displayId+
                 '].mediaClipId"  value="' +
                 displayId+'"/>'+
                 '                         <input class="inputNumber" type="text" name="mediaClips[' +displayId+
                 '].name" value="' +mediaClip.clipName+
                 '"/></td><td width="10">'+
                 '                     <input type="hidden" name="mediaClips[' +displayId+
                 '].mediaId" value="' +  mediaClip.mediaId+
                 '"/><input style="width:40" type="text" name="mediaClips[' +displayId+
                 '].orderNumber" value="' +  displayId+
                 '"/>'+
                 '                          </td>'+
                 '                     <td><input class="clipUrl" type="text" name="mediaClips[' +displayId+
                 '].playUrl" value="' + mediaClip.clipUrl+
                 '"/></td><td><a href="javascript:selectMediaFile(' +displayId+
                 ')">查找文件</a>|' +
                 '<a href="javascript:testMediaFile(' + displayId+
                 ')">测试连接</a></td>\n';
    resultStr += '</tr>\n';
    return resultStr;
}

function getFormElementOfProperty(clipId,propertyName){
    var currentForm = document.forms[0];
    if(currentForm){
        return currentForm.elements("mediaClips["+clipId+"]."+propertyName);
    }
    return null;
}
function getValueOf(clipId,propertyName){
    var ele = getFormElementOfProperty(clipId,propertyName);
    if(ele){
        return ele.value;
    }
    return "";
}
function deleteFromArray(dataArray,index){
    var i;
    if(index>=dataArray.length){
        return;
    }
    for(i=index;i<dataArray.length-1;i++){
        dataArray[i] = dataArray[i+1];
    }
    dataArray.length --;
}
function deleteClip(displayId){
    deleteFromArray(mediaClips,displayId);
}

function addMediaClip(clipId,clipName,clipUrl,mediaId,orderNumber){
    mediaClips[mediaClips.length]=new MediaClip(clipId,clipName,clipUrl,mediaId,orderNumber);
}

function getMediaClip(clipId){
    return new MediaClip(getValueOf(clipId,"mediaClipId"),getValueOf(clipId,"name"),getValueOf(clipId,"playUrl"),
            getValueOf(clipId,"mediaId"),getValueOf(clipId,"orderNumber"));
}

function getClipNumber(clipNumber){
    var resultStr = "";
    if(clipNumber<10){
        resultStr += "00";
    }else if(clipNumber<100){
        resultStr += "0";
    }
    return resultStr += ""+clipNumber;
}
function addClips(mediaId){
    retriveAllClips();
    var newClipUrl = "";
    var newClipName = "第"+getClipNumber(mediaClips.length+1)+"集";
    if(mediaClips.length>0){
        var lastClip = mediaClips[mediaClips.length-1];
        newClipUrl = incFileNo(lastClip.clipUrl);
        newClipName = incFileNo(lastClip.clipName);
    }
    addMediaClip(mediaClips.length,newClipName,newClipUrl,mediaId,mediaClips.length);
    displayAllClips("mediaClipsDiv");
}

function removeClips(){
    retriveAllClips();
    var i=0;
    for(i=mediaClips.length-1;i>=0;i--){
        var clip = mediaClips[i];
        var mediaClipId = getFormElementOfProperty(clip.clipId,"mediaClipId");
        if(mediaClipId){
            if(mediaClipId.checked){
                deleteClip(i);
            }
        }
    }
    displayAllClips("mediaClipsDiv");
}
function upClips(){
    var i=0;
    for(i=0;i<mediaClips.length;i++){
        var clip = mediaClips[i];
        var mediaClipId = getFormElementOfProperty(clip.clipId,"mediaClipId");
        if(mediaClipId){
            if(mediaClipId.checked){
                if(clip.orderNumber<=0){
                    break;
                }

            }
        }
    }
}
function downClips(){
    
}
function selectMediaFile(clipIndex){
   if(clipIndex>=mediaClips.length|| clipIndex<0){
        return;
   }
    var clip = mediaClips[clipIndex];
    var aForm = document.forms[0];
    if(aForm!=null){
        var elementOfUrl = aForm.elements("mediaClips[" +clipIndex+"].playUrl");
        var elementOfName = aForm.elements("mediaClips[" +clipIndex+"].name");
        var elementTypeId = aForm.elements("obj.typeId");

        if(elementOfUrl!=null){
            var serverId = aForm.elements["obj.serverId"];
            if(serverId!=null){
                if(serverId.value>0){
                    var reqUrl = "mediaFtpList.jsp?keyId=" + serverId.value+
                                 "&clipUrl="+(clip.clipUrl)+"&time="+(new Date()).getTime()+
                            "&typeId="+elementTypeId.value;
                    //alert(reqUrl);
                    showSelectDialog(elementOfUrl,elementOfName,reqUrl);
                }
            }
        }
    }
}
function testMediaFile(clipIndex){
    if(clipIndex>=mediaClips.length|| clipIndex<0){
         return;
    }
    var clip = mediaClips[clipIndex];
    var aForm = document.forms[0];
    if(aForm!=null){
        var serverId = aForm.elements["obj.serverId"];
        if(serverId!=null){
            if(serverId.value>0){
                var reqUrl = "../device/serverAction-" + serverId.value+
                             "!preview.action?clipUrl="+clip.clipUrl+"&time="+(new Date()).getTime();
                var diagControlInfo = "dialogWidth=550px; dialogHeight=450px; help=no; scroll=no; status=no;";
                showModalDialog(reqUrl , "preview", diagControlInfo );
            }
        }
    }

}
function isNumber(ch){
    return (ch>='0' && ch<='9');
}


function incFileNo(inputFileName){
    var fileName =""+ inputFileName;
    var i=fileName.length-1;
    while(fileName.substring(i-1,i)!="/" && i>0){
        //alert(fileName[i]);
        i--;
    }
    var result=fileName.substring(0,i-1);

    while(i<fileName.length){
        if(isNumber(fileName.substring(i-1,i))) break;
        result += fileName.substring(i-1,i);
        i++;
    }

    var fileNo = 0;
    var numberCount = 0;
    while(isNumber(fileName.substring(i-1,i))){
        var number = fileName.substring(i-1,i)-'0';
        //alert("fileNo:"+(fileNo*10)+",Number:"+number);
        fileNo = fileNo*10+( number);
        i++;
        numberCount ++;
    }        
    //alert("result="+result+"\nfileNo:"+fileNo);
    fileNo++;
    var fileNoStr = "";
    if(fileNo>1){
        fileNoStr = ""+(fileNo);
        numberCount = numberCount-fileNoStr.length;
        while(numberCount>0){
            numberCount--;
            fileNoStr = "0"+fileNoStr;
        }
    }
    result += fileNoStr+fileName.substring(i-1);
//    alert("result="+result);
    return result;
}

function showSelectDialog(urlReciveField,nameReciveField,requestUrl){
    var diagControlInfo = "dialogWidth=550px; dialogHeight=540px; help=no; scroll=yes; status=no;";
    var returnVal = showModalDialog( requestUrl, "node", diagControlInfo );
    if (typeof(returnVal) != "undefined" ) {
        urlReciveField.value=returnVal;
        if(nameReciveField){

        }
        retriveAllClips();
    }

}
