package com.fortune.common.web.config;

import com.fortune.common.Constants;
import com.fortune.common.business.base.logic.logicInterface.DictionaryLogicInterface;
import com.fortune.common.business.base.model.Dictionary;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.tomcat.jni.Directory;

import java.util.ArrayList;
import java.util.HashMap;
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
public class DictionaryAction extends BaseAction<Dictionary> {
    private static final long serialVersionUID = 3883538538538538l;
    private DictionaryLogicInterface dictionaryLogicInterface;
    private String typeId;
    private List<Dictionary> items;

    public DictionaryAction() {
        super(Dictionary.class);
    }

    public void setDictionaryLogicInterface(DictionaryLogicInterface dictionaryLogicInterface) {
        this.dictionaryLogicInterface = dictionaryLogicInterface;
        setBaseLogicInterface(dictionaryLogicInterface);
    }

    @SuppressWarnings("unchecked")
    public String list() {
        if(typeId!=null&&!typeId.trim().isEmpty()){
            log.debug("准备搜索字典：code="+typeId);
            try {
                if("all".equals(typeId)){
                    items =(List<Dictionary>) CacheUtils.get(typeId,"dictCache",new DataInitWorker(){
                        public Object init(Object key,String cacheName){
                            List<Dictionary> all= dictionaryLogicInterface.getAll();
                            List<Dictionary> result = new ArrayList<Dictionary>();
                            Map<String,Dictionary> parents = new HashMap<String, Dictionary>();
                            for(Dictionary dict:all){
                                parents.put(dict.getCode(),dict);
                            }
                            for(Dictionary dict:all){
                                Dictionary parent = parents.get(dict.getParentCode());
                                if(parent!=null){
                                    List<Dictionary> parentItems = parent.getItems();
                                    if(parentItems==null){
                                        parentItems = new ArrayList<Dictionary>();
                                        parent.setItems(parentItems);
                                    }
                                    parentItems.add(dict);
                                }
                            }
                            for(Dictionary dict:all){
                                List<Dictionary> dictItems = dict.getItems();
                                if(dictItems!=null&&dictItems.size()>0){
                                    result.add(dict);
                                }
                            }
                            return result;
                        }
                    });
                }else{
                    items =(List<Dictionary>) CacheUtils.get(typeId,"dictCache",new DataInitWorker(){
                        public Object init(Object key,String cacheName){
                            return  dictionaryLogicInterface.getItemsOfCode(typeId,1);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            return super.list();
        }
        return "list";
    }

    public int getTotalCount() {
        if(typeId!=null&&!typeId.isEmpty()) {
            if(items==null){
                return 0;
            }
            return items.size();
        }else{
            return super.getTotalCount();
        }
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<Map<String,Object>> getItemMaps(List<Dictionary> items){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        if(items!=null){
            for(Dictionary item:items){
                result.add(getItemMap(item));
            }
        }
        return result;
    }
    public Map<String,Object> getItemMap(Dictionary dict){
        Map<String,Object> result = new HashMap<String, Object>();
        if(dict!=null){
            result.put("name",dict.getName());
            result.put("value",dict.getCode());
            result.put("items", getItemMaps(dict.getItems()));
        }
        return result;
    }
    public String getJsonObjs(){
        if(typeId!=null&&!typeId.isEmpty()){
            return JsonUtils.getJsonString(getItemMaps(items));
        }else{
            return super.getJsonObjs();
        }
    }
    private boolean willCreateNew=false;
    public String save(){
        Dictionary dict =null;
        if(willCreateNew){
            try {
                dict = dictionaryLogicInterface.get(obj.getCode());
            } catch (Exception e) {
                log.debug("");
            }
        }
        if(dict!=null){
            addActionError("代码“"+dict.getCode()+"”已经被条目“" +dict.getName()+
                    "”使用！请换一个code");
            return "success";
        }else{
            CacheUtils.clear("dictCache");
            return super.save();
        }
    }
    private String deleteDictionaries(List<Dictionary> dictionaries){
        String logs = "";
        if(dictionaries!=null&&dictionaries.size()>0){
            for(Dictionary dictionary:dictionaries){
                logs+=","+dictionary.getName();
                logs+=deleteDictionaries(dictionary.getItems());
                dictionaryLogicInterface.remove(dictionary);
            }
        }
        return logs;
    }
    public String delete(){
        Dictionary dict = dictionaryLogicInterface.get(keyId);
        if(dict!=null){
            List<Dictionary> dictionaries = dictionaryLogicInterface.getItemsOfCode(keyId,1);
            String logs=deleteDictionaries(dictionaries);
            logs=dict.getName()+logs;
            writeSysLog("删除字典条目极其子条目："+logs);
            dictionaryLogicInterface.remove(dict);
            setSuccess(true);
        }else{
            setSuccess(false);
            addActionError("无法删除字典，未发现："+keyId);
        }
        return Constants.ACTION_DELETE;
    }
    public void setWillCreateNew(boolean willCreateNew) {
        this.willCreateNew = willCreateNew;
    }
}
