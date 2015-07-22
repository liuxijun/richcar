package com.fortune.rms.business.ad.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.ad.dao.daoInterface.AdDaoInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdLogicInterface;
import com.fortune.rms.business.ad.model.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adLogicInterface")
public class AdLogicImpl extends BaseLogicImpl<Ad> implements AdLogicInterface {

        private AdDaoInterface adDaoInterface;

        /**
         * @param adDaoInterface the adDaoInterface to set
         */
        @Autowired
        public void setAdDaoInterface(AdDaoInterface adDaoInterface) {
            this.adDaoInterface = adDaoInterface;
            baseDaoInterface = (BaseDaoInterface) this.adDaoInterface;
        }

}
