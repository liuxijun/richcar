//-------------�����������Ľṹ ----//
var channelCount = 0;
var onRootCount = 0;
var levelCount = 0;
var channelList = new Array;
var moveTypeIdList=new moveTypeIdList;
var leafCount = 0;        //Ҷ�ӽڵ���Ŀ
function Channel(ParentId,ChannelId,ChannelName,ChannelHit,Tag){
  this.parentId = ParentId;
  this.channelId = ChannelId;
  this.channelName = ChannelName;
  this.channelHit = ChannelHit;
  this.hasChilds = false;
  this.id = 0;
  this.childsCount = 0;     //�����ӽڵ���Ŀ
  this.level=0;
  this.childs = new Array;  //���ӽڵ㣬���������ӽڵ㡣���ӽڵ��ɶ��ӽڵ�ά��
  this.allChildCount = 0;   // ����Ҷ���������Ŀ
  this.tag = Tag;
}

//--------------���Լ�������������Ŀ��1 ------------------------------//
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
     //�и��׽ڵ㰡�����Ҹ�����
     if( parentChannel.hasChilds == false){
         parentChannel.hasChilds = true;                            //�к����ˣ����İɣ�
         subParentChildsCount(parentChannel.parentId);
         leafCount -- ;                                            //Ҷ�ӽڵ�û������
     }
     parentChannel.childs[parentChannel.childsCount] = channelList[channelCount];
     channelList[channelCount].level = parentChannel.level+1;
     if(levelCount < channelList[channelCount].level){
        levelCount = channelList[channelCount].level;           //�����ٲ㣿
     }
     channelList[channelCount].id = parentChannel.childsCount;  //�ֵ�֮�����еڼ���

     parentChannel.childsCount ++;
     addParentChildsCount(parentChannel.channelId);
   }else{
     //���Channelû�и��ڵ㣬�ÿ��������Ǿ������ϲ���
     channelList[channelCount].id = onRootCount;                //���ϲ������ϼ���
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
   Result = Result + '     <th align="center">Ƶ������</th>\n';
   Result = Result + '     <th align="center">�˵�����</th>\n';
   Result = Result + '     <th align="center">���۽��</th>\n';
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
