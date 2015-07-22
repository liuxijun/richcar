package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.EPGDaoInterface;
import com.fortune.rms.business.live.model.EPG;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xjliu 20150614
 *
 */
@Repository
public class EPGDaoAccess extends BaseDaoAccess<EPG, Long>
        implements
        EPGDaoInterface {
    public EPGDaoAccess() {
        super(EPG.class);
    }

    public List<EPG> getEpgOfLive(Long liveId, Long contentId, Date startTime, Date stopTime, Long status,PageBean pageBean) {
        String hql = "from EPG o1";
        String condition = "";
        if(startTime!=null&&stopTime!=null){
            if(startTime.after(stopTime)){
                Date temp = stopTime;
                stopTime = startTime;
                startTime = temp;
            }
        }
        List<Object> parameters = new ArrayList<Object>();
        if(liveId!=null&&liveId>0){
            condition +=" and o1.liveId = "+liveId;
        }
        if(contentId!=null&&contentId>0){
            condition +=" and o1.contentId = "+contentId;
        }
        if(startTime!=null){
            condition += " and o1.endTime>to_date('"+ StringUtils.date2string(startTime)+"','YYYY-MM-DD HH24:MI:SS')";
            //parameters.add(startTime);
        }
        if(stopTime!=null){
            condition+=" and o1.beginTime<to_date('"+ StringUtils.date2string(stopTime)+"','YYYY-MM-DD HH24:MI:SS')";
            //parameters.add(stopTime);
        }
        if(status!=null&&status>=0){
            condition +=" and o1.status = "+status;
        }
        if(!condition.isEmpty()){
            condition = condition.substring(4);
            hql += " where "+ condition;
        }
        try {
            //logger.debug("将要执行的hql="+hql);
            return getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<EPG>(0);
    }
}
