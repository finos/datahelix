package com.scottlogic.deg.profile.reader;

public class ConstraintReaderMapEntry {
    public String operatorCode;
    public String valueCode;
    public ConstraintReader reader;

    public ConstraintReaderMapEntry(String operatorCode, String valueCode, ConstraintReader reader) {
        this.operatorCode = operatorCode;
        this.valueCode = valueCode;
        this.reader = reader;
    }
}
