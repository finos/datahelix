package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.Collections;
import java.util.Set;

public class DataBagValueSource {
    public static final DataBagValueSource Empty = new DataBagValueSource();

    private final String rule;
    private final Set<AtomicConstraint> constraints;

    private DataBagValueSource() {
        rule = null;
        constraints = Collections.emptySet();
    }

    public DataBagValueSource(FieldSpecSource fieldSpecSource) {
        this.rule = fieldSpecSource != null ? fieldSpecSource.getRule() : null;
        this.constraints = fieldSpecSource != null ? fieldSpecSource.getConstraints() : null;
    }

    public String getRule() {
        return rule;
    }

    public Set<AtomicConstraint> getConstraints() {
        return constraints;
    }
}
