package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.ContentCsp;

public interface ContentCspDaoInterface
		extends
			BaseDaoInterface<ContentCsp, Long> {

    public int deleteContentRecommend(String contentId, Long cspId);
    public int deleteContentServiceProduct(String contentId, Long cspId);
//    public int deleteContentChannel(String contentId,Long cspId);
//    public int deleteAvailableChannelOfCsp(String contentId,Long cspId);
//    public int deleteChannelChildOfCsp(String contentId,Long cspId);
    public int deleteAllContentChannel(String contentId, Long cspId, String result);
}