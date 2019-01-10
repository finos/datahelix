package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.stream.Stream;

public class TestCaseDataSet {
    public final RuleInformation violatedRule;
    public final Constraint violatedConstraint;
    private final Stream<GeneratedObject> rows;

    public TestCaseDataSet(RuleInformation violatedRule, Constraint violatedConstraint, GeneratedObject... rows) {
        this(violatedRule, violatedConstraint, Stream.of(rows));
    }

    public TestCaseDataSet(RuleInformation violatedRule, Constraint violatedConstraint, Stream<GeneratedObject> rows) {
        this.rows = rows;
        this.violatedRule = violatedRule;
        this.violatedConstraint = violatedConstraint;
    }

    public Stream<GeneratedObject> stream() {
        return this.rows;
    }
}
