package com.scottlogic.deg.profile.reader;

import java.util.stream.Stream;

public interface ConstraintReaderMap {

    ConstraintReader getReader(String typeCode, String valueCode);

    void add(ConstraintReaderMapEntry entry);

    void add(Stream<ConstraintReaderMapEntry> entries);
}
