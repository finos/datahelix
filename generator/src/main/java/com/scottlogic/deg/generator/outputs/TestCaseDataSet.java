package com.scottlogic.deg.generator.outputs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public class TestCaseDataSet {
    public final String violation;
    private final Collection<TestCaseDataRow> rows;

    public TestCaseDataSet(String violation, Collection<TestCaseDataRow> rows) {
        this.rows = rows;
        this.violation = violation;
    }
    public TestCaseDataSet(String violation, TestCaseDataRow... rows) {
        this(violation, Arrays.asList(rows));
    }

    public Iterable<TestCaseDataRow> enumerateRows()
    {
        return this.rows;
    }
}
