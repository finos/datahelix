package com.scottlogic.deg.generator.outputs;

import java.util.stream.Stream;

public class TestCaseDataSet {
    public final String violation;
    private final Stream<GeneratedObject> rows;

    public TestCaseDataSet(String violation, GeneratedObject... rows) {
        this(violation, Stream.of(rows));
    }

    public TestCaseDataSet(String violation, Stream<GeneratedObject> rows) {
        this.rows = rows;
        this.violation = violation;
    }

    public Stream<GeneratedObject> stream() {
        return this.rows;
    }
}
