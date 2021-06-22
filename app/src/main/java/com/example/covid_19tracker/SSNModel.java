package com.example.covid_19tracker;

public class SSNModel  {
    private long ssn;


    public SSNModel(long ssn) {
        this.ssn = ssn;
    }
    public SSNModel() {
    }

    public long getssn() {
        return ssn;
    }

    public void setssn(long ssn) {
        this.ssn = ssn;
    }
}
