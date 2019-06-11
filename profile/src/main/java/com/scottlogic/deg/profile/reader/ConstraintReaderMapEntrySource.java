package com.scottlogic.deg.profile.reader;

import java.util.stream.Stream;

public interface ConstraintReaderMapEntrySource {
    Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries();
}
