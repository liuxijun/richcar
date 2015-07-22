package com.fortune.rms.business.template.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.template.dao.daoInterface.ChannelTemplateDaoInterface;
import com.fortune.rms.business.template.model.ChannelTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelTemplateDaoAccess extends BaseDaoAccess<ChannelTemplate, Long>
		implements
        ChannelTemplateDaoInterface {

	public ChannelTemplateDaoAccess() {
		super(ChannelTemplate.class);
	}

}



