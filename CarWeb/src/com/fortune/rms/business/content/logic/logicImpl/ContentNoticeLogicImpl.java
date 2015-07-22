package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentNoticeDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentNoticeLogicInterface;
import com.fortune.rms.business.content.model.ContentNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("contentNoticeLogicInterface")
public class ContentNoticeLogicImpl extends BaseLogicImpl<ContentNotice>
        implements
        ContentNoticeLogicInterface{
    private ContentNoticeDaoInterface contentNoticeDaoInterface;

    /**
     * @param contentNoticeDaoInterface the contentNoticeDaoInterface to set
     */
    @Autowired
    public void setContentNoticeDaoInterface(ContentNoticeDaoInterface contentNoticeDaoInterface) {
        this.contentNoticeDaoInterface = contentNoticeDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentNoticeDaoInterface;
    }

    public List<ContentNotice> getOnlineContentNotice(Long status){
        return contentNoticeDaoInterface.getOnlineContentNotice(status);
    }
    public int updateOnlineContentNotice(Long status){
        return contentNoticeDaoInterface.updateOnlineContentNotice(status);
    }

    public List<ContentNotice> getContentNoticeById(long contentNoticeId) {
           return contentNoticeDaoInterface.getContentNoticeById(contentNoticeId);
    }
}