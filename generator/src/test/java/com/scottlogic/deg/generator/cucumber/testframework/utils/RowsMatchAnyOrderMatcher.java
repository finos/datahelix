package com.scottlogic.deg.generator.cucumber.testframework.utils;

import org.hamcrest.Description;

import java.util.List;

public class RowsMatchAnyOrderMatcher extends RowsPresentMatcher {
    public RowsMatchAnyOrderMatcher(List<List<Object>> expectedRows) {
        super(expectedRows);
    }

    @Override
    public boolean matches(Object o) {
        List<List<Object>> actualRows = (List<List<Object>>) o;

        if (expectedRows.size() != actualRows.size())
            return false;

        return super.matches(actualRows);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        List<List<Object>> actualRows = (List<List<Object>>) item;

        super.describeMismatch(item, description);

        description.appendText(String.format("\n  counts: expected %d, but got %d", expectedRows.size(), actualRows.size()));
    }
}
