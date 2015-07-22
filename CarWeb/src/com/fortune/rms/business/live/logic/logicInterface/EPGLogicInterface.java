package com.fortune.rms.business.live.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.live.model.EPG;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

/**
 * Created by xjliu on 2015/6/14.
 * 直播节目单Logic
 */
public interface EPGLogicInterface extends BaseLogicInterface<EPG> {
    public static final Long STATUS_FINISHED=3L;
    public static final Long STATUS_WORKING=2L;
    public static final Long STATUS_WAITING=1L;
    List<EPG> getEpgOfLiveNow(Long liveId, Long contentId);
    List<EPG> getEpgOfLive(Long liveId, Long contentId, Date startTime, Date stopTime, Long status, PageBean pageBean);
    EPG insertEPG(EPG epg);
}
