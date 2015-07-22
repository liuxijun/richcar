package com.fortune.rms.business.train.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.train.dao.daoInterface.ShowMachineDaoInterface;
import com.fortune.rms.business.train.model.ShowMachine;
import org.springframework.stereotype.Repository;

@Repository
public class ShowMachineDaoAccess extends BaseDaoAccess<ShowMachine, Long>
		implements
        ShowMachineDaoInterface {

	public ShowMachineDaoAccess() {
		super(ShowMachine.class);
	}

}
