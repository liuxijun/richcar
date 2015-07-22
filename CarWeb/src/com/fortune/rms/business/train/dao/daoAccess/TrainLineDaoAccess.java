package com.fortune.rms.business.train.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.train.dao.daoInterface.TrainLineDaoInterface;
import com.fortune.rms.business.train.model.TrainLine;
import org.springframework.stereotype.Repository;

@Repository
public class TrainLineDaoAccess extends BaseDaoAccess<TrainLine, Long>
		implements
        TrainLineDaoInterface {

	public TrainLineDaoAccess() {
		super(TrainLine.class);
	}

}
