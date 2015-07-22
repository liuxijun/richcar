package com.fortune.rms.business.content.model;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-16
 * Time: 9:40:01
 * һ��ý���ļ���Ӧ����Ϣ
 */
public class RedexContentFile implements Comparable<RedexContentFile>{
    private Long id;
    private Long index;     // ����
    private String name;    // ����
    private String url;     // �ļ�����

    public RedexContentFile() {
    }

    public RedexContentFile(Long id, Long index, String name, String url) {
        this.id = id;
        this.index = index;
        this.name = name;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //@Override
    public int compareTo(RedexContentFile a) {
        return this.index.compareTo(a.getIndex());
    }  
}
