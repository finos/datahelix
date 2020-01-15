package com.scottlogic.datahelix.generator.core.generation.string.generators.faker;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntersectingFakeValueProvider implements FakeValueProvider {
    private final FakeValueProvider[] services;
    private List<String> allUnfilteredValues;

    public IntersectingFakeValueProvider(FakeValueProvider... services) {
        this.services = services;

        //TODO: Remove duplicates in services
    }

    @Override
    public boolean hasValue(String value, Function<String, Boolean> predicate) {
        return Arrays.stream(services)
            .allMatch(service -> service.hasValue(value, predicate));
    }

    @Override
    public Stream<String> getAllValues(Function<String, Boolean> predicate) {
        if (allUnfilteredValues == null){
            allUnfilteredValues = getAllUnfilteredValues();
        }

        return allUnfilteredValues.stream().filter(predicate::apply);
    }

    private List<String> getAllUnfilteredValues() {
        return Arrays.stream(services)
            .flatMap(service -> service.getAllValues(value -> true)) //accept all values
            .collect(Collectors.toList());
    }
}
