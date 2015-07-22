package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentNoticeDaoInterface;
import com.fortune.rms.business.content.model.ContentNotice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentNoticeDaoAccess extends BaseDaoAccess<ContentNotice, Long>
        implements
        ContentNoticeDaoInterface{

    public ContentNoticeDaoAccess() {
        super(ContentNotice.class);
    }

    public List<ContentNotice> getOnlineContentNotice(Long status){
        String sql = "from ContentNotice where status = "+status;
        return this.getHibernateTemplate().find(sql);
    }
    public int updateOnlineContentNotice(Long status){
        String sql = "update ContentNotice set status=1 where status="+status;
        return executeUpdate(sql);
    }

    public List<ContentNotice>  getContentNoticeById(long contentNoticeId) {
        String sql = "from ContentNotice where id ="+contentNoticeId;
        return this.getHibernateTemplate().find(sql);
    }

}