package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowsPresentMatcher extends BaseMatcher<List<List<Object>>> {
    private final List<List<Object>> expectedRows;

    public RowsPresentMatcher(List<List<Object>> expectedRows) {
        if (expectedRows == null)
            expectedRows = new ArrayList<>();
        this.expectedRows = expectedRows;
    }

    @Override
    public boolean matches(Object o) {
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();

        for (List<Object> actualRow : (List<List<Object>>) o){
            if (!expectedMatchers.stream().anyMatch(matcher -> matcher.matches(actualRow))){
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