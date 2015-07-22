package com.fortune.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

import com.fortune.util.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-9-1
 * Time: 10:50:58
 */
public class TextTag extends BodyTagSupport {
    protected final Log log = LogFactory.getLog("fortune-tag");
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*
    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

*/
    public int doEndTag() {
        Configuration config;
        try {
            String configFileName = "/messages_zh.properties";
            config = new Configuration(configFileName);
            String value = config.getValue(name,name);
            //bodyContent.clearBody();
            JspWriter out = pageContext.getOut();
            out.print(value);
            //this.setBodyContent(new BodyContent());
        } catch (IOException e) {
            log.error("无法输出数据：" + e.getMessage());
        }
        if (bodyContent != null) {
            bodyContent.clearBody();
        }
        return EVAL_PAGE;
    }

}
