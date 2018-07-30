package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;

import java.util.Set;

public class AmongConstraint<T> implements IConstraint, IHasTypeToken<T> {
    private final Field field;
    private final T limit;
    private final Set<T> among;
    private final Class<T> typeToken;

    public AmongConstraint(Field field, T limit, Set<T> among, Class<T> typeToken) {
        this.field = field;
        this.limit = limit;
        this.among = among;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Set<T> getAmong() {
        return among;
    }

    public Class<T> getTypeToken() { return typeToken; }
}
