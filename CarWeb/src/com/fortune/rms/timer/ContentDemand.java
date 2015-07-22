package com.fortune.rms.timer;

import com.fortune.rms.business.log.logic.logicInterface.ContentDemandLogLogicInterface;
import com.fortune.rms.business.log.model.ContentDemandLog;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-8-6
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class ContentDemand extends TimerBase {
    public void run(){
        try{
            logger.debug("自动整理每天的资源点播量 ContentDemandLog run timer:"+ StringUtils.date2string(new Date()));
            ContentDemandLog cd = new ContentDemandLog();
            Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
            Date dateStatics=cl.getTime();
            //写入数据库的时间
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
            logger.debug("数据写入完毕");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
