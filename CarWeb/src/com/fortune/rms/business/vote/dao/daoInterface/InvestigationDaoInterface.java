package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.Investigation;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 * �ʾ�
 */
public interface InvestigationDaoInterface  extends BaseDaoInterface<Investigation, Long> {
    public List<Investigation> searchInvestigation(String searchWord, PageBean pageBean);
    public void removeByIdArray(String idArray);
}
