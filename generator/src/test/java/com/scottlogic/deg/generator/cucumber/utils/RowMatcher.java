package com.scottlogic.deg.generator.cucumber.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RowMatcher extends BaseMatcher<List<Object>> {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private final List<Object> expectedRow;

    RowMatcher(List<Object> expectedRow) {
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
        description.appendText(Objects.toString(
            this.expectedRow
                .stream()
                .map(RowMatcher::formatDate)
                .collect(Collectors.toList())));
    }

    static List<Object> formatDatesInRow(List<Object> row) {
        return row.stream().map(RowMatcher::formatDate).collect(Collectors.toList());
    }

    private static Object formatDate(Object value){
        if (value instanceof LocalDateTime){
            return ((LocalDateTime) value).format(dateTimeFormat);
        }

        return value;
    }
}
