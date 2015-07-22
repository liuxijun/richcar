<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * 主要功能：
 *
 * @param
 *
 *
 * @author：wdyou
 * @ Date
 *    @ 开始时间： 2004-10-22 11:24:53
 *    @ 结束时间：
 *
 *    @ 修改人员：
 *    @ 修改日期与原因：
 */
%><%@ page import="java.util.Date,
                   cn.sh.guanghua.mediastack.business.normal.UserLogLogic,
                   java.util.List,
                   cn.sh.guanghua.mediastack.common.Db,
                   cn.sh.guanghua.mediastack.common.Constants,
                   cn.sh.guanghua.mediastack.dataunit.*,
                   java.util.ArrayList,
                   cn.sh.guanghua.mediastack.user.SessionUser,
                   cn.sh.guanghua.util.hibernate.HBSession,
                   cn.sh.guanghua.util.tools.*,
                   java.util.Iterator"%><%@ page
 contentType="text/html;charset=gb2312" %><%

     PageHelper pageHelper = new PageHelper(request,session);
     SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
     if (su == null){
       response.sendRedirect("Login.jsp?msg=session_unvalid");
       return;
     }
     String userid = su.getUserId();
    //定义数据访问变量
     String mediaName = ParamTools.getParameter(request,"medianame","");
     Date startTime1 = ParamTools.getDateParameter(request,"start_time",StringTools.date2string(System.currentTimeMillis()-24L*3600L*1000L));
     long startTime = startTime1.getTime();
     Date endTime1   = ParamTools.getDateParameter(request,"end_time",StringTools.date2string(System.currentTimeMillis()));
     long endTime = endTime1.getTime();
     long icpId     = ParamTools.getLongParameter(request,"icp_id",-1);
     long impId     = ParamTools.getLongParameter(request,"imp_id",-1);
     long channelId     = ParamTools.getLongParameter(request,"channel_id",-1);
     String orderBy = ParamTools.getParameter(request,"order_by","mlh.medialogStarttime");
     String orderDirectory = ParamTools.getParameter(request,"order_dir","desc");
     String command = ParamTools.getParameter(request,"command","");

     long cpType = pageHelper.getCorporationType();
     SearchHelper searchHelper = null;
     searchHelper = UserLogLogic.getUserStatisticLog(channelId,userid,startTime,endTime,icpId,impId,
             mediaName,orderBy,orderDirectory,pageHelper.getStartRow(),pageHelper.getRowCountPerPage());
     List resultList = searchHelper.getObjList();
     pageHelper.setAllRowCount(searchHelper.getAllRowCount());

     //所有ICP频道
     List channelList = null;
     channelList = Db.Channel().getObjects();

     //所有ICP
     List icpList = null;
     icpList = Db.Icp().getObjects();


//显示参数
     StringBuffer sbData = new StringBuffer();
     sbData.append(PageHelper.getList("media-info",resultList));
     sbData.append(PageHelper.addElement("start-time",startTime1));
     sbData.append(PageHelper.addElement("end-time",endTime1));
     sbData.append(PageHelper.addElement("icp-id",icpId));
     sbData.append(PageHelper.addElement("imp-id",impId));
     sbData.append(PageHelper.addElement("user-id",userid));
     sbData.append(PageHelper.addElement("channel-id",channelId));
     sbData.append(PageHelper.addElement("media-name",mediaName));
     sbData.append(PageHelper.addElement("order-by",orderBy));
     sbData.append(PageHelper.addElement("order-dir",orderDirectory));
     sbData.append(PageHelper.addElement("command",command));
     sbData.append(PageHelper.addElement("cptype",cpType));
     sbData.append(pageHelper.getList("icps",icpList)) ;
     sbData.append(pageHelper.getList("channels",channelList)) ;

     pageHelper.outPut(out,sbData.toString(),"1010-80-0065-20041022",
             "no any error","",
             "1010-80-65-help10-10-80-66-20041022","wdyou"
             );
 %>