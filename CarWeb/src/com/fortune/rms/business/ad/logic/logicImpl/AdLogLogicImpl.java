package com.fortune.rms.business.ad.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.ad.dao.daoInterface.AdLogDaoInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdLogLogicInterface;
import com.fortune.rms.business.ad.model.AdLog;
import com.fortune.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("adLogLogicInterface")
public class AdLogLogicImpl extends BaseLogicImpl<AdLog>
		implements
			AdLogLogicInterface {
	private AdLogDaoInterface adLogDaoInterface;

	/**
	 * @param adLogDaoInterface the adLogDaoInterface to set
	 */
    @Autowired
	public void setAdLogDaoInterface(AdLogDaoInterface adLogDaoInterface) {
		this.adLogDaoInterface = adLogDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adLogDaoInterface;
	}

    public AdLog createAdLog(Long adId,Long adRangeId,Long contentId,Long channelId,Long cspId){
        AdLog log = new AdLog(-1,adId,"",channelId,cspId,new Date(),contentId,adRangeId,
                ConfigManager.getInstance().getConfig("default.trainId",0L));
        return save(log);
    }

    public List<Map<String, Object>> stateForAd(String adName,Date startTime, Date stopTime, Integer trainId,PageBean pageBean) {
        return adLogDaoInterface.stateForAd(adName,startTime,stopTime,trainId,pageBean);
    }

    public List<Map<String, Object>> stateForTrain(String lineName,Date startTime, Date stopTime, Integer adId,PageBean pageBean) {
        return adLogDaoInterface.stateForTrain(lineName,startTime, stopTime, adId,pageBean);
    }
    public List<Map<String, Object>> listLogs(String adName,String contentName,Long channelId,String lineName,
                                              Date startTime, Date stopTime, Integer adId,Integer trainId,PageBean pageBean){
        return adLogDaoInterface.listLogs(adName, contentName, channelId, lineName, startTime, stopTime, adId,trainId, pageBean);
    }
}
