package com.fortune.rms.business.live.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2015/2/26.
 * Live
 */
public interface LiveDaoInterface  extends BaseDaoInterface<Live, Long> {
    public List<Live> searchLive(String channels, String searchValue, PageBean pageBean);
    public List<Live> searchRecord(String channels, String searchValue, PageBean pageBean);
    public Live getLiveByStreamName(String streamName);
    public List<Live> getLiveByTaskId(Long taskId, long[] statusArray);
}
