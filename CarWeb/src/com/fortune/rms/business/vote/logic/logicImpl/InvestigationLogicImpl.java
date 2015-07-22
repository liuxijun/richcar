package com.fortune.rms.business.vote.logic.logicImpl;

import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.frontuser.dao.daoInterface.FrontUserDaoInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationQuestionDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.InvestigationResultDaoInterface;
import com.fortune.rms.business.vote.dao.daoInterface.OptionResultDaoInterface;
import com.fortune.rms.business.vote.logic.logicInterface.InvestigationLogicInterface;
import com.fortune.rms.business.vote.logic.logicInterface.QuestionLogicInterface;
import com.fortune.rms.business.vote.model.*;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ����· on 2015/1/7.
 */
@Service("investigationLogicInterface")
public class InvestigationLogicImpl extends BaseLogicImpl<Investigation> implements InvestigationLogicInterface {
    private InvestigationDaoInterface investigationDaoInterface;
    private InvestigationResultDaoInterface investigationResultDaoInterface;
    private InvestigationQuestionDaoInterface investigationQuestionDaoInterface;
    private OptionResultDaoInterface optionResultDaoInterface;
    private FrontUserDaoInterface frontUserDaoInterface;

    @Autowired
    public void setFrontUserDaoInterface(FrontUserDaoInterface frontUserDaoInterface) {
        this.frontUserDaoInterface = frontUserDaoInterface;
    }

    @Autowired
    public void setOptionResultDaoInterface(OptionResultDaoInterface optionResultDaoInterface) {
        this.optionResultDaoInterface = optionResultDaoInterface;
    }

    @Autowired
    public void setInvestigationQuestionDaoInterface(InvestigationQuestionDaoInterface investigationQuestionDaoInterface) {
        this.investigationQuestionDaoInterface = investigationQuestionDaoInterface;
    }

    private QuestionLogicInterface questionLogicInterface;

    @Autowired
    public void setQuestionLogicInterface(QuestionLogicInterface questionLogicInterface) {
        this.questionLogicInterface = questionLogicInterface;
    }

    @Autowired
    public void setInvestigationResultDaoInterface(InvestigationResultDaoInterface investigationResultDaoInterface) {
        this.investigationResultDaoInterface = investigationResultDaoInterface;
    }

    @Autowired
    public void setInvestigationDaoInterface(InvestigationDaoInterface investigationDaoInterface) {
        this.investigationDaoInterface = investigationDaoInterface;
    }

    public List<Investigation> searchInvestigation(String searchWord, PageBean pageBean){
        List<Investigation> investigationList =  investigationDaoInterface.searchInvestigation(searchWord, pageBean);
        // ����ʾ�ĸ���
        if( investigationList != null && investigationList.size() > 0 ){
            String investIdArray = "";
            for(Investigation v : investigationList){
                investIdArray += investIdArray.isEmpty()? v.getId() : ","+v.getId();
            }

            // ���Ʊ��
            List<Object[]> pollList = investigationResultDaoInterface.getInvestigationPoll(investIdArray);
            for(Object[] objects : pollList){
                Long investId = StringUtils.string2long(objects[0].toString(), -1);
                Long poll = StringUtils.string2long(objects[1].toString(), 0);
                for(Investigation invest : investigationList){
                    if(invest.getId().equals(investId)){
                        invest.setPaperCount(poll);
                        break;
                    }
                }
            }

            // ������
            List<Object[]> questionCountList = investigationQuestionDaoInterface.getInvestQuestionCount(investIdArray);
            for(Object[] objects : questionCountList){
                Long investId = StringUtils.string2long(objects[0].toString(), -1);
                Long questionCount = StringUtils.string2long(objects[1].toString(), 0);
                for(Investigation invest : investigationList){
                    if(invest.getId().equals(investId)){
                        invest.setQuestionCount(questionCount);
                        break;
                    }
                }
            }

        }

        return investigationList;
    }

    /**
     * �����ʾ������ʾ�����
     * @param invest            �ʾ������Ϣ
     * @return invest������ȷ��Id
     */
    public Investigation saveInvestigation(Investigation invest){
        if(invest == null) return null;

        Investigation investigation;
        // ����ļ��Ƿ��Ѿ�����
        if(invest.getId() > 0){
            investigation = investigationDaoInterface.get(invest.getId());
            if( investigation == null ){ invest.setId(-1L);}
            else{invest.setCreateTime(investigation.getCreateTime()); invest.setStatus(investigation.getStatus());}
        }

        // ��������б��е�����
        String questionIdArray = "-1";
        if( invest.getId() > 0 && invest.getQuestionList() != null){
            for(Question question : invest.getQuestionList()){
                questionIdArray += "," + question.getId();
            }
            questionLogicInterface.removeInvestQuestion(invest.getId(), questionIdArray);
        }else{
            invest.setCreateTime(new Date());
            invest.setStatus(Investigation.INVESTIGATION_STATUS_ONLINE);
        }
        invest.setLastModified(new Date());

        List<Question> questionList = invest.getQuestionList();
        List<Question> savedQuestionList = new ArrayList<Question>();
        invest = investigationDaoInterface.save(invest);
        for(Question question : questionList){
            Question q = questionLogicInterface.saveQuestion(question);
            question.setId(q.getId());
            savedQuestionList.add(question);
        }

        // ɾ�����е��ʾ������Ĺ�����ϵ���������
        InvestigationQuestion investigationQuestion = new InvestigationQuestion();
        //investigationQuestion.setInvestigationId(invest.getId());
        //investigationQuestionDaoInterface.remove(investigationQuestion);
        investigationQuestionDaoInterface.removeByInvestigation(""+invest.getId());
        for(Question question : savedQuestionList){
            investigationQuestion.setInvestigationId(invest.getId());
            investigationQuestion.setQuestionId(question.getId());
            investigationQuestion.setSequence(question.getSequence());
            investigationQuestionDaoInterface.save(investigationQuestion);
        }

        return invest;
    }

    /**
     * ����ʾ���ϸ��Ϣ����������
     * @param id �ʾ�Id
     * @return �ʾ����
     */
    public Investigation getInvestigation(Long id){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        // ����ʾ������б�
        investigation.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, false));
        return investigation;
    }

    /**
     * ����û��ļ�������
     * @param id        �ʾ�Id
     * @param userId    �û�Id
     * @return  �ʾ���󣬽����Question��option��
     */
    public Investigation getInvestigationOfUser(Long id, String userId){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        // ����ʾ������б�
        investigation.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, false));

        // ��ȡ�û���ѡ��
        if(userId != null && !userId.isEmpty()) {
            InvestigationResult investigationResult = new InvestigationResult();
            investigationResult.setInvestigationId(id);
            investigationResult.setUserId(userId);

            try {
                List<InvestigationResult> list = investigationResultDaoInterface.getObjects(investigationResult);
                if(list != null && list.size()>0){
                    investigationResult = list.get(0);
                    OptionResult optionResult = new OptionResult();
                    optionResult.setResultId(investigationResult.getId());
                    List<OptionResult> optionResultList = optionResultDaoInterface.getObjects(optionResult);
                    if(optionResultList != null){
                        for(OptionResult r : optionResultList){
                            for(Question q : investigation.getQuestionList()){
                                if(q.setOptionSelected(r.getOptionId())) break;
                            }
                        }
                    }
                }
            }catch (Exception e){e.printStackTrace();}
        }
        return investigation;
    }

    /**
     * �����ó�ָ��id�ĵ����ʾ�
     * @param idArray ���ŷָ����ʾ�id
     */
    public void removeInvestigations(String idArray){
        if(idArray == null || idArray.isEmpty()) return;
        // ɾ���ʾ�����
        questionLogicInterface.removeQuestionByVote(idArray);
        // ɾ���ʾ���
        investigationResultDaoInterface.removeByInvestArray(idArray);
        // ɾ���ʾ�����������ϵ
        investigationQuestionDaoInterface.removeByInvestigation(idArray);
        // ɾ���ʾ�
        investigationDaoInterface.removeByIdArray(idArray);
    }

    /**
     * �����ʾ��״̬
     * @param invest �ʾ�
     */
    public void setStatus(Investigation invest){
        if(invest == null || invest.getId() == null || invest.getStatus() == null) return;
        Investigation investigation = investigationDaoInterface.get(invest.getId());
        if( investigation == null ) return;

        investigation.setStatus(invest.getStatus());
        investigationDaoInterface.save(investigation);
    }

    /**
     * ���������
     * @param investResult ������
     * @return t/f
     */
    public boolean saveInvestResult(InvestigationResultDTO investResult){
        if(investResult == null) return false;

        // ����û��Ƿ��Ѿ������ʾ�
        InvestigationResult result = new InvestigationResult();
        result.setInvestigationId(investResult.getInvestigationId());
        result.setUserId(investResult.getUserId());
        List<InvestigationResult> l = null;
        try {
            l = investigationResultDaoInterface.getObjects(result);
        }catch (Exception e){}
        if( l != null && l.size()>0) return false;

        result.setInvestigateTime(investResult.getInvestigateTime());
        result.setDuration(investResult.getDuration());
        result = investigationResultDaoInterface.save(result);
        // ��������ѡ����
        List<InvestigationQuestionResult> questionResultList = investResult.getQuestionList();
        if( questionResultList != null){
            for(InvestigationQuestionResult r : questionResultList){
                for(Long optionId : r.getOptionList()) {
                    OptionResult optionResult = new OptionResult();
                    optionResult.setOptionId(optionId);
                    optionResult.setResultId(result.getId());
                    optionResult.setType(OptionResult.RESULT_TYPE_INVEST);
                    optionResultDaoInterface.save(optionResult);
                }
            }
        }

        return true;
    }

    /**
     * ����ʾ�Ļ��ܽ��
     * @param id �ʾ�Id
     * @return �ʾ���ܶ���
     */
    public InvestigationStat getInvestStat(Long id){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        InvestigationStat stat = new InvestigationStat(investigation);
        stat.setTotalCount(investigationResultDaoInterface.getInvestigationTotalPoll(id));
        // ƽ����ʱ
        stat.setAvgDuration(investigationResultDaoInterface.getInvestigationAverageDuration(id));
        // �����б�
        stat.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, true));

        return stat;
    }

    /**
     * ��ȡ�ʾ��û�
     * @param id        �ʾ�Id
     * @param pageBean ��ҳ��������Ϣ
     * @param searchWord ��ѯ����
     * @return          InvestUser�б�
     */
    public List<InvestUser> getInvestUser(Long id, PageBean pageBean,String searchWord){
        List<InvestigationResult> investigationResultList = investigationResultDaoInterface.getInvestResult(
                id, pageBean,searchWord);
        if(investigationResultList == null) return  null;

        List<InvestUser> investUserList = new ArrayList<InvestUser>();
        for(InvestigationResult r : investigationResultList){
            InvestUser investUser = new InvestUser(r);
            FrontUser frontUser = frontUserDaoInterface.get(r.getUserId());
            investUser.setName(frontUser == null? "" : frontUser.getName());
            investUserList.add(investUser);
        }

        return investUserList;
    }
}
