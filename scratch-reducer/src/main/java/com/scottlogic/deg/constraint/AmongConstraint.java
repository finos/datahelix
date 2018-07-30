package com.scottlogic.deg.constraint;

import com.scottlogic.deg.input.Field;

import java.util.Set;

public class AmongConstraint<T> implements IConstraint, IHasTypeToken<T> {
    private final Field field;
    private final Set<T> among;
    private final Class<T> typeToken;

    public AmongConstraint(Field field, Set<T> among, Class<T> typeToken) {
        this.field = field;
        this.among = among;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Set<T> getAmong() {
        return among;
    }

    public Class<T> getTypeToken() { return typeToken; }
}
