package com.fortune.rms.business.vote.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.vote.dao.daoAccess.OptionDaoAccess;
import com.fortune.rms.business.vote.dao.daoAccess.QuestionDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationQuestionDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.OptionDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.OptionResultDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.QuestionDaoInterface;
import com.fortune.rms.business.vote.logic.logicInterface.QuestionLogicInterface;
import com.fortune.rms.business.vote.model.InvestigationQuestion;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/29.
 */
@Service("questionLogicInterface")
public class QuestionLogicImpl extends BaseLogicImpl<Question> implements QuestionLogicInterface {
    private QuestionDaoInterface questionDaoInterface;
    private OptionDaoInterface optionDaoInterface;
    private OptionResultDaoInterface optionResultDaoInterface;
    private InvestigationQuestionDaoInterface investigationQuestionDaoInterface;

    @Autowired
    public void setInvestigationQuestionDaoInterface(InvestigationQuestionDaoInterface investigationQuestionDaoInterface) {
        this.investigationQuestionDaoInterface = investigationQuestionDaoInterface;
    }

    @Autowired
    public void setOptionResultDaoInterface(OptionResultDaoInterface optionResultDaoInterface) {
        this.optionResultDaoInterface = optionResultDaoInterface;
    }

    @Autowired
    public void setQuestionDaoInterface(QuestionDaoInterface questionDaoInterface) {
        this.questionDaoInterface = questionDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)questionDaoInterface;
    }

    @Autowired
    public void setOptionDaoInterface(OptionDaoInterface optionDaoInterface) {
        this.optionDaoInterface = optionDaoInterface;
    }

    /**
     * 保存问题选项
     *
     * @param option 选项
     * @return 保存后的选项
     */
    public Option saveOption(Option option) {
        if (option == null || option.getQuestionId() == null || option.getQuestionId() <= 0) return null;

        return optionDaoInterface.save(option);
    }

    /**
     * 保存问题选项序列，删除原来存在，但不在列表中的选项
     *
     * @param optionList 选项列表
     */
    public void saveOptions(List<Option> optionList) {
        if (optionList == null || optionList.size() == 0) return;

        // 删除原来的选项，拼接现有id选项
        String currentOptionIds = "", previousOptionIds = "";
        for (Option option : optionList) {
            currentOptionIds += currentOptionIds.isEmpty() ? option.getId() : "," + option.getId();
        }

        // 需要清理原来的选项，先搜出原来的选项
        List<Option> previousOptions = questionDaoInterface.getQuestionOptionList(optionList.get(0).getQuestionId());
        for (Option option : previousOptions) {
            previousOptionIds += previousOptionIds.isEmpty() ? option.getId() : "," + option.getId();
        }

        // 删除已经不再需要的原有选项
        questionDaoInterface.clearOption(currentOptionIds, previousOptionIds);

        // 更新或新建选项
        for (Option option : optionList){
            optionDaoInterface.save(option);
        }
    }

    /**
     * 根据投票id序列删除问题
     * @param voteIdArray 投票id序列
     */
    public void removeQuestionByVote(String voteIdArray){
        if( voteIdArray == null || voteIdArray.isEmpty() ) return;
        // 删除选项结果
        optionResultDaoInterface.removeByVoteIdArray(voteIdArray);
        // 删除选项
        optionDaoInterface.removeByVoteArray(voteIdArray);
        // 删除问题
        questionDaoInterface.removeByVote(voteIdArray);
    }

    /**
     * 根据问卷Id序列删除问题
     * @param investIdArray 问卷id序列
     */
    public void removeQuestionByInvest(String investIdArray){
        if( investIdArray == null || investIdArray.isEmpty() ) return;
        // 删除选项结果
        optionResultDaoInterface.removeByInvestigationIdArray(investIdArray);
        // 删除选项
        optionDaoInterface.removeByInvestArray(investIdArray);
        // 删除问题
        questionDaoInterface.removeByInvest(investIdArray);
    }

    /**
     * 删除问卷问题，exceptedIdArray中的保留
     * @param investId          问卷Id
     * @param exceptedIdArray  要保留的问题Id序列
     */
    public void removeInvestQuestion(Long investId, String exceptedIdArray){
        List<Question> questionList = questionDaoInterface.getInvestQuestionList(investId, exceptedIdArray);
        if(questionList == null || questionList.size() == 0) return;

        for(Question question : questionList){
            // 删除选项结果
            optionResultDaoInterface.removeByQuestion(question.getId());
            // 删除选项
            optionDaoInterface.removeByQuestion(question.getId());
            // 删除问题
            questionDaoInterface.remove(question.getId());
            // 从和问卷的关联表中删除
            InvestigationQuestion investigationQuestion = new InvestigationQuestion();
            investigationQuestion.setQuestionId(question.getId());
            investigationQuestionDaoInterface.remove(investigationQuestion);
        }
    }

    /**
     * 保存问题，包括选项，以及多余选项的清理
     * @param question 问题
     * @return 保存后的选项
     */
    public Question saveQuestion(Question question){
        if( question == null) return null;

        if(question.getId() == null || question.getId() <= 0 || question.getCreateTime() == null){
            question.setCreateTime(new Date());
        }
        List<Option> optionList = question.getOptionList();
        question = questionDaoInterface.save(question);
        for(Option option : optionList){
            option.setQuestionId(question.getId());
        }
        saveOptions(optionList);

        return question;
    }

    /**
     * 获取问卷的所有问题
     * @param investId 问卷Id
     * @param stat 是否包含统计数据
     * @return 问题列表，包括选项
     */
    public List<Question> getInvestigationQuestion(Long investId, boolean stat){
        List<Question> questionList = questionDaoInterface.getInvestQuestionList(investId, null);
        if( questionList == null || questionList.size() == 0) return null;

        // 查询问题在问卷中的序号，重新排序
        InvestigationQuestion investigationQuestion = new InvestigationQuestion();
        investigationQuestion.setInvestigationId(investId);
        List<InvestigationQuestion> investigationQuestionList = null;
        try {
            investigationQuestionList = investigationQuestionDaoInterface.getObjects(investigationQuestion);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(investigationQuestionList != null){
            for(InvestigationQuestion iq : investigationQuestionList){
                for(Question question : questionList){
                    if(iq.getQuestionId().equals(question.getId())){
                        question.setSequence(iq.getSequence());
                        break;
                    }
                }
            }
        }
        // 根据sequence重新排序
        Collections.sort(questionList, new ComparatorQuestion());

        // 获取问题的选项
        List<Option> optionList = optionDaoInterface.getInvestQuestionOption(investId);
        if( optionList != null){
            for(Option option : optionList){
                for(Question question : questionList){
                    if(option.getQuestionId().equals(question.getId())){
                        question.addOption(option);
                        break;
                    }
                }
            }
        }

        // 设置选项的得票数
        if(stat){
            List<Object[]> optionStatList = optionResultDaoInterface.getInvestOptionPoll(investId);
            if(optionStatList != null){
                for(Object[] objects : optionStatList){
                    Long optionId = StringUtils.string2long(objects[0].toString(), -1);
                    Long count = StringUtils.string2long(objects[1].toString(),0);
                    for(Question question : questionList){
                        if(question.setOptionPoll(optionId, count)){
                            break;
                        }
                    }
                }
            }
        }

        return questionList;
    }

    public class ComparatorQuestion implements Comparator {
        public int compare(Object arg0, Object arg1) {
            Question question1 = (Question) arg0;
            Question question2 = (Question) arg1;

            //根据sequence排序
            return question1.getSequence().compareTo(question2.getSequence());
        }

    }
}


