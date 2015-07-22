package com.fortune.rms.business.live.logic.logicImpl;

import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.live.logic.logicInterface.RecordLogicInterface;
import com.fortune.rms.business.live.model.Record;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ����· on 2015/3/20.
 * ¼���߼�ʵ��
 */
@Service("recordLogicInterface")
public class RecordLogicImpl extends BaseLogicImpl<Record>
        implements
        RecordLogicInterface {
    public RecordLogicImpl() {
    }

    /**
     * ����¼��
     * @param channels      ��Ŀ��Χ
     * @param searchValue   �����ؼ���
     * @param pageBean      ��ҳ��Ϣ
     * @return ¼�ƶ����б�
     */
    public List<Record> searchRecord(String channels, String searchValue, PageBean pageBean){
        return new ArrayList<Record>();
    }
}
