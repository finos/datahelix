package com.scottlogic.deg.generator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ListPredicateMatcher extends BaseMatcher<List<Object>> {
    private final Function<Object, Boolean> predicate;
    private final HashMap<Integer, Object> rowsThatDoNotMatch = new HashMap<>();
    private Integer checkedRows;

    public ListPredicateMatcher(Function<Object, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description
            .appendText(String.format("%d of %d rows do not match\n", rowsThatDoNotMatch.size(), checkedRows));

        rowsThatDoNotMatch
            .entrySet()
            .forEach(entry -> description.appendText(String.format("Row %d: (%s) does not match predicate\n", entry.getKey(), entry.getValue())));
    }

    @Override
    public boolean matches(Object o) {
        List<Object> values = (List<Object>) o;
        checkedRows = values.size();

        for (int index = 0; index < values.size(); index++){
            Object value = values.get(index);

            if (!predicate.apply(value)){
                rowsThatDoNotMatch.put(index, value);
            }
        }

        return rowsThatDoNotMatch.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("All values match predicate");
    }
}
