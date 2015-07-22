/**
 * QuerySubscriptionHistoryRsp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class QuerySubscriptionHistoryRsp  implements java.io.Serializable {
    private int resultCode;

    private String streamingNo;

    private SubHistoryInfo[] subHistoryInfo;

    public QuerySubscriptionHistoryRsp() {
    }

    public QuerySubscriptionHistoryRsp(
           int resultCode,
           String streamingNo,
           SubHistoryInfo[] subHistoryInfo) {
           this.resultCode = resultCode;
           this.streamingNo = streamingNo;
           this.subHistoryInfo = subHistoryInfo;
    }


    /**
     * Gets the resultCode value for this QuerySubscriptionHistoryRsp.
     * 
     * @return resultCode
     */
    public int getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this QuerySubscriptionHistoryRsp.
     * 
     * @param resultCode
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the streamingNo value for this QuerySubscriptionHistoryRsp.
     * 
     * @return streamingNo
     */
    public String getStreamingNo() {
        return streamingNo;
    }


    /**
     * Sets the streamingNo value for this QuerySubscriptionHistoryRsp.
     * 
     * @param streamingNo
     */
    public void setStreamingNo(String streamingNo) {
        this.streamingNo = streamingNo;
    }


    /**
     * Gets the subHistoryInfo value for this QuerySubscriptionHistoryRsp.
     * 
     * @return subHistoryInfo
     */
    public SubHistoryInfo[] getSubHistoryInfo() {
        return subHistoryInfo;
    }


    /**
     * Sets the subHistoryInfo value for this QuerySubscriptionHistoryRsp.
     * 
     * @param subHistoryInfo
     */
    public void setSubHistoryInfo(SubHistoryInfo[] subHistoryInfo) {
        this.subHistoryInfo = subHistoryInfo;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QuerySubscriptionHistoryRsp)) return false;
        QuerySubscriptionHistoryRsp other = (QuerySubscriptionHistoryRsp) obj;
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
            ((this.subHistoryInfo==null && other.getSubHistoryInfo()==null) || 
             (this.subHistoryInfo!=null &&
              java.util.Arrays.equals(this.subHistoryInfo, other.getSubHistoryInfo())));
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
        if (getSubHistoryInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubHistoryInfo());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getSubHistoryInfo(), i);
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
        new org.apache.axis.description.TypeDesc(QuerySubscriptionHistoryRsp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "QuerySubscriptionHistoryRsp"));
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
        elemField.setFieldName("subHistoryInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "subHistoryInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubHistoryInfo"));
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
