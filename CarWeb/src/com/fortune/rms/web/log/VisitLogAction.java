package com.fortune.rms.web.log;

import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.logic.logicInterface.AdminCspLogicInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.rms.business.log.logic.logicInterface.*;
import com.fortune.rms.business.log.model.*;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.*;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.ejb.criteria.expression.function.AggregationFunction;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.snmp4j.event.CounterListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Namespace("/log")
@ParentPackage("default")
@Results({
        @Result(name = "organizationContribution", location = "/log/organizationContributionReport.jsp"),
        @Result(name = "resourceContribution", location = "/log/resourceContributionReport.jsp"),
        @Result(name = "areaContribution", location = "/log/areaContributionReport.jsp"),
        @Result(name = "onLineUserAnalysis", location = "/log/onLineUserAnalysisReport.jsp"),
        @Result(name = "activityUserAnalysis", location = "/log/activityUserAnalysisReport.jsp"),
        @Result(name = "userDemand", location = "/log/userDemandReport.jsp"),
        @Result(name = "userLogin",location = "/log/userLoginReport.jsp"),
        @Result(name = "channelDemand", location = "/log/channelDemandReport.jsp"),
        @Result(name = "channelOnDemand", location = "/log/channelOnDemandReport.jsp"),
        @Result(name = "resourceOnDemand", location = "/log/resourceOnDemandReport.jsp")
})
@Action(value = "visitLog")
public class VisitLogAction extends BaseAction<VisitLog> {
    private static final long serialVersionUID = 3243534534534534l;
    private VisitLogLogicInterface visitLogLogicInterface;
    private AdminCspLogicInterface adminCspLogicInterface;
    private ChannelLogicInterface channelLogicInterface;
    private Map objMaps;
    private List objLists;
    private String objJson;
    private DailyStaticsLogLogicInterface dailyStaticsLogLogicInterface;

    @SuppressWarnings("unchecked")
    public VisitLogAction() {
        super(VisitLog.class);
    }
    /**
     * @param visitLogLogicInterface the visitLogLogicInterface to set
     */
    @Autowired
    public void setVisitLogLogicInterface(
            VisitLogLogicInterface visitLogLogicInterface) {
        this.visitLogLogicInterface = visitLogLogicInterface;
        setBaseLogicInterface(visitLogLogicInterface);
    }

    @Autowired
    public void setAdminCspLogicInterface(AdminCspLogicInterface adminCspLogicInterface) {
        this.adminCspLogicInterface = adminCspLogicInterface;
    }

    @Autowired
    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }
    @Autowired
    public void setDailyStaticsLogLogicInterface(DailyStaticsLogLogicInterface dailyStaticsLogLogicInterface) {
        this.dailyStaticsLogLogicInterface = dailyStaticsLogLogicInterface;
    }

    public String bingfaLogs() {
        try {
            String contentName = getRequestParam("cotnentName","");
            String vl_cpId = getRequestParam("vl_cpId","");
            String vl_spId = getRequestParam("vl_spId","");
            String excel = getRequestParam("excel","");
            String startTime = getRequestParam("startDate", "");
            String endTime = getRequestParam("endDate", "");
            Date fullStartDate = StringUtils.string2date(startTime, "yyyy-MM-dd HH:mm:ss");
            //每15分钟一次，即一天分成96份
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            long dateDisparity =(endDate.getTime()-startDate.getTime())/(1000*60*60*24)+1;
            long totalCount = 96*dateDisparity;
            long period = 900000;

            if("".equals(contentName)&&"".equals(vl_cpId)&&"".equals(vl_spId)){
                //只按日期查询，不按其他条件查询时候查询DAILYSTATICS_LOG表
                String jsonDate = "";
                List<Object[]> list = visitLogLogicInterface.getAllBingFaFromDailyStaticsLog(startDate,endDate);
                if("isTrue".equals(excel)){
                    String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"bingFa");
                    if("success".equals(result)){
                        return null;
                    }
                }else{
                    if(list.size()<=1){
                        Object[]  o = list.get(0);
                        String quarterBingFa  = o[0].toString();
                        String maxBingFa = o[1].toString();
                        String[]  s = quarterBingFa.split(",");
                        String[]  ss =maxBingFa.split("=");
                        String maxBingFaValue = ss[1].toString();
                        if(s.length>0){
                            for(int i = 0;i<s.length;i++){
                                String [] s1 = s[i].split("=");
                                String time = s1[0];
                                String value = s1[1];
                                jsonDate += "{" +
                                        "\"time\":\"" + time+ "\"," +
                                        "\"value\":\"" + value + "\"" +
                                        "},";
                            }
                            jsonDate = jsonDate.substring(0,jsonDate.length()-1);
                            String result = "{ \"totalCount\":\"" + Integer.parseInt(maxBingFaValue)+ "\",\"objs\":[";
                            result += jsonDate;
                            result += "]}";
                            directOut(result);
                        }
                        return null;
                    }else{
                        String jsonData = "";
                        int maxValue=0;
                        for(Object[] o :list ){
                            String MaxBingFa  = o[1].toString();
                            String[]  s = MaxBingFa.split("=");
                            String time = s[0];
                            String value = s[1];
                            jsonData += "{" +
                                    "\"time\":\"" + time+ "\"," +
                                    "\"value\":\"" + value + "\"" +
                                    "},";
                            if(Integer.parseInt(value)>maxValue){
                                maxValue = Integer.parseInt(value);
                            }
                        }
                        jsonData = jsonData.substring(0,jsonData.length()-1);
                        String result = "{ \"totalCount\":\"" + maxValue+ "\",\"objs\":[";
                        result += jsonData;
                        result += "]}";
                        directOut(result);
                    }
                }
            }else{
                //有除日期以外的查询就查询visitLog表。
                String sqlTable = "select " +
                        "vl.startTime,vl.endTime" +
                        " from " +
                        "com.fortune.rms.business.log.model.VisitLog vl";

                long channelId = getRequestIntParam("vl_channelId", -1);
                if (channelId > 0) {
                    String subChannelIds = channelLogicInterface.getAllChildIds(channelId);
                    if ("".equals(subChannelIds)) {
                        subChannelIds = "" + channelId;
                    } else {
                        subChannelIds += "," + channelId;
                    }
                    sqlTable += " where vl.channelId in (" + subChannelIds + ") ";
                }
                String[][] params = new String[][]{
                        {"vl.spId", "", "=", "", ""},
                        {"vl.cpId", "", "=", "", ""},
                        {"c.name", "contentName", "like", "%?%", "vl.contentId in (select c.id from com.fortune.rms.business.content.model.Content c where ?)"},
                        {"vl.startTime", "startDate", ">=", "00:00:00", ""},
                        {"vl.startTime", "endDate", "<=", "23:59:59", ""}
                };
                SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);
                List list1 = searchResult.getRows();
                HashMap timeHm = new HashMap();
                long curTime = fullStartDate.getTime();

                for (int i = 0; i < totalCount; i++) {

                    for (int j = 0; j < list1.size(); j++) {
                        Object objs[] = (Object[]) list1.get(j);
                        long oneStartTime = ((Date) objs[0]).getTime();
                        long oneEndTime = ((Date) objs[1]).getTime();
                        if (oneStartTime < curTime && oneEndTime >= curTime) {
                            if (timeHm.get("" + curTime) == null) {
                                timeHm.put("" + curTime, new Long(0));
                            }
                            long count = ((Long) timeHm.get("" + curTime)).longValue();
                            count++;
                            timeHm.put("" + curTime, new Long(count));
                        }
                    }
                    curTime += period;
                }

                curTime = fullStartDate.getTime();
                List list2 = new ArrayList();

                String dateStyle = "dd-HH:mm　";
                if (startTime.substring(0,10).equals(endTime.substring(0,10))) {
                    dateStyle = "HH:mm　";
                }

                long maxValue = 0;
                long maxValues = 0;
                long count = 0;
                long maxValueTime = 0;
                //如果日期格式为dd-HH:mm则为查询多个日期的，只获取每天的最大并发量。
                if("dd-HH:mm　".equals(dateStyle)){
                    for(int i = 1 ;i <= dateDisparity ;i++ ){
                        //进入第二个日期前要把上一个日期的最大值清空，防止第二天的最大值比第一天的小。无法存储第二天的值。
                        maxValues = 0;
                        for(int j = 96*(i-1); j < i*96;j++){
                            if (timeHm.get("" + curTime) != null) {
                                count = ((Long) timeHm.get("" + curTime)).longValue();
                            } else{
                                //如果timeHm.get("" + curTime) != null则证明所要查询日期未到，并发值肯定为0.下面if不执行，这里的时间也要替换。同上maxValue=0；
                                count = 0;
                                maxValueTime = curTime;
                            }

                            if (count > maxValues) {
                                maxValues = count;
                                maxValueTime = curTime;
                            }
                            curTime += period;
                        }
                        if(maxValues>maxValue){
                            maxValue = maxValues;
                        }
                        Object objs[] = new Object[]{StringUtils.date2string(new Date(maxValueTime), dateStyle), new Long(maxValues)};
                        list2.add(objs);
                    }
                }else{
                    for (int i = 0; i < totalCount; i++) {

                        if (timeHm.get("" + curTime) != null) {
                            count = ((Long) timeHm.get("" + curTime)).longValue();
                        }else{
                            //如果 timeHm.get("" + curTime) ==null 则说明还未到这个时刻，那么这个时刻的并发值应该为0；
                            count = 0;
                        }

                        if (count > maxValue) {
                            maxValue = count;
                        }


                        Object objs[] = new Object[]{StringUtils.date2string(new Date(curTime), dateStyle), new Long(count)};
                        list2.add(objs);
                        //System.out.println(StringTools.date2string(curTime)+" "+count);
                        curTime += period;
                    }
                }
                searchResult.setRowCount((int) maxValue);
                searchResult.setRows(list2);
                String output = searchObjectsJbon(new String[]{"time", "value"}, searchResult);
                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public String bingfaLogs() {
//        try {
//            String sqlTable = "select " +
//                    "vl.startTime,vl.endTime" +
//                    " from " +
//                    "com.fortune.rms.business.log.model.VisitLog vl";
//
//            long channelId = getRequestIntParam("vl_channelId", -1);
//            if (channelId > 0) {
//                String subChannelIds = channelLogicInterface.getAllChildIds(channelId);
//                if ("".equals(subChannelIds)) {
//                    subChannelIds = "" + channelId;
//                } else {
//                    subChannelIds += "," + channelId;
//                }
//                sqlTable += " where vl.channelId in (" + subChannelIds + ") ";
//            }
//            String[][] params = new String[][]{
//                    {"vl.spId", "", "=", "", ""},
//                    {"vl.cpId", "", "=", "", ""},
//                    {"c.name", "contentName", "like", "%?%", "vl.contentId in (select c.id from com.fortune.rms.business.content.model.Content c where ?)"},
//                    {"vl.startTime", "startDate", ">=", "00:00:00", ""},
//                    {"vl.startTime", "endDate", "<=", "23:59:59", ""}
//            };
//            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);
//            List list1 = searchResult.getRows();
//
//            String startDate = getRequestParam("startDate", "");
//            String endDate = getRequestParam("endDate", "");
//
//            Date startTime = StringUtils.string2date(startDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//            Date endTime = StringUtils.string2date(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
//
//            long totalCount = 80;
//            long period = (long) (endTime.getTime() - startTime.getTime()) / totalCount;
//
//            HashMap timeHm = new HashMap();
//
//            long curTime = startTime.getTime();
//
//            for (int i = 0; i < totalCount; i++) {
//
//                for (int j = 0; j < list1.size(); j++) {
//                    Object objs[] = (Object[]) list1.get(j);
//                    long oneStartTime = ((Date) objs[0]).getTime();
//                    long oneEndTime = ((Date) objs[1]).getTime();
//
//                    if (oneStartTime < curTime && oneEndTime >= curTime) {
//                        if (timeHm.get("" + curTime) == null) {
//                            timeHm.put("" + curTime, new Long(0));
//                        }
//                        long count = ((Long) timeHm.get("" + curTime)).longValue();
//                        count++;
//                        timeHm.put("" + curTime, new Long(count));
//                    }
//                }
//
//                //System.out.println(StringTools.date2string(curTime)+" "+count);
//                curTime += period;
//            }
//
////            for (int j=0; j<list1.size(); j++){
////                Object objs[] = (Object[])list1.get(j);
////                long oneStartTime = ((Date)objs[0]).getTime();
////                long oneEndTime = ((Date)objs[0]).getTime();
////
////                long key = oneStartTime - oneStartTime % period + period;
////
////                if (oneStartTime<key && oneEndTime>=key){
////                    if (timeHm.get(""+key)==null){
////                        timeHm.put(""+key, new Long(0));
////                    }
////                    long count = ((Long)timeHm.get(""+key)).longValue();
////                    count ++;
////                    timeHm.put(""+key, new Long(count));
////                }
////            }
//
//            curTime = startTime.getTime();
//
//            List list2 = new ArrayList();
//
//            String dateStyle = "dd-HH:mm　";
//            if (startDate.equals(endDate)) {
//                dateStyle = "HH:mm　";
//            }
//
//            long maxValue = 0;
//
//            for (int i = 0; i < totalCount; i++) {
//                long count = 0;
//                if (timeHm.get("" + curTime) != null) {
//                    count = ((Long) timeHm.get("" + curTime)).longValue();
//                }
//
//                if (count > maxValue) {
//                    maxValue = count;
//                }
//
//                Object objs[] = new Object[]{StringUtils.date2string(new Date(curTime), dateStyle), new Long(count)};
//                list2.add(objs);
//                //System.out.println(StringTools.date2string(curTime)+" "+count);
//                curTime += period;
//            }
//
//            searchResult.setRowCount((int) maxValue);
//            searchResult.setRows(list2);
//
//            String output = searchObjectsJbon(new String[]{"time", "value"}, searchResult);
//
//            directOut(output);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public String visitLogs() {
        try {
            String sqlTable = "select " +
                    "vl.id,vl.spId,vl.cpId,vl.channelId,vl.contentId,vl.contentPropertyId,vl.url," +
                    "vl.userId,vl.userIp,vl.areaId,vl.isFree,vl.startTime,vl.endTime,vl.length" +
                    " from " +
                    "com.fortune.rms.business.log.model.VisitLog vl";

            long channelId = getRequestIntParam("vl_channelId", -1);
            if (channelId > 0) {
                String subChannelIds = channelLogicInterface.getAllChildIds(channelId);
                if ("".equals(subChannelIds)) {
                    subChannelIds = "" + channelId;
                } else {
                    subChannelIds += "," + channelId;
                }
                sqlTable += " where vl.channelId in (" + subChannelIds + ") ";
            }

            String[][] params = new String[][]{
                    {"vl.spId", "", "=", "", ""},
                    {"vl.cpId", "", "=", "", ""},
                    {"c.name", "contentName", "like", "%?%", "vl.contentId in (select c.id from com.fortune.rms.business.content.model.Content c where ?)"},
                    {"vl.startTime", "startDate", ">=", "00:00:00", ""},
                    {"vl.startTime", "endDate", "<=", "23:59:59", ""},
                    {"vl.userId", "", "like", "%?%", ""},
                    {"vl.userIp", "", "like", "%?%", ""},
                    {"vl.length", "", "=", "", ""}
            };


            SearchResult<Object[]> searchResult = searchObjects(sqlTable, params);

            String output = searchObjectsJbon(sqlTable, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String userLoginLogs(){
        try{
            log.debug("in userLoginLogs");
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate","");
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            String excel = getRequestParam("excel","");
            List<Object[]> list = visitLogLogicInterface.getUserLoginLogs(startDate,endDate);
            if(list.size()>0){
                if(("isTrue").equals(excel)){
                   String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"userLoginLog");
                    if("success".equals(result)){
                        return null;
                    }
                }else{
                    SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                    searchResult.setRows(list);
                    searchResult.setRowCount(list.size());

                    String output = searchObjectsJbon(new String[]{"vl_areaId","vl_oneLogin","vl_twoLogin","vl_threeLogin","vl_fourLogin","vl_fiveLogin","vl_countLogin","vl_timeCountLogin","vl_dayCountLogin"}, searchResult);

                    directOut(output);
                }
            }
        }catch(Exception e){
           e.printStackTrace();
        }
        return null;
    }

    public String areaLogs() {
        try {
            log.debug("in areaLogs");
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate", "");
            String excel = getRequestParam("excel","");
            Date startDate = StringUtils.string2date(startTime, "yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            List<Object[]> list = visitLogLogicInterface.getAreaLogsFromAreaDemandLog(startDate,endDate);
            if(list.size()>0){
                if(("isTrue").equals(excel)){
                    String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"areaLog");
                    if("success".equals(result)){
                        return null;
                    }
                }else{
                    //拼成json格式导出
                    String result = "{ \"totalCount\":\"" + list.size() + "\",\"success\":\"true\",\"objs\":[";
                    for(int i = 0 ;i<list.size();i++){
                        Object[] o = list.get(i);
                        String type = o[0].toString();
                        Long areaId = Long.valueOf(o[1].toString());
                        Long count = Long.valueOf(o[2].toString());
                        Long padCount = Long.valueOf(o[3].toString());
                        Long phoneCount = Long.valueOf(o[4].toString());
                        Long length = Long.valueOf(o[5].toString());
                        Long padLength = Long.valueOf(o[6].toString());
                        Long phoneLength = Long.valueOf(o[7].toString());
                        Long bytesSend = Long.valueOf(o[8].toString());
                        Long bytesSendPad = Long.valueOf(o[9].toString());
                        Long bytesSendPhone = Long.valueOf(o[10].toString());
                        Long mobileBytesSend = Long.valueOf(o[11].toString());
                        Long elseBytesSend = Long.valueOf(o[12].toString());
                        Long mobileCount = Long.valueOf(o[13].toString());
                        Long elseCount = Long.valueOf(o[14].toString());
                        Long mobileLength = Long.valueOf(o[15].toString());
                        Long elseLength = Long.valueOf(o[16].toString());
                        Long userOnLineCount = Long.valueOf(o[17].toString());
                        result += "{" +
                                "\"vl_type\":\"" + type + "\"," +
                                "\"vl_areaId\":\"" + areaId + "\"," +
                                "\"vl_count\":\"" + count + "\"," +
                                "\"vl_padCount\":\"" + padCount + "\"," +
                                "\"vl_phoneCount\":\"" + phoneCount + "\"," +
                                "\"vl_length\":\"" + length + "\"," +
                                "\"vl_padLength\":\"" + padLength + "\"," +
                                "\"vl_phoneLength\":\"" + phoneLength + "\"," +
                                "\"vl_bytesSend\":\"" + bytesSend + "\"," +
                                "\"vl_bytesSendPad\":\"" + bytesSendPad + "\"," +
                                "\"vl_bytesSendPhone\":\"" + bytesSendPhone + "\"," +
                                "\"vl_mobileBytesSend\":\""+ mobileBytesSend + "\","+
                                "\"vl_elseBytesSend\":\""+ elseBytesSend + "\","+
                                "\"vl_mobileCount\":\""+ mobileCount + "\","+
                                "\"vl_elseCount\":\""+ elseCount + "\","+
                                "\"vl_mobileLength\":\""+ mobileLength + "\","+
                                "\"vl_elseLength\":\""+ elseLength + "\","+
                                "\"vl_userOnlineCount\":\""+ userOnLineCount + "\""+
                                "},";
                    }
                    result = result.substring(0, result.length() - 1);
                    result += "]}";
                    directOut(result);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String spLogs() {
        try {
            log.debug("in spLogs");
            String startTime = getRequestParam("startDate","").substring(0,10);
            String endTime = getRequestParam("endDate", "").substring(0,10);
            String excel = getRequestParam("excel","");
            Date startDate = StringUtils.string2date(startTime, "yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            List<Object[]> list = visitLogLogicInterface.getSpLogsFromVisitLog(startDate,endDate);
            if("isTrue".equals(excel)){
                String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"spLog");
                if("success".equals(result)){
                    return null;
                }
            }else{
                //拼成json格式
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list);
                searchResult.setRowCount(list.size());
                String output = searchObjectsJbon(new String[]{"vl_spId","vl_count","vl_padCount","vl_phoneCount","vl_length","vl_padLength","vl_phoneLength","vl_bytesSend","vl_bytesSendPad","vl_bytesSendPhone"}, searchResult);
                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String cpLogs() {
        try {

            String sqlTable = "select " +
                    "vl.cpId,count(*),sum(vl.length)" +
                    " from " +
                    "com.fortune.rms.business.log.model.VisitLog vl";

            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String spId = getRequestParam("vl_spId", "");
            if (!"".equals(spId)) {
                searchCondition.appendAndSqlCondition(
                        " vl.spId=? ",
                        new Long(spId),
                        new LongType());
            }

            String startDate = getRequestParam("startDate", "");
            if (!"".equals(startDate)) {
                searchCondition.appendAndSqlCondition(
                        " vl.startTime>? ",
                        new Timestamp(StringUtils.string2date(startDate + " 00:00:00").getTime()),
                        new DateType());
            }

            String endDate = getRequestParam("endDate", "");
            Date startTime = StringUtils.string2date(endDate,"yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.DATE,1);
            Date tempDate = cal.getTime();
            String endDate1 = StringUtils.date2string(tempDate,"yyyy-MM-dd");
            if (!"".equals(endDate)) {
                searchCondition.appendAndSqlCondition(
                        " vl.startTime<? ",
                        new Timestamp(StringUtils.string2date(endDate1 + " 23:59:59").getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            sql += " group by vl.cpId ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by " + orderBy + " " + orderDir;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRows(list1);
            searchResult.setRowCount(list1.size());

            String output = searchObjectsJbon(new String[]{"vl_cpId", "vl_count", "vl_length"}, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String contentFilmAndTvLogs(){
        try{
            log.debug("in contentFilmAndTvLogs");
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate","");
            int contentType = getRequestIntParam("contentType",-1);
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            int pageSize = getRequestIntParam("limit",20);
            String excel = getRequestParam("excel","");
            List<Object[]> list = visitLogLogicInterface.getContentFilmAndTvLog(startDate,endDate,contentType,pageSize);
            if(("isTrue").equals(excel)){
                String result = visitLogLogicInterface.createExcelFile(startDate,endDate,contentType,list,"contentFilmAndTvLog");
                if("success".equals(result)){
                    return null;
                }
                return null;
            }else{
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list);
                searchResult.setRowCount(list.size());
                String output = searchObjectsJbon(new String[]{"vl_spId","vl_contentType" ,"vl_contentId","vl_channelId","vl_count", "vl_length","vl_bytesSend"}, searchResult);
                directOut(output);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public String clientLogs() {
        try {

//            String sqlTable = "select " +
//                    "count(*),status" +
//                    " from " +
//                    "com.fortune.rms.business.log.model.ClientLog";
            String sqlTable = "select count(c.area_id)," +
                    "(select count(cl.area_id) from client_log cl where status =1 and c.area_id = cl.area_id) as download," +
                    "(select count(cl.area_id) from client_log cl where status =2 and c.area_id = cl.area_id) as install," +
                    "c.area_id from client_log c ";

            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String startDate = getRequestParam("startDate", "");
            if (!"".equals(startDate)) {
                searchCondition.appendAndSqlCondition(
                        " Time >? ",
                        new Timestamp(StringUtils.string2date(startDate + " 00:00:00").getTime()),
                        new DateType());
            }

            String endDate = getRequestParam("endDate", "");
            Date startTime = StringUtils.string2date(endDate,"yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.DATE,1);
            Date tempDate = cal.getTime();
            String endDate1 = StringUtils.date2string(tempDate,"yyyy-MM-dd");
            if (!"".equals(endDate)) {
                searchCondition.appendAndSqlCondition(
                        " Time <? ",
                        new Timestamp(StringUtils.string2date(endDate1 + " 23:59:59").getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            //sql += " group by status ";
            sql += " group by c.area_id ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by " + orderBy + " " + orderDir;
            }

            List list1 = HibernateUtils.findSQLList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRows(list1);
            searchResult.setRowCount(list1.size());

            String output = searchObjectsJbon(new String[]{"vl_count","vl_downloadCount","vl_installCount", "vl_areaId"}, searchResult);

            directOut(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String channelLogs() {
        try {

            log.debug("in channelLogs");
            long channelId = getRequestIntParam("channelId", -1);
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate","");
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            String excel = getRequestParam("excel","");
            List<Object[]> list = visitLogLogicInterface.getChannelLogsFromChannelDemandLog(startDate,endDate,channelId,excel);
            if(("isTrue").equals(excel)){
                String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"channelLog");
                if("success".equals(result)){
                    return null;
                }
                return null;
            }else{
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list);
                searchResult.setRowCount(list.size());

                String output = searchObjectsJbon(new String[]{"vl_channelId","vl_name" ,"vl_count", "vl_length","vl_grade","vl_bytesSend"}, searchResult);

                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String contentZtLogs() {
        try {
            String sqlTable = "select "+
                    "count(*),vl.contentId,vl.type,vl.cspId,vl.isSpecial" +
                    " from "+
                    "com.fortune.rms.business.log.model.ContentZtLog vl";

            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String spId = getRequestParam("vl_spId", "");
            if (!"".equals(spId)) {
                searchCondition.appendAndSqlCondition(
                        " vl.cspId=? ",
                        new Long(spId),
                        new LongType());
            }

            String isSpecial = getRequestParam("vl_isSpecial", "");
            if (!"".equals(isSpecial)) {
                searchCondition.appendAndSqlCondition(
                        " vl.isSpecial=? ",
                        new Long(isSpecial),
                        new LongType());
            }

            String contentName = getRequestParam("contentName", "");
            if (!"".equals(contentName)) {
                searchCondition.appendAndSqlCondition(
                        " vl.contentId in (select c.id from Content c where c.name like ? ) ",
                        "%" + contentName + "%",
                        new StringType());
            }


            String startTime = getRequestParam("startDate", "");
            if (!"".equals(startTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.createTime>=? ",
                        new Timestamp(StringUtils.string2date(startTime,"yyyy-MM-dd").getTime()),
                        new DateType());
            }

            String endTime = getRequestParam("endDate", "");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DATE,1);
            Date tempDate = calendar.getTime();
            String endTime1 = StringUtils.date2string(tempDate,"yyyy-MM-dd");
            if (!"".equals(endTime1)) {
                searchCondition.appendAndSqlCondition(
                        " vl.createTime<=? ",
                        new Timestamp(StringUtils.string2date(endTime1+" 23:59:59").getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            sql += " group by vl.contentId,vl.type,vl.cspId,vl.isSpecial ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 20);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if(!"".equals(orderBy)){
                orderBy = orderBy.replace('_', '.');
                sql+= " order by  "+orderBy +" " +orderDir;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);

            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRows(list1);
            searchResult.setRowCount(list1.size());
            String output = searchObjectsJbon(new String[]{"vl_count","vl_contentId","vl_type","vl_cspId","vl_isSpecial"}, searchResult);
            directOut(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String contentLogs() {
        try {
            String sqlTable = "select "+
                    "vl.spId,vl.contentId,sum(vl.count),sum(vl.padCount),sum(vl.phoneCount) ,sum(vl.length),sum(vl.padLength),sum(vl.phoneLength),sum(vl.bytesSend),sum(vl.bytesSendPad),sum(bytesSendPhone)" +
                    " from "+
                    "com.fortune.rms.business.log.model.ContentDemandLog vl ";
            int type = getRequestIntParam("type",-1);
            String excel = getRequestParam("excel","");
            if(!"".equals(type)){
                if(4==type||5==type){
                   //type==？ 1为点播，2为直播，3为拉动，4为按点播时长统计点播量。 5为按流量统计
                    sqlTable += " where vl.type = 1 ";
                }else{
                    sqlTable +=  " where vl.type = "+type;
                }
            }
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String spId = getRequestParam("vl_spId", "");
            if (!"".equals(spId)) {
                searchCondition.appendAndSqlCondition(
                        " vl.spId=? ",
                        new Long(spId),
                        new LongType());
            }

            String contentName = getRequestParam("contentName", "");
            if (!"".equals(contentName)) {
                searchCondition.appendAndSqlCondition(
                        " vl.contentId in (select c.id from Content c where c.name like ? ) ",
                        "%" + contentName + "%",
                        new StringType());
            }


            String startTime = getRequestParam("startDate", "");
            if (!"".equals(startTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics>=? ",
                        new Timestamp(StringUtils.string2date(startTime,"yyyy-MM-dd").getTime()),
                        new DateType());
            }

            String endTime = getRequestParam("endDate", "");
            if (!"".equals(endTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics<=? ",
                        new Timestamp(StringUtils.string2date(endTime).getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            sql += " group by vl.spId,vl.contentId ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 12);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if(4==type){
                sql+= " order by sum(vl.length) " +orderDir;
            }
            if(5==type){
                sql+= " order by sum(vl.bytesSend) "+ orderDir;
            }else{
                sql += " order by sum(vl.count) " +orderDir;
            }
            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            List list2 = list1;
            if("isTrue".equals(excel)){
                String result = "";
                Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
                Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
                if(1==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"contentLog");
                }
                if(2==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"contentLiveLog");
                }
                if(3==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"contentLadongLog");
                }
                if(4==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"contentByLengthLog");
                }
                if(5==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"contentByNetFlowLog");
                }
                if("success".equals(result)){
                    return  null;
                }
            }else{
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list1);
                searchResult.setRowCount(list1.size());
                String output = searchObjectsJbon(new String[]{"vl_spId", "vl_contentId", "vl_count","vl_padCount","vl_phoneCount", "vl_length","vl_padLength","vl_phoneLength","vl_bytesSend","vl_bytesSendPad","vl_bytesSendPhone"}, searchResult);
                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String channelContentLogs() {
        try {
            String sqlTable = "select " +
                    "vl.spId,vl.contentId,vl.channelId,sum(vl.count),sum(vl.length),sum(vl.bytesSend)" +
                    " from " +
                    "com.fortune.rms.business.log.model.ContentDemandLog vl ";

            long channelId = getRequestIntParam("vl_channelId", -1);
            String excel = getRequestParam("excel","");
            String subChannelIds="";
            if (channelId > 0) {
                subChannelIds += channelLogicInterface.getAllChildIds(channelId);
                if (!"".equals(subChannelIds)) {
                    subChannelIds += ","+channelId;
                    sqlTable += " where vl.channelId in (" + subChannelIds + ") ";
                }else{
                    subChannelIds = channelId +"";
                    sqlTable += " where vl.channelId = " + subChannelIds;
                }
            }
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String startTime = getRequestParam("startDate", "");
            if (!"".equals(startTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics>=? ",
                        new Timestamp(StringUtils.string2date(startTime).getTime()),
                        new DateType());
            }

            String endTime = getRequestParam("endDate", "");
            if (!"".equals(endTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics<=? ",
                        new Timestamp(StringUtils.string2date(endTime).getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            sql += " group by vl.spId,vl.contentId,vl.channelId ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by  sum(vl.count) " + orderDir;
            }
            if(("isTrue").equals(excel)){
                sql += " order by  sum(vl.count) DESC" ;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);

            if(("isTrue").equals(excel)){
                Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
                Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
                String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list1,"channelContentLog");
                if("success".equals(result)){
                    return null;
                }
                return null;
            }else{
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list1);
                searchResult.setRowCount(list1.size());

                String output = searchObjectsJbon(new String[]{"vl_spId", "vl_contentId", "vl_channelId","vl_count", "vl_length","vl_bytesSend"}, searchResult);

                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrganizationContributionCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        String channelsAndLeafs = getRequestParam("channelsAndLeafs", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long cspId;
                AdminCsp adminCsp = new AdminCsp();
                adminCsp.setAdminId(admin.getId());
                List<AdminCsp> adminCsps = adminCspLogicInterface.search(adminCsp);
                if (adminCsps != null && adminCsps.size() == 1) {
                    cspId = adminCsps.get(0).getCspId();
                } else {
                    cspId = -1;
                }
                objMaps = visitLogLogicInterface.getOrganizationContributionCount(cspId, startTime, endTime, channelsAndLeafs);
//               JsonUtils jsonUtils = new JsonUtils();
//               directOut(jsonUtils.getJsonArray(objs));
            }
        }
        return "organizationContribution";
    }

    public String getResourceContributionCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        String channelsAndLeafs = getRequestParam("channelsAndLeafs", "");
        String contentName = getRequestParam("contentName", "");
        int cpId = getRequestIntParam("cpId", 0);
        String playTimeSelect = getRequestParam("playTimeSelect", "");
        String channelSelect = getRequestParam("channelSelect", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long cspId;
                AdminCsp adminCsp = new AdminCsp();
                adminCsp.setAdminId(admin.getId());
                List<AdminCsp> adminCsps = adminCspLogicInterface.search(adminCsp);
                if (adminCsps != null && adminCsps.size() == 1) {
                    cspId = adminCsps.get(0).getCspId();
                } else {
                    cspId = -1;
                }
                objMaps = visitLogLogicInterface.getResourceContributionCount(cspId, cpId, startTime, endTime, channelsAndLeafs, contentName, channelSelect, playTimeSelect, pageBean);

            }
        }

        return "resourceContribution";
    }

    public String getAreaContributionCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long cspId = admin.getCspId();
                if (cspId == 1) {
                    cspId = -1;
                }
                objMaps = visitLogLogicInterface.getAreaContributionCount(cspId, startTime, endTime);
//               JsonUtils jsonUtils = new JsonUtils();
//               directOut(jsonUtils.getJsonArray(objs));
            }
        }
        return "areaContribution";
    }

    public String getOnLineUserAnalysisCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        long cpId = 0;
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if (spId == 1) {
                    spId = -1;
                }
                objLists = visitLogLogicInterface.getOnLineUserAnalysisCount(spId, cpId, startTime, endTime);
                JsonUtils jsonUtils = new JsonUtils();
                objJson = jsonUtils.getJsonArray(objLists);
            }
        }
        return "onLineUserAnalysis";
    }

    public String getActivityUserAnalysisCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if (spId == 1) {
                    spId = -1;
                }
                objLists = visitLogLogicInterface.getActivityUserAnalysisCount(spId, startTime, endTime, pageBean);
            }
        }
        return "activityUserAnalysis";
    }

    public String getUserDemandCount() {
        String startTime = getRequestParam("startTime", "").substring(0,10);
        String endTime = getRequestParam("endTime", "").substring(0,10);
        Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE,1);
        Date tempDate = cal.getTime();
        String endTime1 = StringUtils.date2string(tempDate,"yyyy-MM-dd");
        String contentName = getRequestParam("contentName", "");
        String channelName = getRequestParam("channelName", "");
        String userIp = getRequestParam("userIp", "");
        String userId = getRequestParam("userId", "");
        long cpId = getRequestIntParam("cpId", -1);
        String playTime = getRequestParam("playTime", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if (spId == 1) {
                    spId = -1;
                }
                objMaps = visitLogLogicInterface.getUserDemandCount(spId, cpId, startTime, endTime,endTime1, contentName, channelName, userIp, userId, playTime, pageBean);
            }
        }
        return "userDemand";
    }

    public String getUserLoginCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        String userIp = getRequestParam("userIp", "");
        String userId = getRequestParam("userId", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            objMaps = visitLogLogicInterface.getUserLoginCount(startTime, endTime,userIp, userId, pageBean);
        }
        return "userLogin";
    }

    public String getChannelDemandCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        String channelsAndLeafs = getRequestParam("channelsAndLeafs", "");
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if (spId == 1) {
                    spId = -1;
                }
                objLists = visitLogLogicInterface.getChannelDemandCount(spId, startTime, endTime, channelsAndLeafs);
            }
        }

        return "channelDemand";
    }

    public String getChannelOnDemandCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        long isFree = getRequestIntParam("isFree",-1);
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                long spId = admin.getCspId();
                if (spId == 1) {
                    spId = -1;
                }
                objMaps = visitLogLogicInterface.getChannelOnDemandCount(spId,startTime,endTime,isFree,pageBean);
            }
        }
        return "channelOnDemand";
    }

    public String getResourceOnDemandCount() {
        String startTime = getRequestParam("startTime", "");
        String endTime = getRequestParam("endTime", "");
        long spId = getRequestIntParam("spId",-1);
        String channelName = getRequestParam("channelName","");
        String playTime = getRequestParam("playTime","");
        objMaps = visitLogLogicInterface.getResourceOnDemandCount(spId,startTime,endTime,channelName,playTime,pageBean);

        return "resourceOnDemand";
    }

    public String netFlowLogs(){
        log.debug("in netFlowLogs");
        try{
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate","");
            String excel = getRequestParam("excel","");
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            if("".equals(startDate)){
                return null;
            }
           List<Object[]> list = visitLogLogicInterface.getAllNetFlowFromDailyStaticsLog(startDate, endDate);
            if(("isTrue").equals(excel)){
                String result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list,"netFlowLog");
                if("success".equals(result)){
                    return null;
                }
            }else{
                String result = "{ \"totalCount\":\"" + list.size() + "\",\"success\":\"true\",\"objs\":[";
                if(list.size()>0){
                    for (Object[] o:list){
                        String date = o[0].toString();
                        String allNetFlow = o[1].toString();
                        String mobileNetFlow = o[2].toString();
                        String elseNetFlow = o[3].toString();
                        String allNetFlowPad = o[4].toString();
                        String allNetFlowPhone = o[5].toString();
                        String allNetFlowLive = o[6].toString();
                        String allNetFlowLivePad = o[7].toString();
                        String allNetFlowLivePhone = o[8].toString();
                        String allNetFlowContent = o[9].toString();
                        String allNetFlowContentPad = o[10].toString();
                        String allNetFlowContentPhone = o[11].toString();
                        String wasuNetFlow = o[12].toString();
                        String wasuLadongNetFlow = o[13].toString();
                        String vooleNetFlow = o[14].toString();
                        String vooleLadongNetFlow = o[15].toString();
                        String bestvNetFlow = o[16].toString();
                        String bestvLadongNetFlow = o[17].toString();
                        String onlineUser = o[18].toString();
                        String onlineUserNetFlow = o[19].toString();
                        String allLength = o[20].toString();
                        String allCount = o[21].toString();
                        String mobileLength = o[22].toString();
                        String mobileCount = o[23].toString();
                        String padLength = o[24].toString();
                        String padCount = o[25].toString();
                        String phoneLength = o[26].toString();
                        String phoneCount = o[27].toString();
                        String liveLength = o[28].toString();
                        String liveCount = o[29].toString();
                        String livePadLength = o[30].toString();
                        String livePadCount = o[31].toString();
                        String livePhoneLength = o[32].toString();
                        String livePhoneCount = o[33].toString();
                        String contentLength = o[34].toString();
                        String contentCount = o[35].toString();
                        String contentPadLength = o[36].toString();
                        String contentPadCount = o[37].toString();
                        String contentPhoneLength = o[38].toString();
                        String contentPhoneCount = o[39].toString();
                        String bestvCount = o[40].toString();
                        String bestvLength = o[41].toString();
                        String wasuLength = o[42].toString();
                        String wasuCount = o[43].toString();
                        String vooleCount = o[44].toString();
                        String vooleLength = o[45].toString();
                        String elseLength = o[46].toString();
                        String elseCount = o[47].toString();

                        result += "{" +
                                "\"date\":\"" + date+ "\"," +
                                "\"allNetFlow\":\"" + allNetFlow+ "\"," +
                                "\"mobileNetFlow\":\"" + mobileNetFlow+ "\"," +
                                "\"elseNetFlow\":\"" + elseNetFlow+ "\"," +
                                "\"allNetFlowPad\":\"" + allNetFlowPad+ "\"," +
                                "\"allNetFlowPhone\":\"" + allNetFlowPhone+ "\"," +
                                "\"allNetFlowLive\":\"" + allNetFlowLive+ "\"," +
                                "\"allNetFlowLivePad\":\"" + allNetFlowLivePad+ "\"," +
                                "\"allNetFlowLivePhone\":\"" + allNetFlowLivePhone+ "\"," +
                                "\"allNetFlowContent\":\"" + allNetFlowContent + "\"," +
                                "\"allNetFlowContentPad\":\"" + allNetFlowContentPad + "\"," +
                                "\"allNetFlowContentPhone\":\"" + allNetFlowContentPhone + "\"," +
                                "\"wasuNetFlow\":\"" + wasuNetFlow + "\"," +
                                "\"wasuLadongNetFlow\":\"" + wasuLadongNetFlow + "\"," +
                                "\"vooleNetFlow\":\"" + vooleNetFlow + "\"," +
                                "\"vooleLadongNetFlow\":\"" + vooleLadongNetFlow + "\"," +
                                "\"bestvNetFlow\":\"" + bestvNetFlow + "\"," +
                                "\"bestvLadongNetFlow\":\"" + bestvLadongNetFlow + "\"," +
                                "\"onlineUser\":\"" + onlineUser + "\"," +
                                "\"onlineUserNetFlow\":\"" + onlineUserNetFlow + "\"," +
                                "\"allLength\":\"" + allLength + "\"," +
                                "\"allCount\":\"" + allCount+ "\"," +
                                "\"mobileLength\":\"" + mobileLength+ "\"," +
                                "\"mobileCount\":\"" + mobileCount+ "\"," +
                                "\"padLength\":\"" + padLength+ "\"," +
                                "\"padCount\":\"" + padCount+ "\"," +
                                "\"phoneLength\":\"" + phoneLength+ "\"," +
                                "\"phoneCount\":\"" + phoneCount+ "\"," +
                                "\"liveLength\":\"" + liveLength+ "\"," +
                                "\"liveCount\":\"" + liveCount+ "\"," +
                                "\"livePadLength\":\"" + livePadLength + "\"," +
                                "\"livePadCount\":\"" + livePadCount + "\"," +
                                "\"livePhoneLength\":\"" + livePhoneLength + "\"," +
                                "\"livePhoneCount\":\"" + livePhoneCount + "\","+
                                "\"contentLength\":\"" + contentLength+"\","+
                                "\"contentCount\":\"" + contentCount + "\"," +
                                "\"contentPadLength\":\"" + contentPadLength + "\"," +
                                "\"contentPadCount\":\"" + contentPadCount + "\"," +
                                "\"contentPhoneLength\":\"" + contentPhoneLength + "\"," +
                                "\"contentPhoneCount\":\"" + contentPhoneCount + "\"," +
                                "\"wasuLength\":\"" + wasuLength + "\"," +
                                "\"wasuCount\":\"" + wasuCount + "\"," +
                                "\"bestvLength\":\"" + bestvLength + "\"," +
                                "\"bestvCount\":\"" + bestvCount + "\"," +
                                "\"vooleLength\":\"" + vooleLength + "\"," +
                                "\"vooleCount\":\"" + vooleCount + "\"," +
                                "\"elseCount\":\"" + elseCount + "\"," +
                                "\"elseLength\":\"" + elseLength + "\"," +
                                "},";
                    }
                    result = result.substring(0,result.length()-1);
                }
                result += "]}";
                directOut(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getContentByDate(){
        //excel导出
        String startTime = getRequestParam("startTime","");
        String endTime = getRequestParam("endTime","");
        Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
        Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
        List basicInfoList = new ArrayList();
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (session != null) {
            Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null) {
                String adminRealName = admin.getRealname();
                basicInfoList.add(adminRealName);
            }
        }
        basicInfoList.add(StringUtils.date2string(startDate,"yyyy-MM-dd"));
        basicInfoList.add(StringUtils.date2string(endDate,"yyyy-MM-dd"));
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
        String date = sDateFormat.format(new Date());
        basicInfoList.add(date);
        //热词搜素统计
       List<Object[]> searchHotLogList = visitLogLogicInterface.getSearchHotLogs(startDate,endDate);
        //有媒体播放统计
        List<Object[]> systemPlayedLogList = visitLogLogicInterface.getContentByDateAndType(startDate, endDate, "systemPlayedLog");
         //直播趋势统计
        List<Map<String,String>> countLogList =visitLogLogicInterface.getCountList(startDate,endDate);
        //并发查询
        List<Object[]> bingfaList = visitLogLogicInterface.getAllBingFaFromDailyStaticsLog(startDate, endDate);
        //流量查询
        List<Object[]> netFlowList = visitLogLogicInterface.getAllNetFlowFromDailyStaticsLog(startDate,endDate);
        //用户登陆情况查询
        List<Object[]> userLoginLogs = visitLogLogicInterface.getUserLoginLogs(startDate,endDate);
        //区域
        List<Object[]> areaLogList = visitLogLogicInterface.getAreaLogsFromAreaDemandLog(startDate, endDate);
        //SP
        List<Object[]> spLogList = visitLogLogicInterface.getSpLogsFromVisitLog(startDate, endDate);
        //频道点播量
        List<Object[]> channelLogList = visitLogLogicInterface.getChannelLogsFromChannelDemandLog(startDate,endDate,-1,"isTrue");
        //媒体点播量
        List<Object[]> contentLogList = visitLogLogicInterface.getContentByDateAndType(startDate, endDate, "contentLog");
        //媒体时长
        List<Object[]> contentByLengthLogList = visitLogLogicInterface.getContentByDateAndType(startDate, endDate, "contentByLengthLog");
        //媒体流量
        List<Object[]> contentByNetFlowList = visitLogLogicInterface.getContentByDateAndType(startDate, endDate, "contentByNetFlowLog");
        //直播点播量
        List<Object[]> contentLiveLogList = visitLogLogicInterface.getContentByDateAndType(startDate, endDate, "contentLiveLog");
        //拉动点播量
        List<Object[]> contentLadongLogList = visitLogLogicInterface.getContentByDateAndType(startDate,endDate,"contentLadongLog");
        //电影排行
        List<Object[]> contentFilmLogList = visitLogLogicInterface.getContentFilmAndTvLog(startDate,endDate,1,100);
        //电影排行
        List<Object[]> contentTvLogList = visitLogLogicInterface.getContentFilmAndTvLog(startDate,endDate,2,100);
        //写入excel表中。
        String result = visitLogLogicInterface.createExcelFile(basicInfoList,areaLogList,spLogList,contentLogList,contentLiveLogList,contentLadongLogList,bingfaList,netFlowList,channelLogList,contentByLengthLogList,contentByNetFlowList,userLoginLogs,contentFilmLogList,contentTvLogList,countLogList,systemPlayedLogList,searchHotLogList);
        if("success".equals(result)){
            return null;
        }
        return null;
    }


    public Map getObjMaps() {
        return objMaps;
    }

    public void setObjMaps(Map objMaps) {
        this.objMaps = objMaps;
    }

    public List getObjLists() {
        return objLists;
    }

    public void setObjLists(List objLists) {
        this.objLists = objLists;
    }

    public String getObjJson() {
        return objJson;
    }

    public void setObjJson(String objJson) {
        this.objJson = objJson;
    }


    private Date  dateStatics;

    public void setDateStatics(Date dateStatics) {
        this.dateStatics = dateStatics;
    }

    public void saveVisitLogOfOneDay() {
        try{
            log.debug("自动整理每天的区域点播量 AreaDemandLog run timer:"+ StringUtils.date2string(new Date()));
            AreaDemandLog ad = new AreaDemandLog();
//            Calendar cl = Calendar.getInstance();
//            cl.setTime(new Date());
//            cl.add(Calendar.DATE,-1);
//            Date dateStatics = cl.getTime();
            //写入数据库的时间
            String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
            AreaDemandLogLogicInterface areaDemandLogLogicInterface = (AreaDemandLogLogicInterface)SpringUtils.getBeanForApp("areaDemandLogLogicInterface");
            //1是地域，2是拉动，3是3G接入。
            Map<String,List> areaDemandMap = areaDemandLogLogicInterface.getAreaDemandFromVisitLog(dateStatics,1L);
            if(areaDemandMap.size()>0){
                List<Object[]> areaDemandList = areaDemandMap.get("allAreaDemand");
                List<Object[]> padAreaDemandList = areaDemandMap.get("padAreaDemand");
                List<Object[]> phoneAreaDemandList = areaDemandMap.get("phoneAreaDemand");
                if(null!=areaDemandList){
                    for(Object[] o :areaDemandList){
                        long areaId = Long.parseLong(o[0].toString());
                        long count = Long.parseLong(o[1].toString());
                        long length = Long.parseLong(o[2].toString());
                        long bytesSend = Long.parseLong(o[3].toString());
                        long mobileBytesSend = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,1,1);
                        long mobileCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,1,2);
                        long mobileLength = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,1,3);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setCount(count);
                        ad.setLength(length);
                        ad.setBytesSend(bytesSend);
                        ad.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        ad.setType(1);
                        if(areaId==311){ad.setGrade(1);}
                        if(areaId==312){ad.setGrade(2);}
                        if(areaId==315){ad.setGrade(3);}
                        if(areaId==355){ad.setGrade(4);}
                        if(areaId==317){ad.setGrade(5);}
                        if(areaId==316){ad.setGrade(6);}
                        if(areaId==310){ad.setGrade(7);}
                        if(areaId==319){ad.setGrade(8);}
                        if(areaId==313){ad.setGrade(9);}
                        if(areaId==314){ad.setGrade(10);}
                        if(areaId==318){ad.setGrade(11);}
                        if(null!=padAreaDemandList){
                            for(Object[] padO:padAreaDemandList){
                                if(Long.parseLong(padO[0].toString())==areaId){
                                    ad.setPadCount(Long.parseLong(padO[1].toString()));
                                    ad.setPadLength(Long.parseLong(padO[2].toString()));
                                    ad.setBytesSendPad(Long.parseLong(padO[3].toString()));
                                    break;
                                }else{
                                    ad.setPadCount(0);
                                    ad.setPadLength(0);
                                    ad.setBytesSendPad(0);
                                }
                            }
                        }
                        if(null!=phoneAreaDemandList){
                            for(Object[] phoneO:phoneAreaDemandList){
                                if(Long.parseLong(phoneO[0].toString())==areaId){
                                    ad.setPhoneCount(Long.parseLong(phoneO[1].toString()));
                                    ad.setPhoneLength(Long.parseLong(phoneO[2].toString()));
                                    ad.setBytesSendPhone(Long.parseLong(phoneO[3].toString()));
                                    break;
                                }else{
                                    ad.setPhoneCount(0);
                                    ad.setPhoneLength(0);
                                    ad.setBytesSendPhone(0);
                                }
                            }
                        }
                        areaDemandLogLogicInterface.save(ad);
                    }
                }
            }

            Map<String,List> areaDemandLadongMap = areaDemandLogLogicInterface.getAreaDemandFromVisitLog(dateStatics,2L);
            if(areaDemandLadongMap.size()>0){
                List<Object[]> areaDemandLadongList = areaDemandLadongMap.get("allAreaDemand");
                List<Object[]> padAreaDemandLadongList = areaDemandLadongMap.get("padAreaDemand");
                List<Object[]> phoneAreaDemandLadongList = areaDemandLadongMap.get("phoneAreaDemand");
                if(null!= areaDemandLadongList){
                    for(Object[] o1: areaDemandLadongList){
                        long areaId = Long.parseLong(o1[0].toString());
                        long count = Long.parseLong(o1[1].toString());
                        long length = Long.parseLong(o1[2].toString());
                        long bytesSend = Long.parseLong(o1[3].toString());
                        long mobileBytesSend = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,2,1);
                        long mobileCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,2,2);
                        long mobileLength = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,2,3);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setCount(count);
                        ad.setLength(length);
                        ad.setBytesSend(bytesSend);
                        ad.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        ad.setType(2);
                        if(areaId==311){ad.setGrade(101);}
                        if(areaId==312){ad.setGrade(102);}
                        if(areaId==315){ad.setGrade(103);}
                        if(areaId==355){ad.setGrade(104);}
                        if(areaId==317){ad.setGrade(105);}
                        if(areaId==316){ad.setGrade(106);}
                        if(areaId==310){ad.setGrade(107);}
                        if(areaId==319){ad.setGrade(108);}
                        if(areaId==313){ad.setGrade(109);}
                        if(areaId==314){ad.setGrade(110);}
                        if(areaId==318){ad.setGrade(111);}
                        if(null!=padAreaDemandLadongList){
                            for(Object[] padO:padAreaDemandLadongList){
                                if(Long.parseLong(padO[0].toString())==areaId){
                                    ad.setPadCount(Long.parseLong(padO[1].toString()));
                                    ad.setPadLength(Long.parseLong(padO[2].toString()));
                                    ad.setBytesSendPad(Long.parseLong(padO[3].toString()));
                                    break;
                                }else{
                                    ad.setPadCount(0);
                                    ad.setPadLength(0);
                                    ad.setBytesSendPad(0);
                                }
                            }
                        }
                        if(null!=phoneAreaDemandLadongList){
                            for(Object[] phoneO:phoneAreaDemandLadongList){
                                if(Long.parseLong(phoneO[0].toString())==areaId){
                                    ad.setPhoneCount(Long.parseLong(phoneO[1].toString()));
                                    ad.setPhoneLength(Long.parseLong(phoneO[2].toString()));
                                    ad.setBytesSendPhone(Long.parseLong(phoneO[3].toString()));
                                    break;
                                }else{
                                    ad.setPhoneCount(0);
                                    ad.setPhoneLength(0);
                                    ad.setBytesSendPhone(0);
                                }
                            }
                        }
                        areaDemandLogLogicInterface.save(ad);
                    }
                }
            }

            Map<String,List> areaDemand3GMap = areaDemandLogLogicInterface.getAreaDemandFromVisitLog(dateStatics,3L);
            if(areaDemand3GMap.size()>0){
                List<Object[]> areaDemand3GList = areaDemand3GMap.get("allAreaDemand");
                List<Object[]> padAreaDemand3GList = areaDemand3GMap.get("padAreaDemand");
                List<Object[]> phoneAreaDemand3GList = areaDemand3GMap.get("phoneAreaDemand");
                if(null!= areaDemand3GList){
                    for(Object[] o2: areaDemand3GList){
                        long areaId = Long.parseLong(o2[0].toString());
                        long count = Long.parseLong(o2[1].toString());
                        long length = Long.parseLong(o2[2].toString());
                        long bytesSend = Long.parseLong(o2[3].toString());
                        long mobileBytesSend = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,3,1);
                        long mobileCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,3,2);
                        long mobileLength = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,3,3);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setCount(count);
                        ad.setLength(length);
                        ad.setBytesSend(bytesSend);
                        ad.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        ad.setType(3);
                        if(areaId==-1){
                            ad.setGrade(1000);
                        }
                        if(null!=padAreaDemand3GList){
                            for(Object[] padO:padAreaDemand3GList){
                                if(Long.parseLong(padO[0].toString())==areaId){
                                    ad.setPadCount(Long.parseLong(padO[1].toString()));
                                    ad.setPadLength(Long.parseLong(padO[2].toString()));
                                    ad.setBytesSendPad(Long.parseLong(padO[3].toString()));
                                    break;
                                }else{
                                    ad.setPadCount(0);
                                    ad.setPadLength(0);
                                    ad.setBytesSendPad(0);
                                }
                            }
                        }
                        if(null!=phoneAreaDemand3GList){
                            for(Object[] phoneO:phoneAreaDemand3GList){
                                if(Long.parseLong(phoneO[0].toString())==areaId){
                                    ad.setPhoneCount(Long.parseLong(phoneO[1].toString()));
                                    ad.setPhoneLength(Long.parseLong(phoneO[2].toString()));
                                    ad.setBytesSendPhone(Long.parseLong(phoneO[3].toString()));
                                    break;
                                }else{
                                    ad.setPhoneCount(0);
                                    ad.setPhoneLength(0);
                                    ad.setBytesSendPhone(0);
                                }
                            }
                        }
                        areaDemandLogLogicInterface.save(ad);
                    }
                }
            }

//                logger.debug("统计"+startTime+"数据完成");
            log.debug("数据存储完成");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void  DailyStaticsOfOneDay(){
        try{
            log.debug("自动整理每天的流量和并发日志 DailyStatics run timer:"+StringUtils.date2string(new Date()));
            DailyStaticsLog ds = new DailyStaticsLog();
         /*   Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
            Date dateStatics = cl.getTime();*/
            DailyStaticsLogLogicInterface dailyStaticsLogLogicInterface = (DailyStaticsLogLogicInterface) SpringUtils.getBeanForApp("dailyStaticsLogLogicInterface");
            List cspIdList = new ArrayList();
            cspIdList.add(16241843);
            cspIdList.add(15905690);
            cspIdList.add(16241840);
            Map<String,String> map=dailyStaticsLogLogicInterface.getNetFlowLogsFromVisitLog(dateStatics,15884423L,cspIdList);
            if(map.size()>0){
                ds.setDateStatics(StringUtils.string2date(map.get("dateStatics"),"yyyy-MM-dd"));
                Long allNetFlow = StringUtils.string2long(map.get("allNetFlow"), -1);
                Long mobileFlow = StringUtils.string2long(map.get("mobileNetFlow"),-1);

                ds.setAllNetFlow(StringUtils.string2long(map.get("allNetFlow"), -1));
                ds.setAllNetFlowPad(StringUtils.string2long(map.get("allNetFlowPad"),-1));
                ds.setAllNetFlowPhone(StringUtils.string2long(map.get("allNetFlowPhone"),-1));
                ds.setMobileNetFlow(StringUtils.string2long(map.get("mobileNetFlow"),-1));
                ds.setElseNetFlow(allNetFlow-mobileFlow);
                ds.setAllLiveNetFlow(StringUtils.string2long(map.get("allNetFlowLive"), -1));
                ds.setAllLiveNetFlowPad(StringUtils.string2long(map.get("allNetFlowLivePad"), -1));
                ds.setAllLiveNetFlowPhone(StringUtils.string2long(map.get("allNetFlowLivePhone"), -1));
                ds.setAllContentNetFlow(StringUtils.string2long(map.get("allNetFlowContent"), -1));
                ds.setAllContentNetFlowPad(StringUtils.string2long(map.get("allNetFlowContentPad"), -1));
                ds.setAllContentNetFlowPhone(StringUtils.string2long(map.get("allNetFlowContentPhone"), -1));
                ds.setWasuNetFlow(StringUtils.string2long(map.get("wasuNetFlow"), -1));
                ds.setVooleNetFlow(StringUtils.string2long(map.get("vooleNetFlow"), -1));
                ds.setBestvNetFlow(StringUtils.string2long(map.get("bestvNetFlow"), -1));
                ds.setOnlineUser(StringUtils.string2long(map.get("onlineUser"), -1));
                ds.setWasuLadongNetFlow(StringUtils.string2long(map.get("wasuLadongNetFlow"),-1));
                ds.setVooleLadongNetFlow(StringUtils.string2long(map.get("vooleLadongNetFlow"),-1));
                ds.setBestvLadongNetFlow(StringUtils.string2long(map.get("bestvLadongNetFlow"),-1));
                ds.setOnlineUserNetFlow(StringUtils.string2long(map.get("onlineUserNetFlow"), -1));

                Long mobileLength = StringUtils.string2long(map.get("mobileLength"),-1);
                Long mobileCount = StringUtils.string2long(map.get("mobileCount"),-1);
                Long allLength = StringUtils.string2long(map.get("allLength"),-1);
                Long allCount = StringUtils.string2long(map.get("allCount"),-1);
                ds.setAllCount(StringUtils.string2long(map.get("allCount"),-1));
                ds.setAllLength(StringUtils.string2long(map.get("allLength"),-1));
                ds.setMobileCount(mobileCount);
                ds.setMobileLength(mobileLength);
                ds.setElseCount(allCount - mobileCount);
                ds.setElseLength(allLength - mobileLength);
                ds.setPadCount(StringUtils.string2long(map.get("padCount"),-1));
                ds.setPadLength(StringUtils.string2long(map.get("padLength"),-1));
                ds.setPhoneCount(StringUtils.string2long(map.get("phoneCount"),-1));
                ds.setPhoneLength(StringUtils.string2long(map.get("phoneLength"),-1));
                ds.setLiveCount(StringUtils.string2long(map.get("liveCount"),-1));
                ds.setLiveLength(StringUtils.string2long(map.get("liveLength"),-1));
                ds.setLivePadCount(StringUtils.string2long(map.get("livePadCount"),-1));
                ds.setLivePadLength(StringUtils.string2long(map.get("livePadLength"),-1));
                ds.setLivePhoneCount(StringUtils.string2long(map.get("livePhoneCount"),-1));
                ds.setLivePhoneLength(StringUtils.string2long(map.get("livePhoneLength"),-1));
                ds.setContentCount(StringUtils.string2long(map.get("contentCount"),-1));
                ds.setContentLength(StringUtils.string2long(map.get("contentLength"),-1));
                ds.setContentPadCount(StringUtils.string2long(map.get("contentPadCount"),-1));
                ds.setContentPadLength(StringUtils.string2long(map.get("contentPadLength"),-1));
                ds.setContentPhoneCount(StringUtils.string2long(map.get("contentPhoneCount"),-1));
                ds.setContentPhoneLength(StringUtils.string2long(map.get("contentPhoneLength"),-1));
                ds.setWasuLength(StringUtils.string2long(map.get("wasuLength"),-1));
                ds.setWasuCount(StringUtils.string2long(map.get("wasuCount"),-1));
                ds.setVooleCount(StringUtils.string2long(map.get("vooleCount"),-1));
                ds.setVooleLength(StringUtils.string2long(map.get("vooleLength"),-1));
                ds.setBestvCount(StringUtils.string2long(map.get("bestvCount"),-1));
                ds.setBestvLength(StringUtils.string2long(map.get("bestvLength"),-1));
            }
            log.debug("流量整理完成，整理并发数据");
            List<Object[]> list = dailyStaticsLogLogicInterface.getBingFaLogsFromVisitLog(dateStatics);
            String result = "";
            String maxResult = "";
            if(list.size()>0){
                Object[] object = list.get(96);
                String maxTime  = object[0].toString();
                Long maxValue =(Long)object[1];
                maxResult =maxTime+"="+maxValue;
                list.remove(96);
                for(Object[] object1:list){
                    String time = object1[0].toString();
                    Long value = (Long) object1[1];
                    result += time+"="+value+",";
                }
                result = result.substring(0,result.length()-1);
            }
            log.debug("并发数据整理完成");
            ds.setQuarterBingFa(result);
            ds.setMaxBingFa(maxResult);
            Calendar cl1 = Calendar.getInstance();
            cl1.setTime(new Date());
            Date createTime = cl1.getTime();
            ds.setCreateTime(createTime);
            log.debug("开始存储数据");
            dailyStaticsLogLogicInterface.save(ds);
            log.debug("存储数据完成");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void contentDemandOfOneDay() {
        try{
            log.debug("自动整理每天的资源点播量 ContentDemandLog run timer:"+ StringUtils.date2string(new Date()));
            ContentDemandLog cd = new ContentDemandLog();
         /*   Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
            Date dateStatics=cl.getTime();
            //写入数据库的时间*/
            String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
            ContentDemandLogLogicInterface contentDemandLogLogicInterface = (ContentDemandLogLogicInterface)SpringUtils.getBeanForApp("contentDemandLogLogicInterface");
            Map<String,List> contentMap = contentDemandLogLogicInterface.getContentDemandFromVisitLog(dateStatics,1,15884423);
            if(contentMap.size()>0){
                List<Object[]> contentDemandList = contentMap.get("allContentDemand");
                List<Object[]> padContentDemandList = contentMap.get("padContentDemand");
                List<Object[]> phoneContentDemandList = contentMap.get("phoneContentDemand");
                if(contentDemandList.size()>0){
                    for(Object[] o : contentDemandList){
                        long spId = Long.parseLong(o[0].toString());
                        long contentId = Long.parseLong(o[1].toString());
                        long count = Long.parseLong(o[2].toString());
                        long length = Long.parseLong(o[3].toString());
                        long channelId = Long.parseLong(o[4].toString());
                        long bytesSend = Long.parseLong(o[5].toString());
                        cd.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        cd.setSpId(spId);
                        cd.setContentId(contentId);
                        cd.setCount(count);
                        cd.setLength(length);
                        cd.setChannelId(channelId);
                        cd.setBytesSend(bytesSend);
                        //1是点播，2是直播
                        cd.setType(1);
                        if(padContentDemandList.size()>0){
                            for(Object[] padO:padContentDemandList){
                                if(Long.parseLong(padO[1].toString())==contentId&&Long.parseLong(padO[4].toString())==channelId){
                                    cd.setPadCount(Long.parseLong(padO[2].toString()));
                                    cd.setPadLength(Long.parseLong(padO[3].toString()));
                                    cd.setBytesSendPad(Long.parseLong(padO[5].toString()));
                                    break;
                                }else{
                                    cd.setPadCount(0);
                                    cd.setPadLength(0);
                                    cd.setBytesSendPad(0);
                                }
                            }
                        }
                        if(phoneContentDemandList.size()>0){
                            for(Object[] phoneO:phoneContentDemandList){
                                if(Long.parseLong(phoneO[1].toString())==contentId&&Long.parseLong(phoneO[4].toString())==channelId){
                                    cd.setPhoneCount(Long.parseLong(phoneO[2].toString()));
                                    cd.setPhoneLength(Long.parseLong(phoneO[3].toString()));
                                    cd.setBytesSendPhone(Long.parseLong(phoneO[5].toString()));
                                    break;
                                }else{
                                    cd.setPhoneCount(0);
                                    cd.setPhoneLength(0);
                                    cd.setBytesSendPhone(0);
                                }
                            }
                        }
                        contentDemandLogLogicInterface.save(cd);
                    }
                }
            }

            Map<String,List> contentLiveMap = contentDemandLogLogicInterface.getContentDemandFromVisitLog(dateStatics,2,15884423);
            if(contentLiveMap.size()>0){
                List<Object[]> contentDemandList = contentLiveMap.get("allContentDemand");
                List<Object[]> padContentDemandList = contentLiveMap.get("padContentDemand");
                List<Object[]> phoneContentDemandList = contentLiveMap.get("phoneContentDemand");
                if(contentDemandList.size()>0){
                    for(Object[] o : contentDemandList){
                        long spId = Long.parseLong(o[0].toString());
                        long contentId = Long.parseLong(o[1].toString());
                        long count = Long.parseLong(o[2].toString());
                        long length = Long.parseLong(o[3].toString());
                        long channelId = Long.parseLong(o[4].toString());
                        long bytesSend = Long.parseLong(o[5].toString());
                        cd.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        cd.setSpId(spId);
                        cd.setContentId(contentId);
                        cd.setCount(count);
                        cd.setLength(length);
                        cd.setChannelId(channelId);
                        cd.setBytesSend(bytesSend);
                        //1是点播，2是直播
                        cd.setType(2);
                        if(padContentDemandList.size()>0){
                            for(Object[] padO:padContentDemandList){
                                if(Long.parseLong(padO[1].toString())==contentId&&Long.parseLong(padO[4].toString())==channelId){
                                    cd.setPadCount(Long.parseLong(padO[2].toString()));
                                    cd.setPadLength(Long.parseLong(padO[3].toString()));
                                    cd.setBytesSendPad(Long.parseLong(padO[5].toString()));
                                    break;
                                }else{
                                    cd.setPadCount(0);
                                    cd.setPadLength(0);
                                    cd.setBytesSendPad(0);
                                }
                            }
                        }
                        if(phoneContentDemandList.size()>0){
                            for(Object[] phoneO:phoneContentDemandList){
                                if(Long.parseLong(phoneO[1].toString())==contentId&&Long.parseLong(phoneO[4].toString())==channelId){
                                    cd.setPhoneCount(Long.parseLong(phoneO[2].toString()));
                                    cd.setPhoneLength(Long.parseLong(phoneO[3].toString()));
                                    cd.setBytesSendPhone(Long.parseLong(phoneO[5].toString()));
                                    break;
                                }else{
                                    cd.setPhoneCount(0);
                                    cd.setPhoneLength(0);
                                    cd.setBytesSendPhone(0);
                                }
                            }
                        }
                        contentDemandLogLogicInterface.save(cd);
                    }
                }
            }
            Map<String,List> contentLadongMap = contentDemandLogLogicInterface.getContentDemandFromVisitLog(dateStatics,3,15884423);
            if(contentLadongMap.size()>0){
                List<Object[]> contentDemandList = contentLadongMap.get("allContentDemand");
                List<Object[]> padContentDemandList = contentLadongMap.get("padContentDemand");
                List<Object[]> phoneContentDemandList = contentLadongMap.get("phoneContentDemand");
                if(contentDemandList.size()>0){
                    for(Object[] o : contentDemandList){
                        long spId = Long.parseLong(o[0].toString());
                        long contentId = Long.parseLong(o[1].toString());
                        long count = Long.parseLong(o[2].toString());
                        long length = Long.parseLong(o[3].toString());
                        long channelId = Long.parseLong(o[4].toString());
                        long bytesSend = Long.parseLong(o[5].toString());
                        cd.setDateStatics(StringUtils.string2date(startTime,"yyyy-MM-dd"));
                        cd.setSpId(spId);
                        cd.setContentId(contentId);
                        cd.setCount(count);
                        cd.setLength(length);
                        cd.setChannelId(channelId);
                        cd.setBytesSend(bytesSend);
                        //1是点播，2是直播,3是拉动
                        cd.setType(3);
                        if(padContentDemandList.size()>0){
                            for(Object[] padO:padContentDemandList){
                                if(Long.parseLong(padO[1].toString())==contentId&&Long.parseLong(padO[4].toString())==channelId){
                                    cd.setPadCount(Long.parseLong(padO[2].toString()));
                                    cd.setPadLength(Long.parseLong(padO[3].toString()));
                                    cd.setBytesSend(Long.parseLong(padO[5].toString()));
                                    break;
                                }else{
                                    cd.setPadCount(0);
                                    cd.setPadLength(0);
                                    cd.setBytesSendPad(0);
                                }
                            }
                        }
                        if(phoneContentDemandList.size()>0){
                            for(Object[] phoneO:phoneContentDemandList){
                                if(Long.parseLong(phoneO[1].toString())==contentId&&Long.parseLong(phoneO[4].toString())==channelId){
                                    cd.setPhoneCount(Long.parseLong(phoneO[2].toString()));
                                    cd.setPhoneLength(Long.parseLong(phoneO[3].toString()));
                                    cd.setBytesSendPhone(Long.parseLong(phoneO[5].toString()));
                                    break;
                                }else{
                                    cd.setPhoneCount(0);
                                    cd.setPhoneLength(0);
                                    cd.setBytesSendPhone(0);
                                }
                            }
                        }
                        contentDemandLogLogicInterface.save(cd);
                    }
                }
            }
            log.debug("数据写入完毕");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String startDate;
    public String endDate;

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void insertLogDate() {
        ContentDemandLogLogicInterface contentDemandLogLogicInterface = (ContentDemandLogLogicInterface)SpringUtils.getBeanForApp("contentDemandLogLogicInterface");
        List<ContentDemandLog> contentDemandLogList = visitLogLogicInterface.getContentDemandLogByDate(startDate,endDate);
        if(contentDemandLogList != null && contentDemandLogList.size() > 0  ) {
            for(ContentDemandLog cdl : contentDemandLogList) {
                ContentDemandLog contentDemandLog = new ContentDemandLog();
                contentDemandLog.setType(cdl.getType());
                contentDemandLog.setBytesSend(cdl.getBytesSend());
                contentDemandLog.setBytesSendPad(cdl.getBytesSendPad());
                contentDemandLog.setBytesSendPhone(cdl.getBytesSendPhone());
                contentDemandLog.setChannelId(cdl.getChannelId());
                contentDemandLog.setContentId(cdl.getContentId());
                contentDemandLog.setCount(cdl.getCount());
                contentDemandLog.setCreateTime(cdl.getCreateTime());
                contentDemandLog.setDateStatics(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));
                contentDemandLog.setLength(cdl.getLength());
                contentDemandLog.setPadCount(cdl.getPadCount());
                contentDemandLog.setPadLength(cdl.getPadLength());
                contentDemandLog.setPhoneCount(cdl.getPhoneCount());
                contentDemandLog.setPhoneLength(cdl.getPhoneLength());
                contentDemandLog.setSpId(cdl.getSpId());
                contentDemandLogLogicInterface.save(contentDemandLog);
            }
        }
        System.out.println("======================>完成1");

        DailyStaticsLogLogicInterface dailyStaticsLogLogicInterface = (DailyStaticsLogLogicInterface)SpringUtils.getBeanForApp("dailyStaticsLogLogicInterface");
        List<DailyStaticsLog> dailyStaticsLogList = visitLogLogicInterface.getDailyStaticsLogByDate(startDate,endDate);
        if(contentDemandLogList != null && contentDemandLogList.size() > 0  ) {
            for(DailyStaticsLog dsl : dailyStaticsLogList) {
                DailyStaticsLog dailyStaticsLog = new DailyStaticsLog();
                dailyStaticsLog.setAllContentNetFlow(dsl.getAllContentNetFlow());
                dailyStaticsLog.setAllContentNetFlowPad(dsl.getAllContentNetFlowPad());
                dailyStaticsLog.setAllContentNetFlowPhone(dsl.getAllContentNetFlowPhone());
                dailyStaticsLog.setAllLiveNetFlow(dsl.getAllLiveNetFlow());
                dailyStaticsLog.setAllLiveNetFlowPad(dsl.getAllLiveNetFlowPad());
                dailyStaticsLog.setAllLiveNetFlowPhone(dsl.getAllLiveNetFlowPhone());
                dailyStaticsLog.setAllNetFlow(dsl.getAllNetFlow());
                dailyStaticsLog.setAllNetFlowPad(dsl.getAllNetFlowPad());
                dailyStaticsLog.setAllNetFlowPhone(dsl.getAllNetFlowPhone());
                dailyStaticsLog.setBestvLadongNetFlow(dsl.getBestvLadongNetFlow());
                dailyStaticsLog.setBestvNetFlow(dsl.getBestvNetFlow());
                dailyStaticsLog.setCreateTime(dsl.getCreateTime());
                dailyStaticsLog.setDateStatics(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));
                dailyStaticsLog.setElseNetFlow(dsl.getElseNetFlow());
                dailyStaticsLog.setMaxBingFa(dsl.getMaxBingFa());
                dailyStaticsLog.setMobileNetFlow(dsl.getMobileNetFlow());
                dailyStaticsLog.setOnlineUser(dsl.getOnlineUser());
                dailyStaticsLog.setOnlineUserNetFlow(dsl.getOnlineUserNetFlow());
                dailyStaticsLog.setQuarterBingFa(dsl.getQuarterBingFa());
                dailyStaticsLog.setVooleLadongNetFlow(dsl.getVooleLadongNetFlow());
                dailyStaticsLog.setVooleNetFlow(dsl.getVooleNetFlow());
                dailyStaticsLog.setWasuLadongNetFlow(dsl.getWasuLadongNetFlow());
                dailyStaticsLog.setWasuNetFlow(dsl.getWasuNetFlow());
                dailyStaticsLogLogicInterface.save(dailyStaticsLog);
            }
        }
        System.out.println("======================>完成2");

        AreaDemandLogLogicInterface areaDemandLogLogicInterface = (AreaDemandLogLogicInterface)SpringUtils.getBeanForApp("areaDemandLogLogicInterface");
        List<AreaDemandLog>  areaDemandLogList = visitLogLogicInterface.getAreaDemandLogByDate(startDate,endDate);
        if(contentDemandLogList != null && contentDemandLogList.size() > 0  ) {
            for(AreaDemandLog adl : areaDemandLogList) {
                AreaDemandLog areaDemandLog = new AreaDemandLog();
                areaDemandLog.setElseCount(adl.getElseCount());
                areaDemandLog.setElseLength(adl.getElseLength());
                areaDemandLog.setMobileLength(adl.getMobileLength());
                areaDemandLog.setUserOnLineCount(adl.getUserOnLineCount());
                areaDemandLog.setAreaId(adl.getAreaId());
                areaDemandLog.setBytesSend(adl.getBytesSend());
                areaDemandLog.setBytesSendPad(adl.getBytesSendPad());
                areaDemandLog.setBytesSendPhone(adl.getBytesSendPhone());
                areaDemandLog.setCount(adl.getCount());
                areaDemandLog.setCreateTime(adl.getCreateTime());
                areaDemandLog.setDateStatics(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));
                areaDemandLog.setElseBytesSend(adl.getElseBytesSend());
                areaDemandLog.setGrade(adl.getGrade());
                areaDemandLog.setLength(adl.getLength());
                areaDemandLog.setMobileCount(adl.getMobileCount());
                areaDemandLog.setMobileBytesSend(adl.getMobileBytesSend());
                areaDemandLog.setPadCount(adl.getPadCount());
                areaDemandLog.setPadLength(adl.getPadLength());
                areaDemandLog.setPhoneLength(adl.getPhoneLength());
                areaDemandLog.setPhoneCount(adl.getPhoneCount());
                areaDemandLog.setType(adl.getType());
                areaDemandLogLogicInterface.save(areaDemandLog);
            }
        }
        System.out.println("======================>完成3");


        ChannelDemandLogLogicInterface channelDemandLogLogicInterface = (ChannelDemandLogLogicInterface)SpringUtils.getBeanForApp("channelDemandLogLogicInterface");
        List<ChannelDemandLog>  channelDemandLogList = visitLogLogicInterface.getChannelDemandLogByDate(startDate,endDate);
        if(channelDemandLogList != null && channelDemandLogList.size() > 0  ) {
            for(ChannelDemandLog cdl : channelDemandLogList) {
                ChannelDemandLog channelDemandLog = new ChannelDemandLog();
                channelDemandLog.setBytesSend(cdl.getBytesSend());
                channelDemandLog.setChannelId(cdl.getChannelId());
                channelDemandLog.setCount(cdl.getCount());
                channelDemandLog.setCreateTime(cdl.getCreateTime());
                channelDemandLog.setLength(cdl.getLength());
                channelDemandLog.setType(cdl.getType());
                channelDemandLog.setDateStatics(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));
                channelDemandLogLogicInterface.save(channelDemandLog);
            }
        }
        System.out.println("======================>完成4");


        UserLoginLogicInterface userLoginLogicInterface = (UserLoginLogicInterface)SpringUtils.getBeanForApp("userLoginLogicInterface");
        List  userLoginList = visitLogLogicInterface.getUserLoginByDate(startDate,endDate);
        if(userLoginList != null && userLoginList.size() > 0  ) {
            for(Object userL : userLoginList) {
                Object[] o = (Object[])userL;
                UserLogin userLogin = new UserLogin();
                if(o[2] != null) {
                    userLogin.setAddr(o[2].toString());
                }
                if(o[7] != null) {
                    userLogin.setAreaId(Long.valueOf(o[7].toString()));
                }
                if(o[6] != null) {
                    userLogin.setDesp(o[6].toString());
                }
                if(o[1] != null) {
                    userLogin.setLogin(o[1].toString());
                }
                if(o[4] != null) {
                    userLogin.setLoginStatus(Long.valueOf(o[4].toString()));
                }
                if(o[3] != null) {
                    userLogin.setTel(o[3].toString());
                }
                userLogin.setLoginTime(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));

                userLoginLogicInterface.save(userLogin);
            }
        }
        System.out.println("======================>完成5");


        List<VisitLog>  visitLogList = visitLogLogicInterface.getVisitLogByDate(startDate,endDate);
        if(visitLogList != null && visitLogList.size() > 0  ) {
            for(VisitLog vl : visitLogList) {
                VisitLog visitLog = new VisitLog();
                visitLog.setAreaId(vl.getAreaId());
                visitLog.setAvgBandwidth(vl.getAvgBandwidth());
                visitLog.setBytesSend(vl.getBytesSend());
                visitLog.setChannelId(vl.getChannelId());
                visitLog.setContentId(vl.getContentId());
                visitLog.setContentPropertyId(vl.getContentPropertyId());
                visitLog.setCpId(vl.getCpId());
                visitLog.setEndTime(vl.getEndTime());
                visitLog.setStartTime(StringUtils.string2date("2014-08-17", "yyyy-MM-dd"));
                visitLog.setFree(vl.getFree());
                visitLog.setIsFree(vl.getIsFree());
                visitLog.setLength(vl.getLength());
                visitLog.setPlayerVersion(vl.getPlayerVersion());
                visitLog.setsIp(vl.getsIp());
                visitLog.setStatus(vl.getStatus());
                visitLog.setSpId(vl.getSpId());
                visitLog.setUrl(vl.getUrl());
                visitLog.setUserAgent(vl.getUserAgent());
                visitLog.setUserId(vl.getUserId());
                visitLogLogicInterface.save(visitLog);
            }
        }
        System.out.println("======================>完成6");
    }

    public String getDemandCountLogs() {
        log.debug("in demandCountLogs");
        try{
            String startTime = getRequestParam("startDate","");
            String endTime = getRequestParam("endDate","");
            String excel = getRequestParam("excel","");
            Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
            Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
            if("".equals(startDate)){
                return null;
            }
            JsonUtils jsonUtils = new JsonUtils();
            List<Object[]> demandCountLogslist = visitLogLogicInterface.getDemandCountLogs(startDate, endDate);
            List<Map<String,String>> countList =visitLogLogicInterface.getCountList(startDate,endDate);
            if(("isTrue").equals(excel)){
                String result = visitLogLogicInterface.createExcelFile1(startDate,endDate,0,countList,"demandCountLog");
                if("success".equals(result)){
                    return null;
                }
            }else{
                String result = "{ \"totalCount\":\"" + demandCountLogslist.size() + "\",\"success\":\"true\",\"objs\":";

                    result +=  jsonUtils.getJsonArray(countList);
                    result = result.substring(0,result.length()-1);

                result += "]}";
                directOut(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String systemPlayedLogs() {
        try {
            String sqlTable = "select "+
                    "vl.spId,vl.contentId,sum(vl.count),sum(vl.padCount),sum(vl.phoneCount) ,sum(vl.length),sum(vl.padLength),sum(vl.phoneLength),sum(vl.bytesSend),sum(vl.bytesSendPad),sum(bytesSendPhone)" +
                    " from "+
                    "com.fortune.rms.business.log.model.ContentDemandLog vl ";
            int type = getRequestIntParam("type",-1);
            String excel = getRequestParam("excel","");
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String spId = getRequestParam("vl_spId", "");
            if (!"".equals(spId)) {
                searchCondition.appendAndSqlCondition(
                        " vl.spId=? ",
                        new Long(spId),
                        new LongType());
            }

            String contentName = getRequestParam("contentName", "");
            if (!"".equals(contentName)) {
                searchCondition.appendAndSqlCondition(
                        " vl.contentId in (select c.id from Content c where c.name like ? ) ",
                        "%" + contentName + "%",
                        new StringType());
            }
            String startTime = getRequestParam("startDate", "");
            if (!"".equals(startTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics>=? ",
                        new Timestamp(StringUtils.string2date(startTime,"yyyy-MM-dd").getTime()),
                        new DateType());
            }

            String endTime = getRequestParam("endDate", "");
            if (!"".equals(endTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics<=? ",
                        new Timestamp(StringUtils.string2date(endTime).getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();
            sql += " group by vl.spId,vl.contentId ";

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit",99999);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by  sum(vl.count) " + orderDir;
            }
            if(("isTrue").equals(excel)){
                sql += " order by  sum(vl.count) DESC" ;
            }

            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            List list2 = list1;
            if("isTrue".equals(excel)){
                String result = "";
                Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
                Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
                if(1==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"systemPlayedLog");
                }
                if("success".equals(result)){
                    return  null;
                }
            }else{
                SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
                searchResult.setRows(list1);
                searchResult.setRowCount(list1.size());
                String output = searchObjectsJbon(new String[]{"vl_spId", "vl_contentId", "vl_count","vl_padCount","vl_phoneCount", "vl_length","vl_padLength","vl_phoneLength","vl_bytesSend","vl_bytesSendPad","vl_bytesSendPhone"}, searchResult);
                directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String userHotSearch() {
        try {
            long type = getRequestIntParam("type",0);
            String sqlTable="";
            if(1==type){
                sqlTable = "select " +
                        "vl.id,vl.content,vl.createTime,vl.updateCount,vl.searchCountStatus" +
                        " from " +
                        " com.fortune.rms.business.publish.model.UserHotSearch vl "  +
                        " where vl.searchCountStatus = 1"  ;
            }
            if(2==type){
                sqlTable = "select " +
                        "vl.id,vl.adminId,vl.content,vl.createTime,vl.searchCount,vl.searchWeekCount,vl.searchMonthCount,vl.searchCountStatus" +
                        " from " +
                        "com.fortune.rms.business.publish.model.UserHotSearch vl "  ;
            }
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setSqlStr(sqlTable);

            String createTime = getRequestParam("createTime", "");
            if (!"".equals(createTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.createTime = ? ",
                        new Timestamp(StringUtils.string2date(createTime + " 00:00:00").getTime()),
                        new DateType());
            }

            String contentName = getRequestParam("content", "");
            if (!"".equals(contentName)) {
                searchCondition.appendAndSqlCondition(
                        " vl.content like ? ",
                        "%" + contentName + "%",
                        new StringType());
            }
            String startTime = getRequestParam("startDate", "");
            if (!"".equals(startTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics>=? ",
                        new Timestamp(StringUtils.string2date(startTime,"yyyy-MM-dd").getTime()),
                        new DateType());
            }

            String endTime = getRequestParam("endDate", "");
            if (!"".equals(endTime)) {
                searchCondition.appendAndSqlCondition(
                        " vl.dateStatics<=? ",
                        new Timestamp(StringUtils.string2date(endTime).getTime()),
                        new DateType());
            }

            String sql = searchCondition.getSqlStr();

            Object[] params = searchCondition.getObjectArrayParamValues();
            Type[] paramTypes = searchCondition.getTypeArray();

            int startNo = getRequestIntParam("start", 0);
            int pageSize = getRequestIntParam("limit", 10);
            String orderBy = getRequestParam("sort", "");
            String orderDir = getRequestParam("dir", "");
            String excel = getRequestParam("excel","");
            if (!"".equals(orderBy) && !"".equals(orderDir)) {
                orderBy = orderBy.replace('_', '.');
                sql += " order by " + orderBy + " " + orderDir;
            }
            if(("isTrue").equals(excel)){
                sql += " order by  searchCount DESC" ;
            }
            List list1 = HibernateUtils.findList(this.baseLogicInterface.getSession(), sql, params, paramTypes, startNo, pageSize);
            List list2 = list1;
            if("isTrue".equals(excel)){
                String result = "";
                Date startDate = StringUtils.string2date(startTime,"yyyy-MM-dd");
                Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
                if(1==type){
                    result = visitLogLogicInterface.createExcelFile(startDate,endDate,0,list2,"searchHotLog");
                }
                if("success".equals(result)){
                    return  null;
                }
            }else{
            SearchResult<Object[]> searchResult = new SearchResult<Object[]>();
            searchResult.setRows(list1);
            searchResult.setRowCount(list1.size());
            String output = null;
            if(1==type){
                output = searchObjectsJbon(new String[]{"id", "content","vl.createTime","updateCount","searchCountStatus"}, searchResult);
            }
            if(2==type) {
                output = searchObjectsJbon(new String[]{"id", "adminId", "content","createTime","searchCount","searchWeekCount","searchMonthCount","searchCountStatus"}, searchResult);
            }

            directOut(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
