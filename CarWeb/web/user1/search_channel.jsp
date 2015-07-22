<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*"%><%@page
import="cn.sh.guanghua.mediastack.dataunit.*"%><%@page
import="cn.sh.guanghua.mediastack.datainterface.*"%><%@page
import="cn.sh.guanghua.mediastack.common.*"%><%@page
import="cn.sh.guanghua.mediastack.business.normal.MediaLogic"%><%@page
import="cn.sh.guanghua.mediastack.htmlcreator.CreatorHelper"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%
    long lChannelId = ParamTools.getLongParameter(request,"channel_id",-1);
    Channel channel = null;
    long lIcpId;
    try{
      channel = (Channel)Db.Channel().getObject(lChannelId);
      lIcpId  = channel.getIcpId();
    }catch(Exception e){
        lIcpId = -1;
    }
    Icp icp =new Icp();
    icp.setIcpId(lIcpId);
    String sSearchCondition = "";
    String sSearchType ="";
    String sMediaName = "";
    String sMediaDirectors = "";
    String sMediaActors = "";
    StringBuffer sbXML = new StringBuffer();
    long lMediaHometownId = -1;
    long lMediaSubjectId = -1;
    sSearchCondition = ParamTools.getParameter(request,"search_content","");
    sSearchType = ParamTools.getParameter(request,"search_type","media_name");

    //初始化分页相关参数
    String SubjectName=null,HometownName=null;
    long lTotalRows = 0;   //总记录数，调用方法取得或从参数中读
    long lRowsPerPage =  10;//每页记录数，如参数中无，则默认为10
    long lTotalPage = 1;   //总页数，由总记录数和每页记录数算出
    long lPageNumber = 1;  //当前页码，如参数中无，则默认为1
    long lStartRow = 0;    //要取出的记录的开始行
    long lSelectedRowCount = 0;  //要取出的记录数

    List medias = new Vector();
    Iterator mediasItr = null;


//读取页面使用的参数
    if(request.getParameter("total_rows")!=null){
      lTotalRows = Long.parseLong(request.getParameter("total_rows").trim());
    }else{
	try{
            if(sSearchType.equals("media_name")){
             lTotalRows =Db.Media().getNameOfChannelCount(lChannelId,sSearchCondition);
            }
          }catch(Exception e) {
              out.println("error:"+e.getMessage()+"\n");
              return;
          }
     }
    if(request.getParameter("rows_per_page")!=null){
      lRowsPerPage = Long.parseLong(request.getParameter("rows_per_page").trim());
    }
    if(request.getParameter("page_number")!=null){
      lPageNumber = Long.parseLong(request.getParameter("page_number").trim());
      if(lPageNumber==0){
      }

    }

//计算分页相关参数
    lTotalPage = lTotalRows / lRowsPerPage;
    if(lTotalRows > (lTotalPage * lRowsPerPage)) lTotalPage++ ;
    lStartRow = lRowsPerPage * (lPageNumber - 1);
    if(lTotalRows > ( lPageNumber * lRowsPerPage )){
      lSelectedRowCount = lRowsPerPage;
    }else{
      lSelectedRowCount = lTotalRows - ( lPageNumber - 1 ) * lRowsPerPage;
    }
    try{
            if(sSearchType.equals("media_name")){
             medias = Db.Media().getNameOfChannelObjects(lChannelId,sSearchCondition,lStartRow,lSelectedRowCount);
            }
    if(medias==null){
        medias = new Vector();
    }
    mediasItr = medias.iterator();
    }catch(Exception e){
      out.println("本页面在处理过程中发现错误，请检查数据的参数是否正确，请参考系统的错误信息:"+e.getMessage());
        return;
    }

//取得前一页的页码和后一页的页码
    long nextpage=0;
    long prepage=0;
    if(lTotalPage>0){
    if(lPageNumber<lTotalPage)
     nextpage = lPageNumber+1;
    else
      nextpage = lTotalPage;

    if(lPageNumber>1)
      prepage=lPageNumber-1;
    else  prepage = 1 ;
    }else{
    nextpage = 1;
    }
    if(nextpage>lTotalPage){
        nextpage = lTotalPage;
    }
    if(prepage<1){
        prepage = 1;
    }
%>
<html>
<head>
<title>银河宽频</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<link rel="stylesheet" href="css/1.css" type="text/css"/>
                                <link rel="stylesheet" href="css/css_user.css" type="text/css"/>
</head>

<body bgcolor="#cccccc" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" link="#FFFFFF">
<div align="center">
<table width="760" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#f2f2f2">
<tbody>
<tr>
<td colspan="2" height="19">
<table width="760" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td>
      <div align="center">
        <script language="JavaScript" src="http://www.openhe.net/style/openhe_top.js" type="text/JavaScript"></script>
      </div>
    </td>
  </tr>
</table>
</td>
</tr>
<tr>
<td height="20" colspan="2">
<table background="images/green_bg.gif" height="20" cellpadding="0" cellspacing="0" border="0" width="760">
<tbody>
<tr>
<td>&#160;&#160;&#160;&#160;您的位置：<a href="http://www.vod.inhe.net/<%=lIcpId%>/" class="title2">银河网</a><FONT color="#000080">/搜索结果</FONT></td>
<td>
</td>
</tr>
</tbody>
</table>
</td>
</tr>
<tr>
<td width="760">
<table width="760" border="0" cellspacing="0" cellpadding="0" height="100%">
<tbody>
<tr>
<td height="22">
<table id="header" width="760" border="0" cellspacing="0" cellpadding="0" height="22" background="images/topic_bg.gif">
<tbody>
<tr>
<td width="135" background="images/topic_bt.gif" align="center">
<div style="cursor:hand;width: 135; font-size:12px;color:ffffff ; filter: glow(color=black, strength=1)">
搜索结果
</div>
</td>
<td>
<div align="right">共<%=lTotalRows%>部，<%=lTotalPage%>页,第<%=lPageNumber%>页</div>
</td>
<td width="44">
<div align="center">
<a href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=1">
<IMG src="images/first_page.gif" width="29" height="17" border="0"/>
</a>
</div>
</td>
<td width="75">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=prepage%>">
<IMG src="images/pre_page.gif" width="54" height="17" border="0"/>
</A>
</div>
</td>
<td width="75">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=nextpage%>"/>
<img src="images/next_page.gif" width="54" height="17" border="0"/>
</A>
</div>
</td>
<td width="44">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=lTotalPage%>"/>
<img src="images/last_page.gif" width="29" height="17" border="0"/>
</A>
</div>
</td>
</tr>
</tbody>
</table>
</td>
</tr>
<tr>
<td>
<TABLE style="BORDER-COLLAPSE: collapse" borderColor="#111111" cellSpacing="0" cellPadding="0" width="100%" border="0">
<TBODY>
<TR>
<TD vAlign="top" width="28">
</TD>
<TD vAlign="top" width="*">
       <table align="center" border="1" bordercolordark="#ffffff" bordercolorlight="#cccccc" cellpadding="0" cellspacing="0" class="table_border" width="600" frame="box" bordercolor="#ffffff">
         <tr>
                </th><th width="20%">影片名</th><th width="20%">带宽</th><th width="30%">演员</th><th width="30%">导演</th>
         </tr>
<%
for(int i=0;i<medias.size();i++){
        IcpMediaInfo mediaInfo = (IcpMediaInfo)medias.get(i);
        Media media = mediaInfo.getMedia();
        MediaIcp mediaicp = mediaInfo.getMediaicp();
%>
<TR>
<TD>
<A href="http://www.vod.inhe.net/<%=lIcpId%>/<%=mediaicp.getMediaChannelid()%>/media/<%=media.getMediaId()%>.html" class="title2"><%=media.getMediaName()%></A>
</TD>
<TD><%=media.getMediaBandwidth()%>K</TD>
<TD><%=media.getMediaActors()%></TD>
<TD><%=media.getMediaDirectors()%></TD>
</TR>
<%
}
%>
</TABLE>
</TD>
<TD vAlign="top" width="28">
</TD>
</TR>
</TBODY>
</TABLE>
</td>
</tr>
<tr>
<td height="22">
<table id="header" width="580" border="0" cellspacing="0" cellpadding="0" height="22" background="images/topic_bg.gif">
<tbody>
<tr>
<td width="135" background="images/topic_bt.gif" align="center">
<div style="cursor:hand;width: 135; font-size:12px;color:ffffff ; filter: glow(color=black, strength=1)">
搜索结果
</div>
</td>
<td>
<div align="right">共<%=lTotalRows%>部，<%=lTotalPage%>页,第<%=lPageNumber%>页</div>
</td>
<td width="44">
<div align="center">
<a href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=1">
<IMG src="images/first_page.gif" width="29" height="17" border="0"/>
</a>
</div>
</td>
<td width="75">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=prepage%>"/>
<IMG src="images/pre_page.gif" width="54" height="17" border="0"/>
</A>
</div>
</td>
<td width="75">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=nextpage%>"/>
<img src="images/next_page.gif" width="54" height="17" border="0"/>
</A>
</div>
</td>
<td width="44">
<div align="center">
<A href="search_channel.jsp?channel_id=<%=lChannelId%>&search_type=<%=sSearchType%>&search_content=<%=sSearchCondition%>&page_number=<%=lTotalPage%>"/>
<img src="images/last_page.gif" width="29" height="17" border="0"/>
</A>
</div>
</td>
</tr>
</tbody>
</table>
</td>
</tr>
</tbody>
</table>
</td>
<td width="180">
</td>
</tr>
<tr>
<td colspan="2" height="44">
<table width="760" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr> 
    <td>
      <div align="center">
        <script language="JavaScript" src="http://www.openhe.net/style/openhe_fundus.js" type="text/JavaScript"></script>
      </div>
    </td>
  </tr>
</table>
</td>
</tr>
</tbody>
</table>
</div>
</body>
</html>
