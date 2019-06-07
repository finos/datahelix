package com.scottlogic.deg.profile.reader;

import java.util.stream.Stream;

public interface ConstraintReaderMapEntryProvider {
    Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries();
}
