package com.scottlogic.deg.profile.reader;

public interface ConstraintReaderMap {
    ConstraintReader getReader(String typeCode, String valueCode);
}
