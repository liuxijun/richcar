<%@ page contentType="text/html; charset=GBK" %><%@page
import="java.util.*,
        java.sql.Connection,
        cn.sh.guanghua.util.pooldb.DbTool,
        java.sql.ResultSet,
        java.sql.Statement,
        java.sql.PreparedStatement,
        cn.sh.guanghua.mediastack.business.normal.ChannelLogic"%><%@page
import="cn.sh.guanghua.mediastack.dataunit.*"%><%@page
import="cn.sh.guanghua.mediastack.datainterface.*"%><%@page
import="cn.sh.guanghua.mediastack.common.*"%><%@page
import="cn.sh.guanghua.mediastack.business.normal.MediaLogic"%><%@page
import="cn.sh.guanghua.mediastack.htmlcreator.CreatorHelper"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%

    PageHelper pageHelper = new PageHelper(request,session);
    String errMsg = "";

    //获取参数
    String searchname = ParamTools.getParameter(request,"searchname","");
    String searchsort = ParamTools.getParameter(request,"searchsort","");
    long icpId = ParamTools.getLongParameter(request,"icpid",-1);

       System.out.println("joe's"+searchsort+searchname+icpId);//rework by joe

    long lTotalRows = ParamTools.getLongParameter(request,"totalrows",0);   //总记录数，调用方法取得或从参数中读
    long lRowsPerPage =  ParamTools.getLongParameter(request,"rowsperpage",10);//每页记录数，如参数中无，则默认为10
    long lTotalPage = ParamTools.getLongParameter(request,"totalpage",0);   //总页数，由总记录数和每页记录数算出
    long lCurrPage = ParamTools.getLongParameter(request,"currpage",1);  //当前页码，如参数中无，则默认为1
    long lPrevPage = ParamTools.getLongParameter(request,"prevpage",1);  //当前页码，如参数中无，则默认为1
    long lNextPage = ParamTools.getLongParameter(request,"nextpage",1);  //当前页码，如参数中无，则默认为1
    long lGotoPage = ParamTools.getLongParameter(request,"gotopage",1);  //当前页码，如参数中无，则默认为1
    long lStartRow = 0;    //要取出的记录的开始行
    long lSelectedRowCount = ParamTools.getLongParameter(request,"pagesize",lRowsPerPage);  //要取出的记录数
    String isValid = "yes";

    Config helper = ConfigManager.getConfig();
    String remote = "http://";
    remote += request.getRemoteHost();
    if(helper != null){
        remote = helper.node("host").get("www");
    }

    Media media = new Media();
    MediaIcp mediaIcp = new MediaIcp();
    StringBuffer sbData = new StringBuffer();
    List result = new ArrayList();
    List channelList = new ArrayList();
    List mediaList = new ArrayList();

    //计算分页相关参数
    if(lTotalRows == 0){
        lTotalRows = MediaLogic.searchPublishedMediasCount(icpId,searchname,searchsort);
    }
    lTotalPage = (long)Math.ceil((double)lTotalRows /lRowsPerPage);

    lPrevPage = lCurrPage - 1;
    lNextPage = lCurrPage + 1;

    //限制前页和后页的范围
    if(lPrevPage < 1){
        lPrevPage = 1;
    }
    if(lNextPage >= lTotalPage){
        //lPrevPage = lTotalPage;
        //rework by joe
        //2005-03-15
        lNextPage = lTotalPage;
    }
    if(lCurrPage >= lTotalPage){
        lCurrPage = lTotalPage;
    }

    //限制每次查询的数据的范围
    if(lCurrPage >= lTotalPage){
        lSelectedRowCount = lTotalRows % lRowsPerPage;
        if(lSelectedRowCount==0)
          lSelectedRowCount=lRowsPerPage;

    }else{
        lSelectedRowCount = lRowsPerPage;
    }


    lStartRow = lRowsPerPage * (lCurrPage-1);

    Icp icp = new Icp();
    icp.setIcpId(icpId);
    channelList = ChannelLogic.getFirstLevelChannel(icp);

    result = MediaLogic.searchPublishedMedias(icpId, searchname, searchsort, lStartRow,lSelectedRowCount );

    if(lTotalRows == 0){
        isValid = "no";
        errMsg="没有找到记录，请重新输入搜索条件!";
    }

    Iterator it = result.iterator();
    while(it.hasNext()){
        media = (Media)it.next();
        mediaIcp = Db.MediaIcp().getMediaIcp(media,icp);
        long channelId = mediaIcp.getMediaChannelid();
        Channel channel = (Channel)Db.Channel().getObject(channelId);
        String channelName = channel.getChannelName();
        media.setChanelName(channelName);
        media.setMediaPrice((double)mediaIcp.getMediaPrice()/100);
        media.setTag(channelId);
        mediaList.add(media);
    }

        //输出分页相关的参数
        sbData.append("    <page>\n");
        sbData.append("    <total-rows type=\"long\">")
            .append(lTotalRows)
            .append("</total-rows>\n");
        sbData.append("    <total-page type=\"long\">")
            .append(lTotalPage)
            .append("</total-page>\n");
        sbData.append("    <curr-page type=\"long\">")
            .append(lCurrPage)
            .append("</curr-page>\n");
        sbData.append("    <rows-per-page type=\"long\">")
            .append(lRowsPerPage)
            .append("</rows-per-page>\n");
        sbData.append("      <next-page type=\"int\">")
            .append(lNextPage)
            .append("</next-page>\n");
        sbData.append("      <prev-page type=\"int\">")
            .append(lPrevPage)
            .append("</prev-page\n>");
        sbData.append("     <page-size>")
            .append(lSelectedRowCount)
            .append("</page-size>\n");
        sbData.append("    </page>\n");


    sbData.append(PageHelper.addElement("returnurl",remote));
    sbData.append(PageHelper.addElement("isvalid",isValid));
    sbData.append(PageHelper.getList("medias",mediaList));
    sbData.append(PageHelper.getList("channels",channelList));
    sbData.append(PageHelper.addElement("icp-id",icpId));
    sbData.append(PageHelper.addElement("search-name",searchname));
    sbData.append(PageHelper.addElement("search-sort",searchsort));


    pageHelper.outPut(out,sbData.toString(),"10-10-80-0074",errMsg,
            "What is this file can do?","10-10-80-HELP_ID","kevin");
%>