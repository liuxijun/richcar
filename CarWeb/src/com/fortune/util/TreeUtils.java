/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-5-3
 * Time: 7:08:05
 * tree处理的一些帮助
 */
package com.fortune.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TreeUtils {
    private static TreeUtils ourInstance = new TreeUtils();
    private CacheManager cacheManager;
    private ServletContext context = null;
    private Logger logger= Logger.getLogger(this.getClass());
    public static TreeUtils getInstance(ServletContext context) {
        ourInstance.context = context;
        return ourInstance;
    }

    public static TreeUtils getInstance() {
        return ourInstance;
    }

    private TreeUtils() {
        cacheManager = CacheManager.create();
    }

    public Element initCache(Class classType) {
        String simpleName = classType.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        Map<Object, TreeNode> cacheResult = new HashMap<Object, TreeNode>();
        TreeNode root = new TreeNode("-1", "-100", new Object());
        try {
            Object logicBean;
            String beanName = simpleName + "LogicInterface";
            if (context != null) {
                logicBean = SpringUtils.getBean(beanName, context);
            } else {
                logicBean = SpringUtils.getBeanForApp(beanName);
            }
            List objs = (List) BeanUtils.getProperty(logicBean, "all");
            if (objs == null) {
                objs = new ArrayList();
            }
//            List objs = (List) BeanUtils.getProperty(logicBean, "all");
            for (Object obj : objs) {
                Object idValue = BeanUtils.getProperty(obj, "id");
                if (idValue != null) {
                    Object parentIdValue = BeanUtils.getProperty(obj, "parentId");
/*
                    if (parentIdValue != null) {
                        parentId = Long.parseLong(parentIdValue.toString());
                    }
*/
                    TreeNode treeNode = new TreeNode(idValue.toString(), parentIdValue.toString(), obj);
                    cacheResult.put(idValue.toString(), treeNode);
                }
            }
            //遍历数据，获取父子关系
            for (TreeNode treeNode : cacheResult.values()) {
                if (treeNode.getParentId() == null || treeNode.getParentId().equals("-1")) {
                    root.getSons().add(treeNode);
                }
                for (TreeNode tempNode : cacheResult.values()) {
                    if (treeNode.getId().equals(tempNode.getId())) continue;
                    if (treeNode.getId().equals(tempNode.getParentId())) {
                        //treeNode.getAllChilds().add(tempNode);
                        treeNode.getSons().add(tempNode);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheResult.put("-1", root);
        String keyName = "tree-" + classType.getSimpleName();
        return new Element(keyName, cacheResult);
    }

    @SuppressWarnings("unchecked")
    public Map<Object, TreeNode> getAllOf(Class classType) {
        Cache cache = cacheManager.getCache("tree-cache");
        if (cache == null) {
            cache = new Cache("tree-cache", 1, false, false, 600, 600);
            cacheManager.addCache(cache);
        }
        String keyName = "tree-" + classType.getSimpleName();
        Element element = cache.get(keyName);
        if (element == null) {
            element = initCache(classType);
            cache.put(element);
            cache.flush();
        }
        return (Map<Object, TreeNode>) element.getValue();
    }

    public List getParents(Class classType, Object id){
        if(id==null){
            id="";
        }
        Map<Object, TreeNode> allData = getAllOf(classType);
        TreeNode treeNode = allData.get(id.toString());
        List result = new ArrayList();
        int repeatTimes = 0;
        while(treeNode != null) {
            repeatTimes++;
            if(repeatTimes>50){
                logger.error("获取上级树发生异常，重复次数过多：id="+id+",class="+classType.getName());
                break;
            }
            String parentId = treeNode.getParentId();
            if(parentId!=null&&(!"-1".equals(parentId))){
                treeNode = allData.get(parentId);
                if(treeNode!=null){
                    result.add(treeNode.getObj());
                }else{
                    break;
                }
            }else{
                break;
            }
        }
        return result;

    }
    public List getSonOf(Class classType, Object parentId,String orgIdValue) {
        return getSonOf(classType,parentId,"organId",orgIdValue);
    }
    @SuppressWarnings("unchecked")
    public List getSonOf(Class classType, Object parentId,String orgIdName, String orgIdValue) {
        if (parentId == null) {
            parentId = "";
        }
        Map<Object, TreeNode> allData = getAllOf(classType);
        TreeNode treeNode = allData.get(parentId.toString());
        List result = new ArrayList();
        if (treeNode != null) {
            for (TreeNode son : treeNode.getSons()) {
                if (orgIdValue != null&&!"".equals(orgIdValue.trim())) {
                    Object orgIdVal = BeanUtils.getProperty(son.getObj(), orgIdName);
                    if (orgIdVal != null) {
                        if (orgIdValue.equals(orgIdVal.toString())) {
                            result.add(son.getObj());
                        }
                    }
                } else {
                    result.add(son.getObj());
                }
            }
            return result;
        }
        return new ArrayList();
    }

    public List getAllChildOf(Class classType, Object parentId, long repeatLevel) {
        return getAllChildOf(classType,parentId,repeatLevel,false);
    }
    @SuppressWarnings("unchecked")
    public List getAllChildOf(Class classType, Object parentId, long repeatLevel,boolean fixHeader) {
        Map<Object, TreeNode> allData = getAllOf(classType);
        logger.debug("正在查找"+classType.getSimpleName()+"的"+parentId+"下所有的子节点...");
        TreeNode treeNode = allData.get(parentId.toString());
        List result = new ArrayList();
        if (treeNode != null) {
            for (TreeNode son : treeNode.getSons()) {
                if(fixHeader){//如果需要在name前加上若干空格的话，则进行修正
                    Object obj = BeanUtils.clone(son.getObj());
                    String nameHeader = "　";//全角空格
                    for(int i=0;i<repeatLevel;i++){
                        nameHeader+="　";
                    }
                    BeanUtils.setProperty(obj,"name",nameHeader+BeanUtils.getProperty(obj,"name"));
                    result.add(obj);
                }else{
                    result.add(son.getObj());
                }
                if (repeatLevel < 10) {
                    result.addAll(getAllChildOf(classType, son.id, repeatLevel + 1,fixHeader));
                }
            }
            logger.debug("查找"+classType.getSimpleName()+"的"+parentId+"下所有的子节点完成，发现"+result.size()+"个子节点");
            return result;
        }
        return new ArrayList();
    }

    public List getAllChildOfOrg(Class classType, Object parentId,String orgIdValue, long repeatLevel) {
        return getAllChildOfOrg(classType,parentId,"organId",orgIdValue,repeatLevel);
    }
    @SuppressWarnings("unchecked")
    public List getAllChildOfOrg(Class classType, Object parentId,String orgIdName, String orgIdValue, long repeatLevel) {
        Map<Object, TreeNode> allData = getAllOf(classType);
        TreeNode treeNode = allData.get(parentId.toString());
        List result = new ArrayList();
        if (treeNode != null) {
            for (TreeNode son : treeNode.getSons()) {
                Object orgIdVal = BeanUtils.getProperty(son.getObj(), orgIdName);
                if (orgIdVal != null) {
                    if (orgIdVal.toString().equals(orgIdValue)) {
                        result.add(son.getObj());
                        if (repeatLevel < 10) {
                            result.addAll(getAllChildOfOrg(classType, son.id,orgIdName, orgIdValue, repeatLevel + 1));
                        }
                    }
                }
            }
            return result;
        }
        return new ArrayList();
    }

    public Object getObject(Class classType, Object id) {
        Map<Object, TreeNode> allData = getAllOf(classType);
        TreeNode treeNode = allData.get(id.toString());

        if (treeNode != null) {
            return treeNode.getObj();
        }
        return null;
    }

    public Object getObjectByName(Class classType, String name) {
        return getObjectByNameAndOrg(classType, name, null);
    }

    public Object getObjectByNameAndOrg(Class classType, String name,String orgIdValue) {
        return getObjectByNameAndOrg(classType,name,"organId",orgIdValue);
    }
    public Object getObjectByNameAndOrg(Class classType, String name,String orgIdName, String orgIdValue) {
        Map<Object, TreeNode> allData = getAllOf(classType);

        for (TreeNode treeNode : allData.values()) {
            if (treeNode != null) {
                Object obj = treeNode.getObj();
                if (name.equals(BeanUtils.getProperty(obj, "name"))) {
                    if (orgIdValue != null) {
                        String orgIdStr = (String) BeanUtils.getProperty(obj, orgIdName);
                        if (orgIdStr != null && orgIdStr.equals(orgIdValue)) {
                            return obj;
                        }
                    } else {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    public void addTreeNode(Class classType, Object id, Object parentId, Object obj) {
        Map<Object, TreeNode> allData = getAllOf(classType);
        allData.put(id.toString(), new TreeNode(id.toString(), parentId.toString(), obj));
    }

    public class TreeNode {
        private String id;
        private String parentId;
        private Object obj;
        private List<TreeNode> allChilds;
        private List<TreeNode> sons;

        public TreeNode(String id, String parentId, Object obj) {
            this.id = id;
            this.parentId = parentId;
            this.obj = obj;
            allChilds = new ArrayList<TreeNode>();
            sons = new ArrayList<TreeNode>();
        }

        public Object getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public List<TreeNode> getAllChilds() {
            return allChilds;
        }

        public void setAllChilds(List<TreeNode> allChidls) {
            this.allChilds = allChidls;
        }

        public List<TreeNode> getSons() {
            return sons;
        }

        public void setSons(List<TreeNode> sons) {
            this.sons = sons;
        }
    }
}
