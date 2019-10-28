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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RowsAbsentMatcher extends BaseMatcher<List<Map<String, Object>>> {
    private final List<Map<String, Object>> expectedRows;

    public RowsAbsentMatcher(List<Map<String, Object>> expectedRows) {
        if (expectedRows == null)
            expectedRows = new ArrayList<>();
        this.expectedRows = expectedRows;
    }

    @Override
    public boolean matches(Object o) {
        List<Map<String, Object>> actualRows = (List<Map<String, Object>>) o;
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
        List<Map<String, Object>> actualRows = (List<Map<String, Object>>) item;
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

    private Collection<RowMatcher> getFoundRowMatchers(List<Map<String, Object>> actualRows) {
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