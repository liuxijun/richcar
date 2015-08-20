package com.fortune.rms.web.encoder;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTemplateLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTemplate;
import com.fortune.rms.business.module.model.Property;
import com.fortune.util.BeanUtils;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Namespace("/encoder")
@ParentPackage("default")
@Action(value="encoderTemplate")
public class EncoderTemplateAction extends BaseAction<EncoderTemplate> {

    public EncoderTemplateAction() {
        super(EncoderTemplate.class);
    }

   private  EncoderTemplateLogicInterface  encoderTemplateLogicInterface;

    @Autowired
    public void setEncoderTemplateLogicInterface(EncoderTemplateLogicInterface encoderTemplateLogicInterface) {
        this.encoderTemplateLogicInterface = encoderTemplateLogicInterface;
        setBaseLogicInterface(encoderTemplateLogicInterface);
    }

    
    public String list() {
        objs = encoderTemplateLogicInterface.getEncoderTemplates(obj,pageBean);
        return Constants.ACTION_LIST;
    }

    public String delete() {
        try {
            if (keyId != null) {
                obj.setId(StringUtils.string2long(keyId, -1));
                baseLogicInterface.remove(obj);
                log.debug("删除成功，模版ID为："+keyId);
            }
        } catch (Exception e) {
            log.debug("未能成功删除："+keyId);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_DELETE;
    }

    public String save() {
        log.debug("in save");
        try {
            BeanUtils.setDefaultValue(obj,"ACodec","aac");
            BeanUtils.setDefaultValue(obj,"VFrameRate","25");
            BeanUtils.setDefaultValue(obj,"VEncoderType","x264");
            BeanUtils.setDefaultValue(obj,"AChannel","2");
            BeanUtils.setDefaultValue(obj,"VKeyframeInterval","2");
            BeanUtils.setDefaultValue(obj,"VFixedQp","15");
            BeanUtils.setDefaultValue(obj,"VMaxQp","15");
            BeanUtils.setDefaultValue(obj,"ASampleRate","48000");
            BeanUtils.setDefaultValue(obj,"AType","0");
            obj = baseLogicInterface.save(obj);
            writeSysLog("保存" + guessName(obj));
            super.addActionMessage("成功保存数据！");
        } catch (Exception e) {
            super.addActionError("保存数据发生异常：" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Constants.ACTION_SAVE;
    }

    public String deleteSelected() {
        if (keys != null) {
            for (String keyId : keys) {
                try {
                    obj = getBaseObject(keyId);
                    baseLogicInterface.remove(obj);
                    log.debug("删除成功，模版ID为：" + keyId);
                } catch (Exception e) {
                    log.debug("未能成功删除：" + keyId);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return Constants.ACTION_DELETE;
    }

}
