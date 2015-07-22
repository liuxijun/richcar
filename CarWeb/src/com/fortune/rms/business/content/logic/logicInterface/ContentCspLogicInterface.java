package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentCsp;
import com.fortune.rms.business.publish.model.Channel;
import org.hibernate.Session;

import java.util.List;

public interface ContentCspLogicInterface
		extends
			BaseLogicInterface<ContentCsp> {
    public final Long STATUS_ONLINE_PUBLISHED =2L;
    public final Long STATUS_ONLINE=4L;
    public final Long STATUS_WAIT_AUDIT=-1L;
    public final Long STATUS_OFFLINE=1L;
    public final Long STATUS_DELETE=9L;
    public final Long STATUS_RECYCLE=8L;
    public final Long STATUS_APPLY_OFFLINE=6L;
    public final Long STATUS_HIDDEN=-10L;
    public final Long STATUS_NEW=0L;
    public static final Long STATUS_WAIT_TO_ONLINE=3L;
    public static final Long STATUS_WAIT_TO_OFFLINE=6L;
    public Long Sort_Num =0L;

    public String getStatusString(Long status);
    public boolean setStatus(long contentId, long cspId, long channelId, long status);
    public boolean setStatus(long contentId, long cspId, long channelId, long status, long auditId, boolean checkIt);
    public boolean checkStatus(long contentId, long cspId, long channelId, long defaultStatus);
    public boolean publishContent(long contentId, long cspId, long channelId);
    public boolean unPublishContent(long contentId, long cspId, long channelId);
    public int deleteContentRecommend(String contentId, Long cspId);
    public int deleteContentServiceProduct(String contentId, Long cspId);
//    public int deleteContentChannel(String contentId,Long cspId);
//    public int deleteAvailableChannelOfCsp(String contentId,Long cspId);
//    public int deleteChannelChildOfCsp(String contentId,Long cspId);
    public int deleteAllContentChannel(String contentId, Long cspId, String result);
}
