package com.scottlogic.deg.constraint;

import com.scottlogic.deg.input.Field;

public class NumericLimitConstConstraint<T extends Number> implements IConstraint, IHasNumericTypeToken<T> {
    private final Field field;
    private final T limit;
    private final LimitType limitType;
    private final Class<T> typeToken;

    enum LimitType { Min, Max }

    public NumericLimitConstConstraint(Field field, T limit, LimitType limitType, Class<T> typeToken) {
        this.field = field;
        this.limit = limit;
        this.limitType = limitType;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Class<T> getTypeToken() { return typeToken; }
}
