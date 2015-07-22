package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentDTO;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.log.dao.daoInterface.VisitLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface;
import com.fortune.rms.business.log.model.*;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.system.logic.logicInterface.AreaLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.util.JsonUtils;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.*;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("visitLogLogicInterface")
public  class VisitLogLogicImpl extends BaseLogicImpl<VisitLog>
        implements
        VisitLogLogicInterface {
    private VisitLogDaoInterface visitLogDaoInterface;
    private AreaLogicInterface areaLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private ChannelLogicInterface channelLogicInterface;
    private VisitLogLogicInterface visitLogLogicInterface;

    /**
     * @param visitLogDaoInterface the visitLogDaoInterface to set
     */
    @Autowired
    public void setVisitLogDaoInterface(
            VisitLogDaoInterface visitLogDaoInterface) {
        this.visitLogDaoInterface = visitLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.visitLogDaoInterface;
    }
    @Autowired
    public void setAreaLogicInterface(AreaLogicInterface areaLogicInterface) {
        this.areaLogicInterface = areaLogicInterface;
    }
    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public ChannelLogicInterface getChannelLogicInterface() {
        return channelLogicInterface;
    }

    @Autowired
    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public void JDBCSaveVisitLogs(List<VisitLog> visitLogs) {
        this.visitLogDaoInterface.JDBCSaveVisitLogs(visitLogs);
    }


    public Map getOrganizationContributionCount(long spId, String startTime, String endTime, String channelsAndLeafs) {
        Map objs = new HashMap();
        List cpLists = this.visitLogDaoInterface.getOrganizationContributionCount(spId, startTime, endTime, channelsAndLeafs);

        HashMap cpMaps = null;
        long cpNumCount=0;
        long spNumCount=0;
        long channelNumCount=0;
        double cpTimeCount=0;
        double spTimeCount=0;
        double channelTimeCount=0;
        for (Object cpList : cpLists) {
            cpMaps = (HashMap) cpList;
            cpNumCount += (Long)cpMaps.get("cp_num");
            cpTimeCount += (Double)cpMaps.get("cp_time");
            List spLists = (List) cpMaps.get("sp_list");
            HashMap spMap = null;
            for (Object spList : spLists) {
                spMap = (HashMap)spList;
                spNumCount += (Long)spMap.get("sp_num");
                spTimeCount += (Double)spMap.get("sp_time");
                List channelLists = (List)spMap.get("channel_list");
                HashMap channelMap = null;
                for(Object channelList : channelLists){
                    channelMap = (HashMap)channelList;
                    channelNumCount += (Long)channelMap.get("channel_num");
                    channelTimeCount += (Double)channelMap.get("channel_time");
                }

            }
        }
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("hh小时mm分ss秒", Locale.CHINESE);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        long cpNum=0;
        double cpTime=0;
        for (Object cpList : cpLists) {
            cpMaps = (HashMap) cpList;
            cpNum = (Long)cpMaps.get("cp_num");
            cpTime= (Double)cpMaps.get("cp_time");
            cpMaps.put("cp_time",formatTime(cpTime));
            cpMaps.put("cp_num_percentage",df.format(cpNum*100/cpNumCount)+"%");
            //date = new Date(cpTimePercentageTemp*1000);
            String cpTimePercentage = df.format(cpTime*100/cpTimeCount)+"%";
            cpMaps.put("cp_time_percentage",cpTimePercentage);
            List spLists = (List) cpMaps.get("sp_list");
            cpMaps.put("sp_size",spLists.size());
            HashMap spMap = null;
            long spNum=0;
            double spTime=0;
            for (Object spList : spLists) {
                spMap = (HashMap)spList;
                spNum = (Long)spMap.get("sp_num");
                spTime = (Double)spMap.get("sp_time");
                spMap.put("sp_time",formatTime(spTime));
                spMap.put("sp_num_percentage",df.format(spNum*100/spNumCount)+"%");
                //double spTimePercentageTemp = spTime/spTimeCount;
                //date = new Date(spTimePercentageTemp*1000);
                String spTimePercentage = df.format(spTime*100/spTimeCount)+"%";
                spMap.put("sp_time_percentage",spTimePercentage);
                List channelLists = (List)spMap.get("channel_list");
                spMap.put("channel_size",channelLists.size());
                HashMap channelMap = null;
                long channelNum =0;
                double channelTime =0;
                for(Object channelList : channelLists){
                    channelMap = (HashMap)channelList;
                    channelNum = (Long)channelMap.get("channel_num");
                    channelTime = (Double)channelMap.get("channel_time");
                    channelMap.put("channel_time",formatTime(channelTime));
                    channelMap.put("channel_num_percentage",df.format(channelNum*100/channelNumCount)+"%");
//                    double channelTimePercentageTemp = channelTime/channelTimeCount;
//                    date = new Date(channelTimePercentageTemp*1000);
                    String channelTimePercent = df.format(channelTime*100/channelTimeCount)+"%";
                    channelMap.put("channel_time_percentage",channelTimePercent);
                }

            }
        }
        objs.put("cp_list",cpLists);
        objs.put("cp_num_count",cpNumCount);
        objs.put("cp_time_count",formatTime(cpTimeCount));
        objs.put("cp_size",cpLists.size());
        return objs;
    }


    public  String formatTime(double secondTime){
        String str="";
        long hour=0;
        long minute=0;
        long second=0;
        if(secondTime!=0){
            hour = new Double(secondTime/3600).longValue();
            secondTime = secondTime - hour*3600;
            minute = new Double(secondTime/60).longValue();
            secondTime = secondTime - minute*60;
            second = new Double(secondTime).longValue();
        }


        str+=hour+"小时";

        str+=minute+"分";

        str+=second+"秒";

        return str;

    }

    public Map getResourceContributionCount(long spId,long cpId, String startTime, String endTime, String channelsAndLeafs, String contentName, String channelSelect, String playTimeSelect,PageBean pageBean) {

        return this.visitLogDaoInterface.getResourceContributionCount(spId,cpId,startTime,endTime,channelsAndLeafs,contentName,channelSelect,playTimeSelect,pageBean);
    }

    public Map getAreaContributionCount(long spId, String startTime, String endTime) {
        Map<String,Object> areaMaps = new HashMap<String, Object>();
        List<Map> areaList = this.visitLogDaoInterface.getAreaContributionCount(spId,startTime,endTime);
        long sp_num = 0;
        double sp_time = 0;
        for(int i=0;i<areaList.size();i++){
            Map areaMap1 = areaList.get(i);
            String areaName = (String) areaMap1.get("area_name");
            if(areaName == null || areaName == ""){
                sp_num += (Long)areaMap1.get("sp_num");
                sp_time += (Double)areaMap1.get("sp_time");
                areaList.remove(areaMap1);
                --i;
            }
        }

        Map areaMap2 = new HashMap();
        areaMap2.put("area_name",null);
        areaMap2.put("sp_num",sp_num);
        areaMap2.put("sp_time",sp_time);
        areaList.add(areaMap2);
        Collections.sort(areaList,new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                int flag = ((Double)o1.get("sp_time")).compareTo((Double)o2.get("sp_time"));
                return flag;
            }
        });
        int t =0;
        for(int i=0;i<areaList.size();i++){
            Map areaMap3 = areaList.get(i);
            areaMap3.put("sp_time_rank",++t);
        }


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        long sp_num_count = 0;
        double sp_time_count = 0;
        Map<String,Object> areaMap = null;
        for(int i = 0;i<areaList.size();i++){
            areaMap = areaList.get(i);
            sp_num_count += (Long)areaMap.get("sp_num");
            sp_time_count += (Double)areaMap.get("sp_time");

//            areaMap.put("area_num_percentage", (df.format((Long)areaMap.get("area_num")*100/(Long)areaMap.get("area_num_count"))+"%"));
//            areaMap.put("area_time",formatTime((Double)areaMap.get("area_time")));
//            areaMap.put("area_time_percentage", (df.format((Double)areaMap.get("area_time")*100/(Long)areaMap.get("area_time_time"))+"%"));
        }
        areaMaps.put("area_list",areaList);
        areaMaps.put("sp_num_count",sp_num_count);
        areaMaps.put("sp_time_count",sp_time_count);

        for(int j=0;j<areaList.size();j++){
            areaMap = areaList.get(j);
            areaMap.put("sp_num_percentage",(df.format((Long)areaMap.get("sp_num")*100/sp_num_count)+"%"));
            double temp = (Double)areaMap.get("sp_time")*100/sp_time_count;
            String sp_time_percentage = df.format(temp)+"%";
            areaMap.put("sp_time_percentage",sp_time_percentage);

            areaMap.put("sp_time",formatTime((Double)areaMap.get("sp_time")));

        }
        Collections.sort(areaList,new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                int flag = ((Long)o1.get("sp_num")).compareTo((Long)o2.get("sp_num"));
                return flag;
            }
        });
        int m=0;
        for(int k=0;k<areaList.size();k++){
            areaMap = areaList.get(k);
            areaMap.put("sp_num_rank",++m);
            if(areaMap.get("area_name")==null){
                areaMap.put("area_name","外省");
            }
        }
        areaMaps.put("area_list",areaList);
        areaMaps.put("sp_num_count", sp_num_count);
        areaMaps.put("sp_time_count", formatTime(sp_time_count));
//        logger.debug(areaMaps.toString());
        return areaMaps;
    }

    public List getOnLineUserAnalysisCount(long spId,long cpId,String startTime,String endTime){
        List<Map> concurrentList = new ArrayList<Map>();
        Map<String,Object> concurrentMap = null;
        long startTimeTemp = StringUtils.string2date(startTime,"yyyy-MM-dd").getTime();
        String culTime=null;
        for(int i=0;i<288;i++){
            concurrentMap = new TreeMap<String, Object>();
            culTime = StringUtils.date2string(new Date(startTimeTemp+i*10*60*1000));
            concurrentMap.put("culTime",culTime);
            concurrentMap.put("analysisCount",this.visitLogDaoInterface.getOnLineUserAnalysisCount(spId,cpId,culTime));
            concurrentList.add(concurrentMap);
        }
        return concurrentList;
    }



    public List getActivityUserAnalysisCount(long spId, String startTime, String endTime,PageBean pageBean) {
        return this.visitLogDaoInterface.getActivityUserAnalysisCount(spId, startTime, endTime, pageBean);
    }

    public Map getUserDemandCount(long spId, long cpId, String startTime, String endTime,String endTime1, String contentName, String channelName, String userIp, String userId, String playTime, PageBean pageBean) {
        Map<String,Object> userDemand = new HashMap();
        userDemand.put("userDemandList",this.visitLogDaoInterface.getUserDemandCount(spId,cpId,startTime,endTime,endTime1,contentName,channelName,userIp,userId,playTime,pageBean));
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("sp_id",spId);
        searchParams.put("cp_id",cpId);
        searchParams.put("start_time",startTime);
        searchParams.put("end_time",endTime);
        searchParams.put("content_name",contentName);
        searchParams.put("channel_name",channelName);
        searchParams.put("user_ip",userIp);
        searchParams.put("user_id",userId);
        searchParams.put("play_time",playTime);
        userDemand.put("searchParams",searchParams);
        return userDemand;
    }
    public Map getUserLoginCount(String startTime, String endTime, String userIp, String userId, PageBean pageBean){
        Map<String,Object> userLogin = new HashMap();
        userLogin.put("userLoginMap",this.visitLogDaoInterface.getUserLoginCount(startTime,endTime,userIp,userId,pageBean));
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("start_time",startTime);
        searchParams.put("end_time",endTime);
        searchParams.put("user_ip",userIp);
        searchParams.put("user_id",userId);
        userLogin.put("searchParams",searchParams);
        return userLogin;
    }

    public List getChannelDemandCount(long spId, String startTime, String endTime, String channelsAndLeafs) {
        return this.visitLogDaoInterface.getChannelDemandCount(spId,startTime,endTime,channelsAndLeafs);
    }

    public Map getChannelOnDemandCount(long spId,String startTime, String endTime,long isFree, PageBean pageBean) {
        Map<String,Object> channelOnDemand = new HashMap<String, Object>();
        channelOnDemand.put("channelOnDemand",this.visitLogDaoInterface.getChannelOnDemandCount(spId,startTime,endTime,isFree,pageBean));
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("start_time",startTime);
        searchParams.put("end_time",endTime);
        searchParams.put("is_free",isFree);
        channelOnDemand.put("searchParams",searchParams);
        return channelOnDemand;
    }

    public Map getResourceOnDemandCount(long spId, String startTime, String endTime, String channelName, String playTime, PageBean pageBean) {
        Map<String,Object> resourceOnDemand = new HashMap<String, Object>();
        resourceOnDemand.put("resourceOnDemand",this.visitLogDaoInterface.getResourceOnDemandCount(spId,startTime,endTime,channelName,playTime,pageBean));
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("sp_id",spId);
        searchParams.put("start_time",startTime);
        searchParams.put("end_time",endTime);
        searchParams.put("channel_name",channelName);
        searchParams.put("play_time",playTime);
        resourceOnDemand.put("searchParams",searchParams);
        return resourceOnDemand;
    }


    public int getMaxConcurrentOfDate(String startTime,String endTime) {
        int concurrentCount = -1;
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int runCount  = 0;
        Date startDate  =  StringUtils.string2date(startTime,"yyyy-MM-dd");
        Date endDate = StringUtils.string2date(endTime,"yyyy-MM-dd");
        while (true) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.MINUTE,15);
            Date tempDate = cal.getTime();
            logger.debug("startDate--->"+df.format(tempDate)+"   tempDate--->"+df.format(tempDate));
            if (!startDate.before(endDate)) {
                break;
            }
            runCount++;

            int count = visitLogDaoInterface.getMaxConcurrentOfDate(StringUtils.date2string(startDate,"yyyy-MM-dd HH:mm:ss"),StringUtils.date2string(tempDate,"yyyy-MM-dd HH:mm:ss"));
            if (count > concurrentCount) {
                concurrentCount = count;
            }

            startDate = tempDate;
            logger.debug("startTime--->"+df.format(startDate));
            logger.debug("最大并发值---->"+concurrentCount);
        }
        logger.debug("runCount--->"+runCount);
        return concurrentCount;
    }
    public List getSpLogsFromVisitLog(Date startDate, Date endDate){
        return visitLogDaoInterface.getSpLogsFromVisitLog(startDate,endDate);
    }

    public List getUserLoginLogs(Date startDate,Date endDate){
        List<Object[]> list =  visitLogDaoInterface.getUserLoginLogs(startDate,endDate);
        List<Object[]> countList = visitLogDaoInterface.getUserCountLogs(startDate,endDate);
        List<Object[]> loginCountList = visitLogDaoInterface.getUserLoginCountLogs(startDate,endDate);
        List<Object[]> dayCountList = visitLogDaoInterface.getUserDayCountLogs(startDate,endDate);
        long oCount = 0,oneCount=0,twoCount=0,threeCount=0,fourCount=0,fiveCount=0;
        List<Object[]> list1 = new ArrayList<Object[]>();
        List<Area> areaList = areaLogicInterface.getAll();
        for(Area area:areaList){
            //只是11个地市。
            if(area.getId()>0&&area.getId()<1000){
                Object[] o1 = new Object[9];
                oCount= 0 ;
                for(Object[] o:list){
                    long count = Long.parseLong(o[0].toString());
                    long areaId = Long.parseLong(o[1].toString());
                    long type = Long.parseLong(o[2].toString());
                    o1[0] = area.getId();
                    if(area.getId() == areaId && 1 == type ){o1[1]=count;}
                    if(area.getId() == areaId && 2 == type ){o1[2]=count;}
                    if(area.getId() == areaId && 3 == type ){o1[3]=count;}
                    if(area.getId() == areaId && 4 == type ){o1[4]=count;}
                    if(area.getId() == areaId && 5 == type ){o1[5]=count;}
                }
                for(Object[] o:countList){
                    long areaId = Long.parseLong(o[0].toString());
                    long count = Long.parseLong(o[1].toString());
                    if(area.getId()==areaId){
                        o1[6]=count;
                    }
                }
                //加入登陆次数
                for(Object[] o :loginCountList){
                    long areaId = Long.parseLong(o[0].toString());
                    long count = Long.parseLong(o[1].toString());
                    if(area.getId() == areaId){
                        o1[7] = count;
                    }
                }
                //加入登陆天次统计
                long dayCountLogin = 0;
                for(Object[] o:dayCountList){
                    long areaId = Long.parseLong(o[2].toString());
                    if(area.getId() == areaId){
                        dayCountLogin ++ ;
                    }
                }
                o1[8] = dayCountLogin;

                for(int i = 0; i<9; i++){
                    if(null==o1[i]){
                        o1[i]=0;
                    }
                }
                list1.add(o1);
            }
        }
        //每种登陆方式的总量统计
        Object[] o1 = new Object[9];
        oCount= 0 ;
        long countLogin = 0;
        long userCountLogin = 0;
        for(Object[] o:list){
            long count = Long.parseLong(o[0].toString());
            long areaId = Long.parseLong(o[1].toString());
            long type = Long.parseLong(o[2].toString());
            if(1 == type ){
                oneCount += count;
            }
            if(2 == type ){
                twoCount += count;
            }
            if(3 == type ){
                threeCount += count;
            }
            if(4 == type ) {
                fourCount += count;
            }
            if(5 == type ){
                fiveCount += count;
            }

            //其他区域的用户登陆量
            if(-1 ==areaId){
                o1[0] = -1;
                oCount += count;
                if(-1 == areaId && 1 == type){o1[1]=count;}
                if(-1 == areaId && 2 == type){o1[2]=count;}
                if(-1 == areaId && 3 == type){o1[3]=count;}
                if(-1 == areaId && 4 == type){o1[4]=count;}
                if(-1 == areaId && 5 == type){o1[5]=count;}
            }
        }
        if(null!=o1[0]){
            for(Object[] o :countList){
                long areaId = Long.parseLong(o[0].toString());
                long count = Long.parseLong(o[1].toString());
                countLogin += count;
                if(-1 == areaId){
                   o1[6] = count;
                }
            }
            for(Object[] o :loginCountList){
                long areaId = Long.parseLong(o[0].toString());
                long count = Long.parseLong(o[1].toString());
                userCountLogin +=count;
                if(-1 == areaId){
                    //拼成的总量
                    o1[7] = count;
                }
            }
            long count=0;
            for(Object[] o :dayCountList){
                long areaId = Long.parseLong(o[2].toString());
                if(-1 == areaId){
                    //拼成的总量
                    count++;
                }
            }
            o1[8] = count;
            for(int i = 0; i<9; i++){
                if(null==o1[i]){
                    o1[i]=0;
                }
            }
            list1.add(o1);
        }
        //按照要显示的顺序重新排列
        List<Object[]> list2 = new ArrayList<Object[]>();
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==311){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==312){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==315){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==355){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==317){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==316){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==310){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==319){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==313){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==314){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==318){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        for(Object[] o :list1){
            long areaId = Long.parseLong(o[0].toString());
            if(areaId==-1){
                list2.add(o);
                list1.remove(o);
                break;
            }
        }
        Object[] oLoginType = new Object[9];
        oLoginType[0] = 1;
        oLoginType[1] = oneCount;
        oLoginType[2] = twoCount;
        oLoginType[3] = threeCount;
        oLoginType[4] = fourCount;
        oLoginType[5] = fiveCount;
        oLoginType[6] = countLogin;
        oLoginType[7] = userCountLogin;
        oLoginType[8] = dayCountList.size();
        list2.add(oLoginType);
        return list2;
    }
    public List getCountList(Date startDate,Date endDate){
        List<Map<String,String>> countList = new ArrayList();
        List<Object[]> demandCountLogslist = visitLogDaoInterface.getDemandCountLogs(startDate, endDate);
        LinkedHashMap<String,String> map1 = new LinkedHashMap<String, String>();
        if(demandCountLogslist.size()>0){

            String date2 ="";
            int countOfDay = 0;
            int hour = 0;
            int count = 0;
            int time_00 = 0;
            int time_02 = 0;
            int time_01 = 0;
            int time_03 = 0;
            int time_04 = 0;
            int time_05 = 0;
            int time_06 = 0;
            int time_07 = 0;
            int time_08 = 0;
            int time_09 = 0;
            int time_10 = 0;
            int time_11 = 0;
            int time_12 = 0;
            int time_13 = 0;
            int time_14 = 0;
            int time_15 = 0;
            int time_16 = 0;
            int time_17 = 0;
            int time_18 = 0;
            int time_19 = 0;
            int time_20 = 0;
            int time_21 = 0;
            int time_22 = 0;
            int time_23 = 0;
            //  map1.put("0",String.valueOf(time_00));

            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            JsonUtils jsonUtils = new JsonUtils();
            for(int i=0;i< demandCountLogslist.size();){
                Object[] o = (Object[])demandCountLogslist.get(i);
                long demandCount =Long.parseLong(o[0].toString());
                String date =o[1].toString();
                String  date1 = date.split(" ")[0];
                long cHour = Long.valueOf(date.split(" ")[1]);
                Object[] startDateO = demandCountLogslist.get(1);
                Object[] endDateO = demandCountLogslist.get(demandCountLogslist.size()-1);
                date2 = startDateO[1].toString().split(" ")[0].replace("-",".")+ "-" + endDateO[1].toString().split(" ")[0].replace("-",".");
                countOfDay +=demandCount;
                map1.put("date",date2);

                if(hour == 0){
                    map.put("date",date1);
                }
                if( hour != cHour){
                    map.put(String.valueOf(hour),"0");
                }else{
                    map.put(String.valueOf(hour),String.valueOf(demandCount));
                    i++;
                }
                hour++;
                if( hour > 23){
                    if( hour != cHour){
                        countList.add(map);
                    }
                    map.put("Count",String.valueOf(countOfDay));
                    countOfDay = 0;
                    hour = 0;
                    map = new LinkedHashMap<String, String>();
                }

                if(cHour == 0){
                    time_00 +=demandCount;
                    map1.put("0",String.valueOf(time_00));
                }
                if(cHour == 1){
                    time_01 +=demandCount;
                    map1.put("1",String.valueOf(time_01));
                }
                if(cHour == 2){
                    time_02 +=demandCount;
                    map1.put("2",String.valueOf(time_02));
                }
                if(cHour == 3){
                    time_03 +=demandCount;
                    map1.put("3",String.valueOf(time_03));
                }
                if(cHour == 4){
                    time_04 +=demandCount;
                    map1.put("4",String.valueOf(time_04));
                }
                if(cHour == 5){
                    time_05 +=demandCount;
                    map1.put("5",String.valueOf(time_05));
                }
                if(cHour == 6){
                    time_06 +=demandCount;
                    map1.put("6",String.valueOf(time_06));
                }

                if(cHour == 7){
                    time_07 +=demandCount;
                    map1.put("7",String.valueOf(time_07));
                }
                if(cHour == 8){
                    time_08 +=demandCount;
                    map1.put("8",String.valueOf(time_08));
                }
                if(cHour == 9){
                    time_09 +=demandCount;
                    map1.put("9",String.valueOf(time_09));
                }
                if(cHour == 10){
                    time_10 +=demandCount;
                    map1.put("10",String.valueOf(time_10));
                }
                if(cHour == 11){
                    time_11 +=demandCount;
                    map1.put("11",String.valueOf(time_11));
                }
                if(cHour == 12){
                    time_12 +=demandCount;
                    map1.put("12",String.valueOf(time_12));
                }
                if(cHour == 13){
                    time_13 +=demandCount;
                    map1.put("13",String.valueOf(time_13));
                }
                if(cHour == 14){
                    time_14 +=demandCount;
                    map1.put("14",String.valueOf(time_14));
                }
                if(cHour == 15){
                    time_15 +=demandCount;
                    map1.put("15",String.valueOf(time_15));
                }
                if(cHour == 16){
                    time_16 +=demandCount;
                    map1.put("16",String.valueOf(time_16));
                }
                if(cHour == 17){
                    time_17 +=demandCount;
                    map1.put("17",String.valueOf(time_17));
                }
                if(cHour == 18){
                    time_18 +=demandCount;
                    map1.put("18",String.valueOf(time_18));
                }
                if(cHour == 19){
                    time_19 +=demandCount;
                    map1.put("19",String.valueOf(time_19));
                }
                if(cHour == 20){
                    time_20 +=demandCount;
                    map1.put("20",String.valueOf(time_20));
                }
                if(cHour == 21){
                    time_21 +=demandCount;
                    map1.put("21",String.valueOf(time_21));
                }
                if(cHour == 22){
                    time_22 +=demandCount;
                    map1.put("22",String.valueOf(time_22));
                }
                if(cHour == 23){
                    time_23 +=demandCount;
                    map1.put("23",String.valueOf(time_23));
                }
                map1.put("Count","0");
//                countList.add(map);

            }
        }
        countList.add(map1);
        return countList;
    }

    public String createExcelFile1(Date startDate, Date endDate, long contentType, List<Map<String, String>> list, String excelName) {
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        String startTime1 = startTime.replace("-","");
        String endTime1 = endTime.replace("-","");
        String startTime2 = startTime.replace("-",".");
        String endTime2 = endTime.replace("-",".");
        String fileName = "";
        if(("demandCountLog").equals(excelName)){
            fileName =  "C:\\直播趋势统计"+startTime1+"-"+endTime1+".xls";
        }
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            //字体
            //字体
            //字体1：居中，14，黑体，有边框
            WritableFont  wr = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD);
            WritableCellFormat fontFormat1 = new WritableCellFormat(wr);
            fontFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体2：居中，12，有边框
            WritableFont  wr1 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat2 = new WritableCellFormat(wr1);
            fontFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体3：左对齐，12，有边框
            WritableFont  wr2 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat3 = new WritableCellFormat(wr2);
            fontFormat3.setAlignment(jxl.format.Alignment.LEFT);
            fontFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体4：右对齐，12，有边框
            WritableFont  wr3 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat4 = new WritableCellFormat(wr3);
            fontFormat4.setAlignment(Alignment.RIGHT);
            fontFormat4.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            //时间
            jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");
            WritableCellFormat dateFormat = new WritableCellFormat(df);
            dateFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

            //数字
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat = new WritableCellFormat(nf);
            numberFormat.setAlignment(jxl.format.Alignment.CENTRE);
            numberFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat1 = new WritableCellFormat(nf1);
            numberFormat1.setAlignment(Alignment.RIGHT);
            numberFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            if(("demandCountLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("直播趋势统计",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,21);
                sheet.setColumnView(2,21);
                sheet.setColumnView(3,21);
                sheet.setColumnView(4,20);
                sheet.setColumnView(5,20);
                sheet.setColumnView(6,20);
                sheet.setColumnView(7,20);
                sheet.setColumnView(8,20);
                sheet.setColumnView(9,20);
                sheet.setColumnView(10,20);
                sheet.setColumnView(11,20);
                sheet.setColumnView(12,20);
                sheet.setColumnView(13,20);
                sheet.setColumnView(14,20);
                sheet.setColumnView(15,20);
                sheet.setColumnView(16,20);
                sheet.setColumnView(17,20);
                sheet.setColumnView(18,20);
                sheet.setColumnView(19,20);
                sheet.setColumnView(20,20);
                sheet.setColumnView(21,20);
                sheet.setColumnView(22,20);
                sheet.setColumnView(23,20);
                sheet.setColumnView(24,20);
                sheet.setColumnView(25,20);
                sheet.mergeCells(1,0,20,0);

                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"日期",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"00:00",fontFormat2);
                sheet.addCell(label3);
                Label mobileLabel = new Label(3,1,"01:00",fontFormat2);
                sheet.addCell(mobileLabel);
                Label elseLabel = new Label(4,1,"02：:00",fontFormat2);
                sheet.addCell(elseLabel);
                Label label4 = new Label(5,1,"03:00",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(6,1,"04:00",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(7,1,"05:00",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(8,1,"06：:00",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(9,1,"07：:00",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(10,1,"08：:00",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(11,1,"09:00",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(12,1,"10:00",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(13,1,"11：00",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(14,1,"12:00",fontFormat2);
                sheet.addCell(label13);
                Label label14 = new Label(15,1,"13:00",fontFormat2);
                sheet.addCell(label14);
                Label label15 = new Label(16,1,"14：:0",fontFormat2);
                sheet.addCell(label15);
                Label label16 = new Label(17,1,"15:00",fontFormat2);
                sheet.addCell(label16);
                Label label17 = new Label(18,1,"16:00",fontFormat2);
                sheet.addCell(label17);
                Label label18 = new Label(19,1,"17：00",fontFormat2);
                sheet.addCell(label18);
                Label label19 = new Label(20,1,"18:00",fontFormat2);
                sheet.addCell(label19);
                Label label20 = new Label(21,1,"19:00",fontFormat2);
                sheet.addCell(label20);
                Label label21 = new Label(22,1,"20:00",fontFormat2);
                sheet.addCell(label21);
                Label label22 = new Label(23,1,"21:00",fontFormat2);
                sheet.addCell(label22);
                Label label23= new Label(24,1,"22:00",fontFormat2);
                sheet.addCell(label23);
                Label label24= new Label(25,1,"23:00",fontFormat2);
                sheet.addCell(label24);
                Label label25= new Label(26,1,"按天汇总（播放次数）",fontFormat2);
                sheet.addCell(label25);

                int i = 2;
                if(list.size()>0){
                  for (Map map :list) {
                        Label labelDate = new Label(1,i, map.get("date").toString(),fontFormat3);
                        sheet.addCell(labelDate);
                        jxl.write.Number labelTime_00 = new jxl.write.Number(2,i,Long.parseLong(map.get("0").toString()),numberFormat1);
                        sheet.addCell(labelTime_00);
                        jxl.write.Number labelTime_01 = new jxl.write.Number(3,i,Long.parseLong(map.get("1").toString()),numberFormat1);
                        sheet.addCell(labelTime_01);
                        jxl.write.Number labelTime_02 = new jxl.write.Number(4,i,Long.parseLong(map.get("2").toString()),numberFormat1);
                        sheet.addCell(labelTime_02);
                        jxl.write.Number labelTime_03 = new jxl.write.Number(5,i,Long.parseLong(map.get("3").toString()),numberFormat1);
                        sheet.addCell(labelTime_03);
                        jxl.write.Number labelTime_04 = new jxl.write.Number(6,i,Long.parseLong(map.get("4").toString()),numberFormat1);
                        sheet.addCell(labelTime_04);
                        jxl.write.Number labelTime_05 = new jxl.write.Number(7,i,Long.parseLong(map.get("5").toString()),numberFormat1);
                        sheet.addCell(labelTime_05);
                        jxl.write.Number labelTime_06 = new jxl.write.Number(8,i,Long.parseLong(map.get("6").toString()),numberFormat1);
                        sheet.addCell(labelTime_06);
                        jxl.write.Number labelTime_07 = new jxl.write.Number(9,i,Long.parseLong(map.get("7").toString()),numberFormat1);
                        sheet.addCell(labelTime_07);
                        jxl.write.Number labelTime_08 = new jxl.write.Number(10,i,Long.parseLong(map.get("8").toString()),numberFormat1);
                        sheet.addCell(labelTime_08);
                        jxl.write.Number labelTime_09 = new jxl.write.Number(11,i,Long.parseLong(map.get("9").toString()),numberFormat1);
                        sheet.addCell(labelTime_09);
                        jxl.write.Number labelTime_10 = new jxl.write.Number(12,i,Long.parseLong(map.get("10").toString()),numberFormat1);
                        sheet.addCell(labelTime_10);
                        jxl.write.Number labelTime_11 = new jxl.write.Number(13,i,Long.parseLong(map.get("11").toString()),numberFormat1);
                        sheet.addCell(labelTime_11);
                        jxl.write.Number labelTime_12 = new jxl.write.Number(14,i,Long.parseLong(map.get("12").toString()),numberFormat1);
                        sheet.addCell(labelTime_12);
                        jxl.write.Number labelTime_13 = new jxl.write.Number(15,i,Long.parseLong(map.get("13").toString()),numberFormat1);
                        sheet.addCell(labelTime_13);
                        jxl.write.Number labelTime_14 = new jxl.write.Number(16,i,Long.parseLong(map.get("14").toString()),numberFormat1);
                        sheet.addCell(labelTime_14);
                        jxl.write.Number labelTime_15 = new jxl.write.Number(17,i,Long.parseLong(map.get("15").toString()),numberFormat1);
                        sheet.addCell(labelTime_15);
                        jxl.write.Number labelTime_16 = new jxl.write.Number(18,i,Long.parseLong(map.get("16").toString()),numberFormat1);
                        sheet.addCell(labelTime_16);
                        jxl.write.Number labelTime_17 = new jxl.write.Number(19,i,Long.parseLong(map.get("17").toString()),numberFormat1);
                        sheet.addCell(labelTime_17);
                        jxl.write.Number labelTime_18 = new jxl.write.Number(20,i,Long.parseLong(map.get("18").toString()),numberFormat1);
                        sheet.addCell(labelTime_18);
                        jxl.write.Number labelTime_19 = new jxl.write.Number(21,i,Long.parseLong(map.get("19").toString()),numberFormat1);
                        sheet.addCell(labelTime_19);
                        jxl.write.Number labelTime_20 = new jxl.write.Number(22,i,Long.parseLong(map.get("20").toString()),numberFormat1);
                        sheet.addCell(labelTime_20);
                        jxl.write.Number labelTime_21 = new jxl.write.Number(23,i,Long.parseLong(map.get("21").toString()),numberFormat1);
                        sheet.addCell(labelTime_21);
                        jxl.write.Number labelTime_22 = new jxl.write.Number(24,i,Long.parseLong(map.get("22").toString()),numberFormat1);
                        sheet.addCell(labelTime_22);
                        jxl.write.Number labelTime_23 = new jxl.write.Number(25,i,Long.parseLong(map.get("23").toString()),numberFormat1);
                        sheet.addCell(labelTime_23);
                       jxl.write.Number labelTime_24 = new jxl.write.Number(26,i,Long.parseLong(map.get("Count").toString()),numberFormat1);
                      sheet.addCell(labelTime_24);
                      i++;
                }
             }
            }
            workbook.write();
            workbook.close();
            this.downLoadFile(fileName);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



    public String createExcelFile(Date startDate,Date endDate,long contentType,List<Object[]> list,String excelName){
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        String startTime1 = startTime.replace("-","");
        String endTime1 = endTime.replace("-","");
        String startTime2 = startTime.replace("-",".");
        String endTime2 = endTime.replace("-",".");
        String fileName = "";
        if(("bingFa").equals(excelName)){
            fileName =  "C:\\并发数据统计"+startTime1+"-"+endTime1+".xls";
        }
        if(("areaLog").equals(excelName)){
            fileName =  "D:\\区域点播量"+startTime1+"-"+endTime1+".xls";
        }
        if(("spLog").equals(excelName)){
            fileName =  "C:\\SP点播量"+startTime1+"-"+endTime1+".xls";
        }
        if(("channelLog").equals(excelName)){
            fileName =  "C:\\频道点播量"+startTime1+"-"+endTime1+".xls";
        }
        if(("channelContentLog").equals(excelName)){
            fileName =  "C:\\频道资源点击排行"+startTime1+"-"+endTime1+".xls";
        }
        if(("contentLog").equals(excelName)){
            fileName =  "C:\\媒体点播量排行"+startTime1+"-"+endTime1+".xls";
        }
        if(("contentLiveLog").equals(excelName)){
            fileName =  "C:\\媒体直播量排行"+startTime1+"-"+endTime1+".xls";
        }
        if(("contentLadongLog").equals(excelName)){
            fileName =  "C:\\拉动点击排行"+startTime1+"-"+endTime1+".xls";
        }
        if(("netFlowLog").equals(excelName)){
            fileName =  "C:\\流量使用统计"+startTime1+"-"+endTime1+".xls";
        }

        if("contentByLengthLog".equals(excelName)){
            fileName =  "C:\\媒体点播时长排行"+startTime1+"-"+endTime1+".xls";
        }
        if("contentByNetFlowLog".equals(excelName)){
            fileName =  "C:\\媒体点播流量排行"+startTime1+"-"+endTime1+".xls";
        }
        if("userLoginLog".equals(excelName)){
            fileName = "C:\\用户登陆情况统计"+startTime1+"-"+endTime1+".xls";
        }
        if("contentFilmAndTvLog".equals(excelName)&&contentType==1){
            fileName = "C:\\电影点播排行"+startTime1+"-"+endTime1+".xls";
        }
        if("contentFilmAndTvLog".equals(excelName)&&contentType==2){
            fileName = "C:\\电视剧点播排行"+startTime1+"-"+endTime1+".xls";
        }
        if(("systemPlayedLog").equals(excelName)){
            fileName =  "C:\\有播放媒体统计"+startTime1+"-"+endTime1+".xls";
        }
        if(("searchHotLog").equals(excelName)){
            fileName = "C:\\用户搜索热词"+startTime1+"-"+endTime1+".xls";
        }
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            //字体
            //字体
            //字体1：居中，14，黑体，有边框
            WritableFont  wr = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD);
            WritableCellFormat fontFormat1 = new WritableCellFormat(wr);
            fontFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体2：居中，12，有边框
            WritableFont  wr1 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat2 = new WritableCellFormat(wr1);
            fontFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体3：左对齐，12，有边框
            WritableFont  wr2 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat3 = new WritableCellFormat(wr2);
            fontFormat3.setAlignment(jxl.format.Alignment.LEFT);
            fontFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体4：右对齐，12，有边框
            WritableFont  wr3 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat4 = new WritableCellFormat(wr3);
            fontFormat4.setAlignment(Alignment.RIGHT);
            fontFormat4.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            //时间
            jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");
            WritableCellFormat dateFormat = new WritableCellFormat(df);
            dateFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

            //数字
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat = new WritableCellFormat(nf);
            numberFormat.setAlignment(jxl.format.Alignment.CENTRE);
            numberFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat1 = new WritableCellFormat(nf1);
            numberFormat1.setAlignment(Alignment.RIGHT);
            numberFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            if(("bingFa").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("日并发量统计",5);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,16);
                sheet.setColumnView(2,13);
                sheet.mergeCells(1,0,2,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"时间",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"并发量(次)",fontFormat2);
                sheet.addCell(label3);

                if(list.size()>0){
                    int i = 2;
                    if(list.size()==1){
                        for(Object[] o :list){
                            String quarterBingFa = o[0].toString();
                            String[]  s = quarterBingFa.split(",");
                            for(int m = 0; m<s.length;m++){
                                String [] s1 = s[m].split("=");
                                String time = s1[0];
                                String value = s1[1];
                                Label labelDate = new Label(1,i,time,fontFormat3);
                                sheet.addCell(labelDate);
                                jxl.write.Number labelValue = new jxl.write.Number(2,i,Long.parseLong(value),numberFormat1);
                                sheet.addCell(labelValue);
                            }
                        }
                    }else{
                        for(Object[] o :list){
                            String MaxBingFa  = o[1].toString();
                            String[]  s = MaxBingFa.split("=");
                            String time = s[0].substring(0,10);
                            String value = s[1];
                            Label labelDate = new Label(1,i,time,fontFormat3);
                            sheet.addCell(labelDate);
                            jxl.write.Number labelValue = new jxl.write.Number(2,i,Long.parseLong(value),numberFormat1);
                            sheet.addCell(labelValue);
                            i++;
                        }
                    }
                }
            }
            if(("areaLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("区域播放量",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,14);
                sheet.setColumnView(2,14);
                sheet.setColumnView(3,14);
                sheet.setColumnView(4,12);
                sheet.setColumnView(5,15);
                sheet.setColumnView(6,15);
                sheet.setColumnView(7,15);
                sheet.setColumnView(8,15);
                sheet.setColumnView(9,7);
                sheet.setColumnView(10,14);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.setColumnView(13,7);
                sheet.setColumnView(14,17);
                sheet.setColumnView(15,17);
                sheet.setColumnView(16,17);
                sheet.setColumnView(17,17);
                sheet.setColumnView(18,17);
                sheet.setColumnView(19,7);
                sheet.setColumnView(20,7);
                Label label0;
                sheet.mergeCells(1,0,20,0);
                label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"区域名称",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"播放总次数",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"移动播放次数",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"其他播放次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad播放次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone播放次数",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"比重",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"播放时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"移动播放时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"其他播放时长(秒)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad播放时长(秒)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone播放时长(秒)",fontFormat2);
                sheet.addCell(label13);
                Label label14 = new Label(13,1,"比重",fontFormat2);
                sheet.addCell(label14);
                Label label15 = new Label(14,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label15);
                Label label16 = new Label(15,1,"移动流量(字节)",fontFormat2);
                sheet.addCell(label16);
                Label label17 = new Label(16,1,"其他流量(字节)",fontFormat2);
                sheet.addCell(label17);
                Label label18= new Label(17,1,"Pad流量(字节)",fontFormat2);
                sheet.addCell(label18);
                Label label19 = new Label(18,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label19);
                Label label120 = new Label(19,1,"比重",fontFormat2);
                sheet.addCell(label120);
                Label label121 = new Label(20,1,"用户点播数",fontFormat2);
                sheet.addCell(label121);
                List<Area> arealist = areaLogicInterface.getAll();
                Double count = 0.0 ;
                Double length = 0.0;
                Long bytesSend = 0L;
                for(Object[] objects:list) {
                    count += Integer.parseInt(objects[2].toString());
                    length += Integer.parseInt(objects[5].toString());
                    bytesSend += Long.parseLong(objects[8].toString());
                }
                if(arealist.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long areaId = Long.parseLong(o[1].toString());
                        if("Area".equals(o[0].toString())){
                            for(Area area :arealist){
                                if(areaId==area.getId()){
                                    Label areaName = new Label(1,i,area.getName(),fontFormat3);
                                    sheet.addCell(areaName);
                                }
                            }
                        }
                        if("pullArea".equals(o[0].toString())){
                            for(Area area:arealist){
                                if(areaId==area.getId()){
                                    Label areaName = new Label(1,i,area.getName()+"--拉动",fontFormat3);
                                    sheet.addCell(areaName);
                                }
                            }
                        }
                        if("-1".equals(o[1].toString())){
                            Label areaName = new Label(1,i,"其他",fontFormat3);
                            sheet.addCell(areaName);
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelMobileCount = new jxl.write.Number(3,i,Double.valueOf(o[13].toString()),numberFormat1);
                        sheet.addCell(labelMobileCount);
                        jxl.write.Number labelElseCount = new jxl.write.Number(4,i,Double.valueOf(o[14].toString()),numberFormat1);
                        sheet.addCell(labelElseCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Double.valueOf(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Double.valueOf(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[2].toString())/count*1000)/10)+"%";
                        Label labelCountBiZhong = new Label(7,i,countBiZhong,fontFormat4);
                        sheet.addCell(labelCountBiZhong);
                        jxl.write.Number labelLength = new jxl.write.Number(8,i,Double.valueOf(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelMobileLength = new jxl.write.Number(9,i,Double.valueOf(o[15].toString()),numberFormat1);
                        sheet.addCell(labelMobileLength);
                        jxl.write.Number labelElseLength = new jxl.write.Number(10,i,Double.valueOf(o[16].toString()),numberFormat1);
                        sheet.addCell(labelElseLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(11,i,Double.valueOf(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(12,i,Double.valueOf(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[5].toString())/length*1000)/10)+"%";
                        Label labelLengthBiZhong = new Label(13,i,lengthBiZhong,fontFormat4);
                        sheet.addCell(labelLengthBiZhong);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(14,i,Double.valueOf(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelMobileBytesSend = new jxl.write.Number(15,i,Double.valueOf(o[11].toString()),numberFormat1);
                        sheet.addCell(labelMobileBytesSend);
                        jxl.write.Number labelElseBytesSend = new jxl.write.Number(16,i,Double.valueOf(o[12].toString()),numberFormat1);
                        sheet.addCell(labelElseBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(17,i,Double.valueOf(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(18,i,Double.valueOf(o[10].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[8].toString())+0.0)/bytesSend*1000)/10)+"%";
                        Label labelBytesSendBiZhong = new Label(19,i,bytesSendBiZhong,fontFormat4);
                        sheet.addCell(labelBytesSendBiZhong);
                        jxl.write.Number labelUserOnLineCount = new jxl.write.Number(20,i,Double.valueOf(o[17].toString()),numberFormat1);
                        sheet.addCell(labelUserOnLineCount);
                        i++;
                    }
                }
            }
            if(("spLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("SP播放量",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,14);
                sheet.setColumnView(2,12);
                sheet.setColumnView(3,15);
                sheet.setColumnView(4,15);
                sheet.setColumnView(5,7);
                sheet.setColumnView(6,14);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,7);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.setColumnView(13,7);
                sheet.mergeCells(1,0,13,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"SP名称",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"播放总次数",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"Pad播放次数",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"Phone播放次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"比重",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"播放时长(秒)",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"Pad播放时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Phone播放时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"比重",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone流量(字节)",fontFormat2);
                sheet.addCell(label13);
                Label label14 = new Label(13,1,"比重",fontFormat2);
                sheet.addCell(label14);
                List<Csp> cspList = cspLogicInterface.getAll();
                Double count = 0.0 ;
                Double length = 0.0;
                long bytesSend = 0L;
                for(Object[] objects:list) {
                    count += Integer.parseInt(objects[1].toString());
                    length += Integer.parseInt(objects[4].toString());
                    bytesSend += Long.parseLong(objects[7].toString());
                }
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                Label cspName = new Label(1,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[1].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(3,i,Double.valueOf(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[1].toString())/count*1000)/10)+"%";
                        Label labelCountBiZhong = new Label(5,i,countBiZhong,fontFormat4);
                        sheet.addCell(labelCountBiZhong);
                        jxl.write.Number labelLength = new jxl.write.Number(6,i,Double.valueOf(o[4].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(7,i,Double.valueOf(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(8,i,Double.valueOf(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[4].toString())/length*1000)/10)+"%";
                        Label labelLengthBiZhong = new Label(9,i,lengthBiZhong,fontFormat4);
                        sheet.addCell(labelLengthBiZhong);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Double.valueOf(o[7].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Double.valueOf(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Double.valueOf(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[7].toString())+0.0)/bytesSend*1000)/10)+"%";
                        Label bytesSendLengthBiZhong = new Label(13,i,bytesSendBiZhong,fontFormat4);
                        sheet.addCell(bytesSendLengthBiZhong);
                        i++;
                    }
                }
            }
            if(("channelLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("频道点播量",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,20);
                sheet.setColumnView(2,12);
                sheet.setColumnView(3,7);
                sheet.setColumnView(4,15);
                sheet.setColumnView(5,7);
                sheet.setColumnView(6,16);
                sheet.setColumnView(7,7);
                sheet.mergeCells(1,0,7,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"频道名称",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"点播次数",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"比重",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"点播时长",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"比重",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"流量(字节)",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"比重",fontFormat2);
                sheet.addCell(label8);
                Double count = 0.0 ;
                Double length = 0.0;
                long bytesSend = 0L;
                for(Object[] objects:list) {
                    if(objects[4].toString().equals("3")){
                        count += Integer.parseInt(objects[2].toString());
                        length += Integer.parseInt(objects[3].toString());
                        bytesSend += Long.parseLong(objects[5].toString());
                    }
                }
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        Label cspName = new Label(1,i,o[1].toString(),fontFormat3);
                        sheet.addCell(cspName);
                        jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        if(Double.valueOf(o[2].toString())!=0){
                            String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[2].toString())/count*1000)/10)+"%";
                            Label labelCountBiZhong = new Label(3,i,countBiZhong,fontFormat4);
                            sheet.addCell(labelCountBiZhong);
                        }else{
                            jxl.write.Number labelCountBiZhong = new jxl.write.Number(3,i,0,numberFormat1);
                            sheet.addCell(labelCountBiZhong);
                        }
                        jxl.write.Number labelLength = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        if(Double.valueOf(o[3].toString())!=0){
                            String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[3].toString())/length*1000)/10)+"%";
                            Label labelLengthBiZhong = new Label(5,i,lengthBiZhong,fontFormat4);
                            sheet.addCell(labelLengthBiZhong);
                        }else{
                            jxl.write.Number bytesSendLengthBiZhong = new  jxl.write.Number(5,i,0,numberFormat1);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }
                        jxl.write.Number labelBytesSend = new jxl.write.Number(6,i,Double.valueOf(o[5].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        if(Double.valueOf(o[5].toString())!=0){
                            String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[5].toString())+0.0)/bytesSend*1000)/10)+"%";
                            Label bytesSendLengthBiZhong = new Label(7,i,bytesSendBiZhong,fontFormat4);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }else{
                            jxl.write.Number bytesSendLengthBiZhong = new jxl.write.Number(7,i,0,numberFormat1);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }
                        i++;
                    }
                }
            }
            if(("channelContentLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("频道资源点击排行",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,15);
                sheet.setColumnView(3,15);
                sheet.setColumnView(4,25);
                sheet.setColumnView(5,15);
                sheet.setColumnView(6,15);
                sheet.setColumnView(7,15);
                sheet.mergeCells(1,0,7,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"频道",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"资源名称",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"点播次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"点播时长",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label8);
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Channel>  channelList =  channelLogicInterface.getAll();
                List<Csp> cspList = cspLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Channel channel:channelList){
                            long channelId = Long.parseLong(o[2].toString());
                            if(channelId == channel.getId()){
                                Label contentName = new Label(3,i,channel.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(4,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelLength = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        i++;
                    }
                }
            }
            if(("contentLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("媒体点播量",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,35);
                sheet.setColumnView(4,10);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"点播次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad点播次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone点播次数",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label13);
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Csp> cspList = cspLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        i++;
                    }
                }
            }
            if(("contentFilmAndTvLog").equals(excelName)){
                WritableSheet sheet=null;
                if(contentType==1){
                   sheet = workbook.createSheet("电影点播排行",0);
                }else{
                   sheet = workbook.createSheet("电视剧点播排行",0);
                }
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,17);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,17);
                sheet.setColumnView(4,17);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.mergeCells(1,0,7,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"SP名称",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"资源类型",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"频道",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"点播次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"点播时长",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"总流量(b)",fontFormat2);
                sheet.addCell(label8);
                List<Csp> cspList = cspLogicInterface.getAll();
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Channel>  channelList =  channelLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                Label cspName = new Label(1,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        Long type = Long.parseLong(o[1].toString());
                        String contentTypeName = "";
                        if(1==type){
                            contentTypeName = "电影";
                        }else{
                            contentTypeName = "电视剧";
                        }
                        Label labelContentType = new Label(2,i,contentTypeName,fontFormat3);
                        sheet.addCell(labelContentType);
                        long contentId = Long.parseLong(o[2].toString());
                        for(Content content:contentList){
                            if(contentId==content.getId()){
                                Label cspName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        long channelId = Long.parseLong(o[3].toString());
                        for(Channel channel:channelList){
                            if(channelId==channel.getId()){
                                Label channelName = new Label(4,i,channel.getName(),fontFormat3);
                                sheet.addCell(channelName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(5,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelLength = new jxl.write.Number(6,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(7,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        i++;
                    }
                }
            }
            if(("contentByLengthLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("媒体点播时长",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,35);
                sheet.setColumnView(4,17);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"点播时长(秒)",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad点播时长(秒)",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone点播时长(秒)",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"点播次数",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad点播次数",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone点播次数",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label13);
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Csp> cspList = cspLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelLength = new jxl.write.Number(4,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(5,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(6,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelCount = new jxl.write.Number(7,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(8,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(9,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        i++;
                    }
                }
            }
            if(("contentByNetFlowLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("媒体点播流量",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,35);
                sheet.setColumnView(4,17);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"点播次数",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad点播次数",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone点播次数",fontFormat2);
                sheet.addCell(label13);
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Csp> cspList = cspLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelLength = new jxl.write.Number(4,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(5,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(6,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelCount = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        i++;
                    }
                }
            }
            if(("contentLiveLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("直播量统计",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,12);
                sheet.setColumnView(3,14);
                sheet.setColumnView(4,9);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"播放次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad播放次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone播放次数",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"播放时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad播放时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone播放时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label13);

                if(list.size()>0){
                    int i = 2;
                    List<Content>  contentList =  contentLogicInterface.getAll();
                    List<Csp> cspList = cspLogicInterface.getAll();
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        i++;
                    }
                }
            }
            if(("contentLadongLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("拉动点击排行",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,35);
                sheet.setColumnView(4,10);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"播放次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad播放次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone播放次数",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"播放时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad播放时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone播放时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label  label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label13);

                if(list.size()>0){
                    int i = 2;
                    List<Content>  contentList =  contentLogicInterface.getAll();
                    List<Csp> cspList = cspLogicInterface.getAll();
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelByteSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelByteSend);
                        jxl.write.Number labelByteSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelByteSendPad);
                        jxl.write.Number labelByteSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelByteSendPhone);
                        i++;
                    }
                }
            }
            if("userLoginLog".equals(excelName)){
                WritableSheet sheet = workbook.createSheet("用户登陆情况统计",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,15);
                sheet.setColumnView(2,15);
                sheet.setColumnView(3,15);
                sheet.setColumnView(4,15);
                sheet.setColumnView(5,15);
                sheet.setColumnView(6,15);
                sheet.setColumnView(7,15);
                sheet.setColumnView(8,15);
                sheet.setColumnView(9,15);
                Label label0;
                sheet.mergeCells(1,0,9,0);
                label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"区域名称",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"联通总部取号",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"Wap取号",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"河北联通取号",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"验证码登陆",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"客户端登陆",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"总量",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"登陆次数",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"登陆天次",fontFormat2);
                sheet.addCell(label10);
                List<Area> arealist = areaLogicInterface.getAll();
                if(arealist.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long areaId = Long.parseLong(o[0].toString());
                            for(Area area :arealist){
                                if(areaId==area.getId()){
                                    Label areaName = new Label(1,i,area.getName(),fontFormat3);
                                    sheet.addCell(areaName);
                                }
                                if(-1 == areaId){
                                    Label areaName = new Label(1,i,"其他",fontFormat3);
                                    sheet.addCell(areaName);
                                }
                                if(1 == areaId){
                                    Label areaName = new Label(1,i,"总量",fontFormat3);
                                    sheet.addCell(areaName);
                                }
                            }
                        jxl.write.Number labelOneCount = new jxl.write.Number(2,i,Double.valueOf(o[1].toString()),numberFormat1);
                        sheet.addCell(labelOneCount);
                        jxl.write.Number labelTwoCount = new jxl.write.Number(3,i,Double.valueOf(o[2].toString()),numberFormat1);
                        sheet.addCell(labelTwoCount);
                        jxl.write.Number labelThreeCount = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                        sheet.addCell(labelThreeCount);
                        jxl.write.Number labelFourCount = new jxl.write.Number(5,i,Double.valueOf(o[4].toString()),numberFormat1);
                        sheet.addCell(labelFourCount);
                        jxl.write.Number labelFiveCount = new jxl.write.Number(6,i,Double.valueOf(o[5].toString()),numberFormat1);
                        sheet.addCell(labelFiveCount);
                        jxl.write.Number labelCount = new jxl.write.Number(7,i,Double.valueOf(o[6].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labeluserCount = new jxl.write.Number(8,i,Double.valueOf(o[7].toString()),numberFormat1);
                        sheet.addCell(labeluserCount);
                        jxl.write.Number labeldayCount = new jxl.write.Number(9,i,Double.valueOf(o[8].toString()),numberFormat1);
                        sheet.addCell(labeldayCount);
                        i++;
                    }
                }

            }
            if(("netFlowLog").equals(excelName)){
            WritableSheet sheet = workbook.createSheet("流量统计",0);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,21);
            sheet.setColumnView(2,21);
            sheet.setColumnView(3,21);
            sheet.setColumnView(4,20);
            sheet.setColumnView(5,20);
            sheet.setColumnView(6,20);
            sheet.setColumnView(7,20);
            sheet.setColumnView(8,20);
            sheet.setColumnView(9,20);
            sheet.setColumnView(10,20);
            sheet.setColumnView(11,20);
            sheet.setColumnView(12,20);
            sheet.setColumnView(13,20);
            sheet.setColumnView(14,20);
            sheet.setColumnView(15,20);
            sheet.setColumnView(16,20);
            sheet.setColumnView(17,20);
            sheet.setColumnView(18,20);
            sheet.setColumnView(19,20);
            sheet.setColumnView(20,20);
            sheet.setColumnView(21,20);
            sheet.setColumnView(22,20);
            sheet.setColumnView(23,20);
            sheet.setColumnView(24,20);
            sheet.setColumnView(25,20);
            sheet.setColumnView(26,20);
            sheet.setColumnView(27,20);
            sheet.setColumnView(28,20);
            sheet.setColumnView(29,20);
            sheet.setColumnView(30,20);
            sheet.setColumnView(31,20);
            sheet.setColumnView(32,20);
            sheet.setColumnView(33,20);
            sheet.setColumnView(34,20);
            sheet.setColumnView(35,20);
            sheet.setColumnView(36,20);
            sheet.setColumnView(37,20);
            sheet.setColumnView(38,20);
            sheet.setColumnView(39,20);
            sheet.setColumnView(40,20);
            sheet.setColumnView(41,20);
            sheet.setColumnView(42,20);
            sheet.setColumnView(43,20);
            sheet.setColumnView(44,20);
            sheet.setColumnView(45,20);
            sheet.setColumnView(46,20);
            sheet.setColumnView(47,20);
            sheet.setColumnView(48,20);
            sheet.setColumnView(49,20);
            sheet.mergeCells(1,0,20,0);
            Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
            sheet.addCell(label0);
            Label label2 = new Label(1,1,"日期",fontFormat2);
            sheet.addCell(label2);
            Label label3 = new Label(2,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label3);
            Label label4 = new Label(3,1,"次数",fontFormat2);
            sheet.addCell(label4);
            Label label5 = new Label(4,1,"时长",fontFormat2);
            sheet.addCell(label5);
            Label mobileLabel = new Label(5,1,"移动流量(字节)",fontFormat2);
            sheet.addCell(mobileLabel);
            Label label6= new Label(6,1,"次数",fontFormat2);
            sheet.addCell(label6);
            Label label7 = new Label(7,1,"时长",fontFormat2);
            sheet.addCell(label7);
            Label elseLabel = new Label(8,1,"其他流量(字节)",fontFormat2);
            sheet.addCell(elseLabel);
            Label label8= new Label(9,1,"次数",fontFormat2);
            sheet.addCell(label8);
            Label label9 = new Label(10,1,"时长",fontFormat2);
            sheet.addCell(label9);
            Label label10 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label10);
            Label label11 = new Label(12,1,"次数",fontFormat2);
            sheet.addCell(label11);
            Label label12 = new Label(13,1,"时长",fontFormat2);
            sheet.addCell(label12);
            Label label13 = new Label(14,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);
            Label label14 = new Label(15,1,"次数",fontFormat2);
            sheet.addCell(label14);
            Label label15 = new Label(16,1,"时长",fontFormat2);
            sheet.addCell(label15);
            Label label16 = new Label(17,1,"直播总流量(字节)",fontFormat2);
            sheet.addCell(label16);
            Label label17 = new Label(18,1,"次数",fontFormat2);
            sheet.addCell(label17);
            Label label18 = new Label(19,1,"时长",fontFormat2);
            sheet.addCell(label18);
            Label label19 = new Label(20,1,"Pad直播总流量(字节)",fontFormat2);
            sheet.addCell(label19);
            Label label20 = new Label(21,1,"次数",fontFormat2);
            sheet.addCell(label20);
            Label label21 = new Label(22,1,"时长",fontFormat2);
            sheet.addCell(label21);
            Label label22 = new Label(23,1,"Phone直播总流量(字节)",fontFormat2);
            sheet.addCell(label22);
            Label label23 = new Label(24,1,"次数",fontFormat2);
            sheet.addCell(label23);
            Label label24 = new Label(25,1,"时长",fontFormat2);
            sheet.addCell(label24);
            Label label25 = new Label(26,1,"点播总流量(字节)",fontFormat2);
            sheet.addCell(label25);
            Label label26 = new Label(27,1,"次数",fontFormat2);
            sheet.addCell(label26);
            Label label27 = new Label(28,1,"时长",fontFormat2);
            sheet.addCell(label27);
            Label label28 = new Label(29,1,"Pad点播总流量(字节)",fontFormat2);
            sheet.addCell(label28);
            Label label29 = new Label(30,1,"次数",fontFormat2);
            sheet.addCell(label29);
            Label label30 = new Label(31,1,"时长",fontFormat2);
            sheet.addCell(label30);
            Label label31 = new Label(32,1,"Phone点播总流量(字节)",fontFormat2);
            sheet.addCell(label31);
            Label label32 = new Label(33,1,"次数",fontFormat2);
            sheet.addCell(label32);
            Label label33 = new Label(34,1,"时长",fontFormat2);
            sheet.addCell(label33);
            Label label34 = new Label(35,1,"华数点播流量(字节)",fontFormat2);
            sheet.addCell(label34);
            Label label35 = new Label(36,1,"次数",fontFormat2);
            sheet.addCell(label35);
            Label label36 = new Label(37,1,"时长",fontFormat2);
            sheet.addCell(label36);
            Label label37 = new Label(38,1,"华数拉动流量(字节)",fontFormat2);
            sheet.addCell(label37);
            Label label38 = new Label(39,1,"优朋点播流量(字节)",fontFormat2);
            sheet.addCell(label38);
            Label label39 = new Label(40,1,"次数",fontFormat2);
            sheet.addCell(label39);
            Label label40 = new Label(41,1,"时长",fontFormat2);
            sheet.addCell(label40);
            Label label41 = new Label(42,1,"优朋拉动流量(字节)",fontFormat2);
            sheet.addCell(label41);
            Label label42 = new Label(43,1,"百视通点播流量(字节)",fontFormat2);
            sheet.addCell(label42);
            Label label43 = new Label(44,1,"次数",fontFormat2);
            sheet.addCell(label43);
            Label label44 = new Label(45,1,"时长",fontFormat2);
            sheet.addCell(label44);
            Label label45 = new Label(46,1,"百视拉动播流量(字节)",fontFormat2);
            sheet.addCell(label45);
            Label label46 = new Label(47,1,"播放用户数量(个)",fontFormat2);
            sheet.addCell(label46);
            Label label47 = new Label(48,1,"播放用户流量(字节)",fontFormat2);
            sheet.addCell(label47);
            int i = 2;
            if(list.size()>0){
                for(Object[] o:list){
                    Label labelDate = new Label(1,i,o[0].toString(),fontFormat3);
                    sheet.addCell(labelDate);
                    jxl.write.Number labelAllNetFlow = new jxl.write.Number(2,i,Long.parseLong(o[1].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlow);
                    jxl.write.Number labelAllLength = new jxl.write.Number(4,i,Long.parseLong(o[20].toString()),numberFormat1);
                    sheet.addCell(labelAllLength);
                    jxl.write.Number labelAllCount = new jxl.write.Number(3,i,Long.parseLong(o[21].toString()),numberFormat1);
                    sheet.addCell(labelAllCount);


                    jxl.write.Number labelMobileNetFlow = new jxl.write.Number(5,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelMobileNetFlow);
                    jxl.write.Number labelMobileLength = new jxl.write.Number(7,i,Long.parseLong(o[22].toString()),numberFormat1);
                    sheet.addCell(labelMobileLength);
                    jxl.write.Number labelMobileCount = new jxl.write.Number(6,i,Long.parseLong(o[23].toString()),numberFormat1);
                    sheet.addCell(labelMobileCount);



                    jxl.write.Number labelAllNetFlowPad = new jxl.write.Number(11,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowPad);
                    jxl.write.Number labelPadLength = new jxl.write.Number(13,i,Long.parseLong(o[24].toString()),numberFormat1);
                    sheet.addCell(labelPadLength);
                    jxl.write.Number labelPadCount = new jxl.write.Number(12,i,Long.parseLong(o[25].toString()),numberFormat1);
                    sheet.addCell(labelPadCount);


                    jxl.write.Number labelAllNetFlowPhone = new jxl.write.Number(14,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowPhone);
                    jxl.write.Number labelPhoneLength = new jxl.write.Number(16,i,Long.parseLong(o[26].toString()),numberFormat1);
                    sheet.addCell(labelPhoneLength);
                    jxl.write.Number labelPhoneCount = new jxl.write.Number(15,i,Long.parseLong(o[27].toString()),numberFormat1);
                    sheet.addCell(labelPhoneCount);


                    jxl.write.Number labelAllNetFlowLive = new jxl.write.Number(17,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowLive);
                    jxl.write.Number labelLiveLength = new jxl.write.Number(19,i,Long.parseLong(o[28].toString()),numberFormat1);
                    sheet.addCell(labelLiveLength);
                    jxl.write.Number labelLiveCount = new jxl.write.Number(18,i,Long.parseLong(o[29].toString()),numberFormat1);
                    sheet.addCell(labelLiveCount);


                    jxl.write.Number labelAllNetFlowLivePad = new jxl.write.Number(20,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowLivePad);
                    jxl.write.Number labelLivePadLength = new jxl.write.Number(22,i,Long.parseLong(o[30].toString()),numberFormat1);
                    sheet.addCell(labelLivePadLength);
                    jxl.write.Number labelLivePadCount = new jxl.write.Number(21,i,Long.parseLong(o[31].toString()),numberFormat1);
                    sheet.addCell(labelLivePadCount);



                    jxl.write.Number labelAllNetFlowLivePhone = new jxl.write.Number(23,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowLivePhone);
                    jxl.write.Number labelLivePhoneLength = new jxl.write.Number(25,i,Long.parseLong(o[32].toString()),numberFormat1);
                    sheet.addCell(labelLivePhoneLength);
                    jxl.write.Number labelLivePhoneCount = new jxl.write.Number(24,i,Long.parseLong(o[33].toString()),numberFormat1);
                    sheet.addCell(labelLivePhoneCount);



                    jxl.write.Number labelAllNetFlowContent = new jxl.write.Number(26,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowContent);
                    jxl.write.Number labelContentLength = new jxl.write.Number(28,i,Long.parseLong(o[34].toString()),numberFormat1);
                    sheet.addCell(labelContentLength);
                    jxl.write.Number labelContentCount = new jxl.write.Number(27,i,Long.parseLong(o[35].toString()),numberFormat1);
                    sheet.addCell(labelContentCount);


                    jxl.write.Number labelAllNetFlowContentPad = new jxl.write.Number(29,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowContentPad);
                    jxl.write.Number labelContentPadLength = new jxl.write.Number(31,i,Long.parseLong(o[36].toString()),numberFormat1);
                    sheet.addCell(labelContentPadLength);
                    jxl.write.Number labelContentPadCount = new jxl.write.Number(30,i,Long.parseLong(o[37].toString()),numberFormat1);
                    sheet.addCell(labelContentPadCount);


                    jxl.write.Number labelAllNetFlowContentPhone = new jxl.write.Number(32,i,Long.parseLong(o[11].toString()),numberFormat1);
                    sheet.addCell(labelAllNetFlowContentPhone);
                    jxl.write.Number labelContentPhoneLength = new jxl.write.Number(34,i,Long.parseLong(o[38].toString()),numberFormat1);
                    sheet.addCell(labelContentPhoneLength);
                    jxl.write.Number labelContentPhoneCount = new jxl.write.Number(33,i,Long.parseLong(o[39].toString()),numberFormat1);
                    sheet.addCell(labelContentPhoneCount);


                    jxl.write.Number labelWasuNetFlow = new jxl.write.Number(35,i,Long.parseLong(o[12].toString()),numberFormat1);
                    sheet.addCell(labelWasuNetFlow);
                    jxl.write.Number labelWasuLength = new jxl.write.Number(37,i,Long.parseLong(o[42].toString()),numberFormat1);
                    sheet.addCell(labelWasuLength);
                    jxl.write.Number labelWasuCount = new jxl.write.Number(36,i,Long.parseLong(o[43].toString()),numberFormat1);
                    sheet.addCell(labelWasuCount);


                    jxl.write.Number labelWasuLadongNetFlow = new jxl.write.Number(38,i,Long.parseLong(o[13].toString()),numberFormat1);
                    sheet.addCell(labelWasuLadongNetFlow);

                    jxl.write.Number labelVooleNetFlow = new jxl.write.Number(39,i,Long.parseLong(o[14].toString()),numberFormat1);
                    sheet.addCell(labelVooleNetFlow);
                    jxl.write.Number labelVooleLength = new jxl.write.Number(41,i,Long.parseLong(o[45].toString()),numberFormat1);
                    sheet.addCell(labelVooleLength);
                    jxl.write.Number labelVooleCount = new jxl.write.Number(40,i,Long.parseLong(o[44].toString()),numberFormat1);
                    sheet.addCell(labelVooleCount);


                    jxl.write.Number labelVooleLadongNetFlow = new jxl.write.Number(42,i,Long.parseLong(o[15].toString()),numberFormat1);
                    sheet.addCell(labelVooleLadongNetFlow);

                    jxl.write.Number labelBestvNetFlow = new jxl.write.Number(43,i,Long.parseLong(o[16].toString()),numberFormat1);
                    sheet.addCell(labelBestvNetFlow);
                    jxl.write.Number labelBestvLength = new jxl.write.Number(45,i,Long.parseLong(o[40].toString()),numberFormat1);
                    sheet.addCell(labelBestvLength);
                    jxl.write.Number labelBestvCount = new jxl.write.Number(44,i,Long.parseLong(o[41].toString()),numberFormat1);
                    sheet.addCell(labelBestvCount);


                    jxl.write.Number labelBestvLadongNetFlow = new jxl.write.Number(46,i,Long.parseLong(o[17].toString()),numberFormat1);
                    sheet.addCell(labelBestvLadongNetFlow);

                    jxl.write.Number labelOnlineUser = new jxl.write.Number(47,i,Long.parseLong(o[18].toString()),numberFormat1);
                    sheet.addCell(labelOnlineUser);
                    jxl.write.Number labelOnlineUserNetFlow = new jxl.write.Number(48,i,Long.parseLong(o[19].toString()),numberFormat1);
                    sheet.addCell(labelOnlineUserNetFlow);


                    jxl.write.Number labelElseNetFlow = new jxl.write.Number(8,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelElseNetFlow);
                    jxl.write.Number labelElseCount = new jxl.write.Number(9,i,Long.parseLong(o[47].toString()),numberFormat1);
                    sheet.addCell(labelElseCount);
                    jxl.write.Number labelElseLength = new jxl.write.Number(10,i,Long.parseLong(o[46].toString()),numberFormat1);
                    sheet.addCell(labelElseLength);
                    i++;
                }
            }
        }
            if(("demandCountLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("直播趋势统计",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,21);
                sheet.setColumnView(2,21);
                sheet.setColumnView(3,21);
                sheet.setColumnView(4,20);
                sheet.setColumnView(5,20);
                sheet.setColumnView(6,20);
                sheet.setColumnView(7,20);
                sheet.setColumnView(8,20);
                sheet.setColumnView(9,20);
                sheet.setColumnView(10,20);
                sheet.setColumnView(11,20);
                sheet.setColumnView(12,20);
                sheet.setColumnView(13,20);
                sheet.setColumnView(14,20);
                sheet.setColumnView(15,20);
                sheet.setColumnView(16,20);
                sheet.setColumnView(17,20);
                sheet.setColumnView(18,20);
                sheet.setColumnView(19,20);
                sheet.setColumnView(20,20);
                sheet.setColumnView(21,20);
                sheet.setColumnView(22,20);
                sheet.setColumnView(23,20);
                sheet.setColumnView(24,20);
                sheet.setColumnView(25,20);
                sheet.mergeCells(1,0,20,0);

                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"日期",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"00:00",fontFormat2);
                sheet.addCell(label3);
                Label mobileLabel = new Label(3,1,"01:00",fontFormat2);
                sheet.addCell(mobileLabel);
                Label elseLabel = new Label(4,1,"02：:00",fontFormat2);
                sheet.addCell(elseLabel);
                Label label4 = new Label(5,1,"03:00",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(6,1,"04:00",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(7,1,"05:00",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(8,1,"06：:00",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(9,1,"07：:00",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(10,1,"08：:00",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(11,1,"09:00",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(12,1,"10:00",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(13,1,"11：00",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(14,1,"12:00",fontFormat2);
                sheet.addCell(label13);
                Label label14 = new Label(15,1,"13:00",fontFormat2);
                sheet.addCell(label14);
                Label label15 = new Label(16,1,"14：:0",fontFormat2);
                sheet.addCell(label15);
                Label label16 = new Label(17,1,"15:00",fontFormat2);
                sheet.addCell(label16);
                Label label17 = new Label(18,1,"16:00",fontFormat2);
                sheet.addCell(label17);
                Label label18 = new Label(19,1,"17：00",fontFormat2);
                sheet.addCell(label18);
                Label label19 = new Label(20,1,"18:00",fontFormat2);
                sheet.addCell(label19);
                Label label20 = new Label(21,1,"19:00",fontFormat2);
                sheet.addCell(label20);
                Label label21 = new Label(22,1,"20:00",fontFormat2);
                sheet.addCell(label21);
                Label label22 = new Label(23,1,"21:00",fontFormat2);
                sheet.addCell(label22);
                Label label23= new Label(24,1,"22:00",fontFormat2);
                sheet.addCell(label23);
                Label label24= new Label(25,1,"23:00",fontFormat2);
                sheet.addCell(label24);
                Label label25= new Label(26,1,"按天汇总（播放次数）",fontFormat2);
                sheet.addCell(label25);

                int i = 2;
                if(list.size()>0){
                    for(Object[] o:list){
                        Label labelDate = new Label(1,i,o[0].toString(),fontFormat3);
                        sheet.addCell(labelDate);
                        jxl.write.Number labelTime_00 = new jxl.write.Number(2,i,Long.parseLong(o[1].toString()),numberFormat1);
                        sheet.addCell(labelTime_00);
                        jxl.write.Number labelTime_01 = new jxl.write.Number(3,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelTime_01);
                        jxl.write.Number labelTime_02 = new jxl.write.Number(4,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelTime_02);
                        jxl.write.Number labelTime_03 = new jxl.write.Number(5,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelTime_03);
                        jxl.write.Number labelTime_04 = new jxl.write.Number(6,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelTime_04);
                        jxl.write.Number labelTime_05 = new jxl.write.Number(7,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelTime_05);
                        jxl.write.Number labelTime_06 = new jxl.write.Number(8,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelTime_06);
                        jxl.write.Number labelTime_07 = new jxl.write.Number(9,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelTime_07);
                        jxl.write.Number labelTime_08 = new jxl.write.Number(10,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelTime_08);
                        jxl.write.Number labelTime_09 = new jxl.write.Number(11,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelTime_09);
                        jxl.write.Number labelTime_10 = new jxl.write.Number(12,i,Long.parseLong(o[11].toString()),numberFormat1);
                        sheet.addCell(labelTime_10);
                        jxl.write.Number labelTime_11 = new jxl.write.Number(13,i,Long.parseLong(o[12].toString()),numberFormat1);
                        sheet.addCell(labelTime_11);
                        jxl.write.Number labelTime_12 = new jxl.write.Number(14,i,Long.parseLong(o[13].toString()),numberFormat1);
                        sheet.addCell(labelTime_12);
                        jxl.write.Number labelTime_13 = new jxl.write.Number(15,i,Long.parseLong(o[14].toString()),numberFormat1);
                        sheet.addCell(labelTime_13);
                        jxl.write.Number labelTime_14 = new jxl.write.Number(16,i,Long.parseLong(o[15].toString()),numberFormat1);
                        sheet.addCell(labelTime_14);
                        jxl.write.Number labelTime_15 = new jxl.write.Number(17,i,Long.parseLong(o[16].toString()),numberFormat1);
                        sheet.addCell(labelTime_15);
                        jxl.write.Number labelTime_16 = new jxl.write.Number(18,i,Long.parseLong(o[17].toString()),numberFormat1);
                        sheet.addCell(labelTime_16);
                        jxl.write.Number labelTime_17 = new jxl.write.Number(19,i,Long.parseLong(o[18].toString()),numberFormat1);
                        sheet.addCell(labelTime_17);
                        jxl.write.Number labelTime_18 = new jxl.write.Number(20,i,Long.parseLong(o[19].toString()),numberFormat1);
                        sheet.addCell(labelTime_18);
                        jxl.write.Number labelTime_19 = new jxl.write.Number(21,i,Long.parseLong(o[20].toString()),numberFormat1);
                        sheet.addCell(labelTime_19);
                        jxl.write.Number labelTime_20 = new jxl.write.Number(22,i,Long.parseLong(o[21].toString()),numberFormat1);
                        sheet.addCell(labelTime_20);
                        jxl.write.Number labelTime_21 = new jxl.write.Number(23,i,Long.parseLong(o[22].toString()),numberFormat1);
                        sheet.addCell(labelTime_21);
                        jxl.write.Number labelTime_22 = new jxl.write.Number(24,i,Long.parseLong(o[23].toString()),numberFormat1);
                        sheet.addCell(labelTime_22);
                        jxl.write.Number labelTime_23 = new jxl.write.Number(25,i,Long.parseLong(o[24].toString()),numberFormat1);
                        sheet.addCell(labelTime_23);
                        i++;
                    }
                }
            }
            if(("systemPlayedLog").equals(excelName)){
                WritableSheet sheet = workbook.createSheet("有媒体播放统计",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,5);
                sheet.setColumnView(2,17);
                sheet.setColumnView(3,35);
                sheet.setColumnView(4,10);
                sheet.setColumnView(5,17);
                sheet.setColumnView(6,17);
                sheet.setColumnView(7,17);
                sheet.setColumnView(8,17);
                sheet.setColumnView(9,17);
                sheet.setColumnView(10,17);
                sheet.setColumnView(11,17);
                sheet.setColumnView(12,17);
                sheet.mergeCells(1,0,12,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"SP名称",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"资源名称",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"点播次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"Pad点播次数",fontFormat2);
                sheet.addCell(label6);
                Label label7 = new Label(6,1,"Phone点播次数",fontFormat2);
                sheet.addCell(label7);
                Label label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
                sheet.addCell(label8);
                Label label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
                sheet.addCell(label9);
                Label label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
                sheet.addCell(label10);
                Label label11 = new Label(10,1,"总流量(字节)",fontFormat2);
                sheet.addCell(label11);
                Label label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
                sheet.addCell(label12);
                Label label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
                sheet.addCell(label13);
                List<Content>  contentList =  contentLogicInterface.getAll();
                List<Csp> cspList = cspLogicInterface.getAll();
                if(list.size()>0){
                    int i = 2;
                    for(Object[] o:list) {
                        long cspId = Long.parseLong(o[0].toString());
                        for(Csp csp:cspList){
                            if(cspId==csp.getId()){
                                jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                                sheet.addCell(labelGrade);
                                Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                                sheet.addCell(cspName);
                            }
                        }
                        for(Content content:contentList){
                            long contentId = Long.parseLong(o[1].toString());
                            if(contentId == content.getId()){
                                Label contentName = new Label(3,i,content.getName(),fontFormat3);
                                sheet.addCell(contentName);
                            }
                        }
                        jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelCountPad);
                        jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCountPhone);
                        jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelLengthPad);
                        jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelLengthPhone);
                        jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPad);
                        jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelBytesSendPhone);
                        i++;
                    }
                }
            }
            if(("searchHotLog").equals(excelName)){

                WritableSheet sheet = workbook.createSheet("用户热词搜索",0);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,21);
                sheet.setColumnView(2,21);
                sheet.setColumnView(3,21);
                sheet.setColumnView(4,20);
                sheet.mergeCells(1,0,20,0);
                Label label0 = new Label(1,0,startTime2+"-"+endTime2,fontFormat1);
                sheet.addCell(label0);
                Label label2 = new Label(1,1,"内容",fontFormat2);
                sheet.addCell(label2);
                Label label3 = new Label(2,1,"周搜素次数",fontFormat2);
                sheet.addCell(label3);
                Label label4 = new Label(3,1,"月搜索次数",fontFormat2);
                sheet.addCell(label4);
                Label label5 = new Label(4,1,"总搜索次数",fontFormat2);
                sheet.addCell(label5);
                Label label6 = new Label(5,1,"统计时间",fontFormat2);
                sheet.addCell(label6);
                int i = 2;
                if(list.size()>0){
                    for(Object[] o:list){
                        jxl.write.Number labelContent = new jxl.write.Number(1,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelContent);
                        jxl.write.Number labelSearchWeekCount = new jxl.write.Number(3,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelSearchWeekCount);
                        jxl.write.Number labelSearchMonthCount = new jxl.write.Number(4,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelSearchMonthCount);

                        jxl.write.Number labelSeachCount = new jxl.write.Number(5,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelSeachCount);
                        jxl.write.Number labelCreateTime = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelCreateTime);
                        i++;
                    }
                }
            }
        workbook.write();
        workbook.close();
        this.downLoadFile(fileName);
        return "success";
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
    }
    public String createExcelFile(List basicInfoList, List<Object[]> areaLogList, List<Object[]> spLogList, List<Object[]> contentLogList, List<Object[]> contentLiveLogList, List<Object[]> contentLadongLogList, List<Object[]> bingfaList, List<Object[]> netFlowList, List<Object[]> channelLogList, List<Object[]> contentByLengthLogList, List<Object[]> contentByNetFlowList, List<Object[]> userLoginLogList, List<Object[]> contentFilmLogList, List<Object[]> contentTvLogList, List<Map<String,String>> countLogList,List<Object[]> systemPlayedLogList,List<Object[]> searchHotLogList){
        String startTime,endTime,startDate2 = "", endDate2 = "",startDate1 = "",endDate1 = "";
        if(basicInfoList.size()>0){
            startTime = basicInfoList.get(1).toString();
            startDate1 = startTime.replace("-", ".");
            startDate2 = startTime.replace("-", "");
            startDate2 = startDate2.substring(4,startDate2.length());
            endTime = basicInfoList.get(2).toString();
            endDate1 = endTime.replace("-",".");
            endDate2 = endTime.replace("-", "");
            endDate2 = endDate2.substring(4,endDate2.length());
        }
        //开始创建xls文件。
        String fileName =  "D:\\统计数据"+startDate2+"-"+endDate2+".xls";
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            //字体
            //字体1：居中，14，黑体，有边框
            WritableFont  wr = new WritableFont(WritableFont.TIMES,14,WritableFont.BOLD);
            WritableCellFormat fontFormat1 = new WritableCellFormat(wr);
            fontFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体2：居中，12，有边框
            WritableFont  wr1 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat2 = new WritableCellFormat(wr1);
            fontFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            fontFormat2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体3：左对齐，12，有边框
            WritableFont  wr2 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat3 = new WritableCellFormat(wr2);
            fontFormat3.setAlignment(jxl.format.Alignment.LEFT);
            fontFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //字体4：右对齐，12，有边框
            WritableFont  wr3 = new WritableFont(WritableFont.TIMES,11);
            WritableCellFormat fontFormat4 = new WritableCellFormat(wr3);
            fontFormat4.setAlignment(Alignment.RIGHT);
            fontFormat4.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            //时间
            jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");
            WritableCellFormat dateFormat = new WritableCellFormat(df);
            dateFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

            //数字
            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat = new WritableCellFormat(nf);
            numberFormat.setAlignment(jxl.format.Alignment.CENTRE);
            numberFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

            jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("###,###,###,###,###");
            WritableCellFormat numberFormat1 = new WritableCellFormat(nf1);
            numberFormat1.setAlignment(Alignment.RIGHT);
            numberFormat1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
            //建立区域点播量sheet
            WritableSheet sheet = workbook.createSheet("区域播放量",0);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,14);
            sheet.setColumnView(2,14);
            sheet.setColumnView(3,14);
            sheet.setColumnView(4,12);
            sheet.setColumnView(5,15);
            sheet.setColumnView(6,15);
            sheet.setColumnView(7,15);
            sheet.setColumnView(8,15);
            sheet.setColumnView(9,7);
            sheet.setColumnView(10,14);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.setColumnView(13,7);
            sheet.setColumnView(14,17);
            sheet.setColumnView(15,17);
            sheet.setColumnView(16,17);
            sheet.setColumnView(17,17);
            sheet.setColumnView(18,17);
            sheet.setColumnView(19,7);
            sheet.setColumnView(20,7);
            Label label0;
            sheet.mergeCells(1,0,20,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            Label label2 = new Label(1,1,"区域名称",fontFormat2);
            sheet.addCell(label2);
            Label label3 = new Label(2,1,"播放总次数",fontFormat2);
            sheet.addCell(label3);
            Label label4 = new Label(3,1,"移动播放次数",fontFormat2);
            sheet.addCell(label4);
            Label label5 = new Label(4,1,"其他播放次数",fontFormat2);
            sheet.addCell(label5);
            Label label6 = new Label(5,1,"Pad播放次数",fontFormat2);
            sheet.addCell(label6);
            Label label7 = new Label(6,1,"Phone播放次数",fontFormat2);
            sheet.addCell(label7);
            Label label8 = new Label(7,1,"比重",fontFormat2);
            sheet.addCell(label8);
            Label label9 = new Label(8,1,"播放时长(秒)",fontFormat2);
            sheet.addCell(label9);
            Label label10 = new Label(9,1,"移动播放时长(秒)",fontFormat2);
            sheet.addCell(label10);
            Label label11 = new Label(10,1,"其他播放时长(秒)",fontFormat2);
            sheet.addCell(label11);
            Label label12 = new Label(11,1,"Pad播放时长(秒)",fontFormat2);
            sheet.addCell(label12);
            Label label13 = new Label(12,1,"Phone播放时长(秒)",fontFormat2);
            sheet.addCell(label13);
            Label label14 = new Label(13,1,"比重",fontFormat2);
            sheet.addCell(label14);
            Label label15 = new Label(14,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label15);
            Label label16 = new Label(15,1,"移动流量(字节)",fontFormat2);
            sheet.addCell(label16);
            Label label17 = new Label(16,1,"其他流量(字节)",fontFormat2);
            sheet.addCell(label17);
            Label label18= new Label(17,1,"Pad流量(字节)",fontFormat2);
            sheet.addCell(label18);
            Label label19 = new Label(18,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label19);
            Label label120 = new Label(19,1,"比重",fontFormat2);
            sheet.addCell(label120);
            Label label121 = new Label(20,1,"用户点播数",fontFormat2);
            sheet.addCell(label121);
            List<Area> arealist = areaLogicInterface.getAll();
            Double count = 0.0 ;
            Double length = 0.0;
            Long bytesSend = 0L;
            for(Object[] objects:areaLogList) {
                count += Integer.parseInt(objects[2].toString());
                length += Integer.parseInt(objects[5].toString());
                bytesSend += Long.parseLong(objects[8].toString());
            }
            if(arealist.size()>0){
                int i = 2;
                for(Object[] o:areaLogList) {
                    long areaId = Long.parseLong(o[1].toString());
                    if("Area".equals(o[0].toString())){
                        for(Area area :arealist){
                            if(areaId==area.getId()){
                                Label areaName = new Label(1,i,area.getName(),fontFormat3);
                                sheet.addCell(areaName);
                            }
                        }
                    }
                    if("pullArea".equals(o[0].toString())){
                        for(Area area:arealist){
                            if(areaId==area.getId()){
                                Label areaName = new Label(1,i,area.getName()+"--拉动",fontFormat3);
                                sheet.addCell(areaName);
                            }
                        }
                    }
                    if("-1".equals(o[1].toString())){
                                Label areaName = new Label(1,i,"其他",fontFormat3);
                                sheet.addCell(areaName);
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelMobileCount = new jxl.write.Number(3,i,Double.valueOf(o[13].toString()),numberFormat1);
                    sheet.addCell(labelMobileCount);
                    jxl.write.Number labelElseCount = new jxl.write.Number(4,i,Double.valueOf(o[14].toString()),numberFormat1);
                    sheet.addCell(labelElseCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Double.valueOf(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Double.valueOf(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[2].toString())/count*1000)/10)+"%";
                    Label labelCountBiZhong = new Label(7,i,countBiZhong,fontFormat4);
                    sheet.addCell(labelCountBiZhong);
                    jxl.write.Number labelLength = new jxl.write.Number(8,i,Double.valueOf(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelMobileLength = new jxl.write.Number(9,i,Double.valueOf(o[15].toString()),numberFormat1);
                    sheet.addCell(labelMobileLength);
                    jxl.write.Number labelElseLength = new jxl.write.Number(10,i,Double.valueOf(o[16].toString()),numberFormat1);
                    sheet.addCell(labelElseLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(11,i,Double.valueOf(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(12,i,Double.valueOf(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[5].toString())/length*1000)/10)+"%";
                    Label labelLengthBiZhong = new Label(13,i,lengthBiZhong,fontFormat4);
                    sheet.addCell(labelLengthBiZhong);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(14,i,Double.valueOf(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelMobileBytesSend = new jxl.write.Number(15,i,Double.valueOf(o[11].toString()),numberFormat1);
                    sheet.addCell(labelMobileBytesSend);
                    jxl.write.Number labelElseBytesSend = new jxl.write.Number(16,i,Double.valueOf(o[12].toString()),numberFormat1);
                    sheet.addCell(labelElseBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(17,i,Double.valueOf(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(18,i,Double.valueOf(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[8].toString())+0.0)/bytesSend*1000)/10)+"%";
                    Label labelBytesSendBiZhong = new Label(19,i,bytesSendBiZhong,fontFormat4);
                    sheet.addCell(labelBytesSendBiZhong);
                    jxl.write.Number labelUserOnLineCount = new jxl.write.Number(20,i,Double.valueOf(o[17].toString()),numberFormat1);
                    sheet.addCell(labelUserOnLineCount);
                    i++;
                }
            }

            //建立SP点播量sheet
            sheet = workbook.createSheet("SP播放量",1);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,14);
            sheet.setColumnView(2,12);
            sheet.setColumnView(3,15);
            sheet.setColumnView(4,15);
            sheet.setColumnView(5,7);
            sheet.setColumnView(6,14);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,7);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.setColumnView(13,7);
            sheet.mergeCells(1,0,13,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"SP名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"播放总次数",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"Pad播放次数",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"Phone播放次数",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"比重",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"播放时长(秒)",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"Pad播放时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Phone播放时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"比重",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone流量(字节)",fontFormat2);
            sheet.addCell(label13);
            label14 = new Label(13,1,"比重",fontFormat2);
            sheet.addCell(label14);
            List<Csp> cspList = cspLogicInterface.getAll();
            count = 0.0 ;
            length = 0.0;
            bytesSend = 0L;
            for(Object[] objects:spLogList) {
                count += Integer.parseInt(objects[1].toString());
                length += Integer.parseInt(objects[4].toString());
                bytesSend += Long.parseLong(objects[7].toString());
            }
            if(spLogList.size()>0){
                int i = 2;
                for(Object[] o:spLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            Label cspName = new Label(1,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[1].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(3,i,Double.valueOf(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[1].toString())/count*1000)/10)+"%";
                    Label labelCountBiZhong = new Label(5,i,countBiZhong,fontFormat4);
                    sheet.addCell(labelCountBiZhong);
                    jxl.write.Number labelLength = new jxl.write.Number(6,i,Double.valueOf(o[4].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(7,i,Double.valueOf(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(8,i,Double.valueOf(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[4].toString())/length*1000)/10)+"%";
                    Label labelLengthBiZhong = new Label(9,i,lengthBiZhong,fontFormat4);
                    sheet.addCell(labelLengthBiZhong);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Double.valueOf(o[7].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Double.valueOf(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Double.valueOf(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[7].toString())+0.0)/bytesSend*1000)/10)+"%";
                    Label bytesSendLengthBiZhong = new Label(13,i,bytesSendBiZhong,fontFormat4);
                    sheet.addCell(bytesSendLengthBiZhong);
                    i++;
                }
            }

            //频道点播量
            sheet = workbook.createSheet("频道点播量",2);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,20);
            sheet.setColumnView(2,12);
            sheet.setColumnView(3,7);
            sheet.setColumnView(4,15);
            sheet.setColumnView(5,7);
            sheet.setColumnView(6,16);
            sheet.setColumnView(7,7);
            sheet.mergeCells(1,0,7,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"频道名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"点播次数",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"比重",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"点播时长",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"比重",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"流量(字节)",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"比重",fontFormat2);
            sheet.addCell(label8);
            count = 0.0 ;
            length = 0.0;
            bytesSend = 0L;
            for(Object[] objects:channelLogList) {
                if(objects[4].toString().equals("3")){
                    count += Integer.parseInt(objects[2].toString());
                    length += Integer.parseInt(objects[3].toString());
                    bytesSend += Long.parseLong(objects[5].toString());
                }
            }
            if(channelLogList.size()>0){
                int i = 2;
                for(Object[] o:channelLogList) {
                    Label cspName = new Label(1,i,o[1].toString(),fontFormat3);
                    sheet.addCell(cspName);
                    jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    if(Double.valueOf(o[2].toString())!=0){
                        String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[2].toString())/count*1000)/10)+"%";
                        Label labelCountBiZhong = new Label(3,i,countBiZhong,fontFormat4);
                        sheet.addCell(labelCountBiZhong);
                    }else{
                        jxl.write.Number labelCountBiZhong = new jxl.write.Number(3,i,0,numberFormat1);
                        sheet.addCell(labelCountBiZhong);
                    }
                    jxl.write.Number labelLength = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    if(Double.valueOf(o[3].toString())!=0){
                        String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[3].toString())/length*1000)/10)+"%";
                        Label labelLengthBiZhong = new Label(5,i,lengthBiZhong,fontFormat4);
                        sheet.addCell(labelLengthBiZhong);
                    }else{
                        jxl.write.Number bytesSendLengthBiZhong = new  jxl.write.Number(5,i,0,numberFormat1);
                        sheet.addCell(bytesSendLengthBiZhong);
                    }
                    jxl.write.Number labelBytesSend = new jxl.write.Number(6,i,Double.valueOf(o[5].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    if(Double.valueOf(o[5].toString())!=0){
                        String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[5].toString())+0.0)/bytesSend*1000)/10)+"%";
                        Label bytesSendLengthBiZhong = new Label(7,i,bytesSendBiZhong,fontFormat4);
                        sheet.addCell(bytesSendLengthBiZhong);
                    }else{
                        jxl.write.Number bytesSendLengthBiZhong = new jxl.write.Number(7,i,0,numberFormat1);
                        sheet.addCell(bytesSendLengthBiZhong);
                    }
                    i++;
                }
            }
            //频道点播量2
            sheet = workbook.createSheet("一级频道点播量",3);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,20);
            sheet.setColumnView(2,12);
            sheet.setColumnView(3,7);
            sheet.setColumnView(4,15);
            sheet.setColumnView(5,7);
            sheet.setColumnView(6,16);
            sheet.setColumnView(7,7);
            sheet.mergeCells(1,0,7,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"频道名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"点播次数",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"比重",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"点播时长",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"比重",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"流量(字节)",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"比重",fontFormat2);
            sheet.addCell(label8);
            count = 0.0 ;
            length = 0.0;
            bytesSend = 0L;
            for(Object[] objects:channelLogList) {
                if(objects[4].toString().equals("3")){
                    count += Integer.parseInt(objects[2].toString());
                    length += Integer.parseInt(objects[3].toString());
                    bytesSend += Long.parseLong(objects[5].toString());
                }
            }
            if(channelLogList.size()>0){
                int i = 2;
                for(Object[] o:channelLogList) {
                    if(o[4].toString().equals("2")&&!o[0].toString().equals("15884628")&&!o[0].toString().equals("32057870")&&!o[0].toString().equals("374606212")&&!o[0].toString().equals("115390110")&&!o[0].toString().equals("15884508")){
                        Label cspName = new Label(1,i,o[1].toString(),fontFormat3);
                        sheet.addCell(cspName);
                        jxl.write.Number labelCount = new jxl.write.Number(2,i,Double.valueOf(o[2].toString()),numberFormat1);
                        sheet.addCell(labelCount);
                        if(Double.valueOf(o[2].toString())!=0){
                            String countBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[2].toString())/count*1000)/10)+"%";
                            Label labelCountBiZhong = new Label(3,i,countBiZhong,fontFormat4);
                            sheet.addCell(labelCountBiZhong);
                        }else{
                            jxl.write.Number labelCountBiZhong = new jxl.write.Number(3,i,0,numberFormat1);
                            sheet.addCell(labelCountBiZhong);
                        }
                        jxl.write.Number labelLength = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                        sheet.addCell(labelLength);
                        if(Double.valueOf(o[3].toString())!=0){
                            String lengthBiZhong = Float.toString((float)Math.round(Integer.parseInt(o[3].toString())/length*1000)/10)+"%";
                            Label labelLengthBiZhong = new Label(5,i,lengthBiZhong,fontFormat4);
                            sheet.addCell(labelLengthBiZhong);
                        }else{
                            jxl.write.Number bytesSendLengthBiZhong = new  jxl.write.Number(5,i,0,numberFormat1);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }
                        jxl.write.Number labelBytesSend = new jxl.write.Number(6,i,Double.valueOf(o[5].toString()),numberFormat1);
                        sheet.addCell(labelBytesSend);
                        if(Double.valueOf(o[5].toString())!=0){
                            String bytesSendBiZhong = Float.toString((float)Math.round((Long.parseLong(o[5].toString())+0.0)/bytesSend*1000)/10)+"%";
                            Label bytesSendLengthBiZhong = new Label(7,i,bytesSendBiZhong,fontFormat4);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }else{
                            jxl.write.Number bytesSendLengthBiZhong = new jxl.write.Number(7,i,0,numberFormat1);
                            sheet.addCell(bytesSendLengthBiZhong);
                        }
                        i++;
                    }
                   }
            }

            //建立媒体点播量
            sheet = workbook.createSheet("媒体点播量",5);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,35);
            sheet.setColumnView(4,10);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"点播次数",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad点播次数",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone点播次数",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);
            List<Content>  contentList =  contentLogicInterface.getAll();
            if(contentLogList.size()>0){
                int i = 2;
                for(Object[] o:contentLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    i++;
                    if(i>101){
                        break;
                    }
                }
            }
            //资源点播时长排行
            sheet = workbook.createSheet("媒体点播时长",6);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,35);
            sheet.setColumnView(4,10);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"点播时长(秒)",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad点播时长(秒)",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone点播时长(秒)",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"点播次数",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad点播次数",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone点播次数",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);
            if(contentByLengthLogList.size()>0){
                int i = 2;
                for(Object[] o:contentByLengthLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelLength = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelCount = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    i++;
                    if(i>101){
                        break;
                    }
                }
            }
            //资源点播流量排行
            sheet = workbook.createSheet("媒体流量时长",7);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,35);
            sheet.setColumnView(4,17);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"点播次数",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad点播次数",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone点播次数",fontFormat2);
            sheet.addCell(label13);
            if(contentByLengthLogList.size()>0){
                int i = 2;
                for(Object[] o:contentByNetFlowList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelLength = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelCount = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    i++;
                    if(i>101){
                        break;
                    }
                }
            }
            //建立直播量统计sheet
            sheet = workbook.createSheet("直播量统计",8);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,12);
            sheet.setColumnView(3,14);
            sheet.setColumnView(4,9);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"播放次数",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad播放次数",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone播放次数",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"播放时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad播放时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone播放时长(秒)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);

            if(contentLogList.size()>0){
                int i = 2;
                for(Object[] o:contentLiveLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    i++;
                }
            }

            //建立拉动点击排行sheet
            sheet = workbook.createSheet("拉动点击排行",9);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,35);
            sheet.setColumnView(4,10);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"播放次数",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad播放次数",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone播放次数",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"播放时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad播放时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone播放时长(秒)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);

            if(contentLadongLogList.size()>0){
                int i = 2;
                for(Object[] o:contentLadongLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelByteSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelByteSend);
                    jxl.write.Number labelByteSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelByteSendPad);
                    jxl.write.Number labelByteSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelByteSendPhone);
                    i++;
                    if(i>101){
                        break;
                    }
                }
            }

            //电影统计排行
            sheet = workbook.createSheet("电影点播统计",10);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,17);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,17);
            sheet.setColumnView(4,17);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.mergeCells(1,0,7,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"SP名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"资源类型",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"频道",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"点播次数",fontFormat2);
            sheet.addCell(label6);
            label5 = new Label(6,1,"点播时长",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(7,1,"总流量(b)",fontFormat2);
            sheet.addCell(label6);
            List<Channel>  channelList = channelLogicInterface.getAll();
            if(contentFilmLogList.size()>0){
                int i = 2;
                for(Object[] o:contentFilmLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            Label cspName = new Label(1,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    Label labelContentType = new Label(2,i,"电影",fontFormat3);
                    sheet.addCell(labelContentType);
                    long contentId = Long.parseLong(o[2].toString());
                    for(Content content:contentList){
                        if(contentId==content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    long channelId = Long.parseLong(o[3].toString());
                    for(Channel channel:channelList){
                        if(channelId==channel.getId()){
                            Label channelName = new Label(4,i,channel.getName(),fontFormat3);
                            sheet.addCell(channelName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(5,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelLength = new jxl.write.Number(6,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(7,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    i++;
                }
            }

            //电视剧点播排行
            sheet = workbook.createSheet("电视剧点播排行",11);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,17);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,17);
            sheet.setColumnView(4,17);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.mergeCells(1,0,7,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"SP名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"资源类型",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"频道",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"点播次数",fontFormat2);
            sheet.addCell(label6);
            label5 = new Label(6,1,"点播时长",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(7,1,"总流量(b)",fontFormat2);
            sheet.addCell(label6);
            if(contentTvLogList.size()>0){
                int i = 2;
                for(Object[] o:contentTvLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            Label cspName = new Label(1,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    Label labelContentType = new Label(2,i,"电视剧",fontFormat3);
                    sheet.addCell(labelContentType);
                    long contentId = Long.parseLong(o[2].toString());
                    for(Content content:contentList){
                        if(contentId==content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    long channelId = Long.parseLong(o[3].toString());
                    for(Channel channel:channelList){
                        if(channelId==channel.getId()){
                            Label channelName = new Label(4,i,channel.getName(),fontFormat3);
                            sheet.addCell(channelName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(5,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelLength = new jxl.write.Number(6,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(7,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    i++;
                }
            }
            //建立用户登陆情况统计
            sheet = workbook.createSheet("用户登陆情况统计",12);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,15);
            sheet.setColumnView(2,15);
            sheet.setColumnView(3,15);
            sheet.setColumnView(4,15);
            sheet.setColumnView(5,15);
            sheet.setColumnView(6,15);
            sheet.setColumnView(7,15);
            sheet.setColumnView(8,15);
            sheet.setColumnView(9,15);
            sheet.mergeCells(1,0,9,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"区域名称",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"联通总部取号",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"河北联通取号",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"Wap取号",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"验证码登陆",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"客户端登陆",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"总量",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"登陆次数",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"登陆天次",fontFormat2);
            sheet.addCell(label10);
            if(arealist.size()>0){
                int i = 2;
                for(Object[] o:userLoginLogList) {
                    long areaId = Long.parseLong(o[0].toString());
                    for(Area area :arealist){
                        if(areaId==area.getId()){
                            Label areaName = new Label(1,i,area.getName(),fontFormat3);
                            sheet.addCell(areaName);
                        }
                        if(-1 == areaId){
                            Label areaName = new Label(1,i,"其他",fontFormat3);
                            sheet.addCell(areaName);
                        }
                        if(1 == areaId){
                            Label areaName = new Label(1,i,"总量",fontFormat3);
                            sheet.addCell(areaName);
                        }
                    }
                    jxl.write.Number labelOneCount = new jxl.write.Number(2,i,Double.valueOf(o[1].toString()),numberFormat1);
                    sheet.addCell(labelOneCount);
                    jxl.write.Number labelTwoCount = new jxl.write.Number(3,i,Double.valueOf(o[2].toString()),numberFormat1);
                    sheet.addCell(labelTwoCount);
                    jxl.write.Number labelThreeCount = new jxl.write.Number(4,i,Double.valueOf(o[3].toString()),numberFormat1);
                    sheet.addCell(labelThreeCount);
                    jxl.write.Number labelFourCount = new jxl.write.Number(5,i,Double.valueOf(o[4].toString()),numberFormat1);
                    sheet.addCell(labelFourCount);
                    jxl.write.Number labelFiveCount = new jxl.write.Number(6,i,Double.valueOf(o[5].toString()),numberFormat1);
                    sheet.addCell(labelFiveCount);
                    jxl.write.Number labelCount = new jxl.write.Number(7,i,Double.valueOf(o[6].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelLoginCount = new jxl.write.Number(8,i,Double.valueOf(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLoginCount);
                    jxl.write.Number labelDayCount = new jxl.write.Number(9,i,Double.valueOf(o[8].toString()),numberFormat1);
                    sheet.addCell(labelDayCount);
                    i++;
                }
            }

            //建立日并发量sheet
            sheet = workbook.createSheet("日并发量统计",13);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,16);
            sheet.setColumnView(2,13);
            sheet.mergeCells(1,0,2,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"时间",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"并发量(次)",fontFormat2);
            sheet.addCell(label3);

            if(bingfaList.size()>0){
                int i = 2;
                if(bingfaList.size()==1){
                    for(Object[] o :bingfaList){
                        String quarterBingFa = o[0].toString();
                        String[]  s = quarterBingFa.split(",");
                        for(int m = 0; m<s.length;m++){
                            String [] s1 = s[m].split("=");
                            String time = s1[0];
                            String value = s1[1];
                            Label labelDate = new Label(1,i,time,fontFormat3);
                            sheet.addCell(labelDate);
                            jxl.write.Number labelValue = new jxl.write.Number(2,i,Long.parseLong(value),numberFormat1);
                            sheet.addCell(labelValue);
                        }
                    }
                }else{
                    for(Object[] o :bingfaList){
                        String MaxBingFa  = o[1].toString();
                        String[]  s = MaxBingFa.split("=");
                        String time = s[0].substring(0,10);
                        String value = s[1];
                        Label labelDate = new Label(1,i,time,fontFormat3);
                        sheet.addCell(labelDate);
                        jxl.write.Number labelValue = new jxl.write.Number(2,i,Long.parseLong(value),numberFormat1);
                        sheet.addCell(labelValue);
                        i++;
                    }
                }
            }

            //建立流量统计sheet
             sheet = workbook.createSheet("流量统计",14);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,21);
            sheet.setColumnView(2,21);
            sheet.setColumnView(3,21);
            sheet.setColumnView(4,20);
            sheet.setColumnView(5,20);
            sheet.setColumnView(6,20);
            sheet.setColumnView(7,20);
            sheet.setColumnView(8,20);
            sheet.setColumnView(9,20);
            sheet.setColumnView(10,20);
            sheet.setColumnView(11,20);
            sheet.setColumnView(12,20);
            sheet.setColumnView(13,20);
            sheet.setColumnView(14,20);
            sheet.setColumnView(15,20);
            sheet.setColumnView(16,20);
            sheet.setColumnView(17,20);
            sheet.setColumnView(18,20);
            sheet.setColumnView(19,20);
            sheet.setColumnView(20,20);
            sheet.setColumnView(21,20);
            sheet.setColumnView(22,20);
            sheet.setColumnView(23,20);
            sheet.setColumnView(24,20);
            sheet.setColumnView(25,20);
            sheet.setColumnView(26,20);
            sheet.setColumnView(27,20);
            sheet.setColumnView(28,20);
            sheet.setColumnView(29,20);
            sheet.setColumnView(30,20);
            sheet.setColumnView(31,20);
            sheet.setColumnView(32,20);
            sheet.setColumnView(33,20);
            sheet.setColumnView(34,20);
            sheet.setColumnView(35,20);
            sheet.setColumnView(36,20);
            sheet.setColumnView(37,20);
            sheet.setColumnView(38,20);
            sheet.setColumnView(39,20);
            sheet.setColumnView(40,20);
            sheet.setColumnView(41,20);
            sheet.setColumnView(42,20);
            sheet.setColumnView(43,20);
            sheet.setColumnView(44,20);
            sheet.setColumnView(45,20);
            sheet.setColumnView(46,20);
            sheet.setColumnView(47,20);
            sheet.setColumnView(48,20);
            sheet.setColumnView(49,20);
            sheet.mergeCells(1,0,20,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"日期",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"次数",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"时长",fontFormat2);
            sheet.addCell(label5);
            Label mobileLabel = new Label(5,1,"移动流量(字节)",fontFormat2);
            sheet.addCell(mobileLabel);
            label6= new Label(6,1,"次数",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(7,1,"时长",fontFormat2);
            sheet.addCell(label7);
            Label elseLabel = new Label(8,1,"其他流量(字节)",fontFormat2);
            sheet.addCell(elseLabel);
            label8= new Label(9,1,"次数",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(10,1,"时长",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(12,1,"次数",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(13,1,"时长",fontFormat2);
            sheet.addCell(label12);
             label13 = new Label(14,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);
            label14 = new Label(15,1,"次数",fontFormat2);
            sheet.addCell(label14);
            label15 = new Label(16,1,"时长",fontFormat2);
            sheet.addCell(label15);
            label16 = new Label(17,1,"直播总流量(字节)",fontFormat2);
            sheet.addCell(label16);
            label17 = new Label(18,1,"次数",fontFormat2);
            sheet.addCell(label17);
            label18 = new Label(19,1,"时长",fontFormat2);
            sheet.addCell(label18);
            label19 = new Label(20,1,"Pad直播总流量(字节)",fontFormat2);
            sheet.addCell(label19);
            Label label20 = new Label(21,1,"次数",fontFormat2);
            sheet.addCell(label20);
            Label label21 = new Label(22,1,"时长",fontFormat2);
            sheet.addCell(label21);
            Label label22 = new Label(23,1,"Phone直播总流量(字节)",fontFormat2);
            sheet.addCell(label22);
            Label label23 = new Label(24,1,"次数",fontFormat2);
            sheet.addCell(label23);
            Label label24 = new Label(25,1,"时长",fontFormat2);
            sheet.addCell(label24);
            Label label25 = new Label(26,1,"点播总流量(字节)",fontFormat2);
            sheet.addCell(label25);
            Label label26 = new Label(27,1,"次数",fontFormat2);
            sheet.addCell(label26);
            Label label27 = new Label(28,1,"时长",fontFormat2);
            sheet.addCell(label27);
            Label label28 = new Label(29,1,"Pad点播总流量(字节)",fontFormat2);
            sheet.addCell(label28);
            Label label29 = new Label(30,1,"次数",fontFormat2);
            sheet.addCell(label29);
            Label label30 = new Label(31,1,"时长",fontFormat2);
            sheet.addCell(label30);
            Label label31 = new Label(32,1,"Phone点播总流量(字节)",fontFormat2);
            sheet.addCell(label31);
            Label label32 = new Label(33,1,"次数",fontFormat2);
            sheet.addCell(label32);
            Label label33 = new Label(34,1,"时长",fontFormat2);
            sheet.addCell(label33);
            Label label34 = new Label(35,1,"华数点播流量(字节)",fontFormat2);
            sheet.addCell(label34);
            Label label35 = new Label(36,1,"次数",fontFormat2);
            sheet.addCell(label35);
            Label label36 = new Label(37,1,"时长",fontFormat2);
            sheet.addCell(label36);
            Label label37 = new Label(38,1,"华数拉动流量(字节)",fontFormat2);
            sheet.addCell(label37);
            Label label38 = new Label(39,1,"优朋点播流量(字节)",fontFormat2);
            sheet.addCell(label38);
            Label label39 = new Label(40,1,"次数",fontFormat2);
            sheet.addCell(label39);
            Label label40 = new Label(41,1,"时长",fontFormat2);
            sheet.addCell(label40);
            Label label41 = new Label(42,1,"优朋拉动流量(字节)",fontFormat2);
            sheet.addCell(label41);
            Label label42 = new Label(43,1,"百视通点播流量(字节)",fontFormat2);
            sheet.addCell(label42);
            Label label43 = new Label(44,1,"次数",fontFormat2);
            sheet.addCell(label43);
            Label label44 = new Label(45,1,"时长",fontFormat2);
            sheet.addCell(label44);
            Label label45 = new Label(46,1,"百视拉动播流量(字节)",fontFormat2);
            sheet.addCell(label45);
            Label label46 = new Label(47,1,"播放用户数量(个)",fontFormat2);
            sheet.addCell(label46);
            Label label47 = new Label(48,1,"播放用户流量(字节)",fontFormat2);
            sheet.addCell(label47);
            int i = 2;
            if(netFlowList.size()>0){
                    for(Object[] o:netFlowList){
                        Label labelDate = new Label(1,i,o[0].toString(),fontFormat3);
                        sheet.addCell(labelDate);
                        jxl.write.Number labelAllNetFlow = new jxl.write.Number(2,i,Long.parseLong(o[1].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlow);
                        jxl.write.Number labelAllLength = new jxl.write.Number(4,i,Long.parseLong(o[20].toString()),numberFormat1);
                        sheet.addCell(labelAllLength);
                        jxl.write.Number labelAllCount = new jxl.write.Number(3,i,Long.parseLong(o[21].toString()),numberFormat1);
                        sheet.addCell(labelAllCount);

                        jxl.write.Number labelMobileNetFlow = new jxl.write.Number(5,i,Long.parseLong(o[2].toString()),numberFormat1);
                        sheet.addCell(labelMobileNetFlow);
                        jxl.write.Number labelMobileLength = new jxl.write.Number(7,i,Long.parseLong(o[22].toString()),numberFormat1);
                        sheet.addCell(labelMobileLength);
                        jxl.write.Number labelMobileCount = new jxl.write.Number(6,i,Long.parseLong(o[23].toString()),numberFormat1);
                        sheet.addCell(labelMobileCount);

                        jxl.write.Number labelAllNetFlowPad = new jxl.write.Number(11,i,Long.parseLong(o[4].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowPad);
                        jxl.write.Number labelPadLength = new jxl.write.Number(13,i,Long.parseLong(o[24].toString()),numberFormat1);
                        sheet.addCell(labelPadLength);
                        jxl.write.Number labelPadCount = new jxl.write.Number(12,i,Long.parseLong(o[25].toString()),numberFormat1);
                        sheet.addCell(labelPadCount);

                        jxl.write.Number labelAllNetFlowPhone = new jxl.write.Number(14,i,Long.parseLong(o[5].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowPhone);
                        jxl.write.Number labelPhoneLength = new jxl.write.Number(16,i,Long.parseLong(o[26].toString()),numberFormat1);
                        sheet.addCell(labelPhoneLength);
                        jxl.write.Number labelPhoneCount = new jxl.write.Number(15,i,Long.parseLong(o[27].toString()),numberFormat1);
                        sheet.addCell(labelPhoneCount);

                        jxl.write.Number labelAllNetFlowLive = new jxl.write.Number(17,i,Long.parseLong(o[6].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowLive);
                        jxl.write.Number labelLiveLength = new jxl.write.Number(19,i,Long.parseLong(o[28].toString()),numberFormat1);
                        sheet.addCell(labelLiveLength);
                        jxl.write.Number labelLiveCount = new jxl.write.Number(18,i,Long.parseLong(o[29].toString()),numberFormat1);
                        sheet.addCell(labelLiveCount);

                        jxl.write.Number labelAllNetFlowLivePad = new jxl.write.Number(20,i,Long.parseLong(o[7].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowLivePad);
                        jxl.write.Number labelLivePadLength = new jxl.write.Number(22,i,Long.parseLong(o[30].toString()),numberFormat1);
                        sheet.addCell(labelLivePadLength);
                        jxl.write.Number labelLivePadCount = new jxl.write.Number(21,i,Long.parseLong(o[31].toString()),numberFormat1);
                        sheet.addCell(labelLivePadCount);

                        jxl.write.Number labelAllNetFlowLivePhone = new jxl.write.Number(23,i,Long.parseLong(o[8].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowLivePhone);
                        jxl.write.Number labelLivePhoneLength = new jxl.write.Number(25,i,Long.parseLong(o[32].toString()),numberFormat1);
                        sheet.addCell(labelLivePhoneLength);
                        jxl.write.Number labelLivePhoneCount = new jxl.write.Number(24,i,Long.parseLong(o[33].toString()),numberFormat1);
                        sheet.addCell(labelLivePhoneCount);

                        jxl.write.Number labelAllNetFlowContent = new jxl.write.Number(26,i,Long.parseLong(o[9].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowContent);
                        jxl.write.Number labelContentLength = new jxl.write.Number(28,i,Long.parseLong(o[34].toString()),numberFormat1);
                        sheet.addCell(labelContentLength);
                        jxl.write.Number labelContentCount = new jxl.write.Number(27,i,Long.parseLong(o[35].toString()),numberFormat1);
                        sheet.addCell(labelContentCount);

                        jxl.write.Number labelAllNetFlowContentPad = new jxl.write.Number(29,i,Long.parseLong(o[10].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowContentPad);
                        jxl.write.Number labelContentPadLength = new jxl.write.Number(31,i,Long.parseLong(o[36].toString()),numberFormat1);
                        sheet.addCell(labelContentPadLength);
                        jxl.write.Number labelContentPadCount = new jxl.write.Number(30,i,Long.parseLong(o[37].toString()),numberFormat1);
                        sheet.addCell(labelContentPadCount);

                        jxl.write.Number labelAllNetFlowContentPhone = new jxl.write.Number(32,i,Long.parseLong(o[11].toString()),numberFormat1);
                        sheet.addCell(labelAllNetFlowContentPhone);
                        jxl.write.Number labelContentPhoneLength = new jxl.write.Number(34,i,Long.parseLong(o[38].toString()),numberFormat1);
                        sheet.addCell(labelContentPhoneLength);
                        jxl.write.Number labelContentPhoneCount = new jxl.write.Number(33,i,Long.parseLong(o[39].toString()),numberFormat1);
                        sheet.addCell(labelContentPhoneCount);

                        jxl.write.Number labelWasuNetFlow = new jxl.write.Number(35,i,Long.parseLong(o[12].toString()),numberFormat1);
                        sheet.addCell(labelWasuNetFlow);
                        jxl.write.Number labelWasuLength = new jxl.write.Number(37,i,Long.parseLong(o[42].toString()),numberFormat1);
                        sheet.addCell(labelWasuLength);
                        jxl.write.Number labelWasuCount = new jxl.write.Number(36,i,Long.parseLong(o[43].toString()),numberFormat1);
                        sheet.addCell(labelWasuCount);

                        jxl.write.Number labelWasuLadongNetFlow = new jxl.write.Number(38,i,Long.parseLong(o[13].toString()),numberFormat1);
                        sheet.addCell(labelWasuLadongNetFlow);

                        jxl.write.Number labelVooleNetFlow = new jxl.write.Number(39,i,Long.parseLong(o[14].toString()),numberFormat1);
                        sheet.addCell(labelVooleNetFlow);
                        jxl.write.Number labelVooleLength = new jxl.write.Number(41,i,Long.parseLong(o[45].toString()),numberFormat1);
                        sheet.addCell(labelVooleLength);
                        jxl.write.Number labelVooleCount = new jxl.write.Number(40,i,Long.parseLong(o[44].toString()),numberFormat1);
                        sheet.addCell(labelVooleCount);

                        jxl.write.Number labelVooleLadongNetFlow = new jxl.write.Number(42,i,Long.parseLong(o[15].toString()),numberFormat1);
                        sheet.addCell(labelVooleLadongNetFlow);

                        jxl.write.Number labelBestvNetFlow = new jxl.write.Number(43,i,Long.parseLong(o[16].toString()),numberFormat1);
                        sheet.addCell(labelBestvNetFlow);
                        jxl.write.Number labelBestvLength = new jxl.write.Number(45,i,Long.parseLong(o[40].toString()),numberFormat1);
                        sheet.addCell(labelBestvLength);
                        jxl.write.Number labelBestvCount = new jxl.write.Number(44,i,Long.parseLong(o[41].toString()),numberFormat1);
                        sheet.addCell(labelBestvCount);

                        jxl.write.Number labelBestvLadongNetFlow = new jxl.write.Number(46,i,Long.parseLong(o[17].toString()),numberFormat1);
                        sheet.addCell(labelBestvLadongNetFlow);

                        jxl.write.Number labelOnlineUser = new jxl.write.Number(47,i,Long.parseLong(o[18].toString()),numberFormat1);
                        sheet.addCell(labelOnlineUser);
                        jxl.write.Number labelOnlineUserNetFlow = new jxl.write.Number(48,i,Long.parseLong(o[19].toString()),numberFormat1);
                        sheet.addCell(labelOnlineUserNetFlow);

                        jxl.write.Number labelElseNetFlow = new jxl.write.Number(8,i,Long.parseLong(o[3].toString()),numberFormat1);
                        sheet.addCell(labelElseNetFlow);
                        jxl.write.Number labelElseCount = new jxl.write.Number(9,i,Long.parseLong(o[47].toString()),numberFormat1);
                        sheet.addCell(labelElseCount);
                        jxl.write.Number labelElseLength = new jxl.write.Number(10,i,Long.parseLong(o[46].toString()),numberFormat1);
                        sheet.addCell(labelElseLength);
                        i++;
                    }
            }
            //媒体直播趋势统计
            sheet = workbook.createSheet("直播趋势统计",15);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,21);
            sheet.setColumnView(2,21);
            sheet.setColumnView(3,21);
            sheet.setColumnView(4,20);
            sheet.setColumnView(5,20);
            sheet.setColumnView(6,20);
            sheet.setColumnView(7,20);
            sheet.setColumnView(8,20);
            sheet.setColumnView(9,20);
            sheet.setColumnView(10,20);
            sheet.setColumnView(11,20);
            sheet.setColumnView(12,20);
            sheet.setColumnView(13,20);
            sheet.setColumnView(14,20);
            sheet.setColumnView(15,20);
            sheet.setColumnView(16,20);
            sheet.setColumnView(17,20);
            sheet.setColumnView(18,20);
            sheet.setColumnView(19,20);
            sheet.setColumnView(20,20);
            sheet.setColumnView(21,20);
            sheet.setColumnView(22,20);
            sheet.setColumnView(23,20);
            sheet.setColumnView(24,20);
            sheet.setColumnView(25,20);
            sheet.mergeCells(1,0,20,0);
             label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
             sheet.addCell(label0);
             label2 = new Label(1,1,"日期",fontFormat2);
            sheet.addCell(label2);
             label3 = new Label(2,1,"00:00",fontFormat2);
            sheet.addCell(label3);
            Label label03 = new Label(3,1,"01:00",fontFormat2);
            sheet.addCell(label03);
            Label label04 = new Label(4,1,"02：:00",fontFormat2);
            sheet.addCell(label04);
             label4 = new Label(5,1,"03:00",fontFormat2);
            sheet.addCell(label4);
             label5 = new Label(6,1,"04:00",fontFormat2);
            sheet.addCell(label5);
             label6 = new Label(7,1,"05:00",fontFormat2);
            sheet.addCell(label6);
             label7 = new Label(8,1,"06：:00",fontFormat2);
            sheet.addCell(label7);
             label8 = new Label(9,1,"07：:00",fontFormat2);
            sheet.addCell(label8);
             label9 = new Label(10,1,"08：:00",fontFormat2);
            sheet.addCell(label9);
             label10 = new Label(11,1,"09:00",fontFormat2);
            sheet.addCell(label10);
             label11 = new Label(12,1,"10:00",fontFormat2);
            sheet.addCell(label11);
             label12 = new Label(13,1,"11：00",fontFormat2);
            sheet.addCell(label12);
             label13 = new Label(14,1,"12:00",fontFormat2);
            sheet.addCell(label13);
             label14 = new Label(15,1,"13:00",fontFormat2);
            sheet.addCell(label14);
             label15 = new Label(16,1,"14：:0",fontFormat2);
            sheet.addCell(label15);
             label16 = new Label(17,1,"15:00",fontFormat2);
            sheet.addCell(label16);
             label17 = new Label(18,1,"16:00",fontFormat2);
            sheet.addCell(label17);
             label18 = new Label(19,1,"17：00",fontFormat2);
            sheet.addCell(label18);
             label19 = new Label(20,1,"18:00",fontFormat2);
            sheet.addCell(label19);
             label20 = new Label(21,1,"19:00",fontFormat2);
            sheet.addCell(label20);
             label21 = new Label(22,1,"20:00",fontFormat2);
            sheet.addCell(label21);
             label22 = new Label(23,1,"21:00",fontFormat2);
            sheet.addCell(label22);
             label23= new Label(24,1,"22:00",fontFormat2);
            sheet.addCell(label23);
             label24= new Label(25,1,"23:00",fontFormat2);
            sheet.addCell(label24);
             label25= new Label(26,1,"按天汇总（播放次数）",fontFormat2);
            sheet.addCell(label25);
            i = 2;
            if(countLogList.size()>0){
                for (Map map :countLogList) {
                    Label labelDate = new Label(1,i, map.get("date").toString(),fontFormat3);
                    sheet.addCell(labelDate);
                    jxl.write.Number labelTime_00 = new jxl.write.Number(2,i,Long.parseLong(map.get("0").toString()),numberFormat1);
                    sheet.addCell(labelTime_00);
                    jxl.write.Number labelTime_01 = new jxl.write.Number(3,i,Long.parseLong(map.get("1").toString()),numberFormat1);
                    sheet.addCell(labelTime_01);
                    jxl.write.Number labelTime_02 = new jxl.write.Number(4,i,Long.parseLong(map.get("2").toString()),numberFormat1);
                    sheet.addCell(labelTime_02);
                    jxl.write.Number labelTime_03 = new jxl.write.Number(5,i,Long.parseLong(map.get("3").toString()),numberFormat1);
                    sheet.addCell(labelTime_03);
                    jxl.write.Number labelTime_04 = new jxl.write.Number(6,i,Long.parseLong(map.get("4").toString()),numberFormat1);
                    sheet.addCell(labelTime_04);
                    jxl.write.Number labelTime_05 = new jxl.write.Number(7,i,Long.parseLong(map.get("5").toString()),numberFormat1);
                    sheet.addCell(labelTime_05);
                    jxl.write.Number labelTime_06 = new jxl.write.Number(8,i,Long.parseLong(map.get("6").toString()),numberFormat1);
                    sheet.addCell(labelTime_06);
                    jxl.write.Number labelTime_07 = new jxl.write.Number(9,i,Long.parseLong(map.get("7").toString()),numberFormat1);
                    sheet.addCell(labelTime_07);
                    jxl.write.Number labelTime_08 = new jxl.write.Number(10,i,Long.parseLong(map.get("8").toString()),numberFormat1);
                    sheet.addCell(labelTime_08);
                    jxl.write.Number labelTime_09 = new jxl.write.Number(11,i,Long.parseLong(map.get("9").toString()),numberFormat1);
                    sheet.addCell(labelTime_09);
                    jxl.write.Number labelTime_10 = new jxl.write.Number(12,i,Long.parseLong(map.get("10").toString()),numberFormat1);
                    sheet.addCell(labelTime_10);
                    jxl.write.Number labelTime_11 = new jxl.write.Number(13,i,Long.parseLong(map.get("11").toString()),numberFormat1);
                    sheet.addCell(labelTime_11);
                    jxl.write.Number labelTime_12 = new jxl.write.Number(14,i,Long.parseLong(map.get("12").toString()),numberFormat1);
                    sheet.addCell(labelTime_12);
                    jxl.write.Number labelTime_13 = new jxl.write.Number(15,i,Long.parseLong(map.get("13").toString()),numberFormat1);
                    sheet.addCell(labelTime_13);
                    jxl.write.Number labelTime_14 = new jxl.write.Number(16,i,Long.parseLong(map.get("14").toString()),numberFormat1);
                    sheet.addCell(labelTime_14);
                    jxl.write.Number labelTime_15 = new jxl.write.Number(17,i,Long.parseLong(map.get("15").toString()),numberFormat1);
                    sheet.addCell(labelTime_15);
                    jxl.write.Number labelTime_16 = new jxl.write.Number(18,i,Long.parseLong(map.get("16").toString()),numberFormat1);
                    sheet.addCell(labelTime_16);
                    jxl.write.Number labelTime_17 = new jxl.write.Number(19,i,Long.parseLong(map.get("17").toString()),numberFormat1);
                    sheet.addCell(labelTime_17);
                    jxl.write.Number labelTime_18 = new jxl.write.Number(20,i,Long.parseLong(map.get("18").toString()),numberFormat1);
                    sheet.addCell(labelTime_18);
                    jxl.write.Number labelTime_19 = new jxl.write.Number(21,i,Long.parseLong(map.get("19").toString()),numberFormat1);
                    sheet.addCell(labelTime_19);
                    jxl.write.Number labelTime_20 = new jxl.write.Number(22,i,Long.parseLong(map.get("20").toString()),numberFormat1);
                    sheet.addCell(labelTime_20);
                    jxl.write.Number labelTime_21 = new jxl.write.Number(23,i,Long.parseLong(map.get("21").toString()),numberFormat1);
                    sheet.addCell(labelTime_21);
                    jxl.write.Number labelTime_22 = new jxl.write.Number(24,i,Long.parseLong(map.get("22").toString()),numberFormat1);
                    sheet.addCell(labelTime_22);
                    jxl.write.Number labelTime_23 = new jxl.write.Number(25,i,Long.parseLong(map.get("23").toString()),numberFormat1);
                    sheet.addCell(labelTime_23);
                    jxl.write.Number labelTime_24 = new jxl.write.Number(26,i,Long.parseLong(map.get("Count").toString()),numberFormat1);
                    sheet.addCell(labelTime_24);
                    i++;
                }
            }
            //建立有播放媒体统计
            sheet = workbook.createSheet("有播放媒体统计",16);
            sheet.setColumnView(0,6);
            sheet.setColumnView(1,5);
            sheet.setColumnView(2,17);
            sheet.setColumnView(3,35);
            sheet.setColumnView(4,10);
            sheet.setColumnView(5,17);
            sheet.setColumnView(6,17);
            sheet.setColumnView(7,17);
            sheet.setColumnView(8,17);
            sheet.setColumnView(9,17);
            sheet.setColumnView(10,17);
            sheet.setColumnView(11,17);
            sheet.setColumnView(12,17);
            sheet.mergeCells(1,0,12,0);
            label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
            sheet.addCell(label0);
            label2 = new Label(1,1,"排行",fontFormat2);
            sheet.addCell(label2);
            label3 = new Label(2,1,"SP名称",fontFormat2);
            sheet.addCell(label3);
            label4 = new Label(3,1,"资源名称",fontFormat2);
            sheet.addCell(label4);
            label5 = new Label(4,1,"点播次数",fontFormat2);
            sheet.addCell(label5);
            label6 = new Label(5,1,"Pad点播次数",fontFormat2);
            sheet.addCell(label6);
            label7 = new Label(6,1,"Phone点播次数",fontFormat2);
            sheet.addCell(label7);
            label8 = new Label(7,1,"点播时长(秒)",fontFormat2);
            sheet.addCell(label8);
            label9 = new Label(8,1,"Pad点播时长(秒)",fontFormat2);
            sheet.addCell(label9);
            label10 = new Label(9,1,"Phone点播时长(秒)",fontFormat2);
            sheet.addCell(label10);
            label11 = new Label(10,1,"总流量(字节)",fontFormat2);
            sheet.addCell(label11);
            label12 = new Label(11,1,"Pad总流量(字节)",fontFormat2);
            sheet.addCell(label12);
            label13 = new Label(12,1,"Phone总流量(字节)",fontFormat2);
            sheet.addCell(label13);
             contentList =  contentLogicInterface.getAll();
            if(systemPlayedLogList.size()>0){
                i = 2;
                for(Object[] o:systemPlayedLogList) {
                    long cspId = Long.parseLong(o[0].toString());
                    for(Csp csp:cspList){
                        if(cspId==csp.getId()){
                            jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                            sheet.addCell(labelGrade);
                            Label cspName = new Label(2,i,csp.getName(),fontFormat3);
                            sheet.addCell(cspName);
                        }
                    }
                    for(Content content:contentList){
                        long contentId = Long.parseLong(o[1].toString());
                        if(contentId == content.getId()){
                            Label contentName = new Label(3,i,content.getName(),fontFormat3);
                            sheet.addCell(contentName);
                        }
                    }
                    jxl.write.Number labelCount = new jxl.write.Number(4,i,Long.parseLong(o[2].toString()),numberFormat1);
                    sheet.addCell(labelCount);
                    jxl.write.Number labelCountPad = new jxl.write.Number(5,i,Long.parseLong(o[3].toString()),numberFormat1);
                    sheet.addCell(labelCountPad);
                    jxl.write.Number labelCountPhone = new jxl.write.Number(6,i,Long.parseLong(o[4].toString()),numberFormat1);
                    sheet.addCell(labelCountPhone);
                    jxl.write.Number labelLength = new jxl.write.Number(7,i,Long.parseLong(o[5].toString()),numberFormat1);
                    sheet.addCell(labelLength);
                    jxl.write.Number labelLengthPad = new jxl.write.Number(8,i,Long.parseLong(o[6].toString()),numberFormat1);
                    sheet.addCell(labelLengthPad);
                    jxl.write.Number labelLengthPhone = new jxl.write.Number(9,i,Long.parseLong(o[7].toString()),numberFormat1);
                    sheet.addCell(labelLengthPhone);
                    jxl.write.Number labelBytesSend = new jxl.write.Number(10,i,Long.parseLong(o[8].toString()),numberFormat1);
                    sheet.addCell(labelBytesSend);
                    jxl.write.Number labelBytesSendPad = new jxl.write.Number(11,i,Long.parseLong(o[9].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPad);
                    jxl.write.Number labelBytesSendPhone = new jxl.write.Number(12,i,Long.parseLong(o[10].toString()),numberFormat1);
                    sheet.addCell(labelBytesSendPhone);
                    i++;
                    if(i>9999){
                        break;
                    }
                }
            }
                sheet = workbook.createSheet("用户热词搜索",17);
                sheet.setColumnView(0,6);
                sheet.setColumnView(1,21);
                sheet.setColumnView(2,21);
                sheet.mergeCells(1,0,20,0);
                 label0 = new Label(1,0,startDate1+"-"+endDate1,fontFormat1);
                sheet.addCell(label0);
                Label label1 = new Label(1,1,"排行",fontFormat2);
                sheet.addCell(label1);
                label2 = new Label(2,1,"内容",fontFormat2);
                sheet.addCell(label2);
                 label3 = new Label(3,1,"月搜索次数",fontFormat2);
                sheet.addCell(label3);
                 i = 2;
                if(searchHotLogList.size()>0){
                    for(Object[] o:searchHotLogList){
                        jxl.write.Number labelGrade = new jxl.write.Number(1,i,i-1,numberFormat);
                        sheet.addCell(labelGrade);
                        Label labelContent = new Label(2,i,o[0].toString(),numberFormat1);
                        sheet.addCell(labelContent);
                        jxl.write.Number labelSearchMonthCount = new jxl.write.Number(3,i,Long.parseLong(o[1].toString()),numberFormat1);
                        sheet.addCell(labelSearchMonthCount);
                        i++;
                    }
                }
            workbook.write();
            workbook.close();
            downLoadFile(fileName);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void downLoadFile(String filePth) {
        HttpServletResponse response = ServletActionContext.getResponse();
        //HttpServletRequest request = ServletActionContext.getRequest();
        try {

            //得到当前路径
            //String filePath=request.getSession().getServletContext().getRealPath(File.separator);
            File temFile = new File(filePth);
            if(!temFile.exists()){
                response.getWriter().write("ERROR:File Not Found");
                return ;
            }
            String fileName = filePth.substring(filePth.lastIndexOf(File.separator)+1);
            response.setHeader("Content-Disposition", "attachment; filename="+ new String((fileName).getBytes("gbk"),"iso8859-1"));

            response.setContentType("application/x-download");
            OutputStream ot=response.getOutputStream();
            BufferedInputStream bis  = new BufferedInputStream(new FileInputStream(temFile));
            BufferedOutputStream bos = new BufferedOutputStream(ot);
            byte[] buffer = new byte[4096];
            int length = 0;
            while((length = bis.read(buffer)) > 0){
                bos.write(buffer,0,length);
            }
            bos.close();
            bis.close();
            ot.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List getAllPhoneNotIsNull(String startTime,String endTime){
        return visitLogDaoInterface.getAllPhoneNotIsNull(startTime,endTime);
    }
    public Long getOnlineUserOfDate(Date startDate,Date endDate){
        return visitLogDaoInterface.getOnlineUserOfDate(startDate,endDate);
    }

    public List getLoginedUserInfo(String startDate,String endDate){
        return visitLogDaoInterface.getLoginedUserInfo(startDate,endDate);
    }

    public static void main(String args[]){
        // System.out.println("time:"+FormatTime(3600));
    }
    public List getAreaLogsFromAreaDemandLog(Date startDate,Date endDate){
        List<Object[]> list =  visitLogDaoInterface.getAreaLogsFromAreaDemandLog(startDate,endDate);
        List<Object[]> list1 = new ArrayList<Object[]>();
        //将多个日期内，areaId且type相等时的数据加起来
        if(list.size()>0){
            for(int i =0;i<list.size();i++){
                Object[] o = list.get(i);
                Long areaId = Long.valueOf(o[2].toString());
                Long type = Long.valueOf(o[3].toString());
                Long count = Long.valueOf(o[4].toString());
                Long padCount = Long.valueOf(o[5].toString());
                Long phoneCount = Long.valueOf(o[6].toString());
                Long length = Long.valueOf(o[7].toString());
                Long padLength = Long.valueOf(o[8].toString());
                Long phoneLength = Long.valueOf(o[9].toString());
                Long bytesSend = Long.valueOf(o[10].toString());
                Long bytesSendPad = Long.valueOf(o[11].toString());
                Long bytesSendPhone = Long.valueOf(o[12].toString());
                Long mobileBytesSend = Long.valueOf(o[13].toString());
                Long elseBytesSend = Long.valueOf(o[14].toString());
                Long mobileCount = Long.valueOf(o[15].toString());
                Long elseCount = Long.valueOf(o[16].toString());
                Long mobileLength = Long.valueOf(o[17].toString());
                Long elseLength = Long.valueOf(o[18].toString());
                Long userOnLineCount = Long.valueOf(o[19].toString());
                for(int j =i+1;j<list.size();j++){
                    Object[] o1 = list.get(j);
                    Long areaId1 = Long.valueOf(o1[2].toString());
                    Long type1 = Long.valueOf(o1[3].toString());
                    Long count1 = Long.valueOf(o1[4].toString());
                    Long padCount1 = Long.valueOf(o1[5].toString());
                    Long phoneCount1 = Long.valueOf(o1[6].toString());
                    Long length1 = Long.valueOf(o1[7].toString());
                    Long padLength1 = Long.valueOf(o1[8].toString());
                    Long phoneLength1 = Long.valueOf(o1[9].toString());
                    Long bytesSend1 = Long.valueOf(o1[10].toString());
                    Long bytesSendPad1 = Long.valueOf(o1[11].toString());
                    Long bytesSendPhone1 = Long.valueOf(o1[12].toString());
                    Long mobileBytesSend1 = Long.valueOf(o1[13].toString());
                    Long elseBytesSend1 = Long.valueOf(o1[14].toString());
                    Long mobileCount1 = Long.valueOf(o1[15].toString());
                    Long elseCount1 = Long.valueOf(o1[16].toString());
                    Long mobileLength1 = Long.valueOf(o1[17].toString());
                    Long elseLength1 = Long.valueOf(o1[18].toString());
                    Long userOnLineCount1 = Long.valueOf(o1[19].toString());
                    if(areaId.equals(areaId1) && type.equals(type1)){
                        count += count1;
                        padCount += padCount1;
                        phoneCount += phoneCount1;
                        length += length1;
                        padLength += padLength1;
                        phoneLength += phoneLength1;
                        bytesSend += bytesSend1;
                        bytesSendPad += bytesSendPad1;
                        bytesSendPhone += bytesSendPhone1;
                        mobileBytesSend += mobileBytesSend1;
                        elseBytesSend += elseBytesSend1;
                        mobileCount += mobileCount1;
                        elseCount += elseCount1;
                        mobileLength += mobileLength1;
                        elseLength += elseLength1;
                        userOnLineCount += userOnLineCount1;
                        list.remove(j);
                        j=j-1;
                    }
                }
                //将数据赋值到新的object里，塞入新的list里。用type作为区分，来判断是地域，地域拉动，或者是其他
                Object[] newO = new Object[18];
                if(1==type){
                  newO[0]="Area";
                }
                if(2==type){
                  newO[0]="pullArea";
                }
                if(3==type){
                  newO[0]="elseArea";
                }
                newO[1] = areaId;
                newO[2] = count;
                newO[3] = padCount;
                newO[4] = phoneCount;
                newO[5] = length;
                newO[6] = padLength;
                newO[7] = phoneLength;
                newO[8] = bytesSend;
                newO[9] = bytesSendPad;
                newO[10] = bytesSendPhone;
                newO[11] = mobileBytesSend;
                newO[12] = elseBytesSend;
                newO[13] = mobileCount;
                newO[14] = elseCount;
                newO[15] = mobileLength;
                newO[16] = elseLength;
                newO[17] = userOnLineCount;
                list1.add(newO);
            }
        }
        return list1;
    }
    public List getAllNetFlowFromDailyStaticsLog(Date startDate,Date endDate){
        List<Object[]> list =  visitLogDaoInterface.getAllNetFlowFromDailyStaticsLog(startDate,endDate);
        if(list.size()>1){
            //查询的日期为多个日期，求总和，插入list。
            String date1 = "";
            Long allNetFlow = 0L;
            Long allLength = 0L;
            Long mobileNetFlow = 0L;
            Long elseNetFlow = 0L;
            Long allNetFlowPad = 0L;
            Long allNetFlowPhone = 0L;
            Long allNetFlowLive = 0L;
            Long allNetFlowLivePad = 0L;
            Long allNetFlowLivePhone = 0L;
            Long allNetFlowContent = 0L;
            Long allNetFlowContentPad = 0L;
            Long allNetFlowContentPhone = 0L;
            Long wasuNetFlow = 0L;
            Long vooleNetFlow = 0L;
            Long bestvNetFlow = 0L;
            Long wasuLadongNetFlow = 0L;
            Long vooleLadongNetFlow = 0L;
            Long bestvLadongNetFlow = 0L;
            Long onlineUser = 0L;
            Long onlineUserNetFlow = 0L;
            Long allCount = 0L;
            Long mobileLength = 0L ;
            Long mobileCount = 0L;
            Long padLength = 0L;
            Long padCount = 0L;
            Long phoneLength = 0L;
            Long phoneCount = 0L;
            Long liveLength = 0L;
            Long liveCount = 0L;
            Long livePadLength = 0L;
            Long livePadCount = 0L;
            Long livePhoneLength = 0L;
            Long livePhoneCount = 0L;
            Long contentLength = 0L;
            Long contentCount = 0L;
            Long contentPadLength = 0L;
            Long contentPadCount = 0L;
            Long contentPhoneLength = 0L;
            Long contentPhoneCount = 0L;
            Long wasuLength = 0L;
            Long wasuCount = 0L;
            Long vooleLength = 0L;
            Long vooleCount = 0L;
            Long bestvLength = 0L;
            Long bestvCount = 0L;
            Long elseLength = 0L;
            Long elseCount = 0L;

            Object[] startDateO = list.get(0);
            Object[] endDateO = list.get(list.size()-1);
            date1 = startDateO[0].toString().replace("-",".")+ "-" + endDateO[0].toString().replace("-",".");
            for(Object[] o:list){
                allNetFlow  += Long.parseLong(o[1].toString());
                mobileNetFlow +=Long.parseLong(o[2].toString());
                elseNetFlow += Long.parseLong(o[3].toString());
                allNetFlowPad  += Long.parseLong(o[4].toString());
                allNetFlowPhone  += Long.parseLong(o[5].toString());
                allNetFlowLive += Long.parseLong(o[6].toString());
                allNetFlowLivePad += Long.parseLong(o[7].toString());
                allNetFlowLivePhone += Long.parseLong(o[8].toString());
                allNetFlowContent += Long.parseLong(o[9].toString());
                allNetFlowContentPad += Long.parseLong(o[10].toString());
                allNetFlowContentPhone += Long.parseLong(o[11].toString());
                wasuNetFlow += Long.parseLong(o[12].toString());
                wasuLadongNetFlow += Long.parseLong(o[13].toString());
                vooleNetFlow += Long.parseLong(o[14].toString());
                vooleLadongNetFlow += Long.parseLong(o[15].toString());
                bestvNetFlow += Long.parseLong(o[16].toString());
                bestvLadongNetFlow += Long.parseLong(o[17].toString());
                onlineUserNetFlow += Long.parseLong(o[19].toString());

                allLength += Long.parseLong(o[20].toString());
                allCount += Long.parseLong(o[21].toString());
                mobileLength += Long.parseLong(o[22].toString());
                mobileCount += Long.parseLong(o[23].toString());
                padLength += Long.parseLong(o[24].toString());
                padCount += Long.parseLong(o[25].toString());
                phoneLength += Long.parseLong(o[26].toString());
                phoneCount += Long.parseLong(o[27].toString());
                liveLength += Long.parseLong(o[28].toString());
                liveCount += Long.parseLong(o[29].toString());
                livePadLength += Long.parseLong(o[30].toString());
                livePadCount += Long.parseLong(o[31].toString());
                livePhoneLength += Long.parseLong(o[32].toString());
                livePhoneCount += Long.parseLong(o[33].toString());
                contentLength += Long.parseLong(o[34].toString());
                contentCount += Long.parseLong(o[35].toString());
                contentPadLength += Long.parseLong(o[36].toString());
                contentPadCount += Long.parseLong(o[37].toString());
                contentPhoneLength += Long.parseLong(o[38].toString());
                contentPhoneCount += Long.parseLong(o[39].toString());
                bestvLength += Long.parseLong(o[40].toString());
                bestvCount += Long.parseLong(o[41].toString());
                wasuLength += Long.parseLong(o[42].toString());
                wasuCount += Long.parseLong(o[43].toString());
                vooleCount += Long.parseLong(o[44].toString());
                vooleLength += Long.parseLong(o[45].toString());
                elseLength += Long.parseLong(o[46].toString());
                elseCount += Long.parseLong(o[47].toString());

            }
            onlineUser = getOnlineUserOfDate(startDate,endDate);
            Object[] o = new Object[48];
            o[0] = date1;o[1] = allNetFlow;o[2] = mobileNetFlow;o[3] = elseNetFlow;o[4] = allNetFlowPad; o[5] = allNetFlowPhone; o[6] =allNetFlowLive;o[7] =allNetFlowLivePad; o[8] =allNetFlowLivePhone;
            o[9] = allNetFlowContent; o[10] = allNetFlowContentPad;o[11]=allNetFlowContentPhone;o[12] =wasuNetFlow;o[13]=wasuLadongNetFlow;o[14] =vooleNetFlow;
            o[15] = vooleLadongNetFlow; o[16] =bestvNetFlow; o[17] = bestvLadongNetFlow;o[18] =onlineUser;o[19] = onlineUserNetFlow;o[20] = allLength;
            o[21] = allCount;o[22] = mobileLength;o[23] = mobileCount;o[24] = padLength;o[25] = padCount;o[26] = phoneLength;
            o[27] = phoneCount;o[28] = liveLength;
            o[29] = liveCount;o[30] = livePadLength;o[31] = livePadCount;o[32] =livePhoneLength;o[33] = livePhoneCount;o[34] = contentLength;o[35] = contentCount;o[36] =contentPadLength;
            o[37] = contentPadCount; o[38] = contentPhoneLength; o[39] = contentPhoneCount; o[40] = bestvLength; o[41] = bestvCount;o[42] = wasuLength; o[43] = wasuCount;
             o[44] = vooleCount; o[45] = vooleLength; o[46] = elseLength;o[47] = elseCount;
            list.add(o);
        }
        return list;
    }

    public List getChannelLogsFromChannelDemandLog(Date startDate, Date endDate, long channelId,String isExport){
        String subChannelIds="";
        if(channelId>0){
            subChannelIds += channelLogicInterface.getAllChildIds(channelId);
            if (!"".equals(subChannelIds)) {
                subChannelIds += ","+channelId;
            }else{
                subChannelIds = channelId +"";
            }
        }
        List<Object[]> channelList =  visitLogDaoInterface.getChannelLogsFromChannelDemandLog(startDate,endDate,subChannelIds);
        List channels = channelLogicInterface.getAvailableChannelOfCsp(1,1,1);
        List<Channel> channel = formatChannel(-1,channels,0);
        //复制一份channel
        List<Channel> channel1 = formatChannel(-1,channels,0);
        List list2 = new ArrayList();

        long type;
        if(channelId>1){
            for(Channel ch:channel1){
                String availableChannel[] = subChannelIds.split(",");
                boolean isRemove=true;
                for(String str:availableChannel){
                    if(ch.getId()==Long.parseLong(str)){
                        isRemove=false;
                        break;
                    }
                }
                if(isRemove){
                    channel.remove(ch);
                }
            }
        }
        for(Channel ch:channel){
            //去掉直播
            if(ch.getId()==15884423L||ch.getId()==503099300||ch.getId()==594113201||ch.getId()==15884422){
                continue;
            }
            String channelIds ="";
            if(-1 != ch.getParentId()){
                channelIds += ch.getId()+",";
                List list = channelLogicInterface.getSonChannelId(ch.getId());
                type = 3;
                if(list.size()>0){
                    for(Object o:list){
                        Long channelId1 = StringUtils.string2long(o.toString(),-1);
                        channelIds += channelId1+",";
                        type = 2 ;
                        List list3 = channelLogicInterface.getSonChannelId(channelId1);
                        if(list3.size()>0){
                            type = 1;
                            for(Object o1 :list3){
                                channelIds += o1.toString()+",";
                            }
                        }
                    }
                }
                String[] listChannel = channelIds.split(",");
                long count = 0;
                long length = 0;
                long bytesSend = 0;
                for(String s :listChannel){
                    for(Object[] o:channelList){
                        if(Long.parseLong(s)==Long.parseLong(o[0].toString())){
                            count += Float.parseFloat(o[1].toString());
                            length += Float.parseFloat(o[2].toString());
                            bytesSend += Float.parseFloat(o[3].toString());
                            break;
                        }
                    }
                }
                Object[] obj = new Object[6];
                obj[0] = ch.getId();
                obj[1] = ch.getName();
                if(0!=count){
                    obj[2] = count;
                }else{
                    if(("isTrue").equals(isExport)){
                        obj[2] = 0;
                    }
                }
                if(0!=length){
                    obj[3] = length;
                }else{
                    if(("isTrue").equals(isExport)){
                        obj[3] = 0;
                    }
                }
                if(0!=bytesSend){
                    obj[5] = bytesSend;
                }else{
                    if(("isTrue").equals(isExport)){
                        obj[5] = 0;
                    }
                }
                if(Long.parseLong(obj[0].toString())==15884442||Long.parseLong(obj[0].toString())==115390100||Long.parseLong(obj[0].toString())==11584642){
                    obj[4] = 2;
                }else{
                    obj[4] = type;
                }
                list2.add(obj);
            }else{
                Object[] obj = new Object[6];
                obj[0] = ch.getId();
                obj[1] = ch.getName();
                if(isExport.equals("isTrue")){
                    obj[2] = 0;
                    obj[3] = 0;
                    obj[4] = 0;
                    obj[5] = 0;
                }
                list2.add(obj);
            }
        }
        return list2;
    }

    public List<Channel> formatChannel(long parentId, List<Channel> list1, int dec) {
        List<Channel> result = new ArrayList<Channel>();
        try {
            for (int i = 0,l=list1.size(); i < l; i++) {
                Channel c = list1.get(i);
                if (c.getParentId().equals(parentId)) {
                    c.setName(getChannelName(dec, c.getName()));
                    //c.setLevel(dec);
                    result.add(c);
                    List<Channel> children = formatChannel(c.getId(), list1, dec + 1);
                    if(children.size()>=1){
                        c.setLeaf(false);
                    }else{
                        c.setLeaf(true);
                    }
                    result.addAll(children);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getChannelName(int dec,String channelName) {
        String result = "";
        for (int i = 0; i < dec; i++) {
            result += "　";
        }
        if(channelName.contains(result)){

        }else{
            channelName = result+channelName;
        }
        return channelName;
    }

    public List getAllBingFaFromDailyStaticsLog(Date startDate,Date endDate){
        return visitLogDaoInterface.getAllBingFaFromDailyStaticsLog(startDate,endDate);
    }
    public List getContentByDateAndType(Date startDate, Date endDate, String type){
        return visitLogDaoInterface.getContentByDateAndType(startDate,endDate,type);
    }

    public List getContentFilmAndTvLog(Date startDate,Date endDate,long contentType,int pageSize){
        String filmChannelIds = "15884448,15884648,15884446,15884668" ;
        String tvChannelIds = "15884458,15884654,15884454,15884460";
        //经典华语ID
        long channelId = 115389457;
        //经典华语中有电影也有电视剧。
        List<Object[]>  checkChannelList = visitLogDaoInterface.checkChannelFilmOrTv(channelId);
        String filmIds = "";
        String tvIds = "";
        for(Object[] oCheckChannel:checkChannelList){
            long contentId = Long.parseLong(oCheckChannel[0].toString());
            int count = Integer.parseInt(oCheckChannel[1].toString());
            if(count>1){
                tvIds += contentId + ",";
            }else{
                filmIds += contentId +",";
            }
        }
        //经典华语中所有电视剧的ID;
        if(tvIds.length()>0){
            tvIds=tvIds.substring(0,tvIds.length()-1);
        }
        //经典华语中所有电影的ID；
        if(filmIds.length()>0){
            filmIds = filmIds.substring(0,filmIds.length()-1);
        }
        if(contentType!=-1){
            String channelIds = "";
            String contentIds = "";
            if(contentType==1){
                channelIds = filmChannelIds;
                contentIds = filmIds;
            }
            if(contentType==2){
                channelIds=tvChannelIds;
                contentIds = tvIds;
            }
            return  visitLogDaoInterface.getContentFilmAndTvLog(startDate,endDate,contentType,channelIds,contentIds,pageSize);
        }

        return null;
    }


    public List getTotalHotRanked() {
        return visitLogDaoInterface.getTotalHotRanked();
    }


    public List getFileOrTvHotRanked(long contentType) {
        String filmChannelIds = "15884448,15884648,15884446,15884668" ;
        String tvChannelIds = "15884458,15884654,15884454,15884460";
        //经典华语ID
        long channelId = 115389457;
        //经典华语中有电影也有电视剧。
        List<Object[]>  checkChannelList = visitLogDaoInterface.checkChannelFilmOrTv(channelId);
        String filmIds = "";
        String tvIds = "";
        for(Object[] oCheckChannel:checkChannelList){
            long contentId = Long.parseLong(oCheckChannel[0].toString());
            int count = Integer.parseInt(oCheckChannel[1].toString());
            if(count>1){
                tvIds += contentId + ",";
            }else{
                filmIds += contentId +",";
            }
        }
        //经典华语中所有电视剧的ID;
        if(tvIds.length()>0){
            tvIds=tvIds.substring(0,tvIds.length()-1);
        }
        //经典华语中所有电影的ID；
        if(filmIds.length()>0){
            filmIds = filmIds.substring(0,filmIds.length()-1);
        }
        if(contentType!=-1){
            String channelIds = "";
            String contentIds = "";
            if(contentType==1){
                channelIds = filmChannelIds;
                contentIds = filmIds;
            }
            if(contentType==2){
                channelIds=tvChannelIds;
                contentIds = tvIds;
            }
            return  visitLogDaoInterface.getFileOrTvHotRanked(contentType,channelIds,contentIds);
        }

        return null;
    }


    public List<ContentDemandLog> getContentDemandLogByDate(String startDate,String endDate) {
        return  visitLogDaoInterface.getContentDemandLogByDate(startDate,endDate);
    }

    public List<DailyStaticsLog> getDailyStaticsLogByDate(String startDate,String endDate) {
       return visitLogDaoInterface.getDailyStaticsLogByDate(startDate,endDate);
    }

    public List<AreaDemandLog> getAreaDemandLogByDate(String startDate,String endDate) {
       return visitLogDaoInterface.getAreaDemandLogByDate(startDate,endDate);
    }

    public List<ChannelDemandLog> getChannelDemandLogByDate(String startDate,String endDate) {
       return visitLogDaoInterface.getChannelDemandLogByDate(startDate,endDate);
    }

    public List<VisitLog> getVisitLogByDate(String startDate,String endDate) {
        return visitLogDaoInterface.getVisitLogByDate(startDate, endDate);
    }


    public List getUserLoginByDate(String startDate,String endDate) {
        return visitLogDaoInterface.getUserLoginByDate(startDate, endDate);
    }

    /**
     * Redex访问统计
     * @param channelId 频道Id
     * @param startTime 统计时间段开始时间
     * @param endTime   统计时间段结束时间
     * @param pageBean  分页信息
     * @return  统计列表
     */
    @SuppressWarnings("unchecked")
    public List<RedexStatVisit> getRedexContentVisitStat(Long channelId, Date startTime, Date endTime, PageBean pageBean){
        // 从dao中获取统计信息，在从content中获取视频详情
        String channelIds = "";
        if(channelId != null && channelId > 0){
            channelIds += channelId;
            List<Channel> channelList = (List) TreeUtils.getInstance().getAllChildOf(Channel.class, channelId, 9999);
            if( channelList != null ){
                for( Channel c : channelList){
                    channelIds += "," + c.getId();
                }
            }
        }

        // 从dao中获取统计信息
        List<Object[]> visitLogList = visitLogDaoInterface.getRedexStatVisitCount(channelIds, startTime, endTime, pageBean);
        if( visitLogList == null ) return null;

        // 视频详情
        List<RedexStatVisit> logList = new ArrayList();
        for( Object[] objectArray : visitLogList){
            long contentId = StringUtils.string2long(objectArray[0].toString(), 0);
            if( contentId > 0 ){
                RedexStatVisit visitLog = new RedexStatVisit();
                visitLog.setContentId( contentId );
                visitLog.setCount( StringUtils.string2long( objectArray[1].toString(), 0 ));
                Content content = null;
                try {
                    content = contentLogicInterface.get(contentId);
                }catch(Exception e){e.printStackTrace();}
                if(content != null){
                    visitLog.setName(content.getName());
                    visitLog.setPoster(content.getPost1Url());
                }
                logList.add(visitLog);
            }
        }
        return  logList;
    }

    /**
     * 频道统计数据
     * @param startTime 统计开始时间
     * @param endTime   统计截止时间
     * @return 频道Id和访问次数
     */
    public List<RedexStatChannelVisit> getRedexChannelVisitStat(Date startTime, Date endTime){
        List<Object[]> visitLogList = visitLogDaoInterface.getRedexChannelVisitCount(startTime, endTime);
        if( visitLogList == null ) return null;

        List<RedexStatChannelVisit> statList = new ArrayList<RedexStatChannelVisit>();
        for( Object[] objectArray : visitLogList){
            long channelId = StringUtils.string2long(objectArray[0].toString(), 0);
            if( channelId > 0 ){
                RedexStatChannelVisit channelStat = new RedexStatChannelVisit();
                channelStat.setChannelId(channelId);
                channelStat.setVisitCount( StringUtils.string2long( objectArray[1].toString(), 0 ));
                statList.add(channelStat);
            }
        }

        return statList;
    }

    /**
     * 获得并发统计数据
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return 统计时间点和并发个数数据
     */
    public List<RedexStatConcurrent> getRedexConcurrentStat(Date startTime, Date endTime){
        // 每15分钟取一次并发数据，拼接成统计数据
        String[] statTimeSpotArray = {
                "08:00", "08:15","08:30", "08:45",
                "09:00", "09:15","09:30", "09:45",
                "10:00", "10:15","10:30", "10:45",
                "11:00", "11:15","11:30", "11:45",
                "12:00", "12:15","12:30", "12:45",
                "13:00", "13:15","13:30", "13:45",
                "14:00", "14:15","14:30", "14:45",
                "15:00", "15:15","15:30", "15:45",
                "16:00", "16:15","16:30", "16:45",
                "17:00", "17:15","17:30", "17:45",
                "18:00", "18:15","18:30", "18:45",
                "19:00", "19:15","19:30", "19:45",
                "20:00", "20:15","20:30", "20:45",
                "21:00", "21:15","21:30", "21:45",
                "22:00", "22:15","22:30", "22:45",
                "23:00", "23:15","23:30", "23:45",
                "00:00", "00:15","00:30", "00:45",
                "01:00", "01:15","01:30", "01:45",
                "02:00", "02:15","02:30", "02:45",
                "03:00", "03:15","03:30", "03:45",
                "04:00", "04:15","04:30", "04:45",
                "05:00", "05:15","05:30", "05:45",
                "06:00", "06:15","06:30", "06:45",
                "07:00", "07:15","07:30", "07:45"
        };

        List<RedexStatConcurrent> statList = new ArrayList<RedexStatConcurrent>();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        for( String timeSpot : statTimeSpotArray){
            RedexStatConcurrent statItem = new RedexStatConcurrent();
            // 只保留整点的tag，其他都清空
            //statItem.setTimeSpot(timeSpot.endsWith(":00")? timeSpot.split(":")[0] : "");
            statItem.setTimeSpot(timeSpot.endsWith(":00")? timeSpot : "");
            //statItem.setTimeSpot(timeSpot);
            try {
                Date timeSpotTime = parser.parse(timeSpot);
                statItem.setConcurrent( visitLogDaoInterface.getTimeSpotPeakConcurrent(timeSpotTime, startTime, endTime) );
            }catch (Exception e){
                statItem.setConcurrent(0);
                e.printStackTrace();
            }

            statList.add(statItem);
        }

        return statList;
    }

    /**
     * 热播前几名
     * @param channelIdList 获取的频道范围，如果为null则不限
     * @param userType  限定的用户类型，<0表示不限
     * @param days      获取统计时段的天数，从当前时间算
     * @param count     获取的条数
     * @return ContentDTO列表，其中包括媒体基本信息和访问次数
     */
    public List<ContentDTO> getTop(List<Long> channelIdList,Long userType, int days, int count){
        // 获取统计的起始时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1*days);
        Date startTime = cal.getTime();

        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }
        List<Object[]> objList = visitLogDaoInterface.getRedexTop(channelIdString, userType, startTime, count);
        if(objList == null || objList.size() == 0){
            // 从忽略时间查询
            objList = visitLogDaoInterface.getRedexTop(channelIdString, userType, null, count);
        }

        List<ContentDTO> contentDTOList = new ArrayList<ContentDTO>();
        //objList中包括contentId和访问次数，根据id获取content
        for(Object[] objects : objList){
            Long contentId = StringUtils.string2long(objects[0].toString(), 0);
            Content content = null;
            try {
                content = contentLogicInterface.get(contentId);
            }catch (Exception e){e.printStackTrace();}
            if(content != null) {
                ContentDTO dto = new ContentDTO(content);
                dto.setCount( StringUtils.string2long(objects[1].toString(), 0));
                contentDTOList.add(dto);
            }
        }

        return contentDTOList;
    }
    public List getDemandCountLogs(Date startDate,Date endDate) {
        List<Object[]> list =   visitLogDaoInterface.getDemandCountLogs(startDate,endDate);
        return list;
     }


    public List getSearchHotLogs(Date startDate, Date endDate) {
        List<Object[]> list = visitLogDaoInterface.getSearchHotLogs(startDate,endDate);
        return list;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
