package com.fortune.wsdl.zjlib;

/**
 * ICasAuthServiceServiceCallbackHandler Callback class, Users can extend this class and implement
 * their own receiveResult and receiveError methods.
 */
public abstract class ICasAuthServiceServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     *
     * @param clientData Object mechanism by which the user can pass in user data
     *                   that will be avilable at the time this callback is called.
     */
    public ICasAuthServiceServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public ICasAuthServiceServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */

    public Object getClientData() {
        return clientData;
    }


    /**
     * auto generated Axis2 call back method for getFullName method
     * override this method for handling normal response from getFullName operation
     */
    public void receiveResultgetFullName(
            ICasAuthServiceServiceStub.GetFullNameResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getFullName operation
     */
    public void receiveErrorgetFullName(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for casAuth method
     * override this method for handling normal response from casAuth operation
     */
    public void receiveResultcasAuth(
            ICasAuthServiceServiceStub.CasAuthResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from casAuth operation
     */
    public void receiveErrorcasAuth(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for uniteLogoutCas method
     * override this method for handling normal response from uniteLogoutCas operation
     */
    public void receiveResultuniteLogoutCas(
            ICasAuthServiceServiceStub.UniteLogoutCasResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from uniteLogoutCas operation
     */
    public void receiveErroruniteLogoutCas(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for checkToken method
     * override this method for handling normal response from checkToken operation
     */
    public void receiveResultcheckToken(
            ICasAuthServiceServiceStub.CheckTokenResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from checkToken operation
     */
    public void receiveErrorcheckToken(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for checkCx method
     * override this method for handling normal response from checkCx operation
     */
    public void receiveResultcheckCx(
            ICasAuthServiceServiceStub.CheckCxResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from checkCx operation
     */
    public void receiveErrorcheckCx(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for uniteCasAuth method
     * override this method for handling normal response from uniteCasAuth operation
     */
    public void receiveResultuniteCasAuth(
            ICasAuthServiceServiceStub.UniteCasAuthResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from uniteCasAuth operation
     */
    public void receiveErroruniteCasAuth(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for logoutCas method
     * override this method for handling normal response from logoutCas operation
     */
    public void receiveResultlogoutCas(
            ICasAuthServiceServiceStub.LogoutCasResponseE result
    ) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from logoutCas operation
     */
    public void receiveErrorlogoutCas(Exception e) {
    }


}
    