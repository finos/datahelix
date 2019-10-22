package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import java.util.function.Function;

public class GeneratorFieldSpec extends FieldSpec {
    private final FieldValueSource fieldValueSource;
    private final Function<Object, Boolean> acceptWhitelistValueFunction;

    public GeneratorFieldSpec(FieldValueSource fieldValueSource, Function<Object, Boolean> acceptWhitelistValueFunction,  boolean nullable) {
        super(nullable);
        this.fieldValueSource = fieldValueSource;
        this.acceptWhitelistValueFunction = acceptWhitelistValueFunction;
    }

    @Override
    public boolean canCombineWithWhitelistValue(Object value) {
        return acceptWhitelistValueFunction.apply(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return fieldValueSource;
    }

    @Override
    public FieldSpec withNotNull() {
        return new GeneratorFieldSpec(fieldValueSource, acceptWhitelistValueFunction, false);
    }
}
