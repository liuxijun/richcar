package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.ContentNotice;

import java.util.List;

public interface ContentNoticeLogicInterface extends BaseLogicInterface<ContentNotice> {
    public  List<ContentNotice> getOnlineContentNotice(Long status);
    public int updateOnlineContentNotice(Long status);
    public List<ContentNotice> getContentNoticeById(long contentNoticeId);
}
