package com.scottlogic.datahelix.generator.core.generation.string.generators.faker;

import java.util.function.Function;
import java.util.stream.Stream;

public interface FakeValueProvider {
    boolean hasValue(String value, Function<String, Boolean> predicate);
    Stream<String> getAllValues(Function<String, Boolean> predicate);
}
