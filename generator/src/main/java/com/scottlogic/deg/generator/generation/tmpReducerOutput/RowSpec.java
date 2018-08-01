package com.scottlogic.deg.generator.generation.tmpReducerOutput;

import java.util.List;

public class RowSpec {
    private final List<FieldSpec> fieldSpecs;

    public RowSpec(List<FieldSpec> fieldSpecs) {
        this.fieldSpecs = fieldSpecs;
    }

    public List<FieldSpec> getFieldSpecs() {
        return fieldSpecs;
    }
}
