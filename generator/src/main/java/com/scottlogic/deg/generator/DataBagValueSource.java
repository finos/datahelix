package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.restrictions.FieldSpecSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class DataBagValueSource {
    public static final DataBagValueSource Empty = new DataBagValueSource();

    private final Set<AtomicConstraint> constraints;
    private final Set<ConstraintRule> rules;

    private DataBagValueSource() {
        constraints = Collections.emptySet();
        rules = Collections.emptySet();
    }

    public DataBagValueSource(FieldSpecSource fieldSpecSource) {
        this.constraints = fieldSpecSource != null ? fieldSpecSource.getConstraints() : null;
        this.rules = fieldSpecSource != null ? fieldSpecSource.getRules() : null;
    }

    public Set<AtomicConstraint> getConstraints() {
        return constraints;
    }

    public Set<ConstraintRule> getRules() {
        return rules;
    }
}
