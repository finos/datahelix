package com.scottlogic.deg.profile.reader;

public class ConstraintReaderMapEntry {
    public String typeCode;
    public String valueCode;
    public ConstraintReader reader;

    public ConstraintReaderMapEntry(String typeCode, String valueCode, ConstraintReader reader) {
        this.typeCode = typeCode;
        this.valueCode = valueCode;
        this.reader = reader;
    }
}
