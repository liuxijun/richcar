package com.fortune.common.business.base.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.base.model.Dictionary;

import java.util.List;

/**
 * Created by xjliu on 2015/3/13.
 *
 */
public interface DictionaryLogicInterface extends BaseLogicInterface<Dictionary> {
    public List<Dictionary> getItemsOfCode(String code,int repeatTimes);
}
