package com.blueeaglecreditunion.script;

public class SQL {

    public static String queryOne () {
        return "SELECT rBPE.SERIAL,\n" +
                "       rBPE.RATE\n" +
                "\n" +
                "FROM CORE.RISK_BASED_PRICING_ENTRY AS rBPE\n" +
                "\n" +
                "ORDER BY rBPE.PARENT_SERIAL, rBPE.MIN_SCORE, rBPE.MIN_VEHICLE_YEAR, rBPE.MIN_TERM";
    }
}
