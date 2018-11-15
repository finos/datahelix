package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowsPresentMatcher extends BaseMatcher<List<List<Object>>> {
    protected final List<List<Object>> expectedRows;

    public RowsPresentMatcher(List<List<Object>> expectedRows) {
        if (expectedRows == null)
            expectedRows = new ArrayList<>();
        this.expectedRows = expectedRows;
    }

    @Override
    public boolean matches(Object o) {
        List<List<Objects>> actualRows = (List<List<Objects>>) o;
        return getMissingRowMatchers(actualRows).isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
            String.join(
                ", ",
                getExpectedMatchers()
                    .stream()
                    .map(matcher -> matcher.toString())
                    .collect(Collectors.toList())));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        List<List<Objects>> actualRows = (List<List<Objects>>) item;
        Collection<RowMatcher> missingRowMatchers = getMissingRowMatchers(actualRows);

        description.appendText(
            String.join(
                ", ",
                actualRows
                    .stream()
                    .map(row -> row.toString())
                    .collect(Collectors.toList())));

        description.appendText("\n");
        description.appendList(" missing: ",", ", "", missingRowMatchers);
    }

    private Collection<RowMatcher> getMissingRowMatchers(List<List<Objects>> actualRows) {
        Collection<RowMatcher> expectedMatchers = getExpectedMatchers();
        ArrayList<RowMatcher> missingRowMatchers = new ArrayList<>();

        for (RowMatcher expectedMatcher : expectedMatchers){
            if (!actualRows.stream().anyMatch(actualRow -> expectedMatcher.matches(actualRow))){
                missingRowMatchers.add(expectedMatcher);
            }
        }

        return missingRowMatchers;
    }

    private List<RowMatcher> getExpectedMatchers() {
        return expectedRows
            .stream()
            .map(expectedRow -> new RowMatcher(expectedRow))
            .collect(Collectors.toList());
    }
}