package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowsMatchAnyOrderMatcher extends BaseMatcher<List<List<Object>>> {
    private final List<List<Object>> expectedRows;

    public RowsMatchAnyOrderMatcher(List<List<Object>> expectedRows) {
        if (expectedRows == null)
            expectedRows = new ArrayList<>();
        this.expectedRows = expectedRows;
    }

    @Override
    public boolean matches(Object o) {
        List<List<Object>> actualRows = (List<List<Object>>) o;

        if (expectedRows.size() != actualRows.size())
            return false;

        //check that every expected row exists in the actual rows
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();
        for (List<Object> actualRow : actualRows){
            if (!expectedMatchers.stream().anyMatch(matcher -> matcher.matches(actualRow))){
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();
        description.appendText(Objects.toString(expectedMatchers));
    }

    private List<RowMatcher> getExpectedMatchers() {
        return expectedRows
            .stream()
            .map(expectedRow -> new RowMatcher(expectedRow))
            .collect(Collectors.toList());
    }
}
