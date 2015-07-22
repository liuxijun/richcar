var clipWidth = 310,clipHeight=270,playUrl="1";
var contentId=-1;
var deviceId = 1;
function getplayUrl(v){
    var deviceId = getCmpValue('deviceId');
    var mediaUrl = getCmpValue('stringValue'+v);
    var extObj = Ext.getCmp("flashPlayerObj"+v);
    if(extObj!=null){
        extObj.body.update(getFlvStr(deviceId,mediaUrl));
    }
}

function getMediaDuration(id,cmpId) {
    var playerObj = document.getElementById(id);
    var setInter;
    var tryTimes = 1;
    var duration = 0;
    if(playerObj != null) {
      setInter = setInterval(function() {
          //想判断如果是播放状态，再去取时常，不知道如何用JS获取状态
          duration = getDuration(playerObj);
          setCmpValue(cmpId,duration);

          if(duration > 0) {
              clearInterval(setInter);
          }

          tryTimes++;
          if(tryTimes > 5) {
              clearInterval(setInter);
          }
      },1000);
    }
}

function getDuration(obj) {
    return obj.getDuration();
}

function getFlvStr(deviceId,url,contentId){
    if(deviceId>0&&url!=""){
        Ext.Ajax.request({
            url:'getFlashPlayer.jsp',
            method:"post",
            params:{urlPlay:url,deviceId:deviceId,contentId:contentId},
            callback : function(opt, success, response) {

            }

        });
        playUrl = "getPlayUrl.jsp&uid=" + deviceId + "&pid=1";
    }else{
        playUrl = "";
    }
    var flvStr ="<object class='play' classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='"+clipWidth+
        "' height='"+clipHeight+"' id='flashPlayer1'  align='middle'> " +
        "<param name='movie' value='/swfs/player.swf?url=" +playUrl+"/>" +
        "<param name='quality' value='high'/> " +
        "<param name='bgcolor' value='#000000' /> " +
        "<param name='play' value='true' /> " +
        "<param name='allowFullScreen' value='true' /> " +
        "<param name='loop' value='true' /> " +
        "<param name='wmode' value='window' /> " +
        "<param name='scale' value='showall' />" +
        "<param name='menu' value='true' /> " +
        "<param name='devicefont' value='false' /> " +
        "<param name='salign' value='' />" +
        "<param name='allowScriptAccess' value='sameDomain'' /> " +
        "<!--[if !IE]>--> " +
        "<object type='application/x-shockwave-flash' data='../swfs/player.swf?url="+playUrl+"' width='"+clipWidth+"' height='"+clipHeight+"' id='flashPlayer2'>" +
        "<param name='movie' value='../swfs/player.swf?url="+playUrl+"'/>" +
        "<param name='quality' value='high' /> " +
        "<param name='bgcolor' value='#000000' /> " +
        "<param name='play' value='true' /> " +
        "<param name='loop' value='true' /> " +
        "<param name='wmode' value='window' /> " +
        "<param name='scale' value='showall' />" +
        "<param name='menu' value='true' /> " +
        "<param name='devicefont' value='false' /> " +
        "<param name='salign' value='' /> " +
        "<param name='allowFullScreen' value='true' /> " +
        "<param name='allowScriptAccess' value='sameDomain' /> <!--<![endif]--> " +
        "<a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='获得 Adobe Flash Player' /> </a> <!--[if !IE]>--> " +
        "</object> <!--<![endif]--> " +
        "</object>" ;
    return flvStr;
}

