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
 * Created by ����· on 2014/12/26.
 */
@Namespace("/vote")
@ParentPackage("default")
@Action(value = "vote")
public class VoteAction extends BaseAction<Vote> {
    private int maxOption;      // ����ѡѡ��
    private Option option;       // ѡ��deprecated
    private String serializedOption;    // �ڿͻ������л����ѡ��
    private String voteIdArray;     // ���Ҫ�����ͶƱId���У���������ɾ��

    /*ǰ̨�û�ͶƱ���*/
    private String userId;      // ͶƱ�û�Id
    private String selectedOptions; // ͶƱѡ���ѡ��
    private Long duration;      // ͶƱ�õ�ʱ��
    private Date startTime;     // ͶƱ��ʼʱ��

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
        // �������б�
        List<Vote> voteList = voteLogicInterface.searchVote(searchWord, pageBean);

        directOut(JsonUtils.getListJsonString("voteList", voteList, "totalCount", pageBean.getRowCount()));
    }

    @Action(value = "save")
    public void saveVote(){
        obj.setLastModified(new Date());
        Question q = voteLogicInterface.saveVote(obj);

        // ѡ��
        if(q != null) {
            log.debug(serializedOption);
            questionLogicInterface.saveOptions(parseOption(serializedOption, q.getId()));
        }

        directOut(
                "{\"success\":" + ((q != null) ? "true" : "false") + "," +
                        "\"questionId\":" + q.getId() + "}"
        );
    }

    // ��ѡ�����л��ַ���������Ϊѡ���б�
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

    // ����ͶƱ����ѡ��, deprecated
    public void saveOption(){
        option = questionLogicInterface.saveOption(option);
        directOut(
                "{\"success\":" +  ((option != null)? "true" : "false") + "}"
        );
    }

    /**
     * ��ȡvote���飬����ѡ��
     * @return Success
     */
    public String getVote(){
        obj = voteLogicInterface.getVote(obj.getId());
        return SUCCESS;
    }

    /**
     * ɾ���ö�ͶƱ��id����voteIdArray��
     * @return Success
     */
    public String removeVotes(){
        voteLogicInterface.removeVotes(voteIdArray);
        return SUCCESS;
    }

    /**
     * ����ͶƱ��������
     * @return Success
     */
    public String setStatus(){
        voteLogicInterface.setStatus(obj);
        return SUCCESS;
    }

    /**
     * �û�ͶƱ���,
     * @return SUCCESS
     */
    public String doVote(){
       setSuccess(voteLogicInterface.doVote(obj.getId(), userId, startTime, duration, selectedOptions));
       return SUCCESS;
    }

    /**
     * ͶƱ���ܽ��
     */
    public void voteStat(){
        VoteStat voteStat = voteLogicInterface.getVoteStat(obj.getId());
        directOut(JsonUtils.getJsonString(voteStat));
    }

}
