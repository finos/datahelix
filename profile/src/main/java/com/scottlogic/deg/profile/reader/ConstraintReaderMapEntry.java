package com.scottlogic.deg.profile.reader;

public class ConstraintReaderMapEntry {
    private String operatorCode;
    private String valueCode;
    private ConstraintReader reader;

    public ConstraintReaderMapEntry(String operatorCode, String valueCode, ConstraintReader reader) {
        this.operatorCode = operatorCode;
        this.valueCode = valueCode;
        this.reader = reader;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public ConstraintReader getReader() {
        return reader;
    }
}
