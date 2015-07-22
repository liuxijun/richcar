package com.fortune.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspWriter;
import java.util.Map;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-15
 * Time: 21:30:52
 *
 */
public class DictTagSimple extends BaseDictTag {
    public int doEndTag(){
        if(bodyContent!=null){
            String name = null;
            Map tagDataSet = TagUtils.getInstance().getDict(typeName);
            if(tagDataSet!=null){
                name = bodyContent.getString();
                Object nameObj = tagDataSet.get(name);
                if(nameObj!=null){
                    name =nameObj.toString();
                }

            }
            if(name==null){
                name = bodyContent.getString();
            }
            bodyContent.clearBody();
            try {
                JspWriter out = getPreviousOut();
                out.print(name);
            } catch (IOException e) {
                log.error("无法输出数据："+e.getMessage());
            }
        }
        return EVAL_PAGE;
    }

}
