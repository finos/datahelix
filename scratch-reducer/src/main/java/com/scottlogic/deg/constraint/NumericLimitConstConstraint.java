package com.scottlogic.deg.constraint;

import com.scottlogic.deg.input.Field;

public class NumericLimitConstConstraint<T extends Number> implements IConstraint, IHasNumericTypeToken<T> {
    private final Field field;
    private final T limit;
    private final LimitType limitType;
    private final Class<T> typeToken;

    public enum LimitType { Min, Max }

    public NumericLimitConstConstraint(Field field, T limit, LimitType limitType, Class<T> typeToken) {
        this.field = field;
        this.limit = limit;
        this.limitType = limitType;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public T getLimit() {
        return limit;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    @Override
    public Class<T> getTypeToken() { return typeToken; }

    @Override
    public Class<T> getNumericTypeToken() { return typeToken; }

}
