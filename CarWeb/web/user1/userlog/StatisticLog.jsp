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
                   cn.sh.guanghua.util.tools.*"%><%@ page
 contentType="text/html;charset=gb2312" %><%

     PageHelper pageHelper = new PageHelper(request,session);

    //定义数据访问变量
     String mediaName = ParamTools.getParameter(request,"medianame","");
     Date startTime1 = ParamTools.getDateParameter(request,"start_time",StringTools.date2string(System.currentTimeMillis()-196L*3600L*1000L));
     long startTime = startTime1.getTime();
     Date endTime1   = ParamTools.getDateParameter(request,"end_time",StringTools.date2string(System.currentTimeMillis()));
     long endTime = endTime1.getTime();
     long icpId     = ParamTools.getLongParameter(request,"icp_id",-1);
     long impId     = ParamTools.getLongParameter(request,"imp_id",-1);
     long channelId     = ParamTools.getLongParameter(request,"channel_id",-1);
     String orderBy = ParamTools.getParameter(request,"order_by","m.mediaName");
     String orderDirectory = ParamTools.getParameter(request,"order_dir","desc");
     String command = ParamTools.getParameter(request,"command","");

     long cpType = pageHelper.getCorporationType();
     SearchHelper searchHelper = null;
     SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
     String userid = su.getUserId();
     searchHelper = UserLogLogic.getUserStatisticLog(channelId,userid,startTime,endTime,icpId,impId,
             mediaName,orderBy,orderDirectory,pageHelper.getStartRow(),pageHelper.getRowCountPerPage());
     List resultList = searchHelper.getObjList();
     pageHelper.setAllRowCount(searchHelper.getAllRowCount());

     //所有某个ICP频道
     List channelList = new java.util.ArrayList();
     channelList = Db.Channel().getObjects();


//显示参数
     StringBuffer sbData = new StringBuffer();
     sbData.append(PageHelper.getList("media-info",resultList));
     sbData.append(PageHelper.addElement("start-time",startTime1));
     sbData.append(PageHelper.addElement("end-time",endTime1));
     sbData.append(PageHelper.addElement("icp-id",icpId));
     sbData.append(PageHelper.addElement("imp-id",impId));
     sbData.append(PageHelper.addElement("user-id",userid));
     sbData.append(PageHelper.addElement("media-name",mediaName));
     sbData.append(PageHelper.addElement("order-by",orderBy));
     sbData.append(PageHelper.addElement("order-dir",orderDirectory));
     sbData.append(PageHelper.addElement("command",command));
     sbData.append(PageHelper.addElement("cptype",cpType));
     sbData.append(pageHelper.getList("channels",channelList)) ;
     pageHelper.outPut(out,sbData.toString(),"1010-80-0065-20041022",
             "no any error","",
             "1010-80-65-help10-10-80-66-20041022","wdyou"
             );
 %><%!
     public static SearchHelper searchlimit(long channelId,String userId,
                                                    long startTime,long endTime,
                                                    long icpId,long impId,
                                                    String mediaName){
         SearchHelper searchHelper = new SearchHelper();
         List searchLimits = new ArrayList();

         String mediaLogHistoryTables = "MediaLog as mlh";
         String mediaLogHistoryTablesRelation = "";
         String mediaLogHistoryMediaTables = "MediaLog as mlh,Media as m";
         String mediaLogHistoryMediaTablesRelation = "mlh.medialogMediaid = m.mediaId";

         searchLimits.add(new SearchLimit(startTime,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogStarttime",">=",false));
         searchLimits.add(new SearchLimit(endTime,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogEndtime","<",false));
         searchLimits.add(new SearchLimit(icpId,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogIcpid","=",false));
         searchLimits.add(new SearchLimit(impId,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogImpid","=",false));
         searchLimits.add(new SearchLimit(mediaName,mediaLogHistoryMediaTables,
                 mediaLogHistoryMediaTablesRelation,
                 "m.mediaName"," like ",false));
         searchLimits.add(new SearchLimit(channelId,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogChannelid","=",false));
         searchLimits.add(new SearchLimit(userId,mediaLogHistoryTables,
                 mediaLogHistoryTablesRelation,
                 "mlh.medialogUserid","=",false));
         try{
             searchHelper.checkField(searchLimits);
         }catch(Exception e){
             e.printStackTrace();
         }
       return searchHelper;
     }

     /**
     * 用户角度进行媒体搜索
     * @param channelId
     * @param startTime
     * @param endTime
     * @param mediaName
     * @param orderBy
     * @param orderDirectory
     * @param startRow
     * @param rowCount
     * @return
     */
    public static SearchHelper getUserStatisticLog(long channelId,String userId,
                                                   long startTime,long endTime,
                                                   long icpId,long impId,
                                                   String mediaName,String orderBy,
                                                   String orderDirectory,long startRow,
                                                   long rowCount){

        SearchHelper searchHelper = new SearchHelper();
        try{
            HBSession hb = new HBSession();
            searchHelper = searchlimit(channelId,userId,startTime,endTime,icpId,impId,mediaName);

            String baseHql = searchHelper.getWholeHql();
            String orderHql = baseHql;

            if(orderBy != null && !"".equals(orderBy)){
                orderHql = orderHql + " order by "+orderBy+" ";
                if(orderDirectory != null && !"".equals(orderDirectory)){
                    orderHql = orderHql + " "+orderDirectory;
                }
            }

            Object[] params = searchHelper.getObjectParams();
            searchHelper.setObjList( hb.getObjects(orderHql,
                    params,(int)startRow,(int)rowCount) );
            searchHelper.setAllRowCount((int)hb.getCount(baseHql,params));
        }catch(Exception e){
            System.err.println("["+StringTools.date2string(System.currentTimeMillis())+"] error:"+e.getMessage());
            e.printStackTrace();
        }
        return searchHelper;
    }
 %>