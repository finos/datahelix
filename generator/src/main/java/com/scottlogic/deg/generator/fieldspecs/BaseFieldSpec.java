package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;

import java.util.Set;

public class BaseFieldSpec implements IFieldSpec {

    protected final boolean nullable;

    public BaseFieldSpec(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public boolean permits(Object value) {
        return false;
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return new NullOnlySource();
    }

    protected FieldValueSource appendNullSource(FieldValueSource source){
        if (!nullable){
            return source;
        }
        return new NullAppendingValueSource(source);
    }
}
