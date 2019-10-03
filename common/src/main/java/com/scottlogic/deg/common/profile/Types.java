package com.scottlogic.deg.common.profile;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Types {
    NUMERIC(o -> o instanceof Number),
    STRING(o -> o instanceof String),
    DATETIME(o -> o instanceof OffsetDateTime);

    private final Predicate<Object> isInstanceOf;

    Types(final Predicate<Object> isInstanceOf) {
        this.isInstanceOf = isInstanceOf;
    }

    public boolean isInstanceOf(Object o) {
        return isInstanceOf.equals(o);
    }
}
