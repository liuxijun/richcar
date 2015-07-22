/**
 * QueryUserInfoServiceApply_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.ip2phone;

public class QueryUserInfoServiceApply_ServiceLocator extends org.apache.axis.client.Service implements QueryUserInfoServiceApply_Service {

    public QueryUserInfoServiceApply_ServiceLocator() {
    }


    public QueryUserInfoServiceApply_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public QueryUserInfoServiceApply_ServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for QueryUserInfoServiceApplyHttpPort
    private String QueryUserInfoServiceApplyHttpPort_address = "http://61.55.156.205:8090/webservices/services/QueryUserInfoServiceApplyHttpPort";

    public String getQueryUserInfoServiceApplyHttpPortAddress() {
        return QueryUserInfoServiceApplyHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String QueryUserInfoServiceApplyHttpPortWSDDServiceName = "QueryUserInfoServiceApplyHttpPort";

    public String getQueryUserInfoServiceApplyHttpPortWSDDServiceName() {
        return QueryUserInfoServiceApplyHttpPortWSDDServiceName;
    }

    public void setQueryUserInfoServiceApplyHttpPortWSDDServiceName(String name) {
        QueryUserInfoServiceApplyHttpPortWSDDServiceName = name;
    }

    public QueryUserInfoServiceApply_PortType getQueryUserInfoServiceApplyHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(QueryUserInfoServiceApplyHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQueryUserInfoServiceApplyHttpPort(endpoint);
    }

    public QueryUserInfoServiceApply_PortType getQueryUserInfoServiceApplyHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fortune.vac.ip2phone.QueryUserInfoServiceApplyHttpPortSoapBindingStub _stub = new com.fortune.vac.ip2phone.QueryUserInfoServiceApplyHttpPortSoapBindingStub(portAddress, this);
            _stub.setPortName(getQueryUserInfoServiceApplyHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setQueryUserInfoServiceApplyHttpPortEndpointAddress(String address) {
        QueryUserInfoServiceApplyHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (QueryUserInfoServiceApply_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fortune.vac.ip2phone.QueryUserInfoServiceApplyHttpPortSoapBindingStub _stub = new com.fortune.vac.ip2phone.QueryUserInfoServiceApplyHttpPortSoapBindingStub(new java.net.URL(QueryUserInfoServiceApplyHttpPort_address), this);
                _stub.setPortName(getQueryUserInfoServiceApplyHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("QueryUserInfoServiceApplyHttpPort".equals(inputPortName)) {
            return getQueryUserInfoServiceApplyHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "QueryUserInfoServiceApply");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.iuim.zoomtech.com/", "QueryUserInfoServiceApplyHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("QueryUserInfoServiceApplyHttpPort".equals(portName)) {
            setQueryUserInfoServiceApplyHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
