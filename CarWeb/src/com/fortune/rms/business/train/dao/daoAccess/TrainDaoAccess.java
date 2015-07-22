package com.fortune.rms.business.train.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.train.dao.daoInterface.TrainDaoInterface;
import com.fortune.rms.business.train.model.Train;
import org.springframework.stereotype.Repository;

@Repository
public class TrainDaoAccess extends BaseDaoAccess<Train, Long>
		implements
        TrainDaoInterface {

	public TrainDaoAccess() {
		super(Train.class);
	}

}
