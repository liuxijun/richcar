package com.fortune.rms.business.content.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-11-5
 * Time: 10:49:09
 * �쳣��������
 */
public class ContentAbnormal {
    public static final int ABNORMAL_CONTENT_TYPE_AUDIT_FAILED = 1;
    public static final int ABNORMAL_CONTENT_TYPE_TRANS_FAILED = 2;
    
    private long id;        // ��Ӧ��content_id
    private long taskId;    // �����ת��ʧ�ܣ�����ת������Id
    private String title;    // ���ݱ���
    private Date createTime; // ����ʱ��
    private int type;        // ʧ������
    private String message;  // ʧ��ԭ������

    public ContentAbnormal() {
    }

    public ContentAbnormal(Content content) {
        this.id = content.getId();
        this.title = content.getName();
        this.createTime = content.getCreateTime();
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String toString() {
        return com.fortune.util.JsonUtils.getJsonString(this);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getSimpleJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, null);
    }

    @SuppressWarnings({"JpaAttributeMemberSignatureInspection"})
    public String getObjJson() {
        return com.fortune.util.JsonUtils.getJsonString(this, "obj.");
    }
}
