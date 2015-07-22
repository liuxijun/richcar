/**
 * Created by xjliu on 2014/10/16.
 */
var serverOptions = {
    address:'/'
};
var channelOptions = {
    channels:[
        {id:474431590,name:'重大活动',parentId:1}
        ,{id:474431591,name:'新闻频道',parentId:1}
        ,{id:474431592,name:'人文频道',parentId:1}
        ,{id:474431593,name:'工业频道',parentId:1}
        ,{id:474431594,name:'International',parentId:1}
        ,{id:474431595,name:'原创专区',parentId:1}
    ],
    topChannelCls:'.navarea ul',
    leftChannelCls:'.list_item ul',
    bottomChannelCls:'.qlink ul',
    renderTopLevelChannel:function(channels,deviceType){
        var areas = [this.topChannelCls,'top',this.leftChannelCls,'left',this.bottomChannelCls,'bottom'];
        for(var i= 0,l=areas.length;i<l;i+=2){
            var id = areas[i];
            var eleId = areas[i+1];
            var subChannels = $(id);
            if(subChannels==null){
                continue;
            }
            subChannels.html('');
            if(id!='.list_item ul'){
                subChannels.append('<li><a href="index'+deviceType+'.html?date='+new Date().getTime()+'"><em>首页</em></a></li>');
            }
            var tempDeviceType = "'"+deviceType+"'"; //renderChannel中字符窜参数
            $.each(channels,function(){
                var c = this;
                subChannels.append('<li id="' +eleId+
                    'Channel_' + c.id+
                    '"><a href="#" onclick="javascript:renderChannel(' + c.id+
                    ','+tempDeviceType+')"><em>' + c.name + '</em></a></li>');
            });
        }
    },

    renderPhoneTopLevelChannel:function(channels,deviceType){
        var areas = [this.leftChannelCls,'left'];
        for(var i= 0,l=areas.length;i<l;i+=2){
            var id = areas[i];
            var eleId = areas[i+1];
            var subChannels = $(id);
            if(subChannels==null){
                continue;
            }
            subChannels.html('');
            var tempDeviceTypex = "'"+deviceType+"'"; //renderChannel中字符窜参数
                $.each(channels,function(){
                    var c = this;
                    subChannels.append('<li id="' +eleId+
                        'Channel_' + c.id+
                        '"><a id="' +eleId+'Channel_a' + c.id+'" href="javascript:renderPhoneChannel(' + c.id+
                        ','+tempDeviceTypex+')">' + c.name + '</a></li>');
                });
        }
    },

    selectChannel:function(channelId){

    }
};

function appendJs(url){
    var head = document.getElementsByTagName("HEAD")[0];
    var jsFileRef=document.createElement('script');
    jsFileRef.setAttribute("type","text/javascript");
    jsFileRef.setAttribute("src", url);
    head.appendChild(jsFileRef);
}

function renderChannel(id,deviceType){
    currentTopChannel = id;
    if(window.location.href.indexOf('list'+deviceType+'.html')<0){
        window.location.href = 'list'+deviceType+'.html?parentId='+id+'&date='+new Date().getTime();
        return;
    }
    var selected = $(".item_over");
    $.each(selected,function(){
        var id = $(this).tagName;
        $(this).removeClass("item_over");
    });
    $("#topChannel_"+id).closest("li").addClass("item_over");
    $("#leftChannel_"+id).closest("li").addClass("item_over");
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +id+
        '&topChannel=y&callback=doRenderChannel');
    searchData.channelId = channelId;
    //goToPage(1);
}

var firstRender = true;
function renderPhoneChannel(id,deviceType){
    currentTopChannel = id;
    if(window.location.href.indexOf('list'+deviceType+'.html')<0){
        window.location.href = 'list'+deviceType+'.html?parentId='+id;
        return;
    }
    var selected = $(".right_channel_on");
    $.each(selected,function(){
        var id = $(this).tagName;
        $(this).removeClass("right_channel_on");
    });
    $("#leftChannel_a"+id).addClass("right_channel_on");
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +id+
        '&topChannel=y&callback=doRenderChannel');
    searchData.channelId = channelId;

    if(!firstRender) {
        $("#menu_list").toggle();
    }
    firstRender=false;

    //goToPage(1);
}

var currentTopChannel="";

function doRenderChannel(jsonData){
    var firstChannelId = -1;
    if("y" == jsonData.topChannel) {
        firstChannelId = renderChannelAction('y',jsonData);
        searchChannel(firstChannelId);
    } else {
        var sonChannels = jsonData.children;
        if(sonChannels != null  && sonChannels.length > 0) {
            firstChannelId = renderChannelAction('n',jsonData);
            searchChannel(firstChannelId);
        } else {
            searchChannel(jsonData.id);
        }

    }

}

function renderChannelAction(isTopChannel,jsonData) {
    var subChannels = $(".channel_list ul");
    subChannels.html('');
    subChannels.append('<label>频道</label>');
    var firstChannelId = -1;
    if(isTopChannel == 'n') {
        subChannels.append('<li class="subChannel" id="subChannel_' + jsonData.grandfatherId +
            '"><a href="javascript:returnSuperior(' + jsonData.id+ ','+jsonData.grandfatherId+')">《返回上级</a></li>');
    }
    $.each(jsonData.children,function(){
        var c = this;
        if(firstChannelId == -1){
            firstChannelId = c.id;
        }
        if(channelId == c.id){
            firstChannelId = channelId;
        }
        subChannels.append('<li class="subChannel" id="subChannel_' + c.id+
            '"><a href="javascript:hasSubChannels(' + c.id+','+ c.parentId+',-1'+
            ')">' + c.name+
            '</a></li>');
    });
    return firstChannelId;
}


function hasSubChannels(id,parentId,topChannel) {
    if(topChannel < 0) topChannel = 'n';
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +id+'&topChannel='+topChannel+'&grandfatherId='+parentId+'&callback=doRenderChannel');
}

function returnSuperior (id,grandfatherId) {
    var topChannel = 'n';
    if(grandfatherId == currentTopChannel) {
        topChannel = 'y';
    }
    channelId = id;
    appendJs(serverOptions.address+'page/js/channels.jsp?id=' +grandfatherId+'&topChannel='+topChannel+'&callback=doRenderChannel');
}

function doRenderSearchChannel(channels) {
    var searchChannels = $("#searchChannels");
    searchChannels.html('');
    var channelIndex = 0;
//    var length = parseInt(channels.length+1+channels.length/4);
    var length = channels.length+1;
    for(var i = 0;i< length;i++) {
        var channel = channels[channelIndex];
        if(i==0) {
            searchChannels.append('<li class="over" id="subChannel_-1"><a href="javascript:searchChannel(-1)">全部</a></li>');
        }/*else if(i > 0 && i%4 == 0) {
            searchChannels.append('<li></li>');
        }*/ else {
            searchChannels.append('<li id="subChannel_'+channel.id+'"><a href="javascript:searchChannel('+channel.id+')">'+channel.name+'</a></li>');
            channelIndex++;
        }
    }
}

function isShowChannel(channels,channelId) {
     if(channels !=null || channels.length > 0) {
         for(var i = 0;i < channels.length;i++) {
              if(channels[i].id == channelId) {
                  return true;
              }
         }
         return false;
     } else {
         return false;
     }
}

//检测用户是否登录
function checkLogon(redirectUrl,userId) {
    if(userId == null) {
        window.location.href=redirectUrl;
    }
}

function showChannelByUser(userId,channels,channelId) {
    if(userId != null) {
        if(!isShowChannel(channels,474431590)) {
            document.getElementById("474431590").style.display="none";  //如果用户登录，则根据用户
        }
    }
}