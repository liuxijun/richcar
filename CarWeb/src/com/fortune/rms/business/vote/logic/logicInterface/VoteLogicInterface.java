package com.fortune.rms.business.vote.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;
import com.fortune.rms.business.vote.model.Vote;
import com.fortune.rms.business.vote.model.VoteStat;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

/**
 * Created by ÍõÃ÷Â· on 2014/12/26.
 */
public interface VoteLogicInterface extends BaseLogicInterface<Vote> {
    public List<Vote> searchVote(String searchWord, PageBean pageBean);
    public Question saveVote(Vote vote);
    public Vote getVote(Long id);
    public void removeVotes(String idArray);
    public void setStatus(Vote vote);
    public boolean doVote(Long voteId, String userId, Date startTime, Long duration, String selectedOptions);
    public VoteStat getVoteStat(Long voteId);

    public List getVoteAndInvestigationList(PageBean pageBean);
}
