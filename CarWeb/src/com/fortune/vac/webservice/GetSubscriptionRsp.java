/**
 * GetSubscriptionRsp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class GetSubscriptionRsp  implements java.io.Serializable {
    private int resultCode;

    private String streamingNo;

    private SubInfo[] subInfo;

    public GetSubscriptionRsp() {
    }

    public GetSubscriptionRsp(
           int resultCode,
           String streamingNo,
           SubInfo[] subInfo) {
           this.resultCode = resultCode;
           this.streamingNo = streamingNo;
           this.subInfo = subInfo;
    }


    /**
     * Gets the resultCode value for this GetSubscriptionRsp.
     * 
     * @return resultCode
     */
    public int getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this GetSubscriptionRsp.
     * 
     * @param resultCode
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the streamingNo value for this GetSubscriptionRsp.
     * 
     * @return streamingNo
     */
    public String getStreamingNo() {
        return streamingNo;
    }


    /**
     * Sets the streamingNo value for this GetSubscriptionRsp.
     * 
     * @param streamingNo
     */
    public void setStreamingNo(String streamingNo) {
        this.streamingNo = streamingNo;
    }


    /**
     * Gets the subInfo value for this GetSubscriptionRsp.
     * 
     * @return subInfo
     */
    public SubInfo[] getSubInfo() {
        return subInfo;
    }


    /**
     * Sets the subInfo value for this GetSubscriptionRsp.
     * 
     * @param subInfo
     */
    public void setSubInfo(SubInfo[] subInfo) {
        this.subInfo = subInfo;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetSubscriptionRsp)) return false;
        GetSubscriptionRsp other = (GetSubscriptionRsp) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.resultCode == other.getResultCode() &&
            ((this.streamingNo==null && other.getStreamingNo()==null) || 
             (this.streamingNo!=null &&
              this.streamingNo.equals(other.getStreamingNo()))) &&
            ((this.subInfo==null && other.getSubInfo()==null) || 
             (this.subInfo!=null &&
              java.util.Arrays.equals(this.subInfo, other.getSubInfo())));
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
        _hashCode += getResultCode();
        if (getStreamingNo() != null) {
            _hashCode += getStreamingNo().hashCode();
        }
        if (getSubInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubInfo());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getSubInfo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscriptionRsp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "GetSubscriptionRsp"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "resultCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streamingNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "streamingNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "subInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "item"));
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
