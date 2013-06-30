
/**
 * BioDBnetCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.dnalog.fmrp.biodb;

    /**
     *  BioDBnetCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class BioDBnetCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public BioDBnetCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public BioDBnetCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for dbOrtho method
            * override this method for handling normal response from dbOrtho operation
            */
           public void receiveResultdbOrtho(
                    BioDBnetStub.DbOrthoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbOrtho operation
           */
            public void receiveErrordbOrtho(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDirectOutputsForInput method
            * override this method for handling normal response from getDirectOutputsForInput operation
            */
           public void receiveResultgetDirectOutputsForInput(
                    BioDBnetStub.GetDirectOutputsForInputResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDirectOutputsForInput operation
           */
            public void receiveErrorgetDirectOutputsForInput(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbReport method
            * override this method for handling normal response from dbReport operation
            */
           public void receiveResultdbReport(
                    BioDBnetStub.DbReportResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbReport operation
           */
            public void receiveErrordbReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for db2Db method
            * override this method for handling normal response from db2Db operation
            */
           public void receiveResultdb2Db(
                    BioDBnetStub.Db2DbResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from db2Db operation
           */
            public void receiveErrordb2Db(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInputs method
            * override this method for handling normal response from getInputs operation
            */
           public void receiveResultgetInputs(
                    BioDBnetStub.GetInputsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInputs operation
           */
            public void receiveErrorgetInputs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbFind method
            * override this method for handling normal response from dbFind operation
            */
           public void receiveResultdbFind(
                    BioDBnetStub.DbFindResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbFind operation
           */
            public void receiveErrordbFind(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOutputsForInput method
            * override this method for handling normal response from getOutputsForInput operation
            */
           public void receiveResultgetOutputsForInput(
                    BioDBnetStub.GetOutputsForInputResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOutputsForInput operation
           */
            public void receiveErrorgetOutputsForInput(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for dbWalk method
            * override this method for handling normal response from dbWalk operation
            */
           public void receiveResultdbWalk(
                    BioDBnetStub.DbWalkResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from dbWalk operation
           */
            public void receiveErrordbWalk(java.lang.Exception e) {
            }
                


    }
    