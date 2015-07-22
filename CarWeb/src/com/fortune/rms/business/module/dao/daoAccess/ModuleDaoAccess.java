package com.fortune.rms.business.module.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.module.dao.daoInterface.ModuleDaoInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.util.PageBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModuleDaoAccess extends BaseDaoAccess<Module, Long>
		implements
			ModuleDaoInterface {

	public ModuleDaoAccess() {
		super(Module.class);
	}

    public void remove(Module module){
        if(null==module){
            return;
        }
        //�������ý��ʹ�����ģ�棬���鷳�ˣ�Ӧ�õ��������
        String moduleName = module.getName();
        long moduleId = module.getId();
        logger.debug(removeRelatedData(new String[]{
                "MODULE_PROPERTY",
                "CSP_MODULE",
                "RELATED"},moduleName,"MODULE_ID",moduleId));
        super.remove(module);
    }
}
