package com.fortune.rms.business.live.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.live.model.EPG;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

/**
 * Created by xjliu 20150614
 * EPG DAO
 *
 */
public interface EPGDaoInterface extends BaseDaoInterface<EPG, Long> {
    public List<EPG> getEpgOfLive(Long liveId, Long contentId, Date startTime, Date stopTime, Long status, PageBean pageBean);
}
