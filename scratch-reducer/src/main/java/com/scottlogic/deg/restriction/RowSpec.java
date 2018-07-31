package com.scottlogic.deg.restriction;

import java.util.List;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class RowSpec {
    private final List<FieldSpec> fieldSpecs;

    public RowSpec(List<FieldSpec> fieldSpecs) {
        this.fieldSpecs = fieldSpecs;
    }

    public List<FieldSpec> getFieldSpecs() {
        return fieldSpecs;
    }
}
