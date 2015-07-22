package com.fortune.rms.business.live.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.live.model.Record;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by ����· on 2015/3/20.
 * ¼���߼��ӿ�
 */
public interface RecordLogicInterface extends BaseLogicInterface<Record> {
    public List<Record> searchRecord(String channels, String searchValue, PageBean pageBean);
}
