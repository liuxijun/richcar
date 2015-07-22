/**
 * SubInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class SubInfo  implements java.io.Serializable {
    private int VACSub;

    private String spId;

    private String sp_productId;

    private String spec_productId;

    private String productId;

    private String DA;

    private int DAType;

    private String FA;

    private int FAType;

    private String OA;

    private int OAType;

    private String corpID;

    private String effectiveTime;

    private String expireTime;

    private String resumeTime;

    private int status;

    private String subscribeTime;

    private String suspendTime;

    public SubInfo() {
    }

    public SubInfo(
           int VACSub,
           String spId,
           String sp_productId,
           String spec_productId,
           String productId,
           String DA,
           int DAType,
           String FA,
           int FAType,
           String OA,
           int OAType,
           String corpID,
           String effectiveTime,
           String expireTime,
           String resumeTime,
           int status,
           String subscribeTime,
           String suspendTime) {
           this.VACSub = VACSub;
           this.spId = spId;
           this.sp_productId = sp_productId;
           this.spec_productId = spec_productId;
           this.productId = productId;
           this.DA = DA;
           this.DAType = DAType;
           this.FA = FA;
           this.FAType = FAType;
           this.OA = OA;
           this.OAType = OAType;
           this.corpID = corpID;
           this.effectiveTime = effectiveTime;
           this.expireTime = expireTime;
           this.resumeTime = resumeTime;
           this.status = status;
           this.subscribeTime = subscribeTime;
           this.suspendTime = suspendTime;
    }


    /**
     * Gets the VACSub value for this SubInfo.
     * 
     * @return VACSub
     */
    public int getVACSub() {
        return VACSub;
    }


    /**
     * Sets the VACSub value for this SubInfo.
     * 
     * @param VACSub
     */
    public void setVACSub(int VACSub) {
        this.VACSub = VACSub;
    }


    /**
     * Gets the spId value for this SubInfo.
     * 
     * @return spId
     */
    public String getSpId() {
        return spId;
    }


    /**
     * Sets the spId value for this SubInfo.
     * 
     * @param spId
     */
    public void setSpId(String spId) {
        this.spId = spId;
    }


    /**
     * Gets the sp_productId value for this SubInfo.
     * 
     * @return sp_productId
     */
    public String getSp_productId() {
        return sp_productId;
    }


    /**
     * Sets the sp_productId value for this SubInfo.
     * 
     * @param sp_productId
     */
    public void setSp_productId(String sp_productId) {
        this.sp_productId = sp_productId;
    }


    /**
     * Gets the spec_productId value for this SubInfo.
     * 
     * @return spec_productId
     */
    public String getSpec_productId() {
        return spec_productId;
    }


    /**
     * Sets the spec_productId value for this SubInfo.
     * 
     * @param spec_productId
     */
    public void setSpec_productId(String spec_productId) {
        this.spec_productId = spec_productId;
    }


    /**
     * Gets the productId value for this SubInfo.
     * 
     * @return productId
     */
    public String getProductId() {
        return productId;
    }


    /**
     * Sets the productId value for this SubInfo.
     * 
     * @param productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }


    /**
     * Gets the DA value for this SubInfo.
     * 
     * @return DA
     */
    public String getDA() {
        return DA;
    }


    /**
     * Sets the DA value for this SubInfo.
     * 
     * @param DA
     */
    public void setDA(String DA) {
        this.DA = DA;
    }


    /**
     * Gets the DAType value for this SubInfo.
     * 
     * @return DAType
     */
    public int getDAType() {
        return DAType;
    }


    /**
     * Sets the DAType value for this SubInfo.
     * 
     * @param DAType
     */
    public void setDAType(int DAType) {
        this.DAType = DAType;
    }


    /**
     * Gets the FA value for this SubInfo.
     * 
     * @return FA
     */
    public String getFA() {
        return FA;
    }


    /**
     * Sets the FA value for this SubInfo.
     * 
     * @param FA
     */
    public void setFA(String FA) {
        this.FA = FA;
    }


    /**
     * Gets the FAType value for this SubInfo.
     * 
     * @return FAType
     */
    public int getFAType() {
        return FAType;
    }


    /**
     * Sets the FAType value for this SubInfo.
     * 
     * @param FAType
     */
    public void setFAType(int FAType) {
        this.FAType = FAType;
    }


    /**
     * Gets the OA value for this SubInfo.
     * 
     * @return OA
     */
    public String getOA() {
        return OA;
    }


    /**
     * Sets the OA value for this SubInfo.
     * 
     * @param OA
     */
    public void setOA(String OA) {
        this.OA = OA;
    }


    /**
     * Gets the OAType value for this SubInfo.
     * 
     * @return OAType
     */
    public int getOAType() {
        return OAType;
    }


    /**
     * Sets the OAType value for this SubInfo.
     * 
     * @param OAType
     */
    public void setOAType(int OAType) {
        this.OAType = OAType;
    }


    /**
     * Gets the corpID value for this SubInfo.
     * 
     * @return corpID
     */
    public String getCorpID() {
        return corpID;
    }


    /**
     * Sets the corpID value for this SubInfo.
     * 
     * @param corpID
     */
    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }


    /**
     * Gets the effectiveTime value for this SubInfo.
     * 
     * @return effectiveTime
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }


    /**
     * Sets the effectiveTime value for this SubInfo.
     * 
     * @param effectiveTime
     */
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }


    /**
     * Gets the expireTime value for this SubInfo.
     * 
     * @return expireTime
     */
    public String getExpireTime() {
        return expireTime;
    }


    /**
     * Sets the expireTime value for this SubInfo.
     * 
     * @param expireTime
     */
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }


    /**
     * Gets the resumeTime value for this SubInfo.
     * 
     * @return resumeTime
     */
    public String getResumeTime() {
        return resumeTime;
    }


    /**
     * Sets the resumeTime value for this SubInfo.
     * 
     * @param resumeTime
     */
    public void setResumeTime(String resumeTime) {
        this.resumeTime = resumeTime;
    }


    /**
     * Gets the status value for this SubInfo.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this SubInfo.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Gets the subscribeTime value for this SubInfo.
     * 
     * @return subscribeTime
     */
    public String getSubscribeTime() {
        return subscribeTime;
    }


    /**
     * Sets the subscribeTime value for this SubInfo.
     * 
     * @param subscribeTime
     */
    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }


    /**
     * Gets the suspendTime value for this SubInfo.
     * 
     * @return suspendTime
     */
    public String getSuspendTime() {
        return suspendTime;
    }


    /**
     * Sets the suspendTime value for this SubInfo.
     * 
     * @param suspendTime
     */
    public void setSuspendTime(String suspendTime) {
        this.suspendTime = suspendTime;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SubInfo)) return false;
        SubInfo other = (SubInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.VACSub == other.getVACSub() &&
            ((this.spId==null && other.getSpId()==null) || 
             (this.spId!=null &&
              this.spId.equals(other.getSpId()))) &&
            ((this.sp_productId==null && other.getSp_productId()==null) || 
             (this.sp_productId!=null &&
              this.sp_productId.equals(other.getSp_productId()))) &&
            ((this.spec_productId==null && other.getSpec_productId()==null) || 
             (this.spec_productId!=null &&
              this.spec_productId.equals(other.getSpec_productId()))) &&
            ((this.productId==null && other.getProductId()==null) || 
             (this.productId!=null &&
              this.productId.equals(other.getProductId()))) &&
            ((this.DA==null && other.getDA()==null) || 
             (this.DA!=null &&
              this.DA.equals(other.getDA()))) &&
            this.DAType == other.getDAType() &&
            ((this.FA==null && other.getFA()==null) || 
             (this.FA!=null &&
              this.FA.equals(other.getFA()))) &&
            this.FAType == other.getFAType() &&
            ((this.OA==null && other.getOA()==null) || 
             (this.OA!=null &&
              this.OA.equals(other.getOA()))) &&
            this.OAType == other.getOAType() &&
            ((this.corpID==null && other.getCorpID()==null) || 
             (this.corpID!=null &&
              this.corpID.equals(other.getCorpID()))) &&
            ((this.effectiveTime==null && other.getEffectiveTime()==null) || 
             (this.effectiveTime!=null &&
              this.effectiveTime.equals(other.getEffectiveTime()))) &&
            ((this.expireTime==null && other.getExpireTime()==null) || 
             (this.expireTime!=null &&
              this.expireTime.equals(other.getExpireTime()))) &&
            ((this.resumeTime==null && other.getResumeTime()==null) || 
             (this.resumeTime!=null &&
              this.resumeTime.equals(other.getResumeTime()))) &&
            this.status == other.getStatus() &&
            ((this.subscribeTime==null && other.getSubscribeTime()==null) || 
             (this.subscribeTime!=null &&
              this.subscribeTime.equals(other.getSubscribeTime()))) &&
            ((this.suspendTime==null && other.getSuspendTime()==null) || 
             (this.suspendTime!=null &&
              this.suspendTime.equals(other.getSuspendTime())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getVACSub();
        if (getSpId() != null) {
            _hashCode += getSpId().hashCode();
        }
        if (getSp_productId() != null) {
            _hashCode += getSp_productId().hashCode();
        }
        if (getSpec_productId() != null) {
            _hashCode += getSpec_productId().hashCode();
        }
        if (getProductId() != null) {
            _hashCode += getProductId().hashCode();
        }
        if (getDA() != null) {
            _hashCode += getDA().hashCode();
        }
        _hashCode += getDAType();
        if (getFA() != null) {
            _hashCode += getFA().hashCode();
        }
        _hashCode += getFAType();
        if (getOA() != null) {
            _hashCode += getOA().hashCode();
        }
        _hashCode += getOAType();
        if (getCorpID() != null) {
            _hashCode += getCorpID().hashCode();
        }
        if (getEffectiveTime() != null) {
            _hashCode += getEffectiveTime().hashCode();
        }
        if (getExpireTime() != null) {
            _hashCode += getExpireTime().hashCode();
        }
        if (getResumeTime() != null) {
            _hashCode += getResumeTime().hashCode();
        }
        _hashCode += getStatus();
        if (getSubscribeTime() != null) {
            _hashCode += getSubscribeTime().hashCode();
        }
        if (getSuspendTime() != null) {
            _hashCode += getSuspendTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VACSub");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "VACSub"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "spId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sp_productId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "sp_productId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spec_productId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "spec_productId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "productId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "DA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DAType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "DAType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "FA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FAType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "FAType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "OA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OAType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "OAType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corpID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "corpID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("effectiveTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "effectiveTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expireTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "expireTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resumeTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "resumeTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscribeTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "subscribeTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suspendTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "suspendTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
