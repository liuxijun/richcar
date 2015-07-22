package com.fortune.rms.business.live.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.live.dao.daoInterface.EPGDaoInterface;
import com.fortune.rms.business.live.logic.logicInterface.EPGLogicInterface;
import com.fortune.rms.business.live.model.EPG;
import com.fortune.util.BeanUtils;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2015/6/14.
 *
 */
@Service("epgLogicInterface")
public class EPGLogicImpl extends BaseLogicImpl<EPG> implements EPGLogicInterface {
    private EPGDaoInterface epgDaoInterface;

    @Autowired
    public void setEpgDaoInterface(EPGDaoInterface epgDaoInterface) {
        this.epgDaoInterface = epgDaoInterface;
        this.baseDaoInterface =(BaseDaoInterface) epgDaoInterface;
    }
    public List<EPG> getEpgOfLiveNow(Long liveId, Long contentId) {
        PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.beginTime","desc");
        Date now = new Date();
        Date stopTime = new Date(now.getTime()+6*3600*1000L);
        Date startTime = new Date(now.getTime()-3*24*3600*1000L);
        return getEpgOfLive(liveId,contentId,startTime,stopTime,null,pageBean);
    }
    public List<EPG> getEpgOfLive(Long liveId, Long contentId, Date startTime, Date stopTime, Long status,PageBean pageBean) {
        return epgDaoInterface.getEpgOfLive(liveId,contentId,startTime,stopTime,status,pageBean);
    }

    public EPG insertEPG(EPG epg) {
        PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.beginTime","asc");
        List<EPG> oldEPG = getEpgOfLive(epg.getLiveId(),epg.getContentId(),epg.getBeginTime(),epg.getEndTime(),-1L,pageBean);
        String logs="";
        if(oldEPG!=null&&oldEPG.size()>0){
            //如果，这个频道有epg在这个时段，就替换掉！
            //先尝试，找到同名的
            logs+="发现"+oldEPG.size()+"个本时段已经存在的节目单！";
            Date beginTime = epg.getBeginTime();
            Date endTime = epg.getEndTime();
            for(int i=oldEPG.size()-1;i>=0;i--){
                EPG old = oldEPG.get(i);
                if(old.getName().equals(epg.getName())){
                    logs+="找到同名的节目单:《" +old.getName()+"》，将其覆盖！";
                    epg.setId(old.getId());
                    oldEPG.remove(i);
                }else{
                    if(old.getBeginTime().before(beginTime)){
                        logs+="如果要插入的节目《" +epg.getName()+
                                "》起始时间在节目《" +old.getName()+"》的时间之内，";
                        if(old.getEndTime().after(beginTime)){
                            logs+="老节目截至时间在新节目之间，";
                            old.setEndTime(beginTime);
                            save(old);
                            oldEPG.remove(i);
                        }else if(old.getEndTime().after(endTime)){
                            //如果旧的节目完整包含了新的节目
                            logs+="老节目结束时间在新节目之后，克隆一个老节目，设置其起始时间为新节目结束时间："+ StringUtils.date2string(endTime);
                            try {
                                EPG new1 =(EPG) BeanUtils.clone(old);
                                if(new1!=null){
                                    new1.setId(-1);
                                    new1.setBeginTime(endTime);
                                    save(new1);
                                }else{
                                    logger.error("无法插入新的节目单："+old.getName()+",包含了新的节目单："+epg.getName());
                                }
                                old.setEndTime(beginTime);
                                save(old);
                                oldEPG.remove(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        logs+="，处理完毕！";
                    }else if(old.getBeginTime().before(endTime)){
                        logs+="老节目起始时间在结束时间之前，设置老节目起始时间为新节目结束时间";
                        if(old.getEndTime().after(endTime)){
                            old.setBeginTime(endTime);
                            save(old);
                            oldEPG.remove(i);
                        }
                    }
                }
            }
            //删除其余
            for(EPG old:oldEPG){
                logs+="，删除已经被覆盖的节目单："+old.getName();
                remove(old);
            }
        }
        epg = save(epg);
        logs+=" 保存一条："+epg.getId()+","+epg.getName()+","+
                StringUtils.date2string(epg.getBeginTime())+"->"+StringUtils.date2string(epg.getEndTime());
        epg.setExtraObj(logs);
        return epg;
    }
}
