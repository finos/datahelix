package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RowMatcher extends BaseMatcher<List<Object>> {
    private final List<Object> expectedRow;

    public RowMatcher(List<Object> expectedRow) {
        this.expectedRow = expectedRow;
    }

    @Override
    public boolean matches(Object o) {
        List<Object> actualRow = (List<Object>) o;

        if (actualRow == null && expectedRow == null)
            return true;

        if (actualRow == null || expectedRow == null)
            return false;

        Iterator<Object> actualRowIterator = actualRow.iterator();
        Iterator<Object> expectedRowIterator = expectedRow.iterator();

        while (actualRowIterator.hasNext()){
            Object actualColumnValue = actualRowIterator.next();

            if (!expectedRowIterator.hasNext())
                return false; //different lengths

            Object expectedColumnValue = expectedRowIterator.next();

            if (!objectsEquals(actualColumnValue, expectedColumnValue))
                return false;
        }

        return true;
    }

    private boolean objectsEquals(Object actual, Object expected) {
        if (actual == null && expected == null)
            return true;

        if (actual == null || expected == null)
            return false;

        return actual.equals(expected);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(Objects.toString(this.expectedRow));
    }
}
