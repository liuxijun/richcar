/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-15
 * Time: 16:02:32
 * �����ֵ�
 */
package com.fortune.tags;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.DocumentException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.File;

public class TagUtils {
    private static TagUtils ourInstance = new TagUtils();
    private Log logger;

    //�����ѯ��id->name
    private Map<String, Map<String, String>> dictCache = new HashMap<String, Map<String, String>>();
    //�����ѯ, name->id
    private Map<String, Map<String, String>> dictNameCache = new HashMap<String, Map<String, String>>();
    //����dict.xml�е�ÿһ��С�ڵ���Ȼ˳�򱣴���������
    private Map<String, List<String[]>> dictListCache = new HashMap<String, List<String[]>>();
    private long refreshTime = 0;
    private long lastCheckFileDate = 0;
    private String dictionaryFileName = "/dictionary.xml";
    private String absoluteFileName = null;

    public static TagUtils getInstance() {
        return ourInstance;
    }

    //ÿ30�������³�ʼ��

    public void checkRefresh() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - refreshTime > 30 * 60 * 1000l) {
            refreshTime = nowTime;
            refreshDict();
        } else {
            //ÿ��һ���Ӽ���ļ��Ƿ���¹�
            if (nowTime - lastCheckFileDate > 60 * 1000L) {
                File dictFile = new File(absoluteFileName);
                if (dictFile.exists()) {
                    long lastModifiedTime = dictFile.lastModified();
                    if (lastModifiedTime > refreshTime) {
                        refreshDict();
                    }
                }
            }
        }
    }

    public void refreshDict() {
        SAXReader saxReader = new SAXReader();

        logger.debug("��ͼ���ֵ��ļ���" + dictionaryFileName);
        try {
            InputStream is = getClass().getResourceAsStream(dictionaryFileName);
            logger.debug(dictionaryFileName + " InputStream : " + is);
            if (is != null) {
                refreshTime = System.currentTimeMillis();
                try {
                    Document xmlDocument = saxReader.read(is);
                    List dictList = xmlDocument.selectNodes("//dictionary");

                    for (Object aList : dictList) {
                        Node dictNode = (Node) aList;
                        String name = getNodeText(dictNode
                                .selectSingleNode("@name"));
                        Map<String, String> dictItems = new HashMap<String, String>();
                        Map<String, String> dictNames = new HashMap<String, String>();
                        List<String[]> dictListItems = new ArrayList<String[]>();
                        List xmlDictItems = dictNode.selectNodes("item");
                        if (xmlDictItems != null) {
                            for (Object aItem : xmlDictItems) {
                                Node itemNode = (Node) aItem;
                                if (itemNode != null) {
                                    Node itemName = itemNode.selectSingleNode("name");
                                    Node itemValue = itemNode.selectSingleNode("value");
                                    if (itemName != null && itemValue != null) {
                                        String ivalue = itemValue.getText();
                                        String iname = itemName.getText();
                                        dictItems.put(ivalue, iname);
                                        dictNames.put(iname, ivalue);
                                        dictListItems.add(new String[]{ivalue, iname});
                                    }
                                }
                            }
                            dictCache.put(name, dictItems);
                            dictNameCache.put(name, dictNames);
                            dictListCache.put(name, dictListItems);
                        }
                    }
                }catch (DocumentException e) {
                    logger.error("parse the " + dictionaryFileName +
                            " failure", e);
                    e.printStackTrace();
                }catch (Exception e){
                    logger.error("error:"+e.getMessage());
                    e.printStackTrace();
                }
            } else {
                logger.error("�ֵ��ļ������ڣ�");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TagUtils() {
        logger = LogFactory.getLog(TagUtils.class);
        absoluteFileName = getClass().getResource(dictionaryFileName).getFile();
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.indexOf("Window") >= 0) {
                if (absoluteFileName.charAt(0) == '/') {
                    absoluteFileName = absoluteFileName.substring(1);
                }
            }

        }
        checkRefresh();
    }

    private String getNodeText(Node node) {
        if (node == null) {
            return null;
        }

        return node.getText();
    }

    public String getDictName(String typeName,String value){
        Map<String,String> values = getDict(typeName);
        if(values!=null){
            String name = values.get(value);
            if(name!=null){
                return name;
            }
        }else{
        }
        return value;
    }

    public String getDictId(String name, String itemName) {
        Map<String, String> namedDict = getNamesDict(name);
        if (namedDict != null) {
            return namedDict.get(itemName);
        }
        return null;
    }

    /**
     * ��ȡһ�������ѯ�Ļ��棬��������������ҵ���0�� ���Ǹ�language����
     *
     * @param name ��language��
     * @return ����
     */
    public Map<String, String> getNamesDict(String name) {
        checkRefresh();
        return dictNameCache.get(name);
    }

    /**
     * @param name dict
     * @return value
     */
    public Map<String, String> getDict(String name) {
        checkRefresh();
        return dictCache.get(name);
    }

    public List<String[]> getDictList(String name) {
        checkRefresh();
        return dictListCache.get(name);
    }

    public Map<String,List<String[]>> getAllDict(){
        checkRefresh();
        return dictListCache;
    }
}
