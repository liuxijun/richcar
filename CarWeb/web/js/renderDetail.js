/**
 * Created by xjliu on 2015/4/8.
 */
    var detailModePC=1,detailModePhone=2,detailModePAD=3;
var detailRender={
    movieDetailJsonData:{},
    rendered:false,
    mode:detailModePC,
    systemIsReady:false,
    clipNo:1,
    clipWidth:1180,
    clipHeight:530,
    contentId:-1,
    userId:null,
    listLink:'list.html',
    detailLink:'video.html',
    detailReady:function(jsonData){
        detailRender.movieDetailJsonData = jsonData;
        if((!detailRender.systemIsReady)||detailRender.rendered){
            return;
        }

        var content = jsonData.content;
        var channel = jsonData['channel'];
        if(content==null||typeof(content)=='undefined'){
            return;
        }
        detailRender.rendered = true;
        var name = content['name'];
        var isLive= content['isLive'];
        if(isLive==null||typeof(isLive)=='undefined'){
            isLive = (channel!=null&&channel['name'].indexOf('直播')>=0)||(name!=null&&name.indexOf('直播')>=0);
        }
        if(content.type == 'picNews') {
            $("#a1").html('<img src="' + content['posterBig'] +
            '" width="'+detailRender.clipWidth+'" height="' +
            detailRender.clipHeight+'">"');
        }else if(content.type=='soundNews'){
            var picUrl = content['posterBig'];
            if(picUrl==null||picUrl==''||picUrl=='null'){
                picUrl = '/weichai/defaultBig.jpg';
            }
            detailRender.playContent(detailRender.contentId,detailRender.clipNo,detailRender.clipWidth,detailRender.clipHeight,picUrl,name,isLive);
        }else{
            detailRender.playContent(detailRender.contentId,detailRender.clipNo,detailRender.clipWidth,detailRender.clipHeight,null,name,isLive);
        }
        playMoveLog(detailRender.contentId,detailRender.userId);
        detailRender.render(jsonData);
    },
    getPlayContentName:function(movieDetailJsonData,contentId){
        if(movieDetailJsonData==null){
            return null;
        }
        var viewOtherContents = movieDetailJsonData['viewOthers'];
        if(viewOtherContents==null){
            return null;
        }
        var l=viewOtherContents.length;
        var i=0;
        for(;i<l;i++){
            var movie = viewOtherContents[i];
            if(movie.id == contentId){
                return movie.name;
            }
        }
        return null;
    },
    playContent:function(contentId,clipNo,width,height,picUrl,name,isLive,userId){
        if(typeof(userId)=='undefined'||userId==null){
            userId = detailRender.userId;
        }
        if(userId==null||typeof(userId)=='undefined'){
            return;
        }
        if(typeof(openPlayer)=='undefined'){
            return;
        }
        if(isLive==null||typeof(isLive)=='undefined'||isLive==''){
            isLive = detailRender.movieDetailJsonData['isLive'];
            if(isLive==null||typeof(isLive)=='undefined'||isLive==''){
                isLive= name!=null&&name.indexOf("直播")>=0;//是否是直播。如果是直播，设置为true
            }
        }
        var playerContentId= 'a1';//播放器容器的ID
        var playerId = 'fortuneFlashPlayer';//播放器的id，获取播放时间进度、跳转、获取状态等信息时需要。
        if(typeof(width)=='undefined'||width==null){
            width=1180;
        }
        if(typeof(height)=='undefined'||height==null){
            height=530;
        }
        openPlayer(contentId,clipNo,playerContentId,playerId,isLive,width,height,picUrl);
    },

    playOthers:function(contentIdx){
        var viewOtherContents = detailRender.movieDetailJsonData['viewOthers'];
        var l=viewOtherContents.length;
        if(contentIdx>=0&&contentIdx<l){
            var content = viewOtherContents[contentIdx];
            $("#contentName").html(content.name);
            detailRender.playContent(content.id,1);
        }else{
            alert("输入的索引值超出范围："+contentIdx+" vs "+l);
        }

    },
    setChannel:function(id,channel,urlParameters){
        if(typeof(urlParameters)=='undefined'){
            urlParameters = "id";
        }
        if(channel!=null){
            var channelObj = $("#"+id);
            if(channelObj!=null){
                channelObj.attr("href",detailRender.listLink +"?"+urlParameters+
                "="+channel.id);
                channelObj.html(channel.name);
            }
        }

    },
    renderIntro:function(content,contentId,action){
        return '<strong>' +content.name+
            '</strong>'+content.intro;
    },
    renderIntroPad:function(content){
        var action = "'save'";
        return '<strong>' +
        '<a id="has_favorite" href="javascript:collectionContent('+action+','+detailRender.contentId+')" class="add_fav">收藏</a>' +
        '<a id="un_favorite" href="#" class="add_fav" style="display: none" >已收藏</a>'+content.name+'</strong>'+content.intro;
    },
    renderIntroPhone:function(content){
        return detailRender.renderIntroPad(content);
    },
    renderLikeThis:function(c){
        return '<li><a href="'+detailRender.detailLink+'?id=' + c.id+
        '"><img src="' + c.poster+
        '" width="245" height="140"><span '+
        'class="info"><i>' + c.name+
        '</i></span></a></li>'
    },
    renderLikeThisPad:function(c){
        return '<li><a href="' +detailRender.detailLink+
            '?id='+ c.id +'"><img src="'+c.poster+'"><span><i>'+ c.name +'</i></span></a></li>';
    },
    renderLikeThisPhone:function(c){
        return '<li><a href="' +detailRender.detailLink+
            '?id='+ c.id +'"><img src="'+c.poster+'" height="100px"><span><i>'+ c.name +'</i></span></a></li>';
    },
    render:function(movieDetailJsonData){
        if(typeof(movieDetailJsonData)!='undefined'){

            var parentChannel = movieDetailJsonData.parentChannel;
            if(typeof(parentChannel)=='undefined'){
                parentChannel = {id:-1};
            }
            detailRender.setChannel("channel", movieDetailJsonData.channel,'parentId='+parentChannel.id+"&id");
            detailRender.setChannel("parentChannel",parentChannel,"parentId");
            var content =movieDetailJsonData.content;
            var movieName = detailRender.getPlayContentName(movieDetailJsonData,detailRender.contentId);
            if(movieName==null){
                movieName = content.name;
            }
            $("#contentName").html(movieName);
            $("#contentPoster").attr("src",content.poster);
            $("#contentDetail").html(detailRender.renderIntro(content));
            var hasFavoriteIt = movieDetailJsonData['hasFavoriteIt'];
            if(hasFavoriteIt) {
                $("#has_favorite").hide();
                $("#un_favorite").show();
            }
            var likeThese = $("#likeThese");
            var relateContents = movieDetailJsonData['relateContents'];
            if(likeThese!=null&&relateContents!=null&&relateContents.length>0){
                likeThese.html('');
                for(var j= 0,l=relateContents.length;j<l&&j<4;j++){
                    var c = relateContents[j];
                    likeThese.append(detailRender.renderLikeThis(c));
                }
            }
            var viewOthers = $("#viewOthers");
            var viewOtherContents = movieDetailJsonData.viewOthers;
            viewOthers.html('');
            if(viewOtherContents!=null&&viewOtherContents.length>0){
                for(var jk= 0,jl=viewOtherContents.length;jk<jl;jk++){
                    var voc = viewOtherContents[jk];
                    var partIndexName = 'Part '+(jk);
                    if(jk==0){
                        partIndexName = '全集';
                    }
                    var name = voc['name'];
                    if(name!=null){
                        name = detailRender.getClearName(name);
                    }
                    var intro = voc['intro'];
                    if(intro==''||intro==null){
                        intro = name;
                    }
                    intro = detailRender.getClearName(intro);// intro.replace(/潍柴新闻-/,'-');
                    viewOthers.append(detailRender.renderOthers(jk,name,partIndexName,intro,voc));
                }
            }
        }
    },
    getClearName:function(name){
        //name = name.replace(/潍柴新闻-/,'-');
        var p = name.indexOf("潍柴新闻-");
        if(p>0){
            return name.substring(p+5);
        }
        return name;
    },
    renderOthers:function(jk,name,partIndexName,intro,voc){
        return '<li><div class="part_pic"><a href="#" onclick="detailRender.playOthers(' +jk+',1'+
            ')"><img src="' +voc.poster+
            '" width="150" height="85"><span><i>' +name+
            '</i></span></a></div><div class="part_font"><a href="#" onclick="detailRender.playOthers(' +jk+
            ',1)">' +(partIndexName)+
            '</a><span style="display:block;height: 44px;overflow: hidden">' +intro+
            '</span><em class="count"><i>' +voc.allVisitCount+
            '</i></em></div></li>';
    },
    renderOthersPad:function(jk,name,partIndexName,intro,voc){
        return '<li><div class="part_pic"><a href="#" onclick="detailRender.playOthers(' +jk+',1'+
        ')"><img src="' +voc.poster+
        '" width="150" height="85"><span><i>' +name+
        '</i></span></a></div><div class="part_font"><a href="#" onclick="detailRender.playOthers(' +jk+
        ',1)">' +partIndexName+
        '</a><a href="#" onclick="detailRender.playOthers(' +jk+
            ',1)">' +intro+
        '</a><em class="count"><i>' +voc.allVisitCount+
        '</i></em></div></li>'
    },
    renderOthersPhone:function(jk,name,partIdexName,intro,voc){
        return '<li><a href="' +detailRender.detailLink+
            '?id='+voc.id+'"><img src="'+voc.poster+'" height="100"><span><i>'+name+'</i></span></a></li>';
    },
    toPad:function(){
        detailRender.renderIntro = detailRender.renderIntroPad;
        detailRender.renderLikeThis = detailRender.renderLikeThisPad;
        detailRender.renderOthers = detailRender.renderOthersPad;
        detailRender.listLink = "list_pad.html";
        detailRender.detailLink = "video_pad.html";
        detailRender.clipHeight = 380;
        detailRender.clipWidth = "100%";
        detailRender.mode = detailModePAD;
    },
    toPhone:function(){
        detailRender.renderIntro = detailRender.renderIntroPhone;
        detailRender.renderLikeThis = detailRender.renderLikeThisPhone;
        detailRender.renderOthers = detailRender.renderOthersPhone;
        detailRender.listLink = "list_phone.html";
        detailRender.detailLink = "video_phone.html";
        detailRender.clipHeight = 200;
        detailRender.clipWidth = "100%";
        detailRender.mode = detailModePhone;
    }
};