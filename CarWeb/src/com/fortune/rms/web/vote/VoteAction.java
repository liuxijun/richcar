package com.fortune.rms.web.vote;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.vote.logic.logicInterface.QuestionLogicInterface;
import com.fortune.rms.business.vote.logic.logicInterface.VoteLogicInterface;
import com.fortune.rms.business.vote.model.Option;
import com.fortune.rms.business.vote.model.Question;
import com.fortune.rms.business.vote.model.Vote;
import com.fortune.rms.business.vote.model.VoteStat;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/26.
 */
@Namespace("/vote")
@ParentPackage("default")
@Action(value = "vote")
public class VoteAction extends BaseAction<Vote> {
    private int maxOption;      // 最多可选选项
    private Option option;       // 选项deprecated
    private String serializedOption;    // 在客户端序列化后的选项
    private String voteIdArray;     // 多个要处理的投票Id序列，用于批量删除

    /*前台用户投票相关*/
    private String userId;      // 投票用户Id
    private String selectedOptions; // 投票选择的选项
    private Long duration;      // 投票用的时间
    private Date startTime;     // 投票开始时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /*******************************************************/

    public String getVoteIdArray() {
        return voteIdArray;
    }

    public void setVoteIdArray(String voteIdArray) {
        this.voteIdArray = voteIdArray;
    }

    public String getSerializedOption() {
        return serializedOption;
    }

    public void setSerializedOption(String serializedOption) {
        this.serializedOption = serializedOption;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public int getMaxOption() {
        return maxOption;
    }

    public void setMaxOption(int maxOption) {
        this.maxOption = maxOption;
    }

    public VoteAction() {
        super(Vote.class);
    }

    private String searchWord;
    private VoteLogicInterface voteLogicInterface;
    private QuestionLogicInterface questionLogicInterface;

    public void setQuestionLogicInterface(QuestionLogicInterface questionLogicInterface) {
        this.questionLogicInterface = questionLogicInterface;
    }

    public void setVoteLogicInterface(VoteLogicInterface voteLogicInterface) {
        this.voteLogicInterface = voteLogicInterface;
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
        List<Vote> voteList = voteLogicInterface.searchVote(searchWord, pageBean);

        directOut(JsonUtils.getListJsonString("voteList", voteList, "totalCount", pageBean.getRowCount()));
    }

    @Action(value = "save")
    public void saveVote(){
        obj.setLastModified(new Date());
        Question q = voteLogicInterface.saveVote(obj);

        // 选项
        if(q != null) {
            log.debug(serializedOption);
            questionLogicInterface.saveOptions(parseOption(serializedOption, q.getId()));
        }

        directOut(
                "{\"success\":" + ((q != null) ? "true" : "false") + "," +
                        "\"questionId\":" + q.getId() + "}"
        );
    }

    // 将选项序列化字符串解析成为选项列表
    private List<Option> parseOption(String serialized, Long questionId){
        if(serialized == null || serialized.isEmpty()) return null;
        try {
            JSONArray jsonArray = new JSONArray(serialized);
            log.debug("array length:" + jsonArray.length());
            List<Option> optionList = new ArrayList<Option>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Long optionId = Long.parseLong(jsonObject.get("id").toString());
                String optionTitle = "";
                try {
                    optionTitle = java.net.URLDecoder.decode((String) jsonObject.get("title"), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int serial = Integer.parseInt(jsonObject.get("serial").toString());
                optionList.add(new Option(optionId, questionId, optionTitle, serial));
            }
            return optionList;
        }catch (Exception e){
            return null;
        }
    }

    // 保存投票问题选项, deprecated
    public void saveOption(){
        option = questionLogicInterface.saveOption(option);
        directOut(
                "{\"success\":" +  ((option != null)? "true" : "false") + "}"
        );
    }

    /**
     * 获取vote详情，包括选项
     * @return Success
     */
    public String getVote(){
        obj = voteLogicInterface.getVote(obj.getId());
        return SUCCESS;
    }

    /**
     * 删除好多投票，id存在voteIdArray里
     * @return Success
     */
    public String removeVotes(){
        voteLogicInterface.removeVotes(voteIdArray);
        return SUCCESS;
    }

    /**
     * 设置投票的上下线
     * @return Success
     */
    public String setStatus(){
        voteLogicInterface.setStatus(obj);
        return SUCCESS;
    }

    /**
     * 用户投票结果,
     * @return SUCCESS
     */
    public String doVote(){
       setSuccess(voteLogicInterface.doVote(obj.getId(), userId, startTime, duration, selectedOptions));
       return SUCCESS;
    }

    /**
     * 投票汇总结果
     */
    public void voteStat(){
        VoteStat voteStat = voteLogicInterface.getVoteStat(obj.getId());
        directOut(JsonUtils.getJsonString(voteStat));
    }

}
