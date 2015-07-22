package com.fortune.rms.business.train.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.train.dao.daoInterface.ShowMachineDaoInterface;
import com.fortune.rms.business.train.logic.logicInterface.ShowMachineLogicInterface;
import com.fortune.rms.business.train.model.ShowMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("showMachineLogicInterface")
public class ShowMachineLogicImpl extends BaseLogicImpl<ShowMachine> implements ShowMachineLogicInterface {

        private ShowMachineDaoInterface showMachineDaoInterface;

        /**
         * @param showMachineDaoInterface the adDaoInterface to set
         */
        @Autowired
        public void setAdDaoInterface(ShowMachineDaoInterface showMachineDaoInterface) {
            this.showMachineDaoInterface = showMachineDaoInterface;
            baseDaoInterface = (BaseDaoInterface) this.showMachineDaoInterface;
        }

}
