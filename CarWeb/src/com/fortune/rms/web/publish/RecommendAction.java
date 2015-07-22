package com.fortune.rms.web.publish;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Namespace("/publish")
@ParentPackage("default")
@Results({
        @Result(name = "listRecommend",location = "/publish/jsonRecommend.jsp")
})
@Action(value="recommend")
public class RecommendAction extends BaseAction<Recommend> {
    private static final long serialVersionUID = 3243534534534534l;
    private RecommendLogicInterface recommendLogicInterface;
    private List<Recommend> recommends;
    private Long type;
    private String name;
    private String code;
    private Long cspId;

    public void setCspId(Long cspId) {
        this.cspId = cspId;
    }

    @SuppressWarnings("unchecked")
    public RecommendAction() {
        super(Recommend.class);
    }

    /**
     * @param recommendLogicInterface the recommendLogicInterface to set
     */
    @Autowired
    public void setRecommendLogicInterface(
            RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
        setBaseLogicInterface(recommendLogicInterface);
    }


    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<Recommend> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<Recommend> recommends) {
        this.recommends = recommends;
    }

    public String save() {
        this.recommendLogicInterface.save(obj);
        if(0!=obj.getCspId()){
            this.recommendLogicInterface.createJsFile(obj.getCspId());
        }
       writeSysLog("保存推荐信息： "+obj.getId()+","+obj.getName());
        return Constants.ACTION_SAVE;
    }

    public String getRecommendsByCspId(){
            log.debug("in getRecommendsByCspId");
            objs=recommendLogicInterface.getRecommendsByCspId(cspId);
            return Constants.ACTION_LIST;
    }

    public String delete() {
        super.delete();
          writeSysLog("删除数据： "+obj.getId()+","+obj.getName());
        return Constants.ACTION_DELETE;
    }

    public String listRecommend() {
        recommends = this.recommendLogicInterface.getRecommend(obj.getCspId(),type, name, code, pageBean);
           return "listRecommend";
    }

    public String getListJson() {
        pageBean.setOrderDir("desc");
        pageBean.setOrderBy("name");
        return JsonUtils.getListJsonString("objs", recommends, "totalCount", pageBean.getRowCount());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
