package com.scottlogic.deg.generator.generation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;

class ObservableIterableTests {
    @Test
    public void iteratorShouldNotifyReceiverForEachValue(){
        Iterable<String> strings = Arrays.asList("foo", "bar");
        ArrayList<Object> observedValues = new ArrayList<>();
        ObservableIterable observableIterable = new ObservableIterable<>(strings);
        observableIterable.addObserver((observable, value) -> observedValues.add(value));

        observableIterable.forEach(v -> {});

        Assert.assertThat(observedValues, hasItems("foo", "bar"));
    }

    @Test
    public void iteratorNotNotifyAnyValuesWhenIteratorIsEmpty(){
        Iterable<String> strings = Collections.emptyList();
        ArrayList<Object> observedValues = new ArrayList<>();
        ObservableIterable observableIterable = new ObservableIterable<>(strings);
        observableIterable.addObserver((observable, value) -> observedValues.add(value));

        observableIterable.forEach(v -> {});

        Assert.assertThat(observedValues, empty());
    }
}