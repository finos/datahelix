package com.scottlogic.deg.generator.outputs;

import java.util.*;

public class TestCaseDataSet implements Iterable<GeneratedObject> {
    public final String violation;
    private final List<GeneratedObject> rows;

    public TestCaseDataSet(String violation, List<GeneratedObject> rows) {
        this.rows = rows;
        this.violation = violation;
    }
    public TestCaseDataSet(String violation, GeneratedObject... rows) {
        this(violation, Arrays.asList(rows));
    }
    public TestCaseDataSet(String violation, Iterable<GeneratedObject> rows) {
        this.rows = new ArrayList<>();
        rows.forEach(this.rows::add);

        this.violation = violation;
    }

    @Override
    public Iterator<GeneratedObject> iterator() {
        return this.rows.iterator();
    }
}
