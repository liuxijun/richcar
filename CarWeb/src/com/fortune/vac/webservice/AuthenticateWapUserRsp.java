/**
 * AuthenticateWapUserRsp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class AuthenticateWapUserRsp  implements java.io.Serializable {
    private String streamingNo;

    private int resultCode;

    private int registered;

    private String SPID;

    private int testUserTag;

    private int corpTag;

    private String corpID;

    public AuthenticateWapUserRsp() {
    }

    public AuthenticateWapUserRsp(
           String streamingNo,
           int resultCode,
           int registered,
           String SPID,
           int testUserTag,
           int corpTag,
           String corpID) {
           this.streamingNo = streamingNo;
           this.resultCode = resultCode;
           this.registered = registered;
           this.SPID = SPID;
           this.testUserTag = testUserTag;
           this.corpTag = corpTag;
           this.corpID = corpID;
    }


    /**
     * Gets the streamingNo value for this AuthenticateWapUserRsp.
     * 
     * @return streamingNo
     */
    public String getStreamingNo() {
        return streamingNo;
    }


    /**
     * Sets the streamingNo value for this AuthenticateWapUserRsp.
     * 
     * @param streamingNo
     */
    public void setStreamingNo(String streamingNo) {
        this.streamingNo = streamingNo;
    }


    /**
     * Gets the resultCode value for this AuthenticateWapUserRsp.
     * 
     * @return resultCode
     */
    public int getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this AuthenticateWapUserRsp.
     * 
     * @param resultCode
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the registered value for this AuthenticateWapUserRsp.
     * 
     * @return registered
     */
    public int getRegistered() {
        return registered;
    }


    /**
     * Sets the registered value for this AuthenticateWapUserRsp.
     * 
     * @param registered
     */
    public void setRegistered(int registered) {
        this.registered = registered;
    }


    /**
     * Gets the SPID value for this AuthenticateWapUserRsp.
     * 
     * @return SPID
     */
    public String getSPID() {
        return SPID;
    }


    /**
     * Sets the SPID value for this AuthenticateWapUserRsp.
     * 
     * @param SPID
     */
    public void setSPID(String SPID) {
        this.SPID = SPID;
    }


    /**
     * Gets the testUserTag value for this AuthenticateWapUserRsp.
     * 
     * @return testUserTag
     */
    public int getTestUserTag() {
        return testUserTag;
    }


    /**
     * Sets the testUserTag value for this AuthenticateWapUserRsp.
     * 
     * @param testUserTag
     */
    public void setTestUserTag(int testUserTag) {
        this.testUserTag = testUserTag;
    }


    /**
     * Gets the corpTag value for this AuthenticateWapUserRsp.
     * 
     * @return corpTag
     */
    public int getCorpTag() {
        return corpTag;
    }


    /**
     * Sets the corpTag value for this AuthenticateWapUserRsp.
     * 
     * @param corpTag
     */
    public void setCorpTag(int corpTag) {
        this.corpTag = corpTag;
    }


    /**
     * Gets the corpID value for this AuthenticateWapUserRsp.
     * 
     * @return corpID
     */
    public String getCorpID() {
        return corpID;
    }


    /**
     * Sets the corpID value for this AuthenticateWapUserRsp.
     * 
     * @param corpID
     */
    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof AuthenticateWapUserRsp)) return false;
        AuthenticateWapUserRsp other = (AuthenticateWapUserRsp) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.streamingNo==null && other.getStreamingNo()==null) || 
             (this.streamingNo!=null &&
              this.streamingNo.equals(other.getStreamingNo()))) &&
            this.resultCode == other.getResultCode() &&
            this.registered == other.getRegistered() &&
            ((this.SPID==null && other.getSPID()==null) || 
             (this.SPID!=null &&
              this.SPID.equals(other.getSPID()))) &&
            this.testUserTag == other.getTestUserTag() &&
            this.corpTag == other.getCorpTag() &&
            ((this.corpID==null && other.getCorpID()==null) || 
             (this.corpID!=null &&
              this.corpID.equals(other.getCorpID())));
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
        if (getStreamingNo() != null) {
            _hashCode += getStreamingNo().hashCode();
        }
        _hashCode += getResultCode();
        _hashCode += getRegistered();
        if (getSPID() != null) {
            _hashCode += getSPID().hashCode();
        }
        _hashCode += getTestUserTag();
        _hashCode += getCorpTag();
        if (getCorpID() != null) {
            _hashCode += getCorpID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AuthenticateWapUserRsp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "authenticateWapUserRsp"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streamingNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "streamingNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "resultCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "registered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SPID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SPID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("testUserTag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "testUserTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corpTag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "corpTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corpID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "corpID"));
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
