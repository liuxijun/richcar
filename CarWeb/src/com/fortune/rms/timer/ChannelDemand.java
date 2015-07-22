package com.fortune.rms.timer;

import com.fortune.rms.business.log.logic.logicInterface.ChannelDemandLogLogicInterface;
import com.fortune.rms.business.log.model.ChannelDemandLog;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-20
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class ChannelDemand extends TimerBase {
    public void run(){
        try{
            logger.debug("自动整理每天的频道点播量 ChannelDemandLog run timer:"+ StringUtils.date2string(new Date()));
            ChannelDemandLog cd = new ChannelDemandLog();
            Calendar cl = Calendar.getInstance();
            cl.setTime(new Date());
            cl.add(Calendar.DATE,-1);
            Date dateStatics=cl.getTime();
            //写入数据库的时间
            String startTime = StringUtils.date2string(dateStatics,"yyyy-MM-dd");
            ChannelDemandLogLogicInterface channelDemandLogLogicInterface = (ChannelDemandLogLogicInterface) SpringUtils.getBeanForApp("channelDemandLogLogicInterface");
            List<Object[]> channelList = channelDemandLogLogicInterface.getChannelDemandFromVisitLog(dateStatics);
            if(channelList.size()>0){
                for(Object[] o : channelList){
                    long channelId = Long.parseLong(o[0].toString());
                    long count = Long.parseLong(o[1].toString());
                    long length = Long.parseLong(o[2].toString());
                    long bytesSend = Long.parseLong(o[3].toString());
                    cd.setDateStatics(StringUtils.string2date(startTime, "yyyy-MM-dd"));
                    cd.setCount(count);
                    cd.setLength(length);
                    cd.setBytesSend(bytesSend);
                    cd.setChannelId(channelId);
                    channelDemandLogLogicInterface.save(cd);
                }
            }
            logger.debug("数据写入完毕");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
