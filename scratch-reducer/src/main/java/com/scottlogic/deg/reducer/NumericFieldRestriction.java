package com.scottlogic.deg.reducer;

import com.scottlogic.deg.input.Field;

import java.util.Set;

public class NumericFieldRestriction<T extends Number> implements IFieldRestriction {
    private final Field field;

    public NumericFieldRestriction(Field field) {
        this.field = field;
    }

    public T min;
    public T max;
    public Set<T> among;
}
