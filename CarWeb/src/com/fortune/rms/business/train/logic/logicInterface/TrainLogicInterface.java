package com.fortune.rms.business.train.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.train.model.Train;

import java.io.File;
import java.util.List;


public interface TrainLogicInterface extends BaseLogicInterface<Train> {
    List<String> importLog(File logFile);
}
