package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.VoteResult;

import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/24.
 */
public interface VoteResultDaoInterface   extends BaseDaoInterface<VoteResult, Long> {
    public void removeByVoteArray(String voteIdArray);
    public List<Object[]> getVotePoll(String voteIdArray);
    public long getVoteTotalPoll(Long id);
}
