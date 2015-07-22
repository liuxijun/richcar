package com.fortune.rms.timer;

import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.log.logic.logicInterface.DailyStaticsLogLogicInterface;
import com.fortune.rms.business.log.model.DailyStaticsLog;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-21
 * Time: 下午5:57
 * 每天凌晨整理前一天的流量和并发日志
 */
public class DailyStatics extends TimerBase {
    public void run(){
        try{
            logger.debug("自动整理每天的流量和并发日志 DailyStatics run timer:"+StringUtils.date2string(new Date()));
            DailyStaticsLog ds = new DailyStaticsLog();
            Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
           Date dateStatics = cl.getTime();

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
                ds.setMobileNetFlow(mobileFlow);
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
                ds.setWasuCount(StringUtils.string2long(map.get("wasuCount"),-1));
                ds.setWasuLength(StringUtils.string2long(map.get("wasuLength"),-1));
                ds.setVooleCount(StringUtils.string2long(map.get("vooleCount"),-1));
                ds.setVooleLength(StringUtils.string2long(map.get("vooleLength"),-1));
                ds.setBestvCount(StringUtils.string2long(map.get("bestvCount"),-1));
                ds.setBestvLength(StringUtils.string2long(map.get("bestvLength"),-1));
            }
            logger.debug("流量整理完成，整理并发数据");
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
            logger.debug("并发数据整理完成");
            ds.setQuarterBingFa(result);
            ds.setMaxBingFa(maxResult);
            Calendar cl1 = Calendar.getInstance();
            cl1.setTime(new Date());
            Date createTime = cl1.getTime();
            ds.setCreateTime(createTime);
            logger.debug("开始存储数据");
            dailyStaticsLogLogicInterface.save(ds);
            logger.debug("存储数据完成");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static  void main(String args[]){
        DailyStatics ds = new DailyStatics();
        ds.run();
    }

}
