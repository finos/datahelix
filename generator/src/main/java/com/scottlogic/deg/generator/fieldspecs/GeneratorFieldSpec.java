package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import java.util.function.Function;

public class GeneratorFieldSpec extends FieldSpec {
    private final FieldValueSource fieldValueSource;
    private final Function<Object, Boolean> permit;

    public GeneratorFieldSpec(FieldValueSource fieldValueSource, Function<Object, Boolean> permit,  boolean nullable) {
        super(nullable);
        this.fieldValueSource = fieldValueSource;
        this.permit = permit;
    }

    @Override
    public boolean permits(Object value) {
        return permit.apply(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return fieldValueSource;
    }

    @Override
    public FieldSpec withNotNull() {
        return new GeneratorFieldSpec(fieldValueSource, permit, false);
    }
}
