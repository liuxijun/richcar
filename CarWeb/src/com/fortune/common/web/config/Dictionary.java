package com.fortune.common.web.config;

import com.fortune.common.web.base.FortuneAction;
import com.fortune.tags.TagUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-18
 * Time: 13:38:46
 * 服务器配置文件
 */
@Namespace("/config")
@ParentPackage("default")
@Action(value="dict")
public class Dictionary extends FortuneAction {
    private String typeId;
    private int totalCount;
    TagUtils tagUtils = TagUtils.getInstance();
    private List<String[]> items;
    private Map<String,List<String[]>> allItems;
    public String list() {
        if(typeId!=null){
            log.debug("typeId="+typeId);
            try {
                if("all".equals(typeId)){
                     allItems = tagUtils.getAllDict();
                }
                items = tagUtils.getDictList(typeId);
                if(items!=null){
                    totalCount = items.size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "list";
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getItemJson(List<String[]> item){
        StringBuffer result = new StringBuffer();
        result.append("\t[\n");
        if(item!=null){
            int i=0;
             for(String[] values:item){
                 if(i>0){
                     result.append(",\n");
                 }
                 if(values.length>=2){
                     String value=values[0];
                     String name = values[1];
                     result.append("\t\t{name:\"").append(name).append("\",value:\"").append(value.replace("\"","\\\"")).append("\"}");
                 }
                 i++;
             }
        }
        result.append("\n\t]\n");
        return result.toString();
    }
    public String getJsonObjs(){
        StringBuffer result = new StringBuffer();
        result.append("  [\n");
        if("all".equals(typeId)){
            if(allItems!=null){
                int i=0;
                for(String itemName:allItems.keySet()){
                    List<String[]> item = allItems.get(itemName);
                    if(i>0){
                        result.append(",\n");
                    }
                    i++;
                    result.append("    {dictName:\"").append(itemName).append("\",dictItems:\n").append(getItemJson(item)).append("    }");
                }
            }
        }else if(typeId!=null){
            result.append(getItemJson(items));
        }
        result.append("\n  ]\n");
        return result.toString();
    }
}
