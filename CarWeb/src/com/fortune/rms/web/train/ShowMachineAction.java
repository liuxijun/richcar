package com.fortune.rms.web.train;

/**
 * Created by xjliu on 14-6-12.
 *
 */

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.train.logic.logicInterface.ShowMachineLogicInterface;
import com.fortune.rms.business.train.model.ShowMachine;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/train")
@ParentPackage("default")
@Action(value="showMachine")
@Results({

})

public class ShowMachineAction extends BaseAction<ShowMachine> {
    private static final long serialVersionUID = 5743534534534534l;
    private ShowMachineLogicInterface showMachineLogicInterface;
    @SuppressWarnings("unchecked")
    public ShowMachineAction() {
        super(ShowMachine.class);
    }
    /**
     * @param showMachineLogicInterface the showMachineLogicInterface to set
     */
    @Autowired
    public void setShowMachineLogicInterface(ShowMachineLogicInterface showMachineLogicInterface) {
        this.showMachineLogicInterface = showMachineLogicInterface;
        setBaseLogicInterface(showMachineLogicInterface);
    }

}
