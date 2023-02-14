package com.blueeaglecreditunion.script;

import com.corelationinc.script.*;
import java.io.*;
import java.util.ArrayList;
import java.sql.*;

public class RiskBasedPricingUpdate {
    Script script = null;

    private RiskBasedPricingUpdate(Script value) { this.script = value; }

    public static void runScript(Script script) throws Exception{
        RiskBasedPricingUpdate rBP = new RiskBasedPricingUpdate(script);

        rBP.run();
    }

    private void run() throws Exception {
        Connection conn = this.script.openDatabaseConnection();
        Report report = this.script.openReport("Report A", Report.Format.txt);
        String postingDate = script.retrievePostingDateString(conn);
        report.setPostingOption(true);
        PrintStream pS = new PrintStream(report.getBufferedOutputStream());
        XMLSerialize xml = new XMLSerialize();
        xml.setXMLWriter(pS);
        xml.putStartDocument();
        xml.putBatchQuery(postingDate);
        xml.putSequence();
        ArrayList<RiskBasedPricingEntry> infoRisk = getChargeOff(conn);
        chargeBridge(infoRisk, xml);
        xml.put();
        xml.putEndDocument();
    }

    public ArrayList<RiskBasedPricingEntry> getChargeOff(Connection conn) throws Exception{
        ArrayList<RiskBasedPricingEntry> rI = new ArrayList<>();
        String sql = SQL.queryOne();
        PreparedStatement pS = conn.prepareStatement(sql);
        ResultSet rS = pS.executeQuery();

        while (rS.next()){
            Serial serial = Serial.get(rS, 1);
            Float rate = rS.getFloat(2);

            RiskBasedPricingEntry RiskBasedPricingEntry = new RiskBasedPricingEntry(serial, rate);
            rI.add(RiskBasedPricingEntry);
        }

        pS.close();
        return rI;
    }

    public void chargeBridge(ArrayList<RiskBasedPricingEntry> risk_Info, XMLSerialize xml) throws Exception{
        for (RiskBasedPricingEntry r : risk_Info){
            xml.putTransaction();
            xml.putStep();
            xml.putRecord();
            {

            }
        }
    }

    public static void main(String[] args) throws Throwable {
        String javaClass = "-javaClass=com.blueeaglecreditunion.script.RiskBasedPricingUpdate";
        String javaMethod = "-javaMethod=runScript";
        String database = "-database=D0062LIV";
        String databaseHome = "-databaseHome=U:/Desktop/Test/";
        String jdbcDriver = "-jdbcDriver=com.ibm.db2.jcc.DB2Driver";
        String jdbcURLPrefix = "-jdbcURLPrefix=" + myPassword.getDatabase();
        String userName = "-userName=" + myPassword.getUsername();
        String password = "-password=" + myPassword.getPassword();
        String passwordStdInFlag = "-passwordStdInFlag=";
        String userHome = "-userHome=U:/Desktop/Test/";
        String defaultThreadQueueServerCount = "-defaultThreadQueueServerCount=32";
        String javaClassPath = "-javaClassPath=out/artifacts/risk_based_pricing_jar/RiskBasedPricingUpdate.jar";
        String resultPathName = "-resultPathName=U:/Desktop/Test/OutputReport.xml";
        String terminatePathName = "-terminatePathName=";
        String arg = "-arg=RiskBasedPricingEntries.csv"; //CSV File
        args = new String[]{javaClass, javaMethod, database, databaseHome, jdbcDriver, jdbcURLPrefix, userName, password, passwordStdInFlag, userHome, defaultThreadQueueServerCount, javaClassPath, resultPathName, terminatePathName, arg};
        Script.main(args);
    }
}
