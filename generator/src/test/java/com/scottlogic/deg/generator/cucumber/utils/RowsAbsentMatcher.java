package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();
        List<List<Objects>> actualRows = (List<List<Objects>>) o;

        for (RowMatcher expectedMatcher : expectedMatchers){
            if (actualRows.stream().anyMatch(actualRow -> expectedMatcher.matches(actualRow))){
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(Objects.toString(getExpectedMatchers()));
    }

    private List<RowMatcher> getExpectedMatchers() {
        return expectedRows
            .stream()
            .map(expectedRow -> new RowMatcher(expectedRow))
            .collect(Collectors.toList());
    }
}