/**
 * QueryUserInfoRespone.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.ip2phone;

public class QueryUserInfoRespone  implements java.io.Serializable {
    private com.fortune.vac.ip2phone.QueryUserInfoResponeServerInfo serverInfo;

    private com.fortune.vac.ip2phone.QueryUserInfoResponeUserInfo userInfo;

    public QueryUserInfoRespone() {
    }

    public QueryUserInfoRespone(
           com.fortune.vac.ip2phone.QueryUserInfoResponeServerInfo serverInfo,
           com.fortune.vac.ip2phone.QueryUserInfoResponeUserInfo userInfo) {
           this.serverInfo = serverInfo;
           this.userInfo = userInfo;
    }


    /**
     * Gets the serverInfo value for this QueryUserInfoRespone.
     * 
     * @return serverInfo
     */
    public com.fortune.vac.ip2phone.QueryUserInfoResponeServerInfo getServerInfo() {
        return serverInfo;
    }


    /**
     * Sets the serverInfo value for this QueryUserInfoRespone.
     * 
     * @param serverInfo
     */
    public void setServerInfo(com.fortune.vac.ip2phone.QueryUserInfoResponeServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }


    /**
     * Gets the userInfo value for this QueryUserInfoRespone.
     * 
     * @return userInfo
     */
    public com.fortune.vac.ip2phone.QueryUserInfoResponeUserInfo getUserInfo() {
        return userInfo;
    }


    /**
     * Sets the userInfo value for this QueryUserInfoRespone.
     * 
     * @param userInfo
     */
    public void setUserInfo(com.fortune.vac.ip2phone.QueryUserInfoResponeUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QueryUserInfoRespone)) return false;
        QueryUserInfoRespone other = (QueryUserInfoRespone) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.serverInfo==null && other.getServerInfo()==null) || 
             (this.serverInfo!=null &&
              this.serverInfo.equals(other.getServerInfo()))) &&
            ((this.userInfo==null && other.getUserInfo()==null) || 
             (this.userInfo!=null &&
              this.userInfo.equals(other.getUserInfo())));
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
        if (getServerInfo() != null) {
            _hashCode += getServerInfo().hashCode();
        }
        if (getUserInfo() != null) {
            _hashCode += getUserInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryUserInfoRespone.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", ">QueryUserInfoRespone"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "ServerInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", ">>QueryUserInfoRespone>ServerInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "UserInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", ">>QueryUserInfoRespone>UserInfo"));
        elemField.setMinOccurs(0);
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

    public String toString(){
        StringBuilder builder = new StringBuilder("<userInfoResponse>\r\n");
        if(serverInfo!=null){
            builder.append("\t<serverInfo resultCode=\"").append(serverInfo.getResultCode()).append("\" description=\"")
                    .append(serverInfo.getDescription()).append("\"/>\r\n");
        }
        if(userInfo!=null){
            builder.append("\t<userInfo userName=\"").append(userInfo.getUserName()).append("\"/>\r\n");
        }
        builder.append("</userInfoResponse>\r\n");
        return  builder.toString();
    }
}
