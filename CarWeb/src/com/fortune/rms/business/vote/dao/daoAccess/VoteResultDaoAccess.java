package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.VoteResultDaoInterface;
import com.fortune.rms.business.vote.model.VoteResult;
import com.fortune.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2014/12/24.
 */
@Repository
public class VoteResultDaoAccess extends BaseDaoAccess<VoteResult, Long> implements VoteResultDaoInterface {
    public VoteResultDaoAccess() {
        super(VoteResult.class);
    }

    /**
     * 根据投票Id序列删除投票结果
     * @param voteIdArray 投票id序列
     */
    public void removeByVoteArray(String voteIdArray){
        String hql = "delete from VoteResult vr where vr.voteId in (" + voteIdArray + ")";
        executeUpdate(hql);
    }

    /**
     * 查询指定投票Id的总票数
     * @param voteIdArray 投票Id序列，逗号分隔
     * @return voteId,poll
     */
    public List<Object[]> getVotePoll(String voteIdArray){
        String hql = "select vr.voteId, count(vr.voteId) from VoteResult vr where vr.voteId in (" +
                voteIdArray + ") group by vr.voteId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * 投票的总票数
     * @param id 投票Id
     * @return 票数
     */
    public long getVoteTotalPoll(Long id){
        String hql =  "select count(*) from VoteResult vr where vr.voteId=" + id;
        List<Object> objectList = getHibernateTemplate().find(hql);
        if( objectList != null && objectList.size()>0){
            return StringUtils.string2long(objectList.get(0).toString(), 0);
        }else{
            return 0;
        }
    }
}
