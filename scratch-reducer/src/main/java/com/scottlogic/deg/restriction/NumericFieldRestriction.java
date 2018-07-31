package com.scottlogic.deg.restriction;

import com.scottlogic.deg.constraint.IHasNumericTypeToken;
import com.scottlogic.deg.input.Field;

import java.util.Set;

public class NumericFieldRestriction<T extends Number> implements FieldSpec, IHasNumericTypeToken {
    private final Field field;
    private final Class<T> typeToken;

    private T min;
    private T max;
    private Set<T> among;

    public NumericFieldRestriction(Field field, Class<T> typeToken) {
        this.field = field;
        this.typeToken = typeToken;
    }

    public Field getField() {
        return field;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public Set<T> getAmong() {
        return among;
    }

    public void setAmong(Set<T> among) {
        this.among = among;
    }

    @Override
    public Class getTypeToken() {
        return typeToken;
    }

    @Override
    public Class getNumericTypeToken() {
        return typeToken;
    }
}
