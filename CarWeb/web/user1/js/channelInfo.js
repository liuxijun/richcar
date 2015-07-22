//-------------重新整理树的结构 ----//
var channelCount = 0;
var onRootCount = 0;
var levelCount = 0;
var channelList = new Array;
var moveTypeIdList=new moveTypeIdList;
var leafCount = 0;        //叶子节点数目
function Channel(ParentId,ChannelId,ChannelName,ChannelHit,Tag){
  this.parentId = ParentId;
  this.channelId = ChannelId;
  this.channelName = ChannelName;
  this.channelHit = ChannelHit;
  this.hasChilds = false;
  this.id = 0;
  this.childsCount = 0;     //所有子节点数目
  this.level=0;
  this.childs = new Array;  //儿子节点，不包括孙子节点。孙子节点由儿子节点维护
  this.allChildCount = 0;   // 所有叶子子孙的数目
  this.tag = Tag;
}

//--------------将自己的祖先子孙数目加1 ------------------------------//
function addParentChildsCount( parentId ){
   var parentChannel = getChannelById( parentId);
   if(parentChannel == null){
     return;
   }
   parentChannel.allChildCount ++;
   addParentChildsCount(parentChannel.parentId);
}
function subParentChildsCount( parentId ){
   var parentChannel = getChannelById( parentId);
   if(parentChannel == null){
     return;
   }
   parentChannel.allChildCount --;
   addParentChildsCount(parentChannel.parentId);
}

function getChannelById( ChannelId ){
  var tChannel;
  for(i=0;i<channelCount;i++){
      tChannel = channelList[i];
      if(tChannel == null){
         continue;
      }
      if(tChannel.channelId == ChannelId){
         return tChannel;
      }
  }
  return null;
}
var lastAddElementId = "ffffff";
function addElement(ParentId,ChannelId,ChannelName,ChannelHit,Tag){
   if(lastAddElementId == ChannelId){
      return;
   }
   lastAddElementId = ChannelId;
   channelList[channelCount] = new Channel(ParentId,ChannelId,ChannelName,ChannelHit,Tag);
   var parentChannel;
   leafCount ++;
   parentChannel = getChannelById(ParentId);
   if(parentChannel != null){
     //有父亲节点啊，好幸福啊！
     if( parentChannel.hasChilds == false){
         parentChannel.hasChilds = true;                            //有孩子了，开心吧？
         subParentChildsCount(parentChannel.parentId);
         leafCount -- ;                                            //叶子节点没有增加
     }
     parentChannel.childs[parentChannel.childsCount] = channelList[channelCount];
     channelList[channelCount].level = parentChannel.level+1;
     if(levelCount < channelList[channelCount].level){
        levelCount = channelList[channelCount].level;           //最大多少层？
     }
     channelList[channelCount].id = parentChannel.childsCount;  //兄弟之中排行第几？

     parentChannel.childsCount ++;
     addParentChildsCount(parentChannel.channelId);
   }else{
     //这个Channel没有父节点，好可怜啊！那就是最上层了
     channelList[channelCount].id = onRootCount;                //最上层排行老几？
     onRootCount++;
   }
   channelCount++;
}
function createTree(parentChannelId,channelTree){
   if(parentChannelId == null){
      return;
   }
   var tempChannel = null;
   var i = 0;
   for(i=0;i<channelCount;i++){
     tempChannel = channelList[i];
     if(tempChannel == null){
       continue;
     }
     if(tempChannel.parentId == parentChannelId){
       if(tempChannel.hasChilds){
          channelTree.addTree("channel_"+i,tempChannel.channelName,
                          tempChannel.channelId,
                          "ViewChannel.jsp?channel_id="+tempChannel.channelId,"");
          createTree(tempChannel.channelId,channelTree[tempChannel.id]);
       }else{
          channelTree.addLine("channel_"+i,"ViewChannel.jsp?channel_id="+tempChannel.channelId,
                              "",
                              tempChannel.channelName,tempChannel.channelId);
       }
     }
   }
}
function createTreeTable(aChannel,tdWidth){
   if(aChannel == null){
      return "";
   }
   var Result = '    <tr>\n        <td width="'+tdWidth+'">';
   var i = 0;
   for(i=0;i<aChannel.level;i++){
     Result = Result + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
   }
   Result = Result + '<a href="SaleStat.jsp?channel_id='
                   + aChannel.channelId+'">'
                   + aChannel.channelName
                   + '</a></td>\n';
   Result = Result + '        <td align="center" width="'
                   + '100" >'
                   + aChannel.tag+'</td>\n';
   Result = Result + '        <td align="center" width="'
                   + '100" >'
                   + aChannel.tag+'</td>\n';
   Result = Result + "    </tr>\n";
   if(! aChannel.hasChilds){

   }else{
       var tempChannel = null;
       for(i=0;i<aChannel.childsCount;i++){
         tempChannel = aChannel.childs[i];
         if(tempChannel == null){
           continue;
         }
         Result = Result + createTreeTable(tempChannel,tdWidth);
       }
   }
  return Result;

}
function createTable(aChannel,tdWidth){
   if(aChannel == null){
      return "";
   }
   var Result = '        <td align="center" rowspan="'+aChannel.allChildCount+'"';
   if((aChannel.level < levelCount) &&(! aChannel.hasChilds) ){
       Result = Result + ' colspan="'+(levelCount-aChannel.level+1)+
                '"  width="'+tdWidth*(levelCount-aChannel.level+1)+'"';
   }else{
       Result = Result + ' width="'+tdWidth+'"';
   }
   Result = Result + '><a href="SaleStat.jsp?channel_id='+aChannel.channelId+'">'+aChannel.channelName+'</a></td>';

   if(! aChannel.hasChilds){
      Result = Result + '<td align="center" width="'
                      + tdWidth+'" rowspan="'+
                      aChannel.allChildCount+'">'+
                      aChannel.tag+'</td>\n';
      Result = Result + "    </tr>\n    <tr>\n";
   }else{
       var tempChannel = null;
       var i = 0;
       for(i=0;i<aChannel.childsCount;i++){
         tempChannel = aChannel.childs[i];
         if(tempChannel == null){
           continue;
         }
         Result = Result + createTable(tempChannel,tdWidth);
       }
   }
  return Result;
}
function createAllTable(tdWidth){
   var tempChannel = null;
   var i = 0;
   Result = '<table align="center" border="1" bordercolordark="#ffffff" bordercolorlight="#cccccc" cellpadding="0" cellspacing="0" width="500" class="table_border" frame="box">\n '+
            '    <tr>\n';
   Result = Result + '     <th align="center">频道名称</th>\n';
   Result = Result + '     <th align="center">账单数量</th>\n';
   Result = Result + '     <th align="center">销售金额</th>\n';
   for(i=0;i<channelCount;i++){
     tempChannel = channelList[i];
     if(tempChannel == null){
       continue;
     }
     if(tempChannel.level ==0){
        Result = Result + createTreeTable(tempChannel,tdWidth);
     }
   }
  Result = Result + "    </tr>\n</table>";
  return Result;
}
