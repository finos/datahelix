package com.scottlogic.deg.generator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RowsAbsentMatcher extends BaseMatcher<List<List<Object>>> {
    private final List<List<Object>> expectedRows;

    public RowsAbsentMatcher(List<List<Object>> expectedRows) {
        if (expectedRows == null)
            expectedRows = new ArrayList<>();
        this.expectedRows = expectedRows;
    }

    @Override
    public boolean matches(Object o) {
        List<List<Object>> actualRows = (List<List<Object>>) o;
        return getFoundRowMatchers(actualRows).isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
            getExpectedMatchers()
                .stream()
                .map(BaseMatcher::toString)
                .collect(Collectors.joining(", ")));
    }

    public void describeMismatch(Object item, Description description) {
        List<List<Object>> actualRows = (List<List<Object>>) item;
        Collection<RowMatcher> foundRowMatchers = getFoundRowMatchers(actualRows);

        description.appendText(
            actualRows
                .stream()
                .map(RowMatcher::formatDatesInRow)
                .map(Object::toString)
                .collect(Collectors.joining(", ")));

        description.appendText("\n");
        description.appendList("   found: ",", ", "", foundRowMatchers);
    }

    private Collection<RowMatcher> getFoundRowMatchers(List<List<Object>> actualRows) {
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();
        ArrayList<RowMatcher> missingRowMatchers = new ArrayList<>();

        for (RowMatcher expectedMatcher : expectedMatchers){
            if (actualRows.stream().anyMatch(expectedMatcher::matches)){
                missingRowMatchers.add(expectedMatcher);
            }
        }

        return missingRowMatchers;
    }

    private List<RowMatcher> getExpectedMatchers() {
        return expectedRows
            .stream()
            .map(RowMatcher::new)
            .collect(Collectors.toList());
    }
}