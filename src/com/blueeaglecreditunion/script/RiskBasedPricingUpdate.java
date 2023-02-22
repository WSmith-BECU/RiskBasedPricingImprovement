package com.blueeaglecreditunion.script;

import com.corelationinc.script.*;
import com.corelationinc.script.utils.CSV.CSVResultReader;
import com.corelationinc.script.utils.CSV.CSVResultSet;
import com.corelationinc.script.utils.CSV.CSVResultReader.DELIMITER;
import com.corelationinc.script.Report.Format;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;

public class RiskBasedPricingUpdate {
    Script script = null;

    private RiskBasedPricingUpdate(Script value) { this.script = value; }

    public static void runScript(Script script) throws Exception{
        RiskBasedPricingUpdate rBP = new RiskBasedPricingUpdate(script);

        rBP.run();
    }

    private void run() throws Exception {
        String filePath = this.script.getDatabaseHomePathName() + "/import/";
        String fileName = "";
        Iterator<String> iterator = this.script.getArgumentIterator();
        if (iterator.hasNext()) {
            fileName = (String)iterator.next();
        }
        if (fileName.trim().isEmpty()) {
            throw new ScriptException("No filename was passed as an argument");
        } else {
            filePath = filePath + fileName;
            Connection conn = this.script.openDatabaseConnection();
            String postingDate = script.retrievePostingDateString(conn);
            Report report = this.script.openReport("Report A", Report.Format.txt);
            report.setPostingOption(true);
            PrintStream pS = new PrintStream(report.getBufferedOutputStream());
            XMLSerialize xml = new XMLSerialize();
            xml.setXMLWriter(pS);
            xml.putStartDocument();
            xml.putBatchQuery(postingDate);
            xml.putSequence();
            xml.put();
            xml.putEndDocument();
            Report excReport = this.script.openReport("Email Opt Out Exception Report", Format.txt);
            PrintStream excOS = new PrintStream(excReport.getBufferedOutputStream());
            excOS.println("Ran on: " + postingDate);
            ArrayList<RiskBasedPricingEntry> rateUpdate = this.riskUpdate(filePath, excOS);
            Serial noteType = this.getRateSerial(conn, excOS);
            this.buildRiskRequest(xml, rateUpdate, excOS, noteType);
        }
    }

    public ArrayList<RiskBasedPricingEntry> riskUpdate(String filePath, PrintStream p) {
        ArrayList<RiskBasedPricingEntry> rBPE = new ArrayList();

        try {
            CSVResultSet rs = CSVResultReader.readCsvData(filePath, DELIMITER.COMMA, true);

            while(rs.next()) {
                Serial serial = rs.getSerial("SERIAL");
                int minYear = rs.getInt("MIN_VEHICLE_YEAR");
                int maxYear = rs.getInt("MAX_VEHICLE_YEAR");
                Rate rate = rs.getRate("RATE");
                RiskBasedPricingEntry rBP = new RiskBasedPricingEntry(serial, rate, minYear, maxYear);
                rBPE.add(rBP);
            }
        } catch (Exception var9) {
            p.println(var9.getMessage() + "\n Method: riskUpdate");
        }
        return rBPE;
    }

    public void buildRiskRequest(XMLSerialize xml, ArrayList<RiskBasedPricingEntry> riskList, PrintStream p, Serial s){
        try {
            xml.putTransaction();
            riskList.forEach((note) -> {
                try {
                    xml.putStep();
                    xml.putRecord();
                    xml.putOption("", "");
                    xml.put("tableName", "RISK_BASED_PRICING_ENTRY");
                    xml.put("Serial", note.getSerial());
                    xml.put("field");
                    xml.put("columnName", "SERIAL");
                    xml.putOption("operation", "S");
                    xml.put("newContents", s);
                    xml.put();
                    xml.put("field");
                    xml.put("columnName", "RATE");
                    xml.putOption("operation", "S");
                    xml.put("newContents", note.getRate());
                    xml.put();
                    xml.put("field");
                    xml.put("columnName", "MIN_VEHICLE_YEAR");
                    xml.putOption("operation", "S");
                    xml.put("newContents", note.getMinYear());
                    xml.put();
                    xml.put("field");
                    xml.put("columnName", "MAX_VEHICLE_YEAR");
                    xml.putOption("operation", "S");
                    xml.put("newContents", note.getMaxYear());
                    xml.put();
                    xml.put();
                    xml.put();
                } catch (XMLStreamException var5) {
                    p.println(var5.getMessage());
                    var5.printStackTrace(p);
                }
            });
        } catch (XMLStreamException var6) {
            var6.printStackTrace();
        }
    }

    public Serial getRateSerial(Connection conn, PrintStream p) {
        Serial s = null;
        String sql = SQL.getSerial();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = Serial.get(rs, 1);
            }
        } catch (Exception var7) {
            p.println(var7.getMessage());
        }

        return s;
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

        args = new String[]{javaClass, javaMethod, database,
                            databaseHome, jdbcDriver, jdbcURLPrefix,
                            userName, password, passwordStdInFlag,
                            userHome, defaultThreadQueueServerCount, javaClassPath,
                            resultPathName, terminatePathName, arg};
        Script.main(args);
    }
}
