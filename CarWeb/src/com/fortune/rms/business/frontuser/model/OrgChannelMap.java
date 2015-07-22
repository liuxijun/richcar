package com.fortune.rms.business.frontuser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-27
 * Time: 22:43:58
 * ��֯���û���Ķ�Ӧ
 * ���ڸ�ʽ�����ݣ�һ���ֶ���Ϊ���ֶΣ���һ����ֵ
 *
 */
public class OrgChannelMap {
    private Long id;        // ���ֶΣ���Ƶ������valueΪƵ����Ӧ�������û���id����֮��Ȼ
    private String value;
    List<Long> valueList;

    @SuppressWarnings("unchecked")
    public OrgChannelMap(Long id) {
        this.id = id;
        this.value = "";
        this.valueList = new ArrayList();
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        //return valueList.toString();
        String value = "";
        if( valueList != null && valueList.size() > 0){
            for(Long v: valueList){
                value += value.isEmpty()? v : "," + v;
            }
        }
        return value;
    }

    public void appendValue(Long v){
        //if( v == null) v = "";
        //this.value += this.value.isEmpty()?  v : "," + v;
        if( valueList.contains( v) ) return;
        valueList.add(v);
    }
}
