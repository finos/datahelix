package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Set;

public class CellSource {
    public final Field field;
    private final DataBagValueSource source;

    public CellSource(DataBagValue value, Field field) {
        this.field = field;
        this.source = value.source;
    }

    public String getRule(){
        return source != null ? source.getRule() : null;
    }

    public Set<IConstraint> getConstraints(){
        return source != null ? source.getConstraints() : null;
    }
}
