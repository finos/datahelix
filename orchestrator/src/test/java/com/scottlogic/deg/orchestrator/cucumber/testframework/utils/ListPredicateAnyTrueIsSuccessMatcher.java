package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ListPredicateAnyTrueIsSuccessMatcher extends BaseMatcher<List<Object>> {
    private final Function<Object, Boolean> predicate;
    private final HashMap<Integer, Object> rowsThatMatch = new HashMap<>();
    private Integer checkedRows;

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
        checkedRows = values.size();

        for (int index = 0; index < values.size(); index++){
            Object value = values.get(index);

            if (predicate.apply(value)){
                rowsThatMatch.put(index, value);
            }
        }

        return !rowsThatMatch.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Some values match predicate");
    }
}
