package com.fortune.rms.web.user;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.user.logic.logicInterface.StbLogicInterface;
import com.fortune.rms.business.user.model.Stb;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/user")
@ParentPackage("default")
@Action(value="stb")
public class StbAction extends BaseAction<Stb> {
    public StbAction() {
        super(Stb.class);
    }

    private StbLogicInterface stbLogicInterface;


    @Autowired
    public void setStbLogicInterface(StbLogicInterface stbLogicInterface) {
        this.stbLogicInterface = stbLogicInterface;
        setBaseLogicInterface(stbLogicInterface);
    }

//验证系列号serialNo是否重复
public String checkExists() {
    log.debug("in checkExists.");
    setSuccess(true);
    setSuccess(stbLogicInterface.selectStb(obj));
    return Constants.ACTION_VIEW;
}
}
