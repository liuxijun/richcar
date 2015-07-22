var renderOfIndex={
    canViewChannel:function(userId,channels,channelId){
        if(userId != null) {
            if(!isShowChannel(channels,channelId)) {
                return false;
            }
        }
        return true;
    },
    repairName:function(contentName){
        if(contentName!=null){
            var p = contentName.indexOf("潍柴新闻-");
            if(p>0){
                contentName = contentName.substring(p+8);
            }
        }
        return contentName;
    },
    getRecommendContentsOfTypeNews:function(recommendId,channelId,name,contents,userId,channels){
        if(!renderOfIndex.canViewChannel(userId,channels,channelId)){
            return;
        }
        var recommendsObj = $("#recommends");
        var result =
            '<section class="section clearfix">\r\n'+
                '  <div class="news" id="channel_'+channelId+'">\r\n'+
                '    <h2><a href="list.html?parentId='+channelId+'">'+name+'</a></h2>\r\n'+
                '    <ul id="channel_'+channelId+'_section" class="loading">\r\n';
        if(contents){
            var newsOutputCount=0;
            $.each(contents,function(){
                var item =this;
                if(newsOutputCount<4){
                    var contentName = renderOfIndex.repairName(item.name);
                    result+=('<li><a href="video.html?id=' +item.id+
                        '"><img class="loading" src="' +item.poster+'" width="285" height="160"><span'+
                        ' class="info"><i>' +contentName+
                        '</i><em>' +item.date+
                        '</em></span></a></li>');
                    newsOutputCount++;
                }
            });
        }
        result+=     '    </ul>\r\n'+
            '  </div>\r\n'+
            '</section>\r\n';
        recommendsObj.append(result);
    },
    getRecommendContentsOfTypeCulture:function(recommendId,channelId,name,contents,userId,channels){
        if(!renderOfIndex.canViewChannel(userId,channels,channelId)){
            return;
        }
        var recommendsObj = $("#recommends");
        var result='<section class="section clearfix">\r\n'+
            '  <div class="culture" id="channel_' +channelId+'">\r\n'+
            '    <h2><a href="list.html?parentId='+channelId+'">'+name+'</a></h2>\r\n'+
            '    <div class="culture_1">\r\n'+
            '      <ul id="channel'+channelId+'_header" class="loading">\r\n';
        if(contents!=null&&typeof(contents)!='undefined'&&contents.length>0){
            var len=contents.length;
            var content = contents[0];
            result+=('<li><a href="video.html?id=' +content.id+
                '"><img src="' + content.poster+
                '" width="530" height="375"><span '+
                'class="info"><em></em><i>' +content.name+
                '</i></span></a></li>');
            result+= '       </ul>\r\n'+
                '    </div>\r\n'+
                '    <div class="culture_2">\r\n'+
                '      <ul class="loading" id="channel_'+channelId+'_next">\r\n';
            for(var j=1;j<len&&j<5;j++){
                content = contents[j];
                result+=('<li ><a href="video.html?id=' + content.id+
                    '"><img src="' +content.poster+
                    '" width="310" height="180"><span class="info"><i>' +renderOfIndex.repairName(content.name)+
                    '</i></span></a></li>');
            }
        }
        result +='       </ul>\r\n'+
            '    </div>\r\n'+
            '    <div class="culture_2">\r\n'+
            '      <ul id="channel_'+channelId+'_next">\r\n'+
            '       </ul>\r\n'+
            '    </div>\r\n'+
            '  </div>\r\n'+
            '</section>\r\n';
        recommendsObj.append(result);
    },
    getRecommendContentsOfPad:function(recommendId,channelId,name,contents,userId,channels) {
        if(!renderOfIndex.canViewChannel(userId,channels,channelId)){
            return;
        }
        var recommendsObj = $("#recommends");
        var result='<section class="section fixed" style="height: 223px">\r\n'+
            '  <div class="home_list" id="channel_' +channelId+'">\r\n'+
            '  <h3>'+name+'<a href="list_pad.html?parentId='+channelId+'" class="moreinfo">更多>></a></h3>\r\n'+
            '  <ul id="channel'+channelId+'_header" class="fixed">\r\n';
        if(contents!=null&&typeof(contents)!='undefined'&&contents.length>0){
            var len=contents.length;
            for(var j=0;j<len&&j<4;j++){
                var content = contents[j];
                var contentName = content['name'];

                result+=('<li><a href="video_pad.html?id='+content.id+
                    '"><img src="'+content.poster+'" style="height:171px"><span><i>"'+renderOfIndex.repairName(content.name)+'"</i></span></a></li>');
            }
        }
        result +='       </ul>\r\n'+
            '    </div>\r\n'+
            '</section>\r\n';
        recommendsObj.append(result);
    },

    getRecommendContentsOfPhone:function(recommendId,channelId,name,contents,userId,channels) {
        if(!renderOfIndex.canViewChannel(userId,channels,channelId)){
            return;
        }
        var recommendsObj = $("#recommends");
        var result='<section class="section fixed">\r\n'+
            '  <div class="home_list" id="channel_' +channelId+'">\r\n'+
            '  <h3>'+name+'<a href="list_phone.html?parentId='+channelId+'" class="moreinfo">更多>></a></h3>\r\n'+
            '  <ul id="channel'+channelId+'_header" class="fixed">\r\n';
        if(contents!=null&&typeof(contents)!='undefined'&&contents.length>0){
            var len=contents.length;
            for(var j=0;j<len&&j<4;j++){
                var content = contents[j];
                result+=('<li><a href="video_phone.html?id='+content.id+
                    '"><img src="'+content.poster+'" height="130px"><span><i>'+renderOfIndex.repairName(content.name)+'</i></span></a></li>');
            }
        }
        result +='       </ul>\r\n'+
            '    </div>\r\n'+
            '</section>\r\n';
        recommendsObj.append(result);
    }


};