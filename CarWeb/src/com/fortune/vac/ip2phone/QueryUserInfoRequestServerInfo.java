/**
 * QueryUserInfoRequestServerInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.ip2phone;

public class QueryUserInfoRequestServerInfo  implements java.io.Serializable {
    private String serverID;

    private String timeStamp;

    public QueryUserInfoRequestServerInfo() {
    }

    public QueryUserInfoRequestServerInfo(
           String serverID,
           String timeStamp) {
           this.serverID = serverID;
           this.timeStamp = timeStamp;
    }


    /**
     * Gets the serverID value for this QueryUserInfoRequestServerInfo.
     * 
     * @return serverID
     */
    public String getServerID() {
        return serverID;
    }


    /**
     * Sets the serverID value for this QueryUserInfoRequestServerInfo.
     * 
     * @param serverID
     */
    public void setServerID(String serverID) {
        this.serverID = serverID;
    }


    /**
     * Gets the timeStamp value for this QueryUserInfoRequestServerInfo.
     * 
     * @return timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this QueryUserInfoRequestServerInfo.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QueryUserInfoRequestServerInfo)) return false;
        QueryUserInfoRequestServerInfo other = (QueryUserInfoRequestServerInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.serverID==null && other.getServerID()==null) || 
             (this.serverID!=null &&
              this.serverID.equals(other.getServerID()))) &&
            ((this.timeStamp==null && other.getTimeStamp()==null) || 
             (this.timeStamp!=null &&
              this.timeStamp.equals(other.getTimeStamp())));
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
        if (getServerID() != null) {
            _hashCode += getServerID().hashCode();
        }
        if (getTimeStamp() != null) {
            _hashCode += getTimeStamp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryUserInfoRequestServerInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", ">>QueryUserInfoRequest>ServerInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "ServerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "TimeStamp"));
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
