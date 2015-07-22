package com.fortune.rms.business.vote.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.vote.dao.daoAccess.QuestionDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.OptionResultDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.QuestionDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.VoteDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.VoteResultDaoInterface;
import com.fortune.rms.business.vote.logic.logicInterface.QuestionLogicInterface;
import com.fortune.rms.business.vote.logic.logicInterface.VoteLogicInterface;
import com.fortune.rms.business.vote.model.*;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 王明路 on 2014/12/26.
 */
@Service("voteLogicInterface")
public class VoteLogicImpl extends BaseLogicImpl<Vote> implements VoteLogicInterface {
    private VoteDaoInterface voteDaoInterface;
    private QuestionDaoInterface questionDaoInterface;
    private QuestionLogicInterface questionLogicInterface;
    private VoteResultDaoInterface voteResultDaoInterface;
    private OptionResultDaoInterface optionResultDaoInterface;

    @Autowired
    public void setOptionResultDaoInterface(OptionResultDaoInterface optionResultDaoInterface) {
        this.optionResultDaoInterface = optionResultDaoInterface;
    }

    @Autowired
    public void setVoteResultDaoInterface(VoteResultDaoInterface voteResultDaoInterface) {
        this.voteResultDaoInterface = voteResultDaoInterface;
    }

    @Autowired
    public void setQuestionLogicInterface(QuestionLogicInterface questionLogicInterface) {
        this.questionLogicInterface = questionLogicInterface;
    }

    @Autowired
    public void setQuestionDaoInterface(QuestionDaoInterface questionDaoInterface) {
        this.questionDaoInterface = questionDaoInterface;
    }

    @Autowired
    public void setVoteDaoInterface(VoteDaoInterface voteDaoInterface) {
        this.voteDaoInterface = voteDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)voteDaoInterface;
    }

    public List<Vote> searchVote(String searchWord, PageBean pageBean){
        //return voteDaoInterface.searchVote(searchWord, pageBean);
        List<Vote> voteList = voteDaoInterface.searchVote(searchWord, pageBean);
        // 获得投票的票数
        if( voteList != null && voteList.size() > 0 ){
            String voteIdArray = "";
            for(Vote v : voteList){
                voteIdArray += voteIdArray.isEmpty()? v.getId() : ","+v.getId();
            }

            // 获得票数
            List<Object[]> pollList = voteResultDaoInterface.getVotePoll(voteIdArray);
            for(Object[] objects : pollList){
                Long voteId = StringUtils.string2long(objects[0].toString(), -1);
                Long poll = StringUtils.string2long(objects[1].toString(), 0);
                for(Vote vote : voteList){
                    if(vote.getId().equals(voteId)){
                        vote.setTicketCount(poll);
                        break;
                    }
                }
            }
        }

        return voteList;
    }

    /**
     * 保存投票基本信息
     * @param vote 投票基本信息
     * @return 创建成功的投票对应的问题
     */
    public Question saveVote(Vote vote){
        if( vote == null) return null;

        Vote v = null;
        Question q = null;
        if(vote.getId() > 0){
            v = voteDaoInterface.get(vote.getId());
        }
        if( v != null ){
            q = questionDaoInterface.get(v.getQuestionId());
        }
        if( q == null){
            q = new Question();
            q.setCreateTime(new Date());
        }
        q.setTitle(vote.getTitle());
        q.setMaxOption(vote.getMaxOption());
        q = questionDaoInterface.save(q);

        if( v == null){
            v = new Vote();
            v.setCreateTime(new Date());
            // 默认上线
            v.setStatus(Vote.VOTE_STATUS_ONLINE);
        }
        v.setQuestionId(q.getId());
        v.setStartTime(vote.getStartTime());
        v.setEndTime(vote.getEndTime());
        v.setTitle(vote.getTitle());
        v.setLastModified(new Date());
        voteDaoInterface.save(v);

        return  q;
    }

    /**
     * 获取投票详情，包括标题，岂止时间、最多选项数和选项
     * @param id 投票id
     * @return vote
     */
    public Vote getVote(Long id){
        Vote vote = voteDaoInterface.get(id);
        if( vote == null) return null;

        // 获取选项
        Question question = questionDaoInterface.get(vote.getQuestionId());
        if( question != null ){
            vote.setMaxOption( question.getMaxOption() );

            List<Option> optionList = questionDaoInterface.getQuestionOptionList(question.getId());
            vote.setOptionList(optionList);
        }

        return vote;
    }

    /**
     * 批量删除投票
     * @param idArray id序列
     */
    public void removeVotes(String idArray){
        if(idArray == null || idArray.isEmpty()) return;
        // 删除选项结果
        questionLogicInterface.removeQuestionByVote(idArray);
        // 删除投票结果
        voteResultDaoInterface.removeByVoteArray(idArray);
        // 删除投票
        voteDaoInterface.removeByIdArray(idArray);
    }

    /**
     * 设置投票的状态
     * @param vote 投票
     */
    public void setStatus(Vote vote){
        if(vote == null || vote.getId() == null || vote.getStatus() == null) return;
        Vote v = voteDaoInterface.get(vote.getId());
        if( v == null ) return;

        v.setStatus(vote.getStatus());
        voteDaoInterface.save(v);
    }

    /**
     * 投票
     * @param voteId    投票Id
     * @param userId    用户Id
     * @param startTime 投票开始时间
     * @param duration  用时
     * @param selectedOptions 选择的选项id，用逗号分隔
     * @return t/f，如果已经投票，返回false
     */
    public boolean doVote(Long voteId, String userId, Date startTime, Long duration, String selectedOptions){
        // 检查是否已经投票
        VoteResult vr = new VoteResult();
        vr.setUserId(userId);
        vr.setVoteId(voteId);
        try {
            List l = voteResultDaoInterface.getObjects(vr);
            if( l != null && l.size() > 0 ) return false;
        }catch (Exception e){
            e.printStackTrace();
        }

        vr.setVoteTime(startTime);
        vr = voteResultDaoInterface.save(vr);
        if( vr == null ) return false;

        // 保存选项
        String[] optionArray = selectedOptions.split(",");
        for(String optionId : optionArray){
            OptionResult optionResult = new OptionResult();
            optionResult.setOptionId(StringUtils.string2long(optionId, 0));
            optionResult.setResultId(vr.getId());
            optionResult.setType(OptionResult.RESULT_TYPE_VOTE);
            optionResultDaoInterface.save(optionResult);
        }

        return true;
    }

    /**
     * 投票结果
     * @param voteId 投票id
     * @return 投票结果
     */
    public VoteStat getVoteStat(Long voteId){
        Vote vote = new Vote();
        if(voteId > 0) {
            vote = voteDaoInterface.get(voteId)  ;
        }else {
            vote = voteDaoInterface.getLastVote();
        }

        if( vote == null) return null;

        VoteStat voteStat = new VoteStat();
        voteStat.setId(vote.getId());
        voteStat.setTitle(vote.getTitle());

        Question question = questionDaoInterface.get(vote.getQuestionId());
        if( question != null ){
            voteStat.setMaxOption(question.getMaxOption());
        }

        // 总票数
        voteStat.setTotalTicketCount(voteResultDaoInterface.getVoteTotalPoll(vote.getId()));

        // 先查询到所有的选项
        List<Option> optionList = questionDaoInterface.getQuestionOptionList(vote.getQuestionId());
        // 查询选项的得票数
        List<Object[]> pollList = optionResultDaoInterface.getOptionPoll(vote.getQuestionId());
        for(Object[] objects : pollList){
            long optionId = StringUtils.string2long(objects[0].toString(), -1);
            long poll = StringUtils.string2long(objects[1].toString(), 0);

            for(Option option : optionList){
                if(option.getId().equals(optionId)){
                    option.setTicketCount(poll);
                    break;
                }
            }
        }
        voteStat.setOptionList(optionList);

        return voteStat;
    }

    public List getVoteAndInvestigationList(PageBean pageBean) {
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        List list = voteDaoInterface.getVoteAndInvestigationList(pageBean);
        if(list!=null && list.size() > 0) {
             for(Iterator it = list.iterator();it.hasNext();) {
                 Object[] o = (Object[])it.next();
                 Map<String,String> map = new HashMap<String,String>();
                 map.put("id",String.valueOf(o[0]));
                 map.put("title",String.valueOf(o[1]));
                 map.put("type",String.valueOf(o[2]));
                 map.put("createTime",String.valueOf(o[3]));
                 data.add(map);
             }
        }
        return data;
    }
}
