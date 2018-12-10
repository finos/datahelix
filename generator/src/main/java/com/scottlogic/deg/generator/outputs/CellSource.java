package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class CellSource {
    public final Field field;
    private final DataBagValueSource source;

    public CellSource(DataBagValue value, Field field) {
        this.field = field;
        this.source = value.source;
    }

    public Set<AtomicConstraint> getConstraints(){
        return source != null ? source.getConstraints() : null;
    }

    public Set<ConstraintRule> getRules(){
        return source != null ? source.getRules() : null;
    }
}
