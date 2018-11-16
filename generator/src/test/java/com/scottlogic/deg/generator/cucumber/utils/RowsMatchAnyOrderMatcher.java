package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowsMatchAnyOrderMatcher extends RowsPresentMatcher {
    public RowsMatchAnyOrderMatcher(List<List<Object>> expectedRows) {
        super(expectedRows);
    }

    @Override
    public boolean matches(Object o) {
        List<List<Object>> actualRows = (List<List<Object>>) o;

        if (expectedRows.size() != actualRows.size())
            return false;

        return super.matches(o);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        List<List<Objects>> actualRows = (List<List<Objects>>) item;

        super.describeMismatch(item, description);

        description.appendText(String.format("\n  counts: expected %d, but got %d", expectedRows.size(), actualRows.size()));
    }
}
