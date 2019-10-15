package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;

public abstract class FieldSpec {

    public abstract boolean permits(Object value);
    public abstract FieldValueSource getFieldValueSource();
    public abstract FieldSpec withNotNull();

    protected final boolean nullable;

    public FieldSpec(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isNullable(){
        return nullable;
    }

    protected FieldValueSource appendNullSource(FieldValueSource source){
        if (!nullable){
            return source;
        }
        return new NullAppendingValueSource(source);
    }

}
