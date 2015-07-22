package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationDaoInterface;
import com.fortune.rms.business.vote.model.Investigation;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class InvestigationDaoAccess extends BaseDaoAccess<Investigation, Long> implements InvestigationDaoInterface {
    public InvestigationDaoAccess() {
        super(Investigation.class);
    }

    public List<Investigation> searchInvestigation(String searchWord, PageBean pageBean){
        String hql = "from Investigation invest";
        if( searchWord != null && !searchWord.isEmpty() ){
            hql += " where invest.title like '%" + searchWord + "%'";
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Investigation>();
        }
    }

    /**
     * 根据id序列删除多个问卷
     * @param idArray 逗号分隔的问卷id
     */
    public void removeByIdArray(String idArray){
        String hql = "delete from Investigation invest where invest.id in (" + idArray + ")";
        executeUpdate(hql);
    }
}
