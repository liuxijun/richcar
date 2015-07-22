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
 * Created by 王明路 on 2015/1/7.
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
        // 获得问卷的个数
        if( investigationList != null && investigationList.size() > 0 ){
            String investIdArray = "";
            for(Investigation v : investigationList){
                investIdArray += investIdArray.isEmpty()? v.getId() : ","+v.getId();
            }

            // 获得票数
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

            // 问题数
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
     * 保存问卷，包括问卷问题
     * @param invest            问卷基本信息
     * @return invest对象，正确的Id
     */
    public Investigation saveInvestigation(Investigation invest){
        if(invest == null) return null;

        Investigation investigation;
        // 检查文件是否已经存在
        if(invest.getId() > 0){
            investigation = investigationDaoInterface.get(invest.getId());
            if( investigation == null ){ invest.setId(-1L);}
            else{invest.setCreateTime(investigation.getCreateTime()); invest.setStatus(investigation.getStatus());}
        }

        // 清除不在列表中的问题
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

        // 删除所有的问卷和问题的关联关系，重新添加
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
     * 获得问卷详细信息，包括问题
     * @param id 问卷Id
     * @return 问卷对象
     */
    public Investigation getInvestigation(Long id){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        // 获得问卷问题列表
        investigation.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, false));
        return investigation;
    }

    /**
     * 获得用户文件的详情
     * @param id        问卷Id
     * @param userId    用户Id
     * @return  问卷对象，结果在Question的option中
     */
    public Investigation getInvestigationOfUser(Long id, String userId){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        // 获得问卷问题列表
        investigation.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, false));

        // 获取用户的选择
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
     * 撒谎拿出指定id的调查问卷
     * @param idArray 逗号分隔的问卷id
     */
    public void removeInvestigations(String idArray){
        if(idArray == null || idArray.isEmpty()) return;
        // 删除问卷问题
        questionLogicInterface.removeQuestionByVote(idArray);
        // 删除问卷结果
        investigationResultDaoInterface.removeByInvestArray(idArray);
        // 删除问卷和问题关联关系
        investigationQuestionDaoInterface.removeByInvestigation(idArray);
        // 删除问卷
        investigationDaoInterface.removeByIdArray(idArray);
    }

    /**
     * 设置问卷的状态
     * @param invest 问卷
     */
    public void setStatus(Investigation invest){
        if(invest == null || invest.getId() == null || invest.getStatus() == null) return;
        Investigation investigation = investigationDaoInterface.get(invest.getId());
        if( investigation == null ) return;

        investigation.setStatus(invest.getStatus());
        investigationDaoInterface.save(investigation);
    }

    /**
     * 保存调查结果
     * @param investResult 调查结果
     * @return t/f
     */
    public boolean saveInvestResult(InvestigationResultDTO investResult){
        if(investResult == null) return false;

        // 检查用户是否已经做了问卷
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
        // 保存问题选项结果
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
     * 获得问卷的汇总结果
     * @param id 问卷Id
     * @return 问卷汇总对象
     */
    public InvestigationStat getInvestStat(Long id){
        Investigation investigation = investigationDaoInterface.get(id);
        if(investigation == null) return null;

        InvestigationStat stat = new InvestigationStat(investigation);
        stat.setTotalCount(investigationResultDaoInterface.getInvestigationTotalPoll(id));
        // 平均用时
        stat.setAvgDuration(investigationResultDaoInterface.getInvestigationAverageDuration(id));
        // 问题列表
        stat.setQuestionList(questionLogicInterface.getInvestigationQuestion(id, true));

        return stat;
    }

    /**
     * 获取问卷用户
     * @param id        问卷Id
     * @param pageBean 分页和排序信息
     * @param searchWord 查询条件
     * @return          InvestUser列表
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
