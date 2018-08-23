package com.scottlogic.deg.generator.outputs;

import java.util.*;

public class TestCaseDataSet implements Iterable<TestCaseDataRow> {
    public final String violation;
    private final List<TestCaseDataRow> rows;

    public TestCaseDataSet(String violation, List<TestCaseDataRow> rows) {
        this.rows = rows;
        this.violation = violation;
    }
    public TestCaseDataSet(String violation, TestCaseDataRow... rows) {
        this(violation, Arrays.asList(rows));
    }
    public TestCaseDataSet(String violation, Iterable<TestCaseDataRow> rows) {
        this.rows = new ArrayList<>();
        rows.forEach(this.rows::add);

        this.violation = violation;
    }

    @Override
    public Iterator<TestCaseDataRow> iterator() {
        return this.rows.iterator();
    }
}
