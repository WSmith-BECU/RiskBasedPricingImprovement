package com.blueeaglecreditunion.script;

import com.corelationinc.script.Serial;

public class RiskBasedPricingEntry {
    private Serial serial;
    private Float rate;

    public RiskBasedPricingEntry() {

    }

    public RiskBasedPricingEntry(Serial serial, Float rate) {
        this.serial = serial;
        this.rate = rate;
    }

    public Serial getSerial() {
        return serial;
    }

    public void setSerial(Serial serial) {
        this.serial = serial;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
