package com.fortune.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fortune.common.business.security.model.Permission;
import com.fortune.common.Constants;
import com.fortune.util.AppConfigurator;
import com.opensymphony.xwork2.ActionContext;

public class PermissionTag extends BodyTagSupport {
	protected final Log log = LogFactory.getLog(getClass());
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	public int doEndTag() {
        boolean checkPermission = AppConfigurator.getInstance().getBoolConfig("checkPermission",false);
        boolean hasPermission = !checkPermission;
        if(checkPermission){
            ActionContext ctx = ActionContext.getContext();
            Map<String, Object> session = ctx.getSession();
            if (session == null)
                return EVAL_PAGE;

            Map<String, Permission> map = (Map<String, Permission>) session
                    .get(Constants.SESSION_ADMIN_PERMISSION);
            if (map == null)
                return EVAL_PAGE;
            if (target == null || "".equals(target))
                return EVAL_PAGE;

            if (map.containsKey(target)) {
                hasPermission = true;
            }
        }
        if (hasPermission) {
            try {
                String bodyValue = bodyContent.getString();
                if (bodyValue == null)
                    return EVAL_PAGE;
                JspWriter out = getPreviousOut();
                out.print(bodyValue);
            } catch (IOException e) {
                log.error("无法输出数据：" + e.getMessage());
            }
        }
		return EVAL_PAGE;
	}

}
