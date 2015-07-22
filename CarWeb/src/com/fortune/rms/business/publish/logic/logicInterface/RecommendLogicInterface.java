package com.fortune.rms.business.publish.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.rms.business.publish.model.RecommendDTO;
import com.fortune.util.PageBean;

import java.util.List;

public interface RecommendLogicInterface extends BaseLogicInterface<Recommend> {
    public static final Long NOT_SYSTEM_RECOMMEND = 999L;
    public void createJsFile(long cspId);
    public long getCspIdByCode(String code);
    public List<Recommend> getRecommend(Long cspId, Long type, String name, String code, PageBean pageBean);
    public List<Recommend> getRecommendsByCspId(Long cspId);
    public Recommend getRecommendByChannelId(Long channelId);
    // 获取用户可以观看的推荐列表，列表中包含推荐内容
    public List<RecommendDTO> getChannelRecommendList(FrontUser user, List<Long> channelIdList);
}
