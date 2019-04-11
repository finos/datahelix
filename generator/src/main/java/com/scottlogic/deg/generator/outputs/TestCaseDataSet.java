package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.generation.databags.Row;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.stream.Stream;

public class TestCaseDataSet {
    public final RuleInformation violation;
    private final Stream<Row> rows;

    public TestCaseDataSet(RuleInformation violation, Stream<Row> rows) {
        this.rows = rows;
        this.violation = violation;
    }

    public Stream<Row> stream() {
        return this.rows;
    }
}
