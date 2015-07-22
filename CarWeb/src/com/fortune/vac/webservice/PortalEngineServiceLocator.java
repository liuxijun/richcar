/**
 * PortalEngineServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class PortalEngineServiceLocator extends org.apache.axis.client.Service implements PortalEngineService {

    public PortalEngineServiceLocator() {
    }


    public PortalEngineServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PortalEngineServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PortalEngine
    private String PortalEngine_address = "http://localhost:8282/wsdlProject/services/PortalEngine";

    public String getPortalEngineAddress() {
        return PortalEngine_address;
    }

    // The WSDD service name defaults to the port name.
    private String PortalEngineWSDDServiceName = "PortalEngine";

    public String getPortalEngineWSDDServiceName() {
        return PortalEngineWSDDServiceName;
    }

    public void setPortalEngineWSDDServiceName(String name) {
        PortalEngineWSDDServiceName = name;
    }

    public PortalEngine_PortType getPortalEngine() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PortalEngine_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPortalEngine(endpoint);
    }

    public PortalEngine_PortType getPortalEngine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fortune.vac.webservice.PortalEngineSoapBindingStub _stub = new com.fortune.vac.webservice.PortalEngineSoapBindingStub(portAddress, this);
            _stub.setPortName(getPortalEngineWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPortalEngineEndpointAddress(String address) {
        PortalEngine_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PortalEngine_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fortune.vac.webservice.PortalEngineSoapBindingStub _stub = new com.fortune.vac.webservice.PortalEngineSoapBindingStub(new java.net.URL(PortalEngine_address), this);
                _stub.setPortName(getPortalEngineWSDDServiceName());
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
        if ("PortalEngine".equals(inputPortName)) {
            return getPortalEngine();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "PortalEngineService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "PortalEngine"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("PortalEngine".equals(portName)) {
            setPortalEngineEndpointAddress(address);
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
