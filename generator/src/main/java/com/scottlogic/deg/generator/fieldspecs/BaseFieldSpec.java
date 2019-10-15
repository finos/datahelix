package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;

public abstract class BaseFieldSpec implements FieldSpec {
    protected final boolean nullable;

    public BaseFieldSpec(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
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
