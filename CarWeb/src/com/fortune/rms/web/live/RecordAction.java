package com.fortune.rms.web.live;

import com.fortune.common.Constants;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.live.logic.logicInterface.RecordLogicInterface;
import com.fortune.rms.business.live.model.Record;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王明路 on 2015/3/20.
 * 录制任务Action
 */

@Namespace("/live")
@ParentPackage("default")
@Action(value = "record")
public class RecordAction extends BaseAction<Record> {

    private AdminLogicInterface adminLogicInterface;
    private RecordLogicInterface recordLogicInterface;
    private String searchValue;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setAdminLogicInterface(AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
    }

    public void setRecordLogicInterface(RecordLogicInterface recordLogicInterface) {
        this.recordLogicInterface = recordLogicInterface;
    }

    public RecordAction() {
        super(Record.class);
    }

    /**
     * 获取直播列表，用于管理后台直播管理，可能有查询条件，只查询管理员可以管理的栏目范围内的直播
     */
    public void getRecordList(){
        Admin admin = (Admin)session.get(Constants.SESSION_ADMIN);
        if(admin == null){
            directOut(com.fortune.util.JsonUtils.getListJsonString("recordList", new ArrayList<Record>(), "totalCount", -1));
        }else {
            String channels = "";
            if( admin.getIsRoot() != 1 ) {
                channels = adminLogicInterface.getAdminGrantChannel(admin);
            }
            List<Record> recordList = recordLogicInterface.searchRecord(channels, searchValue, pageBean);

            directOut(com.fortune.util.JsonUtils.getListJsonString("recordList", recordList, "totalCount", pageBean.getRowCount()));
        }
    }
}
