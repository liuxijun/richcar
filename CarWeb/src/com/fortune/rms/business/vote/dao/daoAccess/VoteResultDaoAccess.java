package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.VoteResultDaoInterface;
import com.fortune.rms.business.vote.model.VoteResult;
import com.fortune.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class VoteResultDaoAccess extends BaseDaoAccess<VoteResult, Long> implements VoteResultDaoInterface {
    public VoteResultDaoAccess() {
        super(VoteResult.class);
    }

    /**
     * ����ͶƱId����ɾ��ͶƱ���
     * @param voteIdArray ͶƱid����
     */
    public void removeByVoteArray(String voteIdArray){
        String hql = "delete from VoteResult vr where vr.voteId in (" + voteIdArray + ")";
        executeUpdate(hql);
    }

    /**
     * ��ѯָ��ͶƱId����Ʊ��
     * @param voteIdArray ͶƱId���У����ŷָ�
     * @return voteId,poll
     */
    public List<Object[]> getVotePoll(String voteIdArray){
        String hql = "select vr.voteId, count(vr.voteId) from VoteResult vr where vr.voteId in (" +
                voteIdArray + ") group by vr.voteId";
        return getHibernateTemplate().find(hql);
    }

    /**
     * ͶƱ����Ʊ��
     * @param id ͶƱId
     * @return Ʊ��
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
