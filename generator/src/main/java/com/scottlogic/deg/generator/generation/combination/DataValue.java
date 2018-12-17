package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.restrictions.FieldSpec;

public class DataValue {

    private Object value;
    private FieldSpec source;

    public DataValue(Object value, FieldSpec source) {
        this.value = value;
        this.source = source;
    }

    public Object getValue() {
        return value;
    }

    public FieldSpec getSource() {
        return source;
    }

}
