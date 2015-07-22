package com.fortune.rms.web.vote;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.vote.logic.logicInterface.InvestigationLogicInterface;
import com.fortune.rms.business.vote.model.*;
import com.fortune.util.JsonUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2015/1/7.
 */
@Namespace("/invest")
@ParentPackage("default")
@Action(value = "invest")
public class InvestigationAction extends BaseAction<Investigation> {
    public InvestigationAction() {
        super(Investigation.class);
    }

    private String searchWord;
    private InvestigationLogicInterface investigationLogicInterface;
    private String serializedQuestion;
    private String investIdArray;
    private String serializedInvestResult;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSerializedInvestResult() {
        return serializedInvestResult;
    }

    public void setSerializedInvestResult(String serializedInvestResult) {
        this.serializedInvestResult = serializedInvestResult;
    }

    public String getInvestIdArray() {
        return investIdArray;
    }

    public void setInvestIdArray(String investIdArray) {
        this.investIdArray = investIdArray;
    }

    public String getSerializedQuestion() {
        return serializedQuestion;
    }

    public void setSerializedQuestion(String serializedQuestion) {
        this.serializedQuestion = serializedQuestion;
    }

    public void setInvestigationLogicInterface(InvestigationLogicInterface investigationLogicInterface) {
        this.investigationLogicInterface = investigationLogicInterface;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    @Action(value = "search")
    public void searchVote(){
        // 搜索和列表
        List<Investigation> investigationList = investigationLogicInterface.searchInvestigation(searchWord, pageBean);

        directOut(JsonUtils.getListJsonString("investList", investigationList, "totalCount", pageBean.getRowCount()));
    }

    @Action(value = "save")
    public void saveInvestigation(){
        obj.setLastModified(new Date());
        obj.setQuestionList(parseQuestion(serializedQuestion));
        obj = investigationLogicInterface.saveInvestigation(obj);

        directOut(
                "{\"success\":" + ((obj != null) ? "true" : "false") + "}"
        );
    }

    // 将选项序列化字符串解析成为选项列表
    private List<Question> parseQuestion(String serialized){
        if(serialized == null || serialized.isEmpty()) return null;
        try {
            JSONArray jsonArray = new JSONArray(serialized);
            log.debug("array length:" + jsonArray.length());
            List<Question> questionList = new ArrayList<Question>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Long questionId = Long.parseLong(jsonObject.get("id").toString());
                String questionTitle = "";
                try {
                    questionTitle = java.net.URLDecoder.decode((String) jsonObject.get("title"), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int serial = Integer.parseInt(jsonObject.get("serial").toString());
                int maxOption = Integer.parseInt(jsonObject.get("maxOption").toString());
                // parse question options
                List<Option> optionList = new ArrayList<Option>();
                JSONArray optionArray = new JSONArray(jsonObject.get("optionList").toString());
                for (int j = 0; j < optionArray.length(); j++) {
                    JSONObject optionObject = (JSONObject) optionArray.get(j);
                    Long optionId = Long.parseLong(optionObject.get("id").toString());
                    String optionTitle = "";
                    try {
                        optionTitle = java.net.URLDecoder.decode((String) optionObject.get("title"), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int s = Integer.parseInt(optionObject.get("serial").toString());
                    optionList.add(new Option(optionId, questionId, optionTitle, s));
                }
                Question question = new Question();
                question.setId(questionId);
                question.setTitle(questionTitle);
                question.setSequence((long)serial);
                question.setMaxOption(maxOption);
                question.setOptionList(optionList);

                questionList.add(question);
            }
            return questionList;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获得问卷信息，包括问题和选项信息
     * @return Success
     */
    public String getInvest(){
        obj = investigationLogicInterface.getInvestigation(obj.getId());
        return SUCCESS;
    }

    /**
     * 批量删除问卷
     * @return Success
     */
    public String removeInvests(){
        investigationLogicInterface.removeInvestigations(investIdArray);
        return SUCCESS;
    }

    /**
     * 设置问卷的上下线
     * @return Success
     */
    public String setStatus(){
        investigationLogicInterface.setStatus(obj);
        return SUCCESS;
    }

    /**
     * 保存用户文件结果
     * @return SUCCESS
     */
    public String doInvest(){
        setSuccess(investigationLogicInterface.saveInvestResult(parseInvestigationResult(serializedInvestResult)));
        return SUCCESS;
    }

    /**
     * 解析用户提交的问卷结果，返回InvestigationResultDTO
     * @param result stringify后的结果
     * @return
     */
    private InvestigationResultDTO parseInvestigationResult(String result){
        if(serializedInvestResult == null || serializedInvestResult.isEmpty()) return null;

        try {
            JSONObject jsonObject = new JSONObject(result);

            InvestigationResultDTO dto = new InvestigationResultDTO();
            dto.setInvestigationId( jsonObject.getLong("investId") );
            dto.setUserId( jsonObject.getString("userId"));
            dto.setDuration( jsonObject.getLong("duration"));
            dto.setInvestigateTime(StringUtils.string2date(jsonObject.getString("startTime")));
            // 解析问题选项
            JSONArray questionArray = jsonObject.getJSONArray("questionList");
            if( questionArray != null){
                for(int i = 0; i < questionArray.length(); i++) {
                    JSONObject questionObj = (JSONObject) questionArray.get(i);
                    InvestigationQuestionResult questionResult = new InvestigationQuestionResult();
                    questionResult.setQuestionId( questionObj.getLong("questionId") );
                    JSONArray optionArray = questionObj.getJSONArray("optionList");
                    if(optionArray != null){
                        for(int j=0; j<optionArray.length(); j++){
                            questionResult.addOption(StringUtils.string2long(optionArray.get(j).toString(), -1));
                        }
                    }
                    dto.addQuestionResult(questionResult);
                }
            }

            return dto;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 问卷的汇总结果
     */
    public void investStat(){
        InvestigationStat investigationStat = investigationLogicInterface.getInvestStat(obj.getId());
        directOut(JsonUtils.getJsonString(investigationStat));
    }

    /**
     * 问卷参与用户列表
     */
    public void investUser(){
        List<InvestUser> investUserList = investigationLogicInterface.getInvestUser(obj.getId(), pageBean, searchWord);
        directOut(JsonUtils.getListJsonString("userList", investUserList, "totalCount", pageBean.getRowCount()));
    }

    /**
     * 获得用户文件详情
     * @return Success
     */
    public String getUserInvest(){
        obj = investigationLogicInterface.getInvestigationOfUser(obj.getId(),userId);
        return SUCCESS;
    }
}
