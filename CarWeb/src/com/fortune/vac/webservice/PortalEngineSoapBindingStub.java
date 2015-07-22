/**
 * PortalEngineSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public class PortalEngineSoapBindingStub extends org.apache.axis.client.Stub implements PortalEngine_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[6];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSubscription");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "getSubscriptionReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "GetSubscriptionReq"), GetSubscriptionReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "GetSubscriptionRsp"));
        oper.setReturnClass(GetSubscriptionRsp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "getSubscriptionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("readUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "readUserReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "ReadUserReq"), ReadUserReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "ReadUserRsp"));
        oper.setReturnClass(ReadUserRsp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "readUserReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("authenticateWapUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "authenticateWapUserReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "authenticateWapUserReq"), AuthenticateWapUserReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "authenticateWapUserRsp"));
        oper.setReturnClass(AuthenticateWapUserRsp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "authenticateWapUserReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("authorizeUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "authorizeUserReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "AuthorizeUserReq"), AuthorizeUserReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "AuthorizeUserRsp"));
        oper.setReturnClass(AuthorizeUserRsp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "authorizeUserReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("querySubscriptionHistory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "querySubscriptionHistoryReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "QuerySubscriptionHistoryReq"), com.fortune.vac.webservice.QuerySubscriptionHistoryReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "QuerySubscriptionHistoryRsp"));
        oper.setReturnClass(com.fortune.vac.webservice.QuerySubscriptionHistoryRsp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "querySubscriptionHistoryReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUserInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "userIdRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "UserIdRequest"), UserIdRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "UserInfoResponse"));
        oper.setReturnClass(UserInfoResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "getUserInfoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

    }

    public PortalEngineSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PortalEngineSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PortalEngineSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "ArrayOf_tns2_SubHistoryInfo");
            cachedSerQNames.add(qName);
            cls = SubHistoryInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubHistoryInfo");
            qName2 = new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "ArrayOf_tns2_SubInfo");
            cachedSerQNames.add(qName);
            cls = SubInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubInfo");
            qName2 = new javax.xml.namespace.QName("http://portalEngine.vac.unicom.com", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "authenticateWapUserReq");
            cachedSerQNames.add(qName);
            cls = AuthenticateWapUserReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "AuthorizeUserReq");
            cachedSerQNames.add(qName);
            cls = AuthorizeUserReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "GetSubscriptionReq");
            cachedSerQNames.add(qName);
            cls = GetSubscriptionReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "QuerySubscriptionHistoryReq");
            cachedSerQNames.add(qName);
            cls = com.fortune.vac.webservice.QuerySubscriptionHistoryReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "ReadUserReq");
            cachedSerQNames.add(qName);
            cls = ReadUserReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://req.portalEngine.vac.unicom.com", "UserIdRequest");
            cachedSerQNames.add(qName);
            cls = UserIdRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "authenticateWapUserRsp");
            cachedSerQNames.add(qName);
            cls = AuthenticateWapUserRsp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "AuthorizeUserRsp");
            cachedSerQNames.add(qName);
            cls = AuthorizeUserRsp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "GetSubscriptionRsp");
            cachedSerQNames.add(qName);
            cls = GetSubscriptionRsp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "QuerySubscriptionHistoryRsp");
            cachedSerQNames.add(qName);
            cls = com.fortune.vac.webservice.QuerySubscriptionHistoryRsp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "ReadUserRsp");
            cachedSerQNames.add(qName);
            cls = ReadUserRsp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubHistoryInfo");
            cachedSerQNames.add(qName);
            cls = SubHistoryInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "SubInfo");
            cachedSerQNames.add(qName);
            cls = SubInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "UserInfo");
            cachedSerQNames.add(qName);
            cls = UserInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://rsp.portalEngine.vac.unicom.com", "UserInfoResponse");
            cachedSerQNames.add(qName);
            cls = UserInfoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)
                                 cachedSerFactories.get(i);
                            Class df = (Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public GetSubscriptionRsp getSubscription(GetSubscriptionReq getSubscriptionReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getSubscription"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {getSubscriptionReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (GetSubscriptionRsp) _resp;
            } catch (Exception _exception) {
                return (GetSubscriptionRsp) org.apache.axis.utils.JavaUtils.convert(_resp, GetSubscriptionRsp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public ReadUserRsp readUser(ReadUserReq readUserReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "readUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {readUserReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ReadUserRsp) _resp;
            } catch (Exception _exception) {
                return (ReadUserRsp) org.apache.axis.utils.JavaUtils.convert(_resp, ReadUserRsp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public AuthenticateWapUserRsp authenticateWapUser(AuthenticateWapUserReq authenticateWapUserReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "authenticateWapUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {authenticateWapUserReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (AuthenticateWapUserRsp) _resp;
            } catch (Exception _exception) {
                return (AuthenticateWapUserRsp) org.apache.axis.utils.JavaUtils.convert(_resp, AuthenticateWapUserRsp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public AuthorizeUserRsp authorizeUser(AuthorizeUserReq authorizeUserReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "authorizeUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {authorizeUserReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (AuthorizeUserRsp) _resp;
            } catch (Exception _exception) {
                return (AuthorizeUserRsp) org.apache.axis.utils.JavaUtils.convert(_resp, AuthorizeUserRsp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.fortune.vac.webservice.QuerySubscriptionHistoryRsp querySubscriptionHistory(com.fortune.vac.webservice.QuerySubscriptionHistoryReq querySubscriptionHistoryReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "querySubscriptionHistory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {querySubscriptionHistoryReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.fortune.vac.webservice.QuerySubscriptionHistoryRsp) _resp;
            } catch (Exception _exception) {
                return (com.fortune.vac.webservice.QuerySubscriptionHistoryRsp) org.apache.axis.utils.JavaUtils.convert(_resp, com.fortune.vac.webservice.QuerySubscriptionHistoryRsp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public UserInfoResponse getUserInfo(UserIdRequest userIdRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getUserInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {userIdRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (UserInfoResponse) _resp;
            } catch (Exception _exception) {
                return (UserInfoResponse) org.apache.axis.utils.JavaUtils.convert(_resp, UserInfoResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
