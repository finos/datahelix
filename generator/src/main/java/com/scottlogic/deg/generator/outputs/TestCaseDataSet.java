package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.stream.Stream;

public class TestCaseDataSet {
    public final RuleInformation violation;
    private final Stream<GeneratedObject> rows;

    public TestCaseDataSet(RuleInformation violation, GeneratedObject... rows) {
        this(violation, Stream.of(rows));
    }

    public TestCaseDataSet(RuleInformation violation, Stream<GeneratedObject> rows) {
        this.rows = rows;
        this.violation = violation;
    }

    public Stream<GeneratedObject> stream() {
        return this.rows;
    }
}
