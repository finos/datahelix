package com.scottlogic.datahelix.generator.core.fieldspecs;

import java.util.function.Function;

public interface ValuesFieldSpec {
    FieldSpec withMappedValues(Function<Object, Object> parse);
}
