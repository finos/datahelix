package com.scottlogic.deg.constraint;

import com.scottlogic.deg.input.Field;

public class TypeConstraint<T> implements IConstraint, IHasTypeToken<T> {
    private final Field field;
    private final Class<T> typeToken;

    public TypeConstraint(Field field, Class<T> typeToken) {
        this.field = field;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Class<T> getTypeToken() { return typeToken; }
}
