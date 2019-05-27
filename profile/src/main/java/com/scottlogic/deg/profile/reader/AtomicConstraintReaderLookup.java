package com.scottlogic.deg.profile.reader;

public interface AtomicConstraintReaderLookup {

    ConstraintReader getByTypeCode(String typeCode);
}
