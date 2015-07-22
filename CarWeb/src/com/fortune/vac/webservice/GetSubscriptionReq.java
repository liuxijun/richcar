/**
 * GetSubscriptionReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class GetSubscriptionReq  implements java.io.Serializable {
    private String srcDeviceID;

    private int srcDeviceType;

    private String streamingNo;

    private String userID;

    private int userIDType;

    private String[] serviceType;

    private String spAreaId;

    private String spId;

    public GetSubscriptionReq() {
    }

    public GetSubscriptionReq(
           String srcDeviceID,
           int srcDeviceType,
           String streamingNo,
           String userID,
           int userIDType,
           String[] serviceType,
           String spAreaId,
           String spId) {
           this.srcDeviceID = srcDeviceID;
           this.srcDeviceType = srcDeviceType;
           this.streamingNo = streamingNo;
           this.userID = userID;
           this.userIDType = userIDType;
           this.serviceType = serviceType;
           this.spAreaId = spAreaId;
           this.spId = spId;
    }


    /**
     * Gets the srcDeviceID value for this GetSubscriptionReq.
     * 
     * @return srcDeviceID
     */
    public String getSrcDeviceID() {
        return srcDeviceID;
    }


    /**
     * Sets the srcDeviceID value for this GetSubscriptionReq.
     * 
     * @param srcDeviceID
     */
    public void setSrcDeviceID(String srcDeviceID) {
        this.srcDeviceID = srcDeviceID;
    }


    /**
     * Gets the srcDeviceType value for this GetSubscriptionReq.
     * 
     * @return srcDeviceType
     */
    public int getSrcDeviceType() {
        return srcDeviceType;
    }


    /**
     * Sets the srcDeviceType value for this GetSubscriptionReq.
     * 
     * @param srcDeviceType
     */
    public void setSrcDeviceType(int srcDeviceType) {
        this.srcDeviceType = srcDeviceType;
    }


    /**
     * Gets the streamingNo value for this GetSubscriptionReq.
     * 
     * @return streamingNo
     */
    public String getStreamingNo() {
        return streamingNo;
    }


    /**
     * Sets the streamingNo value for this GetSubscriptionReq.
     * 
     * @param streamingNo
     */
    public void setStreamingNo(String streamingNo) {
        this.streamingNo = streamingNo;
    }


    /**
     * Gets the userID value for this GetSubscriptionReq.
     * 
     * @return userID
     */
    public String getUserID() {
        return userID;
    }


    /**
     * Sets the userID value for this GetSubscriptionReq.
     * 
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }


    /**
     * Gets the userIDType value for this GetSubscriptionReq.
     * 
     * @return userIDType
     */
    public int getUserIDType() {
        return userIDType;
    }


    /**
     * Sets the userIDType value for this GetSubscriptionReq.
     * 
     * @param userIDType
     */
    public void setUserIDType(int userIDType) {
        this.userIDType = userIDType;
    }


    /**
     * Gets the serviceType value for this GetSubscriptionReq.
     * 
     * @return serviceType
     */
    public String[] getServiceType() {
        return serviceType;
    }


    /**
     * Sets the serviceType value for this GetSubscriptionReq.
     * 
     * @param serviceType
     */
    public void setServiceType(String[] serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType(int i) {
        return this.serviceType[i];
    }

    public void setServiceType(int i, String _value) {
        this.serviceType[i] = _value;
    }


    /**
     * Gets the spAreaId value for this GetSubscriptionReq.
     * 
     * @return spAreaId
     */
    public String getSpAreaId() {
        return spAreaId;
    }


    /**
     * Sets the spAreaId value for this GetSubscriptionReq.
     * 
     * @param spAreaId
     */
    public void setSpAreaId(String spAreaId) {
        this.spAreaId = spAreaId;
    }


    /**
     * Gets the spId value for this GetSubscriptionReq.
     * 
     * @return spId
     */
    public String getSpId() {
        return spId;
    }


    /**
     * Sets the spId value for this GetSubscriptionReq.
     * 
     * @param spId
     */
    public void setSpId(String spId) {
        this.spId = spId;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetSubscriptionReq)) return false;
        GetSubscriptionReq other = (GetSubscriptionReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.srcDeviceID==null && other.getSrcDeviceID()==null) || 
             (this.srcDeviceID!=null &&
              this.srcDeviceID.equals(other.getSrcDeviceID()))) &&
            this.srcDeviceType == other.getSrcDeviceType() &&
            ((this.streamingNo==null && other.getStreamingNo()==null) || 
             (this.streamingNo!=null &&
              this.streamingNo.equals(other.getStreamingNo()))) &&
            ((this.userID==null && other.getUserID()==null) || 
             (this.userID!=null &&
              this.userID.equals(other.getUserID()))) &&
            this.userIDType == other.getUserIDType() &&
            ((this.serviceType==null && other.getServiceType()==null) || 
             (this.serviceType!=null &&
              java.util.Arrays.equals(this.serviceType, other.getServiceType()))) &&
            ((this.spAreaId==null && other.getSpAreaId()==null) || 
             (this.spAreaId!=null &&
              this.spAreaId.equals(other.getSpAreaId()))) &&
            ((this.spId==null && other.getSpId()==null) || 
             (this.spId!=null &&
              this.spId.equals(other.getSpId())));
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
        if (getSrcDeviceID() != null) {
            _hashCode += getSrcDeviceID().hashCode();
        }
        _hashCode += getSrcDeviceType();
        if (getStreamingNo() != null) {
            _hashCode += getStreamingNo().hashCode();
        }
        if (getUserID() != null) {
            _hashCode += getUserID().hashCode();
        }
        _hashCode += getUserIDType();
        if (getServiceType() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getServiceType());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getServiceType(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSpAreaId() != null) {
            _hashCode += getSpAreaId().hashCode();
        }
        if (getSpId() != null) {
            _hashCode += getSpId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscriptionReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "GetSubscriptionReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srcDeviceID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "srcDeviceID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srcDeviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "srcDeviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streamingNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "streamingNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "userID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userIDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "userIDType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "serviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spAreaId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "spAreaId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "spId"));
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
