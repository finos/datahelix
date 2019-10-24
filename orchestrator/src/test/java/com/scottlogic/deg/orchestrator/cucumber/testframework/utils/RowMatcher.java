/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.util.NumberUtils.coerceToBigDecimal;

public class RowMatcher extends BaseMatcher<List<Object>> {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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

        while (actualRowIterator.hasNext()) {
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

        if (actual instanceof Number && expected instanceof Number && actual.getClass() != expected.getClass()) {
            return numbersEqual((Number) actual, (Number) expected);
        }

        return actual.equals(expected);
    }

    /**
     * Compares two numbers by casting up to the widest supported number type, BigDecimal.
     *
     * @param actual   Actual number of any Number type
     * @param expected Expected number of any Number type
     * @return True if numbers are mathematically equal, i.e. have the same value
     */
    private boolean numbersEqual(Number actual, Number expected) {
        BigDecimal decimalActual = coerceToBigDecimal(actual);
        BigDecimal decimalExpected = coerceToBigDecimal(expected);
        return decimalActual.compareTo(decimalExpected) == 0;
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

    private static Object formatDate(Object value) {
        if (value instanceof OffsetDateTime) {
            return ((OffsetDateTime) value).format(dateTimeFormat);
        }

        return value;
    }
}
