package com.scottlogic.deg.restriction;

import com.scottlogic.deg.input.Field;
import com.scottlogic.deg.restriction.IFieldRestriction;

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
