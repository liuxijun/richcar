package com.fortune.tags;

import javax.servlet.jsp.JspWriter;
import java.util.List;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-17
 * Time: 16:12:17
 */
public class DictSelectTag extends BaseDictTag {
    private String name;
    private String value;
    private String headerKey;
    private String headerValue;
    private String styleClass;
    private String onchange;
    private String style;

    public String outProperty(String propertyName, String propertyValue) {
        if (propertyName != null && !"".equals(propertyName.trim())
                && propertyValue != null && !"".equals(propertyValue.trim())) {
            return " " + propertyName + "=\"" + propertyValue.replace("\"","\\\"") + "\"";
        }
        return "";
    }

    public int doEndTag() {
        List<String[]> tagDataSet = TagUtils.getInstance().getDictList(typeName);
        try {
            JspWriter out = pageContext.getOut();//getPreviousOut();
            out.print("<select ");
            out.print(outProperty("name", name));
            out.print(outProperty("onchange",onchange));
            out.print(outProperty("class",styleClass));
            out.print(outProperty("style",style));
            out.println(">");
            if (headerKey != null && !"".equals(headerKey)) {
                out.println("<option " + outProperty("value",headerKey)+
                        ">" + headerValue + "</option>");
            }
            if (tagDataSet != null) {
                if (bodyContent != null) {
                    String bodyValue = bodyContent.getString();
                    if (bodyValue != null && !"".equals(bodyValue)) {
                        value = bodyValue;
                    }
                    bodyContent.clearBody();
                }
                
                for (String[] dictItem : tagDataSet) {
                    String objName = dictItem[1];
                    String objValue = dictItem[0];
                    out.print("<option value=\"" + objValue + "\"");
                    if (value != null) {
                        if (value.equals(objValue)) {
                            out.print(" selected ");
                        }
                    }
                    out.println(">" + objName + "</option>");
                }
            }
            out.println("</select>");
        } catch (IOException e) {
            log.error("无法输出数据：" + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
