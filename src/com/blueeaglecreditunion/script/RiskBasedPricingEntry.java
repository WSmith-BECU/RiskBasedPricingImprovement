package com.blueeaglecreditunion.script;

import com.corelationinc.script.Serial;
import com.corelationinc.script.Rate;

public class RiskBasedPricingEntry {
    private Serial serial;
    private Rate rate;
    private int minYear;
    private int maxYear;

    public RiskBasedPricingEntry() {

    }

    public RiskBasedPricingEntry(Serial serial, Rate rate, int minYear, int maxYear) {
        this.serial = serial;
        this.rate = rate;
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    public Serial getSerial() {
        return serial;
    }

    public void setSerial(Serial serial) {
        this.serial = serial;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }
}
