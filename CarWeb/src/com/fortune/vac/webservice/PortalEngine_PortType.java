/**
 * PortalEngine_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fortune.vac.webservice;

public interface PortalEngine_PortType extends java.rmi.Remote {
    public GetSubscriptionRsp getSubscription(GetSubscriptionReq getSubscriptionReq) throws java.rmi.RemoteException;
    public ReadUserRsp readUser(ReadUserReq readUserReq) throws java.rmi.RemoteException;
    public AuthenticateWapUserRsp authenticateWapUser(AuthenticateWapUserReq authenticateWapUserReq) throws java.rmi.RemoteException;
    public AuthorizeUserRsp authorizeUser(AuthorizeUserReq authorizeUserReq) throws java.rmi.RemoteException;
    public com.fortune.vac.webservice.QuerySubscriptionHistoryRsp querySubscriptionHistory(com.fortune.vac.webservice.QuerySubscriptionHistoryReq querySubscriptionHistoryReq) throws java.rmi.RemoteException;
    public UserInfoResponse getUserInfo(UserIdRequest userIdRequest) throws java.rmi.RemoteException;
}
