package com.dnalog.fmrp.biodb;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;

import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: andrejus
 * Date: 30/06/2013
 * Time: 02:44
 * To change this template use File | Settings | File Templates.
 */
public class BioDBnetImpl {

    private final BioDBnetStub stub;

    public BioDBnetImpl() throws AxisFault
    {
        this.stub = new BioDBnetStub();
        stub._getServiceClient().getOptions().setProperty(HTTPConstants.HTTP_PROTOCOL_VERSION,
                HTTPConstants.HEADER_PROTOCOL_10);
    }

    public String getInputs() throws RemoteException
    {
        BioDBnetStub.GetInputs getInputs = new BioDBnetStub.GetInputs();
        BioDBnetStub.GetInputsResponse getInputsResponse = this.stub.getInputs(getInputs);

        String inputResult = getInputsResponse.get_return();
        return inputResult;
    }

    public String getOutputsForInput(String input)
            throws RemoteException
    {
        BioDBnetStub.GetOutputsForInput getOutputsForInput	= new BioDBnetStub.GetOutputsForInput();
        getOutputsForInput.setInput(input);
        BioDBnetStub.GetOutputsForInputResponse getOutputsForInputResponse	=
                this.stub.getOutputsForInput(getOutputsForInput);

        String outputResult = getOutputsForInputResponse.get_return();
        return outputResult;
    }

    public String getDirectOutputsForInput(String input)
            throws RemoteException
    {
        BioDBnetStub.GetDirectOutputsForInput getDirectOutputsForInput = new BioDBnetStub.GetDirectOutputsForInput();
        getDirectOutputsForInput.setInput(input);
        BioDBnetStub.GetDirectOutputsForInputResponse getDirectOutputsForInputResponse =
                this.stub.getDirectOutputsForInput(getDirectOutputsForInput);

        String directOutputResult = getDirectOutputsForInputResponse.get_return();
        return directOutputResult;
    }

    public String db2db(String input, String inputValues, String outputs, String taxonId)
            throws RemoteException
    {
        BioDBnetStub.Db2DbParams db2dbParams = new BioDBnetStub.Db2DbParams();

        db2dbParams.setInput(input);
        db2dbParams.setInputValues(inputValues);
        db2dbParams.setOutputs(outputs);
        db2dbParams.setTaxonId(taxonId);

        BioDBnetStub.Db2Db db2db = new BioDBnetStub.Db2Db();
        db2db.setDb2DbParams(db2dbParams);

        BioDBnetStub.Db2DbResponse db2dbResponse = this.stub.db2Db(db2db);
        String db2dbResult = db2dbResponse.get_return();
        return db2dbResult;
    }

    public String dbReport(String input, String inputValues, String taxonId)
            throws RemoteException
    {
        BioDBnetStub.DbReportParams dbReportParams	= new BioDBnetStub.DbReportParams();

        dbReportParams.setInput(input);
        dbReportParams.setInputValues(inputValues);
        dbReportParams.setTaxonId(taxonId);

        BioDBnetStub.DbReport dbReport = new BioDBnetStub.DbReport();
        dbReport.setDbReportParams(dbReportParams);

        BioDBnetStub.DbReportResponse dbReportResponse = this.stub.dbReport(dbReport);
        String dbReportResult = dbReportResponse.get_return();
        return dbReportResult;
    }

    public String dbFind(String output, String inputValues, String taxonId)
            throws RemoteException
    {
        BioDBnetStub.DbFindParams dbFindParams = new BioDBnetStub.DbFindParams();

        dbFindParams.setOutput(output);
        dbFindParams.setInputValues(inputValues);
        dbFindParams.setTaxonId(taxonId);

        BioDBnetStub.DbFind dbFind = new BioDBnetStub.DbFind();
        dbFind.setDbFindParams(dbFindParams);

        BioDBnetStub.DbFindResponse dbFindResponse = this.stub.dbFind(dbFind);
        String dbFindResult = dbFindResponse.get_return();
        return dbFindResult;
    }

    public String dbWalk(String dbPath, String inputValues, String taxonId)
            throws RemoteException
    {

        BioDBnetStub.DbWalkParams dbWalkParams = new BioDBnetStub.DbWalkParams();

        dbWalkParams.setDbPath(dbPath);
        dbWalkParams.setInputValues(inputValues);
        dbWalkParams.setTaxonId(taxonId);

        BioDBnetStub.DbWalk dbWalk = new BioDBnetStub.DbWalk();
        dbWalk.setDbWalkParams(dbWalkParams);

        BioDBnetStub.DbWalkResponse dbWalkResponse = this.stub.dbWalk(dbWalk);
        String dbWalkResult = dbWalkResponse.get_return();
        return dbWalkResult;
    }

    public String dbOrtho(String input, String output,
                           String inputValues,
                           String taxonId, String outputTaxon)
            throws RemoteException
    {

        BioDBnetStub.DbOrthoParams dbOrthoParams = new BioDBnetStub.DbOrthoParams();

        dbOrthoParams.setInput(input);
        dbOrthoParams.setOutput(output);
        dbOrthoParams.setInputValues(inputValues);
        dbOrthoParams.setInputTaxon(taxonId);
        dbOrthoParams.setOutputTaxon(outputTaxon);

        BioDBnetStub.DbOrtho dbOrtho = new BioDBnetStub.DbOrtho();
        dbOrtho.setDbOrthoParams(dbOrthoParams);

        BioDBnetStub.DbOrthoResponse dbOrthoResponse = this.stub.dbOrtho(dbOrtho);
        String dbOrthoResult = dbOrthoResponse.get_return();
        return dbOrthoResult;
    }
}
