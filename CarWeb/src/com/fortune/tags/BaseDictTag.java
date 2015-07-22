package com.fortune.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-17
 * Time: 16:13:07
 * base tag
 */
public class BaseDictTag extends BodyTagSupport {
    protected String typeName = null;
    protected final Log log = LogFactory.getLog(getClass());
    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


}
