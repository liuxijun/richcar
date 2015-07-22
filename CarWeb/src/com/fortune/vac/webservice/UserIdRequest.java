/**
 * UserIdRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class UserIdRequest  implements java.io.Serializable {
    private int srcDeviceType;

    private int srcDeviceId;

    private String userId;

    private int userIdType;

    public UserIdRequest() {
    }

    public UserIdRequest(
           int srcDeviceType,
           int srcDeviceId,
           String userId,
           int userIdType) {
           this.srcDeviceType = srcDeviceType;
           this.srcDeviceId = srcDeviceId;
           this.userId = userId;
           this.userIdType = userIdType;
    }


    /**
     * Gets the srcDeviceType value for this UserIdRequest.
     * 
     * @return srcDeviceType
     */
    public int getSrcDeviceType() {
        return srcDeviceType;
    }


    /**
     * Sets the srcDeviceType value for this UserIdRequest.
     * 
     * @param srcDeviceType
     */
    public void setSrcDeviceType(int srcDeviceType) {
        this.srcDeviceType = srcDeviceType;
    }


    /**
     * Gets the srcDeviceId value for this UserIdRequest.
     * 
     * @return srcDeviceId
     */
    public int getSrcDeviceId() {
        return srcDeviceId;
    }


    /**
     * Sets the srcDeviceId value for this UserIdRequest.
     * 
     * @param srcDeviceId
     */
    public void setSrcDeviceId(int srcDeviceId) {
        this.srcDeviceId = srcDeviceId;
    }


    /**
     * Gets the userId value for this UserIdRequest.
     * 
     * @return userId
     */
    public String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this UserIdRequest.
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * Gets the userIdType value for this UserIdRequest.
     * 
     * @return userIdType
     */
    public int getUserIdType() {
        return userIdType;
    }


    /**
     * Sets the userIdType value for this UserIdRequest.
     * 
     * @param userIdType
     */
    public void setUserIdType(int userIdType) {
        this.userIdType = userIdType;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof UserIdRequest)) return false;
        UserIdRequest other = (UserIdRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.srcDeviceType == other.getSrcDeviceType() &&
            this.srcDeviceId == other.getSrcDeviceId() &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            this.userIdType == other.getUserIdType();
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
        _hashCode += getSrcDeviceType();
        _hashCode += getSrcDeviceId();
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        _hashCode += getUserIdType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserIdRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "UserIdRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srcDeviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "srcDeviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srcDeviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "srcDeviceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userIdType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "userIdType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
