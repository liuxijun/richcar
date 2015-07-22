package com.fortune.rms.business.ad.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.ad.dao.daoInterface.AdRangeDaoInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdLogicInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdRangeLogicInterface;
import com.fortune.rms.business.ad.model.Ad;
import com.fortune.rms.business.ad.model.AdRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adRangeLogicInterface")
public class AdRangeLogicImpl extends BaseLogicImpl<AdRange>
		implements
			AdRangeLogicInterface {
	private AdRangeDaoInterface adRangeDaoInterface;
    private AdLogicInterface adLogicInterface;
	/**
	 * @param adRangeDaoInterface the adRangeDaoInterface to set
	 */
    @Autowired
	public void setAdRangeDaoInterface(AdRangeDaoInterface adRangeDaoInterface) {
		this.adRangeDaoInterface = adRangeDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.adRangeDaoInterface;
	}

    @Autowired
    public void setAdLogicInterface(AdLogicInterface adLogicInterface) {
        this.adLogicInterface = adLogicInterface;
    }

    public List<AdRange> getAdRangeOfContent(Long contentId,Long channelId){
        List<AdRange> result = adRangeDaoInterface.getAdRangeOfContent(contentId,channelId);
        for(AdRange ar:result){
            if(ar.getAd()==null){
                ar.setAd(adLogicInterface.get(ar.getAdId()));
            }
        }
        return result;
    }

}
