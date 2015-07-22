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
    long lChannelId = ParamTools.getLongParameter(request,"channel_id",-1);
    Channel channel = null;

    PageHelper pageHelper = new PageHelper(request,session);

    //��ȡ����
    String searchname = ParamTools.getParameter(request,"searchname","");
    String searchsort = ParamTools.getParameter(request,"searchsort","");
    long icpId = ParamTools.getLongParameter(request,"icpid",-1);
    long lTotalRows = ParamTools.getLongParameter(request,"totalrows",0);   //�ܼ�¼�������÷���ȡ�û�Ӳ����ж�
    long lRowsPerPage =  ParamTools.getLongParameter(request,"rowsperpage",10);//ÿҳ��¼������������ޣ���Ĭ��Ϊ10
    long lTotalPage = ParamTools.getLongParameter(request,"totalpage",0);   //��ҳ�������ܼ�¼����ÿҳ��¼�����
    long lCurrPage = ParamTools.getLongParameter(request,"currpage",1);  //��ǰҳ�룬��������ޣ���Ĭ��Ϊ1
    long lPrevPage = ParamTools.getLongParameter(request,"prevpage",1);  //��ǰҳ�룬��������ޣ���Ĭ��Ϊ1
    long lNextPage = ParamTools.getLongParameter(request,"nextpage",1);  //��ǰҳ�룬��������ޣ���Ĭ��Ϊ1
    long lGotoPage = ParamTools.getLongParameter(request,"gotopage",1);  //��ǰҳ�룬��������ޣ���Ĭ��Ϊ1
    long lStartRow = 0;    //Ҫȡ���ļ�¼�Ŀ�ʼ��
    long lSelectedRowCount = ParamTools.getLongParameter(request,"pagesize",lRowsPerPage);  //Ҫȡ���ļ�¼��
    Media media = new Media();
    StringBuffer sbData = new StringBuffer();
    List result = new ArrayList();
    List channelList = new ArrayList();

    //�����ҳ��ز���
    if(lTotalRows == 0){
        lTotalRows = MediaLogic.searchPublishedMediasCount(icpId,searchname,searchsort);
    }
    lTotalPage = (long)Math.ceil((double)lTotalRows /lRowsPerPage);

    lPrevPage = lCurrPage - 1;
    lNextPage = lCurrPage + 1;

    //����ǰҳ�ͺ�ҳ�ķ�Χ
    if(lPrevPage < 1){
        lPrevPage = 1;
    }
    if(lNextPage > lTotalPage){
        lPrevPage = lTotalPage;
    }
    if(lCurrPage >= lTotalPage){
        lCurrPage = lTotalPage;
    }

    //����ÿ�β�ѯ�����ݵķ�Χ
    if(lCurrPage >= lTotalPage){
        lSelectedRowCount = lTotalRows % lRowsPerPage;
    }else{
        lSelectedRowCount = lRowsPerPage;
    }

    lStartRow = lRowsPerPage * (lCurrPage-1);

    Icp icp = new Icp();
    icp.setIcpId(icpId);
    channelList = ChannelLogic.getFirstLevelChannel(icp);

    result = MediaLogic.searchPublishedMedias(icpId, searchname, searchsort, lStartRow,lSelectedRowCount );
    lTotalRows = result.size();
    if(lTotalRows == 0){
        out.print("û���ҵ���¼��������������������!");
    }

        //�����ҳ��صĲ���
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


    sbData.append(PageHelper.getList("medias",result));
    sbData.append(PageHelper.getList("channels",channelList));

    pageHelper.outPut(out,sbData.toString(),"10-10-80-0074","no error",
            "What is this file can do?","10-10-80-HELP_ID","kevin");
%>