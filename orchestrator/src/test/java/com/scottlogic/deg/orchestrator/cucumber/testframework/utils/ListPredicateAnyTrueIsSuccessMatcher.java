package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ListPredicateAnyTrueIsSuccessMatcher extends BaseMatcher<List<Object>> {
    private final Function<Object, Boolean> predicate;

    public ListPredicateAnyTrueIsSuccessMatcher(Function<Object, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description
            .appendText("No rows match.");
    }

    @Override
    public boolean matches(Object o) {
        List<Object> values = (List<Object>) o;

        return values.stream().anyMatch(predicate::apply);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Some values match predicate");
    }
}
