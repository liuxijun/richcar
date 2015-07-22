/**
 * PortalEngineService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public interface PortalEngineService extends javax.xml.rpc.Service {
    public String getPortalEngineAddress();

    public com.fortune.vac.webservice.PortalEngine_PortType getPortalEngine() throws javax.xml.rpc.ServiceException;

    public com.fortune.vac.webservice.PortalEngine_PortType getPortalEngine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
