package com.fortune.rms.business.system.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.system.model.Individual;

/**
 * Created by 王明路 on 2014/11/28.
 */
public interface IndividualLogicInterface  extends BaseLogicInterface<Individual> {
    public boolean saveIndividual(String logoPath, String name);
    public boolean saveIndividual(Individual individual);
}
