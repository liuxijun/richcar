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
 * Created by ����· on 2014/12/29.
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
     * ��������ѡ��
     *
     * @param option ѡ��
     * @return ������ѡ��
     */
    public Option saveOption(Option option) {
        if (option == null || option.getQuestionId() == null || option.getQuestionId() <= 0) return null;

        return optionDaoInterface.save(option);
    }

    /**
     * ��������ѡ�����У�ɾ��ԭ�����ڣ��������б��е�ѡ��
     *
     * @param optionList ѡ���б�
     */
    public void saveOptions(List<Option> optionList) {
        if (optionList == null || optionList.size() == 0) return;

        // ɾ��ԭ����ѡ�ƴ������idѡ��
        String currentOptionIds = "", previousOptionIds = "";
        for (Option option : optionList) {
            currentOptionIds += currentOptionIds.isEmpty() ? option.getId() : "," + option.getId();
        }

        // ��Ҫ����ԭ����ѡ����ѳ�ԭ����ѡ��
        List<Option> previousOptions = questionDaoInterface.getQuestionOptionList(optionList.get(0).getQuestionId());
        for (Option option : previousOptions) {
            previousOptionIds += previousOptionIds.isEmpty() ? option.getId() : "," + option.getId();
        }

        // ɾ���Ѿ�������Ҫ��ԭ��ѡ��
        questionDaoInterface.clearOption(currentOptionIds, previousOptionIds);

        // ���»��½�ѡ��
        for (Option option : optionList){
            optionDaoInterface.save(option);
        }
    }

    /**
     * ����ͶƱid����ɾ������
     * @param voteIdArray ͶƱid����
     */
    public void removeQuestionByVote(String voteIdArray){
        if( voteIdArray == null || voteIdArray.isEmpty() ) return;
        // ɾ��ѡ����
        optionResultDaoInterface.removeByVoteIdArray(voteIdArray);
        // ɾ��ѡ��
        optionDaoInterface.removeByVoteArray(voteIdArray);
        // ɾ������
        questionDaoInterface.removeByVote(voteIdArray);
    }

    /**
     * �����ʾ�Id����ɾ������
     * @param investIdArray �ʾ�id����
     */
    public void removeQuestionByInvest(String investIdArray){
        if( investIdArray == null || investIdArray.isEmpty() ) return;
        // ɾ��ѡ����
        optionResultDaoInterface.removeByInvestigationIdArray(investIdArray);
        // ɾ��ѡ��
        optionDaoInterface.removeByInvestArray(investIdArray);
        // ɾ������
        questionDaoInterface.removeByInvest(investIdArray);
    }

    /**
     * ɾ���ʾ����⣬exceptedIdArray�еı���
     * @param investId          �ʾ�Id
     * @param exceptedIdArray  Ҫ����������Id����
     */
    public void removeInvestQuestion(Long investId, String exceptedIdArray){
        List<Question> questionList = questionDaoInterface.getInvestQuestionList(investId, exceptedIdArray);
        if(questionList == null || questionList.size() == 0) return;

        for(Question question : questionList){
            // ɾ��ѡ����
            optionResultDaoInterface.removeByQuestion(question.getId());
            // ɾ��ѡ��
            optionDaoInterface.removeByQuestion(question.getId());
            // ɾ������
            questionDaoInterface.remove(question.getId());
            // �Ӻ��ʾ�Ĺ�������ɾ��
            InvestigationQuestion investigationQuestion = new InvestigationQuestion();
            investigationQuestion.setQuestionId(question.getId());
            investigationQuestionDaoInterface.remove(investigationQuestion);
        }
    }

    /**
     * �������⣬����ѡ��Լ�����ѡ�������
     * @param question ����
     * @return ������ѡ��
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
     * ��ȡ�ʾ����������
     * @param investId �ʾ�Id
     * @param stat �Ƿ����ͳ������
     * @return �����б�����ѡ��
     */
    public List<Question> getInvestigationQuestion(Long investId, boolean stat){
        List<Question> questionList = questionDaoInterface.getInvestQuestionList(investId, null);
        if( questionList == null || questionList.size() == 0) return null;

        // ��ѯ�������ʾ��е���ţ���������
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
        // ����sequence��������
        Collections.sort(questionList, new ComparatorQuestion());

        // ��ȡ�����ѡ��
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

        // ����ѡ��ĵ�Ʊ��
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

            //����sequence����
            return question1.getSequence().compareTo(question2.getSequence());
        }

    }
}


