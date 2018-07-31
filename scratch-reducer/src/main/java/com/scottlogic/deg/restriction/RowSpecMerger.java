package com.scottlogic.deg.restriction;

import java.util.List;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class RowSpecMerger {
    private final List<FieldSpec> fieldSpecs;

    public RowSpecMerger(List<FieldSpec> fieldSpecs) {
        this.fieldSpecs = fieldSpecs;
    }

    public List<FieldSpec> getFieldSpecs() {
        return fieldSpecs;
    }
}
