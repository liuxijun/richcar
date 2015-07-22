package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.ContentNotice;

import java.util.List;

public interface ContentNoticeDaoInterface extends BaseDaoInterface<ContentNotice, Long> {
    public List<ContentNotice> getOnlineContentNotice(Long status);
    public int updateOnlineContentNotice(Long status);
    public List<ContentNotice> getContentNoticeById(long contentNoticeId);
}