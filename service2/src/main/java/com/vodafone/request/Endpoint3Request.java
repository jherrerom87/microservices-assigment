package com.vodafone.request;

public class Endpoint3Request {

    public Endpoint3Request(String sampleField) {
        this.sampleField = sampleField;
    }

    public Endpoint3Request() {
    }

    private String sampleField;

    public String getSampleField() {
        return sampleField;
    }

    public void setSampleField(String sampleField) {
        this.sampleField = sampleField;
    }
}
