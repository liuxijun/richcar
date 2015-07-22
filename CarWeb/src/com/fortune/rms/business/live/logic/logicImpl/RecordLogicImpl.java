package com.fortune.rms.business.live.logic.logicImpl;

import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.live.logic.logicInterface.RecordLogicInterface;
import com.fortune.rms.business.live.model.Record;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2015/3/20.
 * 录制逻辑实现
 */
@Service("recordLogicInterface")
public class RecordLogicImpl extends BaseLogicImpl<Record>
        implements
        RecordLogicInterface {
    public RecordLogicImpl() {
    }

    /**
     * 搜索录制
     * @param channels      栏目范围
     * @param searchValue   搜索关键字
     * @param pageBean      分页信息
     * @return 录制对象列表
     */
    public List<Record> searchRecord(String channels, String searchValue, PageBean pageBean){
        return new ArrayList<Record>();
    }
}
