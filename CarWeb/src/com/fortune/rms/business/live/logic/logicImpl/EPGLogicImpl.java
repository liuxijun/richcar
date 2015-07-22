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
            //��������Ƶ����epg�����ʱ�Σ����滻����
            //�ȳ��ԣ��ҵ�ͬ����
            logs+="����"+oldEPG.size()+"����ʱ���Ѿ����ڵĽ�Ŀ����";
            Date beginTime = epg.getBeginTime();
            Date endTime = epg.getEndTime();
            for(int i=oldEPG.size()-1;i>=0;i--){
                EPG old = oldEPG.get(i);
                if(old.getName().equals(epg.getName())){
                    logs+="�ҵ�ͬ���Ľ�Ŀ��:��" +old.getName()+"�������串�ǣ�";
                    epg.setId(old.getId());
                    oldEPG.remove(i);
                }else{
                    if(old.getBeginTime().before(beginTime)){
                        logs+="���Ҫ����Ľ�Ŀ��" +epg.getName()+
                                "����ʼʱ���ڽ�Ŀ��" +old.getName()+"����ʱ��֮�ڣ�";
                        if(old.getEndTime().after(beginTime)){
                            logs+="�Ͻ�Ŀ����ʱ�����½�Ŀ֮�䣬";
                            old.setEndTime(beginTime);
                            save(old);
                            oldEPG.remove(i);
                        }else if(old.getEndTime().after(endTime)){
                            //����ɵĽ�Ŀ�����������µĽ�Ŀ
                            logs+="�Ͻ�Ŀ����ʱ�����½�Ŀ֮�󣬿�¡һ���Ͻ�Ŀ����������ʼʱ��Ϊ�½�Ŀ����ʱ�䣺"+ StringUtils.date2string(endTime);
                            try {
                                EPG new1 =(EPG) BeanUtils.clone(old);
                                if(new1!=null){
                                    new1.setId(-1);
                                    new1.setBeginTime(endTime);
                                    save(new1);
                                }else{
                                    logger.error("�޷������µĽ�Ŀ����"+old.getName()+",�������µĽ�Ŀ����"+epg.getName());
                                }
                                old.setEndTime(beginTime);
                                save(old);
                                oldEPG.remove(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        logs+="��������ϣ�";
                    }else if(old.getBeginTime().before(endTime)){
                        logs+="�Ͻ�Ŀ��ʼʱ���ڽ���ʱ��֮ǰ�������Ͻ�Ŀ��ʼʱ��Ϊ�½�Ŀ����ʱ��";
                        if(old.getEndTime().after(endTime)){
                            old.setBeginTime(endTime);
                            save(old);
                            oldEPG.remove(i);
                        }
                    }
                }
            }
            //ɾ������
            for(EPG old:oldEPG){
                logs+="��ɾ���Ѿ������ǵĽ�Ŀ����"+old.getName();
                remove(old);
            }
        }
        epg = save(epg);
        logs+=" ����һ����"+epg.getId()+","+epg.getName()+","+
                StringUtils.date2string(epg.getBeginTime())+"->"+StringUtils.date2string(epg.getEndTime());
        epg.setExtraObj(logs);
        return epg;
    }
}
