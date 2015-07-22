/**
 * Created by xjliu on 2015/4/1.
 * 推荐的显示和管理
 */
var RECOMMEND_STATUS_CP_ONLINE=2;
var recommendUtils = {
    containId:'myTab',
    isSlider:true,
    renderRecommends:function(recommends){
        var result = '';
        //recommends.push({id:-1,name:"栏目推荐"});
        var activeCls='';
        for(var i= 0,l=recommends.length;i<l;i++){
            var recommend = recommends[i];
            var id=recommend['id'];
            if(recommendUtils.selectedRecommendId==null){
                if(recommend['code']=='slider'){
                    recommendUtils.selectedRecommendId = id;
                }
            }
            var linkUrl = '#';
            var code = recommend['code'];
            if(id==recommendUtils.selectedRecommendId){
                if(code=='slider'){
                    recommendUtils.isSlider = true;
                }else{
                    recommendUtils.isSlider = false;
                }
                activeCls = ' class="active"';
            }else{
                activeCls = '';
                linkUrl = ' onclick="recommendUtils.goToRecommend('+id+')"';
            }
            result +='<li'+activeCls+'>\n<a data-toggle="tab" href="#"'+linkUrl+'>'+recommend['name']+'</a></li>\n';
        }
        if(recommendUtils.selectedRecommendId==null&&l>0){
            recommendUtils.selectedRecommendId = recommends[0]['id'];
        }
        if(recommendUtils.selectedRecommendId=='channelRecommend'){
            activeCls = ' class="active"';
        }else{
            activeCls = '';
        }
        result+='<li'+activeCls+'><a data-toggle="tab" href="#" onclick="recommendUtils.goToChannelRecommends()">频道推荐</a></li>';
        $("#"+recommendUtils.containId).html(result);
        recommendUtils.getRecommend(recommendUtils.selectedRecommendId);
    },
    goToChannelRecommends:function(){
        window.location.href='channelRecommendList.jsp';
    },
    goToRecommend:function(recommendId){
        window.location.href='recommend.jsp?recommendId='+recommendId;
    },
    selectedRecommendId:null,
    getRecommend:function(id){
        //alert("尚未实现该方法");
    },
    getRecommends:function(selectedId){
        recommendUtils.selectedRecommendId = selectedId;
        $.ajax({
            type: "POST",
            url: "/publish/recommend!search.action",
            //dataType: "json",
            dataType: "text",
            data: {"obj.type": 1},
            success: function(msg){
                var response = eval("(function(){return " + msg + ";})()");
                recommendUtils.renderRecommends(response['objs']);
            }
        });
    }
};