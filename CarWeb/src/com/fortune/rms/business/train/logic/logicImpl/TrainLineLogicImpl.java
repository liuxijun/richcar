package com.fortune.rms.business.train.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.train.dao.daoInterface.TrainLineDaoInterface;
import com.fortune.rms.business.train.logic.logicInterface.TrainLineLogicInterface;
import com.fortune.rms.business.train.model.TrainLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("trainLineLogicInterface")
public class TrainLineLogicImpl extends BaseLogicImpl<TrainLine> implements TrainLineLogicInterface {

        private TrainLineDaoInterface trainLineDaoInterface;

        /**
         * @param trainLineDaoInterface the adDaoInterface to set
         */
        @Autowired
        public void setAdDaoInterface(TrainLineDaoInterface trainLineDaoInterface) {
            this.trainLineDaoInterface = trainLineDaoInterface;
            baseDaoInterface = (BaseDaoInterface) this.trainLineDaoInterface;
        }

}
