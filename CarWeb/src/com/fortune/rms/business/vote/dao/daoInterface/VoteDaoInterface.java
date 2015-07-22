package com.fortune.rms.business.vote.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.vote.model.Vote;
import com.fortune.util.PageBean;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 * 投票Dao Interface
 */
public interface VoteDaoInterface  extends BaseDaoInterface<Vote, Long> {
    public List<Vote> searchVote(String searchWord, PageBean pageBean);
    public List<Vote> normalVotes();
    public void removeByIdArray(String idArray);
    public List getVoteAndInvestigationList(PageBean pageBean);
    public Vote getLastVote();
}
