package com.fortune.rms.timer;

import com.fortune.rms.business.log.logic.logicInterface.AreaDemandLogLogicInterface;
import com.fortune.rms.business.log.model.AreaDemandLog;
import com.fortune.rms.business.system.logic.logicInterface.AreaLogicInterface;
import com.fortune.rms.business.system.model.Area;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-8-1
 * Time: 下午3:01
 * To change this template use File | Settings | File Templates.
 */
public class AreaDemand extends TimerBase {
    public void run(){
        try{
            logger.debug("自动整理每天的区域点播量 AreaDemandLog run timer:"+ StringUtils.date2string(new Date()));
            AreaDemandLog ad = new AreaDemandLog();
            Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
            Date dateStatics = cl.getTime();
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
                        long userOnlineCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,1,4);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setUserOnLineCount(userOnlineCount);
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
                        long userOnlineCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,2,4);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setUserOnLineCount(userOnlineCount);
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
                        long userOnlineCount = areaDemandLogLogicInterface.getAreaDemandByAreaId(dateStatics,areaId,3,4);
                        ad.setAreaId(areaId);
                        ad.setMobileBytesSend(mobileBytesSend);
                        ad.setElseBytesSend(bytesSend-mobileBytesSend);
                        ad.setMobileCount(mobileCount);
                        ad.setElseCount(count-mobileCount);
                        ad.setMobileLength(mobileLength);
                        ad.setElseLength(length-mobileLength);
                        ad.setUserOnLineCount(userOnlineCount);
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
            logger.debug("数据存储完成");
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
