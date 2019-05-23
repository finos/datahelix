package com.scottlogic.deg.generator.utils;

import java.util.*;
import java.util.stream.Collectors;

public class HeterogeneousTypeContainer<I> {

    private final Map<Class<? extends I>, Object> map;

    public HeterogeneousTypeContainer() {
        this.map = new HashMap<>();
    }

    private HeterogeneousTypeContainer(Map<Class<? extends I>, Object> map) {
        this.map = map;
    }

    public <T extends I> HeterogeneousTypeContainer put(Class<T> type, T element) {
        Map<Class<? extends I>, Object> copy = new HashMap<>(map);
        copy.put(Objects.requireNonNull(type), element);
        return new HeterogeneousTypeContainer<>(copy);
    }

    public <T extends I> T get(Class<T> type) {
        return type.cast(map.get(type));
    }

    public Collection<Object> getMultiple(Set<Class<? extends I>> types) {
        return types.stream().map(this::get).collect(Collectors.toSet());
    }

    public Collection<Object> values() {
        return map.values();
    }

}
