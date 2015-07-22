package com.fortune.rms.business.system.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.system.dao.daoInterface.IndividualDaoInterface;
import com.fortune.rms.business.system.model.Individual;
import org.springframework.stereotype.Repository;

/**
 * Created by 王明路 on 2014/11/28.
 */
@Repository
public class IndividualDaoAccess extends BaseDaoAccess<Individual, Long>
        implements
        IndividualDaoInterface {

    public IndividualDaoAccess() {
        super(Individual.class);
    }
}
