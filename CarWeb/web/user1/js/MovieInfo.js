
// Global variables
// ****************
ItemList = new Array
MovieItemCount = 0
// Definition of class Folder
// *****************************************************************
// addItem(new MovieItem("1001","职业杀手","刘的话","吴晓勇","http://10.10.224.66/vodbase/1.html","http://10.10.224.158/movie/1.jpg","","华纳兄弟","-1000","故事片","印度"));
function MovieItem(MediaId,Title,MediaType,Actors,Directors,DescURL,Post1URL,Post2URL,Company,RemarkId,TypeCaption,HometownCaption,MediaCost,MediaVisitors) //constructor
{
  //constant data
  this.MediaId=MediaId;
  this.Actors = Actors;
  this.MediaType = MediaType;
  this.Directors = Directors;
  this.DescURL = DescURL;
  this.Post1URL = Post1URL
  this.Post2URL = Post2URL;
  this.Title = Title;
  this.Company = Company;
  this.RemarkId = RemarkId;
  this.TypeCaption = TypeCaption;
  this.HometownCaption = HometownCaption;
  this.MediaCost = MediaCost;
  this.MediaVisitors = MediaVisitors;
}
function addMovie(node)
{
  	ItemList[MovieItemCount] = node
  	MovieItemCount++
}
function addItem(MovieTitle,MovieURL,Hints){
	addMovie(new MovieItem("0",MovieTitle,"N/A","N/A","N/A",MovieURL,"N/A","N/A","N/A","N/A","N/A","N/A","N/A",Hints));
}
function ClearList(){
	MovieItemCount = 0
}
function GetItemCount()
{
	return MovieItemCount;
}
function GetItem(ItemId)
{
	if(ItemId >=0 && ItemId < MovieItemCount)
	{
		return ItemList[ItemId];
	}
	else
	{
		return null;
	}
}
function deleteByMediaId(aMediaId){
	var i,j;
	var aMovieItem;

	for(i=0;i<MovieItemCount;i++){
		aMovieItem = ItemList[i];
		if(aMovieItem.MediaId == aMediaId)
		{
			//找到了,就从这里开始
			for(j=i;j < MovieItemCount-1;j++)
			{
				ItemList[j]=ItemList[j+1];
			}
			//少一个，减一
			MovieItemCount--;
			return MovieItem;
		}
	}
	return null;
}
function getItemByMediaId(MediaId){
	var i;
	var MovieItem;
	for(i=0;i<MovieItemCount;i++){
		MovieItem = ItemList[i];
		if(MovieItem.MediaId == MediaId){
			return MovieItem;
		}
	}
	return null;
}
function OpenNewWindow(newURL)
{
	//alert(newURL)
	//window.open(newURL,"surveywin","width=400,height=300,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1");
/*	var strHtml;
	strHtml = '<html><head><link rel="stylesheet" href="../css/htmlStyle.css" type="text/css"><meta http-equiv="Content-Type" content="text/html; charset=gb2312"><script>function myLoad(){iFraMovieDesc.document.bgColor = "white";}</script></head>'+
		'<body onload="myLoad();" scroll=no topmargin=0 leftmargin=0 rightmargin=0><table width=100% bordercolor=silver border=1 cellspacing=0 cellpadding=0 height=100%><td><table width=100% cellspacing=0 cellpadding=0 height=100% background="../images/newnormaluser/bkground.jpg">' +
		'<tr height=16><td>&nbsp;</td></tr><tr><td align=center valign=top><iframe src="' + newURL + '" frameborder=0 name=iFraMovieDesc width=360 height=260 scrolling=auto></iframe></td></tr>' +
		'<tr height=16><td align=right valign=middle height=20><!--<img style="cursor:hand;" src="../images/newnormaluser/cursor-top1.gif" border=0 width=13 height=13 onclick="iFraMovieDesc.scrollBy(0,-150);">' +
            	'<img style="cursor:hand;" src="../images/newnormaluser/cursor-bottom1.gif" border=0 width=13 height=13 onclick="iFraMovieDesc.scrollBy(0,150);">' +
            	'<img style="cursor:hand;" src="../images/newnormaluser/cursor-left1.gif" border=0 width=13 height=13 onclick="iFraMovieDesc.scrollBy(-320,0);">' +
            	'<img style="cursor:hand;" src="../images/newnormaluser/cursor-right1.gif" border=0 width=13 height=13 onclick="iFraMovieDesc.scrollBy(320,0);">-->' +
            	'&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="window.close();return false;">关闭</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
		'</td></tr></table></td></table></body></html>';*/
	var winDesc = window.open("DescWindow.htm","surveywin","width=400,height=300,fullscreen=1,toolbar=0,location=0,directories=0,status=0,menubar=0,resizable=0");
	winDesc.resizeTo(400, 300);
	winDesc.moveTo((window.screen.width-400)/2 ,(window.screen.height-300)/2);
	winDesc.nowDesc = newURL;
}

function PrintDesc(ItemId)
{
	var Result="";
	if(ItemId <0 || ItemId >= MovieItemCount)
	{
		return Result;
	}
	Result = "<br><br><table  align=center cellspacing=0 cellpadding=0 width=155 border=\"1\" bordercolorlight=\"#000080\" bordercolordark=\"#FFFFFF\"><tr><td><table align=center cellspacing=0 cellpadding=0 width=155>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>片名：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].Title+"</td></tr>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>主演：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].Actors+"</td></tr>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>导演：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].Directors+"</td></tr>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>类型：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].TypeCaption+"</td></tr>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>产地：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].HometownCaption+"</td></tr>";
	Result = Result + "<tr><td height=35 width=40 bgcolor=#01CFFF>公司：</td><td height=35 width=80 bgcolor=#01CFFF><font class=myfont2>"+ItemList[ItemId].Company+"</td></tr>";
	if(ItemList[ItemId].DescURL != null)
	{
		if(ItemList[ItemId].DescURL != ""||ItemList[ItemId].length>0)
		{
/*
			Result = Result +"<tr><td colspan=2 width=100%><table bgcolor=#01CFFF width=100%>"+
			         "<tr><td width=50%><a href='#' onClick='fnLinkBBS()' ><font color=blue>&gt;&gt;影片评论</font>"+
			         "</a></td><td width=50% align=right><a href='#' onclick='window.open(\""+
			         ItemList[ItemId].DescURL+
			         "\",\"NewWIndow\",\"toolbar=false,width=780,height=590,top=10,left=10\")'"+
			         "><font color=blue>&gt;&gt;影片简介</font></a></td></tr></table></tr>";
*/
			Result = Result +"<tr><td colspan=2 width=100%><table bgcolor=#01CFFF width=100%>"+
			         "<tr><td width=50%><a href='#' onClick='fnLinkBBS()' ><font color=blue>【影片评论】</font>"+
			         "</a></td><td width=50% align=right><a href='#' onclick='OpenNewWindow(\""+
			         ItemList[ItemId].DescURL+
			         "\")'"+
			         "><font color=blue>【影片简介】</font></a></td></tr></table></tr>";
		}
		else
		{
			Result = Result + "<tr><td colspan=2 width=100% bgcolor=#01CFFF><a href='#' onClick='fnLinkBBS()' ><font color=blue>【影片评论】</td></tr>";
		}
	}
	Result = Result + "</table></td></tr></table>";
	return Result;
}
function SetParentSelectedMovieId(MovieId)
{
	if(parent!=null)
	{
				parent.SelectedMovieId = MovieId;
	}
}
function OutList_old()
{
var i,j;
var TempHTML="<table bordercolordark='#FFFFFF' bordercolorlight='#FFFFFF' width='100%' bordercolor='#111111' style='border-collapse: collapse' cellspacing='0' cellpadding='0' border='1'>";
for(i=1;i<=MovieItemCount;i++)
{
	 j=i-1;
	 //if(j%2==0)
	 {
	    TempHTML = TempHTML + '<tr>';
	 }
	 TempHTML = TempHTML +
	 		'<td width="100%" height="24">&nbsp;' +
			'<img height="14" width="30" align="middle"'+
			' src="images/top' + i + '.gif" border="0">'+
			'<font color="#000000"><a href="'+ ItemList[j].DescURL+'">'+
			ItemList[j].Title + '('+ItemList[j].MediaVisitors+')</a></font></td>';
	 //if(j%2==1)
	 {
	    TempHTML = TempHTML + '</tr>';
	 }

}
TempHTML=TempHTML+"</table>";
window.top10Div.innerHTML = TempHTML;
//window.Test.innerHTML = TempHTML;
}

function OutList()
{
var i,j;
var TempHTML="<table bordercolordark='#FFFFFF' bordercolorlight='#FFFFFF' width='100%' bordercolor='#111111' style='border-collapse: collapse' cellspacing='0' cellpadding='0' border='1'>";
for(i=1;i<=MovieItemCount;i++)
{
	 j=i-1;
	 //if(j%2==0)
	 {
	    TempHTML = TempHTML + '<tr>';
	 }
	 TempHTML = TempHTML +
	 		'<td width="100%" height="24">&nbsp;' +
			'-'+
			'<font color="#000000"><a href="'+ ItemList[j].DescURL+'">'+
			ItemList[j].Title + '('+ItemList[j].MediaVisitors+')</a></font></td>';
	 //if(j%2==1)
	 {
	    TempHTML = TempHTML + '</tr>';
	 }

}
TempHTML=TempHTML+"</table>";
window.top10Div.innerHTML = TempHTML;
//window.Test.innerHTML = TempHTML;
}