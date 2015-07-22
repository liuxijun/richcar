/**
 * UserInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class UserInfo  implements java.io.Serializable {
    private String userId;

    private int userIdType;

    private String msisdn;

    private String fakeId;

    private String imsi;

    private int userState;

    private int userType;

    private String userbrand;

    private int userScpType;

    private String userPrepaidId;

    private int userServiceType;

    public UserInfo() {
    }

    public UserInfo(
           String userId,
           int userIdType,
           String msisdn,
           String fakeId,
           String imsi,
           int userState,
           int userType,
           String userbrand,
           int userScpType,
           String userPrepaidId,
           int userServiceType) {
           this.userId = userId;
           this.userIdType = userIdType;
           this.msisdn = msisdn;
           this.fakeId = fakeId;
           this.imsi = imsi;
           this.userState = userState;
           this.userType = userType;
           this.userbrand = userbrand;
           this.userScpType = userScpType;
           this.userPrepaidId = userPrepaidId;
           this.userServiceType = userServiceType;
    }


    /**
     * Gets the userId value for this UserInfo.
     * 
     * @return userId
     */
    public String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this UserInfo.
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * Gets the userIdType value for this UserInfo.
     * 
     * @return userIdType
     */
    public int getUserIdType() {
        return userIdType;
    }


    /**
     * Sets the userIdType value for this UserInfo.
     * 
     * @param userIdType
     */
    public void setUserIdType(int userIdType) {
        this.userIdType = userIdType;
    }


    /**
     * Gets the msisdn value for this UserInfo.
     * 
     * @return msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }


    /**
     * Sets the msisdn value for this UserInfo.
     * 
     * @param msisdn
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }


    /**
     * Gets the fakeId value for this UserInfo.
     * 
     * @return fakeId
     */
    public String getFakeId() {
        return fakeId;
    }


    /**
     * Sets the fakeId value for this UserInfo.
     * 
     * @param fakeId
     */
    public void setFakeId(String fakeId) {
        this.fakeId = fakeId;
    }


    /**
     * Gets the imsi value for this UserInfo.
     * 
     * @return imsi
     */
    public String getImsi() {
        return imsi;
    }


    /**
     * Sets the imsi value for this UserInfo.
     * 
     * @param imsi
     */
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }


    /**
     * Gets the userState value for this UserInfo.
     * 
     * @return userState
     */
    public int getUserState() {
        return userState;
    }


    /**
     * Sets the userState value for this UserInfo.
     * 
     * @param userState
     */
    public void setUserState(int userState) {
        this.userState = userState;
    }


    /**
     * Gets the userType value for this UserInfo.
     * 
     * @return userType
     */
    public int getUserType() {
        return userType;
    }


    /**
     * Sets the userType value for this UserInfo.
     * 
     * @param userType
     */
    public void setUserType(int userType) {
        this.userType = userType;
    }


    /**
     * Gets the userbrand value for this UserInfo.
     * 
     * @return userbrand
     */
    public String getUserbrand() {
        return userbrand;
    }


    /**
     * Sets the userbrand value for this UserInfo.
     * 
     * @param userbrand
     */
    public void setUserbrand(String userbrand) {
        this.userbrand = userbrand;
    }


    /**
     * Gets the userScpType value for this UserInfo.
     * 
     * @return userScpType
     */
    public int getUserScpType() {
        return userScpType;
    }


    /**
     * Sets the userScpType value for this UserInfo.
     * 
     * @param userScpType
     */
    public void setUserScpType(int userScpType) {
        this.userScpType = userScpType;
    }


    /**
     * Gets the userPrepaidId value for this UserInfo.
     * 
     * @return userPrepaidId
     */
    public String getUserPrepaidId() {
        return userPrepaidId;
    }


    /**
     * Sets the userPrepaidId value for this UserInfo.
     * 
     * @param userPrepaidId
     */
    public void setUserPrepaidId(String userPrepaidId) {
        this.userPrepaidId = userPrepaidId;
    }


    /**
     * Gets the userServiceType value for this UserInfo.
     * 
     * @return userServiceType
     */
    public int getUserServiceType() {
        return userServiceType;
    }


    /**
     * Sets the userServiceType value for this UserInfo.
     * 
     * @param userServiceType
     */
    public void setUserServiceType(int userServiceType) {
        this.userServiceType = userServiceType;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof UserInfo)) return false;
        UserInfo other = (UserInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            this.userIdType == other.getUserIdType() &&
            ((this.msisdn==null && other.getMsisdn()==null) || 
             (this.msisdn!=null &&
              this.msisdn.equals(other.getMsisdn()))) &&
            ((this.fakeId==null && other.getFakeId()==null) || 
             (this.fakeId!=null &&
              this.fakeId.equals(other.getFakeId()))) &&
            ((this.imsi==null && other.getImsi()==null) || 
             (this.imsi!=null &&
              this.imsi.equals(other.getImsi()))) &&
            this.userState == other.getUserState() &&
            this.userType == other.getUserType() &&
            ((this.userbrand==null && other.getUserbrand()==null) || 
             (this.userbrand!=null &&
              this.userbrand.equals(other.getUserbrand()))) &&
            this.userScpType == other.getUserScpType() &&
            ((this.userPrepaidId==null && other.getUserPrepaidId()==null) || 
             (this.userPrepaidId!=null &&
              this.userPrepaidId.equals(other.getUserPrepaidId()))) &&
            this.userServiceType == other.getUserServiceType();
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
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        _hashCode += getUserIdType();
        if (getMsisdn() != null) {
            _hashCode += getMsisdn().hashCode();
        }
        if (getFakeId() != null) {
            _hashCode += getFakeId().hashCode();
        }
        if (getImsi() != null) {
            _hashCode += getImsi().hashCode();
        }
        _hashCode += getUserState();
        _hashCode += getUserType();
        if (getUserbrand() != null) {
            _hashCode += getUserbrand().hashCode();
        }
        _hashCode += getUserScpType();
        if (getUserPrepaidId() != null) {
            _hashCode += getUserPrepaidId().hashCode();
        }
        _hashCode += getUserServiceType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "UserInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userIdType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userIdType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msisdn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "msisdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fakeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "fakeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imsi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "imsi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userbrand");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userbrand"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userScpType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userScpType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userPrepaidId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userPrepaidId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userServiceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "userServiceType"));
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
